package dev.aj.service;

import dev.aj.dao.ApplicationDao;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationDao applicationDao;

    public Double addGradeResultsForSingleClass(List<Double> listOfGrades) {
       return applicationDao.addGradeResultsForSingleClass(listOfGrades);
    }

    public Double findGradePointAverage(List<Double> listOfGrades) {
        return applicationDao.findGradePointAverage(listOfGrades);
    }

    public Object nullSafeObject(Object object) {
       return applicationDao.checkForNull(object);
    }
}
