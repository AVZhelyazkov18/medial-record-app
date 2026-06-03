package bg.nbu.medialrecordapp.data.dto.MedicalRecord;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MedicalRecordInsertRequestDTO {
    LocalDate checkoutDate;
    String diagnosis;
    String treatment;
    BigDecimal appointmentPrice;
}
