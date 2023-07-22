package dev.aj.service;

import dev.aj.domain.model.BeerDto;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BeerServiceImpl implements BeerService {

    @Override
    public BeerDto getBeerById(UUID beerId) {
        return BeerDto.builder()
                      .id(beerId)
                      .beerName("Galaxy Cat")
                      .beerStyle("Pale Ale")
                      .upc(8974322893L)
                      .build();
    }
}
