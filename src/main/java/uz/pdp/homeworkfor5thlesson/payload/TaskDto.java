package uz.pdp.homeworkfor5thlesson.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private String taskName;
    @Size(max = 10000)
    private String taskText;


    private long endTime;

    private String workerEmail;
}
