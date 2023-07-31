package dev.aj.domain.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsDto {

    @Size(min = 2, message = "First name must not be less than 2 characters")
    private String firstName;

    @Size(min = 2, message = "Last name must not be less than 2 characters")
    private String lastName;


    private String email;

    @Size(min = 8, max = 16, message = "Password must be equal to or greater than 8 characters and less than 16 characters")
    private String password;

    @Size(min = 8, max = 16, message = "Repeat Password must be equal to or greater than 8 characters and less than 16 characters")
    private String repeatPassword;

}
