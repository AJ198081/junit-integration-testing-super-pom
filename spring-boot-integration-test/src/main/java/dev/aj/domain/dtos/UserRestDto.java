package dev.aj.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRestDto {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
}
