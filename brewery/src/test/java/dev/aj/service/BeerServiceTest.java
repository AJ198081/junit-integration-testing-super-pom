package dev.aj.service;

import dev.aj.domain.model.BeerDto;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
class BeerServiceTest {

    public static final long UPC = 8974322893L;
    public static final UUID RANDOM_UUID = UUID.randomUUID();
    private BeerDto beer;

    @Mock
    private BeerServiceImpl beerService;

    @BeforeEach
    void setUp() {

        beer = BeerDto.builder()
                      .id(RANDOM_UUID)
                      .beerName("Galaxy Cat")
                      .beerStyle("Pale Ale")
                      .upc(UPC)
                      .build();

//        beerService = new BeerServiceImpl();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getBeerById() {

        Mockito.when(beerService.getBeerById(argThat(argument -> argument.equals(RANDOM_UUID))))
               .thenReturn(beer);

        Assertions.assertThat(beerService.getBeerById(RANDOM_UUID))
                  .extracting(BeerDto::getUpc, BeerDto::getId)
                  .contains(UPC, RANDOM_UUID);

    }
}