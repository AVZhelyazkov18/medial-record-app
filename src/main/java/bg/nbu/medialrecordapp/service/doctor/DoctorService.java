package bg.nbu.medialrecordapp.service.doctor;

import bg.nbu.medialrecordapp.data.dto.Doctor.DoctorHomeResponseDTO;
import bg.nbu.medialrecordapp.data.dto.Doctor.DoctorInsertRequestDTO;
import bg.nbu.medialrecordapp.data.entity.Doctor;
import bg.nbu.medialrecordapp.service.UIInterfaceMethods;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DoctorService extends UIInterfaceMethods<Doctor> {
    Doctor getDoctorFromAuthentication();
    Doctor findByEmail(String email);
    Map<String, Long> getTop3DoctorsByFitNotes();
    List<Doctor> findAll();
    DoctorHomeResponseDTO getCurrentDoctorHomeInfo();
    Doctor save(DoctorInsertRequestDTO dto);
    Set<Doctor> getAllDoctors();
}
