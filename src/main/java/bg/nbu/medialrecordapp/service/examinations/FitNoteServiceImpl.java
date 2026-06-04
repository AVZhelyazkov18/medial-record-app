package bg.nbu.medialrecordapp.service.examinations;

import bg.nbu.medialrecordapp.data.entity.FitNote;
import bg.nbu.medialrecordapp.data.entity.MedicalRecord;
import bg.nbu.medialrecordapp.repository.FitNoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
@AllArgsConstructor
public class FitNoteServiceImpl implements FitNoteService {
    private FitNoteRepository fitNoteRepository;

    @Override
    public void delete(FitNote fitNote) {
        fitNoteRepository.delete(fitNote);
    }

    @Override
    public FitNote save(FitNote fitNote) {
        return fitNoteRepository.save(fitNote);
    }

    @Override
    public FitNote create(MedicalRecord record, LocalDate date, long days) {
        FitNote fitNote = new FitNote(record, date, days);
        return fitNoteRepository.save(fitNote);
    }

    @Override
    public String getMonthWithMostFitNotes() {
        List<Object[]> months = fitNoteRepository.findMonthsOrderByFitNoteCountDesc();

        if (months.isEmpty()) return "N/A";
        Object[] firstRow = months.getFirst();

        Integer monthNumber = (Integer) firstRow[0];
        return Month.of(monthNumber).name();
    }

    @Override
    public FitNote findById(Long Id) {
        return fitNoteRepository.findById(Id).orElse(null);
    }

    @Override
    public List<FitNote> findAll() {
        return fitNoteRepository.findAll();
    }
}
