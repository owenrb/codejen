package io.owenrbee.zapb.codegen.model.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileVO {

    private String path;
    private String filename;
    private String content;

    public String getFullPath() {
        return (path.isEmpty() ? "" : (path + "/")) + filename;
    }
}
