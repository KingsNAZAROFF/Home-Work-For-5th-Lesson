package uz.pdp.homeworkfor5thlesson.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.homeworkfor5thlesson.entity.Role;
import uz.pdp.homeworkfor5thlesson.entity.Turniket;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDtoForSend {
    private String firstName;


    private String lastName;


    private String email;

    private Set<Role> roles;

    private List<Turniket> turniketList;
}
