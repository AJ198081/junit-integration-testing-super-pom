package dev.aj.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dollar {

    private int amount;

    public Dollar times(int multiplier) {
        this.amount *= multiplier;
        return this;
    }
}
