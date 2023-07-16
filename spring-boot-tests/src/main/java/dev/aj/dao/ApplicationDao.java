package dev.aj.dao;

import java.util.List;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public interface ApplicationDao {
    Double addGradeResultsForSingleClass(List<Double> listOfGrades);

    Double findGradePointAverage(List<Double> listOfGrades);

    Object checkForNull(Object objectToBeCheckedForNull);
}
