package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Project extends AbsEntity {
    private String name;
    private String description;
    private boolean expired;
    private boolean isProduction;
    private double budget;
    private int process;
    private Date startDate;
    private Date endDate;
    private Date deadline = new Date();
    @ManyToOne
    private ProjectType projectType;
    @ManyToOne
    private Customer customer;
    @ManyToMany
    private List<User> users;
    @ManyToMany
    private List<FileData> fileDataList;
    @ManyToOne
    private ProjectStatus projectStatus;
    @ManyToMany
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Stage> stageList;
    @ManyToOne
    private Branch branch;
}
