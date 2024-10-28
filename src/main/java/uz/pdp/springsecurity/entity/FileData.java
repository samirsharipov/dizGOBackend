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
}
