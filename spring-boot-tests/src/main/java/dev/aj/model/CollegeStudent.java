package dev.aj.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("collegeStudent")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CollegeStudent implements Student {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private StudentGrades studentGrades;

    @Override
    public String studentInformation() {
        return null;
    }

    @Override
    public String getFullName() {
        return null;
    }

    private String getEmailDomain() {
        return this.email.split("@")[1];
    }
}
