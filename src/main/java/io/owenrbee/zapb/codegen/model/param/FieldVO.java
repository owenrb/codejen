package io.owenrbee.zapb.codegen.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldVO {

    @Schema(example = "id")
    private String name;
    @Schema(example = "String")
    private String type;
    @Schema(example = "true")
    private boolean id;
    @Schema(example = "false")
    private boolean list;

}
