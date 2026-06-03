package bg.nbu.medialrecordapp.repository;

import bg.nbu.medialrecordapp.data.entity.Doctor;
import bg.nbu.medialrecordapp.data.entity.MedicalRecord;
import bg.nbu.medialrecordapp.enums.MedicalRecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientWebAccountEmailOrderByDateDesc(String email);

    long countByDoctor(Doctor doctor);

    @Query("""
            SELECT COALESCE(SUM(m.appointmentPrice), 0) FROM MedicalRecord m
            WHERE m.doctor = :doctor
            """)
    BigDecimal sumAppointmentPriceByDoctor(Doctor doctor);

    @Query("""
            SELECT m.diagnosis FROM MedicalRecord m
            WHERE m.doctor = :doctor AND m.diagnosis is not null AND m.diagnosis <> ''
            GROUP BY m.diagnosis
            ORDER BY COUNT(m.diagnosis) DESC
            """)
    String findMostFoundDiagnosisByDoctor(Doctor doctor);

    List<MedicalRecord> findTop3ByDoctorAndStatusOrderByDateAsc(
            Doctor doctor,
            MedicalRecordStatus status
    );
}
