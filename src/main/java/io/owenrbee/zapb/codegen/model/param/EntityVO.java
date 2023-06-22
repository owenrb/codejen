package io.owenrbee.zapb.codegen.model.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityVO {

    @Schema(example = "Student")
    private String name;

    private FieldVO[] fields;

}
