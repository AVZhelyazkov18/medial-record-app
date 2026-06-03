package bg.nbu.medialrecordapp.repository;

import bg.nbu.medialrecordapp.data.entity.Doctor;
import bg.nbu.medialrecordapp.data.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByWebAccountEmail(String email);

    @Query("SELECT mr.doctor, COUNT(f) AS fitNoteCount " +
            "FROM MedicalRecord mr " +
            "LEFT JOIN mr.fitNote f " +
            "GROUP BY mr.doctor " +
            "ORDER BY fitNoteCount DESC")
    List<Object[]> findDoctorsWithFitNoteCounts();
}
