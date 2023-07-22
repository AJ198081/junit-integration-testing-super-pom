package dev.aj.domain.model;

import jakarta.validation.constraints.PositiveOrZero;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Money {

    @PositiveOrZero
    protected double amount;
    protected Currency currency;

    public static Dollar getDollar(double amount) {
        return new Dollar(amount);
    }

    public static Franc getFranc(double amount) {
        return new Franc(amount);
    }

    public Money times(double multiplier) {
        this.amount *= multiplier;
        return this;
    }

    public double getAmount() {
        return this.amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount == money.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
