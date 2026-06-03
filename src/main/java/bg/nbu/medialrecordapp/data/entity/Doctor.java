package bg.nbu.medialrecordapp.data.entity;

import bg.nbu.medialrecordapp.data.entity.auth.WebAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "doctor")
public class Doctor extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "qualified_for_gp")
    private boolean qualifiedForGeneralPractitioner;

    @OneToMany(mappedBy = "generalPractitioner", fetch = FetchType.EAGER)
    private Set<Patient> patients;

    @OneToOne
    @JoinColumn(name = "web_account_id", unique = true)
    private WebAccount webAccount;
}
