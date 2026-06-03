package bg.nbu.medialrecordapp.data.dto;

import bg.nbu.medialrecordapp.validation.PasswordMatches;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@PasswordMatches
public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
}