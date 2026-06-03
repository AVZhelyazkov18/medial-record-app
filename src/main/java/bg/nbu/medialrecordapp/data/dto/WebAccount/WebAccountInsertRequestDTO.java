package bg.nbu.medialrecordapp.data.dto.WebAccount;

import bg.nbu.medialrecordapp.enums.auth.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class WebAccountInsertRequestDTO {
    private String email;
    private Role role;
}
