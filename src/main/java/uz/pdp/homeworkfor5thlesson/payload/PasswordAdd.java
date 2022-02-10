package uz.pdp.homeworkfor5thlesson.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordAdd {
    private String password;
    private String passwordCheck;
}
