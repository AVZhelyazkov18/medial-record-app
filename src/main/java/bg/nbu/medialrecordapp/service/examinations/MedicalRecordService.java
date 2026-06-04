package bg.nbu.medialrecordapp.service.examinations;

import bg.nbu.medialrecordapp.data.dto.MedicalRecord.MedicalRecordInsertRequestDTO;
import bg.nbu.medialrecordapp.data.dto.MedicalRecord.MedicalRecordPatientResponseDTO;
import bg.nbu.medialrecordapp.data.entity.Doctor;
import bg.nbu.medialrecordapp.data.entity.MedicalRecord;
import bg.nbu.medialrecordapp.data.entity.Patient;
import bg.nbu.medialrecordapp.service.UIInterfaceMethods;

import java.time.LocalDate;
import java.util.List;

public interface MedicalRecordService extends UIInterfaceMethods<MedicalRecord> {
    MedicalRecord save(MedicalRecord medicalRecord);
    MedicalRecord getMedicalRecordById(Long id);
    List<MedicalRecordPatientResponseDTO> getCurrentPatientMedicalRecords();
    void createMedicalRecord(Doctor doctor, LocalDate date, Patient patient);
    List<MedicalRecord> findAll();
    MedicalRecord save(MedicalRecordInsertRequestDTO dto);
}
