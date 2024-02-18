package uz.pdp.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.Importance;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task extends AbsEntity {
    private String name;
    @ManyToOne
    private TaskType taskType;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;
    @ManyToOne
    private Stage stage;
    private Date startDate;
    private Date EndDate;
    private boolean expired;
    private Date deadLine;
    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<TaskPrice> taskPriceList;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TaskStatus taskStatus;
    @Enumerated(EnumType.STRING)
    private Importance importance;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Task dependTask;
    @Column(nullable = false)
    private boolean isProductions;
    @ManyToOne(cascade = CascadeType.REMOVE)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Production production;
    @ManyToOne
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Content content;
    private double goalAmount;
    private double taskPrice;
    private boolean given = false;
    @ManyToOne
    private Branch branch;

    private String description;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "task")
    @JsonIgnore
    private List<FileData> fileDataList;

}


