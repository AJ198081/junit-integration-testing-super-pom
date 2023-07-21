package dev.aj.rest.exception_handlers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class StudentOrGradeErrorResponse {
    private int status;
    private String message;
    private long timeStamp;

}
