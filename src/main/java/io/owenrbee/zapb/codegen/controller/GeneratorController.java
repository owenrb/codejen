package io.owenrbee.zapb.codegen.controller;

import java.io.IOException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.owenrbee.zapb.codegen.model.param.EntityVO;
import io.owenrbee.zapb.codegen.model.param.PomVO;
import io.owenrbee.zapb.codegen.model.spec.FileVO;
import io.owenrbee.zapb.codegen.service.TemplateService;
import io.owenrbee.zapb.codegen.service.ZipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("${apiPrefix}/generator")
@Slf4j
public class GeneratorController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ZipService zipService;

    @Operation(summary = "Generate Code")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<byte[]> generate(@Valid @RequestBody GenerateCodeRequest input) {

        log.info("Generating Code");

        String packageName = input.getPackageName();

        FileVO pom = templateService.generatePom(input.toPomVO());
        FileVO main = templateService.generateMain(packageName);
        FileVO[] exceptions = templateService.generateExceptions(packageName);
        FileVO idao = templateService.generateIDao(packageName);

        try {
            byte[] zipData = zipService.compile(pom, main,
                    exceptions[0], exceptions[1], idao);

            // Set the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=codejen.zip");

            // Return the ZIP file as a ResponseEntity
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(zipData.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(zipData);
        } catch (IOException e) {
            log.error("Zip Error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}

@Getter
@Setter
class GenerateCodeRequest {

    @NotBlank
    @Schema(example = "io.owenrbee")
    private String group;

    @NotBlank
    @Schema(example = "auto-api")
    private String artifact;

    @NotBlank
    @Schema(example = "Demo")
    private String name;

    @NotBlank
    @Schema(example = "io.owenrbee.codejen")
    private String packageName;

    private EntityVO[] entities;

    PomVO toPomVO() {

        PomVO vo = new PomVO();
        vo.setGroup(group);
        vo.setName(name);
        vo.setArtifact(artifact);
        vo.setDescription("Generated " + name);
        vo.setPackageName(packageName);

        return vo;
    }

}
