package bg.nbu.medialrecordapp.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterPatientRequestDTO {
    private String firstName;
    private String lastName;
}
