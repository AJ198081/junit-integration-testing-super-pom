package rest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "student")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CollegeStudent implements Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @JsonProperty("first_name")
    private String firstName;

    private String lastName;

    private String email;

    @Override
    public String studentInformation() {
        return String.format("Student %s %s, email %s", firstName, lastName, email);
    }

    @Override
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
