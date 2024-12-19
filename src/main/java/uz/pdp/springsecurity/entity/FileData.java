package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "file_data")
@AllArgsConstructor
@NoArgsConstructor
public class FileData extends AbsEntity {

    private String fileName;

    private byte[] fileData;

    private long size;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Task task;

    @ManyToOne
    private User user;

    private String description;

    public String getFileType() {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "unknown";
    }
}
