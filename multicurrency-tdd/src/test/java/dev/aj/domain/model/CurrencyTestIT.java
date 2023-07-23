package dev.aj.domain.model;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CurrencyTestIT {



    @ParameterizedTest
    @EnumSource
    void getByName(Currency currency) {
        Assertions.assertThat(currency.getName())
                  .isIn(Arrays.stream(currency.values())
                              .map(Currency::getName)
                              .collect(Collectors.toList()));
    }

    @ParameterizedTest
    @EnumSource(value = Currency.class)
    void valueOf(Currency currency) {
        Assertions.assertThat(currency)
                  .isIn(currency.values());

    }
}