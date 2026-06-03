package bg.nbu.medialrecordapp.repository;

import bg.nbu.medialrecordapp.data.entity.Doctor;
import bg.nbu.medialrecordapp.data.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByWebAccountEmail(String email);

    long countByGeneralPractitioner(Doctor doctor);
}
