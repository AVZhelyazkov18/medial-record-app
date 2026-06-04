package bg.nbu.medialrecordapp.service.patient;

import bg.nbu.medialrecordapp.data.dto.Patient.PatientHomeResponseDTO;
import bg.nbu.medialrecordapp.data.dto.Patient.PatientSearchResponseDTO;
import bg.nbu.medialrecordapp.data.dto.RegisterPatientRequestDTO;
import bg.nbu.medialrecordapp.data.entity.Patient;
import bg.nbu.medialrecordapp.data.entity.auth.WebAccount;
import bg.nbu.medialrecordapp.service.UIInterfaceMethods;

import java.util.Set;

public interface PatientService extends UIInterfaceMethods<Patient> {
    Set<PatientSearchResponseDTO> getAllPatients();
    Patient registerPatientWithAccount(RegisterPatientRequestDTO registerPatientRequestDTO, WebAccount account);
    Patient getPatientFromAuthentication();
    PatientHomeResponseDTO getCurrentPatientHomeInfo();
    Patient findByEmail(String email);
}
