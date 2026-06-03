package bg.nbu.medialrecordapp.service.doctor;

import bg.nbu.medialrecordapp.data.dto.Doctor.DoctorAppointmentHomeDTO;
import bg.nbu.medialrecordapp.data.dto.Doctor.DoctorHomeResponseDTO;
import bg.nbu.medialrecordapp.data.dto.Doctor.DoctorInsertRequestDTO;
import bg.nbu.medialrecordapp.data.entity.Doctor;
import bg.nbu.medialrecordapp.data.entity.MedicalRecord;
import bg.nbu.medialrecordapp.data.entity.Patient;
import bg.nbu.medialrecordapp.enums.MedicalRecordStatus;
import bg.nbu.medialrecordapp.repository.DoctorRepository;
import bg.nbu.medialrecordapp.repository.MedicalRecordRepository;
import bg.nbu.medialrecordapp.repository.PatientRepository;
import bg.nbu.medialrecordapp.service.UIInterfaceMethods;
import bg.nbu.medialrecordapp.service.auth.WebAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorService implements UIInterfaceMethods<Doctor> {
    private final DoctorRepository doctorRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final WebAccountService webAccountService;

    public Set<Doctor> getAllDoctors() {
        return new HashSet<>(doctorRepository.findAll());
    }

    public Doctor getDoctorFromAuthentication() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null)
            return null;

        String email = authentication.getName();

        return doctorRepository.findByWebAccountEmail(email).orElse(null);
    }

    public DoctorHomeResponseDTO getCurrentDoctorHomeInfo() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        Doctor doctor = doctorRepository.findByWebAccountEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found for logged-in user"));

        BigDecimal doctorEarnings = medicalRecordRepository.sumAppointmentPriceByDoctor(doctor);
        String mostFoundDiagnosis = medicalRecordRepository.findMostFoundDiagnosisByDoctor(doctor);
        long gpPatientsCount = patientRepository.countByGeneralPractitioner(doctor);
        long medicalRecordsCount = medicalRecordRepository.countByDoctor(doctor);

        List<DoctorAppointmentHomeDTO> nextAppointments = medicalRecordRepository
                .findTop3ByDoctorAndStatusOrderByDateAsc(doctor, MedicalRecordStatus.SCHEDULED)
                .stream()
                .map(record -> new DoctorAppointmentHomeDTO(
                        record.getId(),
                        String.format("%s %s",
                                record.getPatient().getFirstName(),
                                record.getPatient().getLastName()
                        ),
                        record.getDate()
                ))
                .toList();



        if (doctorEarnings == null) {
            doctorEarnings = BigDecimal.ZERO;
        }

        return new DoctorHomeResponseDTO(
                doctor.getFirstName(),
                doctor.getLastName(),
                nextAppointments,
                mostFoundDiagnosis,
                gpPatientsCount,
                medicalRecordsCount,
                doctorEarnings
        );
    }

    public Map<String, Long> getTop3DoctorsByFitNotes() {
        List<Object[]> results = doctorRepository.findDoctorsWithFitNoteCounts();
        return results.stream()
                .limit(3)
                .collect(Collectors.toMap(
                        obj -> String.format("%s %s", ((Doctor) obj[0]).getFirstName(), ((Doctor) obj[0]).getLastName()),
                        obj -> (Long) obj[1]
                ));
    }

    public Doctor findByEmail(String email) {
        return doctorRepository.findByWebAccountEmail(email).orElse(null);
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Override
    public void delete(Doctor doctor) {
        doctorRepository.delete(doctor);
    }

    @Override
    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor save(DoctorInsertRequestDTO dto) {
        Doctor doctor = new Doctor();

        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setSpecialty(dto.getSpecialty());
        doctor.setQualifiedForGeneralPractitioner(dto.isQualifiedForGeneralPractitioner());
        doctor.setWebAccount(null);

        return doctorRepository.save(doctor);
    }
}
