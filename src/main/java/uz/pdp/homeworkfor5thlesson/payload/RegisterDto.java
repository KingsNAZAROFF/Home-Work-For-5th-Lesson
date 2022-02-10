package uz.pdp.homeworkfor5thlesson.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @Size(min = 3,max = 50)
    @NotNull
    private String firstName;//ismi

    @NotNull
    @Size(min = 3,max = 50)
    private String lastName;//familyasi

    @NotNull
    @Email
    private String email;//userning emaili //USERNAME sifatida ketadi


}
