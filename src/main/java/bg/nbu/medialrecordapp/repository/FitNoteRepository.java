package bg.nbu.medialrecordapp.repository;

import bg.nbu.medialrecordapp.data.entity.FitNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FitNoteRepository extends JpaRepository<FitNote, Long> {
    @Query("SELECT FUNCTION('MONTH', f.issuedOn), COUNT(f) " +
            "FROM FitNote f " +
            "GROUP BY FUNCTION('MONTH', f.issuedOn) " +
            "ORDER BY COUNT(f) DESC")
    List<Object[]> findMonthsOrderByFitNoteCountDesc();
}
