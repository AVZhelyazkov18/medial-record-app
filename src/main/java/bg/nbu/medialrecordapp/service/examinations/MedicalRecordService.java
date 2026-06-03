package bg.nbu.medialrecordapp.service.examinations;

import bg.nbu.medialrecordapp.data.dto.MedicalRecord.MedicalRecordInsertRequestDTO;
import bg.nbu.medialrecordapp.data.dto.MedicalRecord.MedicalRecordPatientResponseDTO;
import bg.nbu.medialrecordapp.data.entity.Doctor;
import bg.nbu.medialrecordapp.data.entity.MedicalRecord;
import bg.nbu.medialrecordapp.data.entity.Patient;
import bg.nbu.medialrecordapp.enums.MedicalRecordStatus;
import bg.nbu.medialrecordapp.repository.MedicalRecordRepository;
import bg.nbu.medialrecordapp.service.UIInterfaceMethods;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MedicalRecordService implements UIInterfaceMethods<MedicalRecord> {
    private final MedicalRecordRepository medicalRecordRepository;

    @Override
    public void delete(MedicalRecord medicalRecord) {
        medicalRecordRepository.delete(medicalRecord);
    }

    public MedicalRecord save(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id).orElse(null);
    }

    public List<MedicalRecordPatientResponseDTO> getCurrentPatientMedicalRecords() {
        List<MedicalRecord> records =  medicalRecordRepository.findByPatientWebAccountEmailOrderByDateDesc(
                getCurrentUserEmail()
        );

        List<MedicalRecordPatientResponseDTO> dtoRecords = new ArrayList<>();
        for (MedicalRecord record : records) {
            String diagnosis = record.getDiagnosis() != null ? record.getDiagnosis() : "No diagnosis";
            String treatment = record.getTreatment() != null ? record.getTreatment() : "No treatment";

            LocalDate issuedOn = record.getFitNote() != null ? record.getFitNote().getIssuedOn() : null;
            Long leaveDays = record.getFitNote() != null ? record.getFitNote().getLeaveDays() : null;

            dtoRecords.add(new MedicalRecordPatientResponseDTO(
                    record.getDate(),
                    record.getDoctor().getFirstName(),
                    record.getDoctor().getLastName(),
                    diagnosis,
                    treatment,
                    issuedOn,
                    leaveDays
            ));
        }

        return dtoRecords;
    }

    private String getCurrentUserEmail() {
        return org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    public void createMedicalRecord(Doctor doctor, LocalDate date, Patient patient) {
        MedicalRecord record = new MedicalRecord(doctor, date, patient);

        medicalRecordRepository.save(record);
    }

    public List<MedicalRecord> findAll() {
        return this.medicalRecordRepository.findAll();
    }

    public MedicalRecord save(MedicalRecordInsertRequestDTO dto) {
        MedicalRecord record = new MedicalRecord();

        record.setDate(dto.getCheckoutDate());
        record.setDiagnosis(dto.getDiagnosis());
        record.setTreatment(dto.getTreatment());
        record.setAppointmentPrice(dto.getAppointmentPrice());
        record.setStatus(MedicalRecordStatus.SCHEDULED);

        return medicalRecordRepository.save(record);
    }
}