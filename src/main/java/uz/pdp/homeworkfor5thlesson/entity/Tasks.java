package uz.pdp.homeworkfor5thlesson.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Tasks {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(length = 10000)
    private String taskName;
    private String taskText;

    @CreationTimestamp
    private Timestamp startTime;

    private Timestamp endTime;

    private boolean done=false;

    private String taskCode;

    @LastModifiedDate
    private Timestamp doneTime;

    private String workerEmail;
    

    @CreatedBy
    private UUID givenBy;

}
