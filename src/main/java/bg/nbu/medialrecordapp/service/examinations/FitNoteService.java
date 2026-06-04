package bg.nbu.medialrecordapp.service.examinations;

import bg.nbu.medialrecordapp.data.entity.FitNote;
import bg.nbu.medialrecordapp.data.entity.MedicalRecord;
import bg.nbu.medialrecordapp.service.UIInterfaceMethods;

import java.time.LocalDate;
import java.util.List;

public interface FitNoteService extends UIInterfaceMethods<FitNote> {
    FitNote create(MedicalRecord record, LocalDate date, long days);
    String getMonthWithMostFitNotes();
    FitNote findById(Long Id);
    List<FitNote> findAll();
}
