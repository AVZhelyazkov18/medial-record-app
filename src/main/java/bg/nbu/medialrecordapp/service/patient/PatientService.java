package bg.nbu.medialrecordapp.service.patient;

import bg.nbu.medialrecordapp.data.dto.Patient.PatientHomeResponseDTO;
import bg.nbu.medialrecordapp.data.dto.Patient.PatientSearchResponseDTO;
import bg.nbu.medialrecordapp.data.dto.RegisterPatientRequestDTO;
import bg.nbu.medialrecordapp.data.entity.Doctor;
import bg.nbu.medialrecordapp.data.entity.Patient;
import bg.nbu.medialrecordapp.data.entity.auth.WebAccount;
import bg.nbu.medialrecordapp.repository.PatientRepository;
import bg.nbu.medialrecordapp.service.UIInterfaceMethods;
import bg.nbu.medialrecordapp.service.auth.WebAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class PatientService implements UIInterfaceMethods<Patient> {
    private final PatientRepository patientRepository;

    public Set<PatientSearchResponseDTO> getAllPatients() {
        Set<PatientSearchResponseDTO> patients = new HashSet<>();

        patientRepository.findAll().forEach(patient ->
                patients.add(new PatientSearchResponseDTO(
                        patient.getFirstName(),
                        patient.getLastName(),
                        patient.getWebAccount().getEmail(),
                        patient.getHealthInsuranceStatus(),
                        patient.getGeneralPractitioner()
                ))
        );

        return patients;
    }

    public Patient registerPatientWithAccount(RegisterPatientRequestDTO registerPatientRequestDTO, WebAccount account) {
        Patient patient = new Patient(registerPatientRequestDTO, account);

        patientRepository.save(patient);

        return patient;
    }

    public Patient getPatientFromAuthentication() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null)
            return null;

        String email = authentication.getName();

        return patientRepository.findByWebAccountEmail(email).orElse(null);
    }

    public PatientHomeResponseDTO getCurrentPatientHomeInfo() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        Patient patient = patientRepository.findByWebAccountEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found for logged-in user"));

        if (patient.getGeneralPractitioner() == null) {
            return new PatientHomeResponseDTO(
                    patient.getFirstName(),
                    patient.getLastName(),
                    null,
                    null,
                    patient.getHealthInsuranceStatus()
            );
        }
        return new PatientHomeResponseDTO(
                patient.getFirstName(),
                patient.getLastName(),
                patient.getGeneralPractitioner().getFirstName(),
                patient.getGeneralPractitioner().getLastName(),
                patient.getHealthInsuranceStatus()
        );
    }

    @Override
    public void delete(Patient object) {
        patientRepository.delete(object);
    }

    @Override
    public Patient save(Patient object) {
        return patientRepository.save(object);
    }

    public Patient findByEmail(String email) {
        return patientRepository.findByWebAccountEmail(email).orElse(null);
    }
}
