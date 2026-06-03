package bg.nbu.medialrecordapp.data.dto.MedicalRecord;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class MedicalRecordPatientResponseDTO {
    private LocalDate checkoutDate;
    private String doctorFirstName;
    private String doctorLastName;
    private String diagnosis;
    private String treatment;
    private LocalDate fitNoteIssuedOn;
    private Long fitNoteLeaveDays;
}
