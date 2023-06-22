package io.owenrbee.zapb.codegen.model.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PomVO {

    private String group;
    private String artifact;
    private String name;
    private String description;
    private String packageName;

}
