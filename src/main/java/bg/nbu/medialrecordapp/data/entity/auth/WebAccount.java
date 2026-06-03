package bg.nbu.medialrecordapp.data.entity.auth;

import bg.nbu.medialrecordapp.data.dto.RegisterRequestDTO;
import bg.nbu.medialrecordapp.data.entity.BaseEntity;
import bg.nbu.medialrecordapp.data.entity.Doctor;
import bg.nbu.medialrecordapp.data.entity.Patient;
import bg.nbu.medialrecordapp.enums.auth.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WebAccount extends BaseEntity {
    public WebAccount(RegisterRequestDTO registerRequestDTO) {
        this.email = registerRequestDTO.getEmail();
        this.password = registerRequestDTO.getPassword();
        this.role = Role.PATIENT;
    }

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "webAccount")
    private Patient patient;

    @OneToOne(mappedBy = "webAccount")
    private Doctor doctor;
}
