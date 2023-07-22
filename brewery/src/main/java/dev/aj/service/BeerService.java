package dev.aj.service;

import dev.aj.domain.model.BeerDto;
import java.util.UUID;
import org.springframework.stereotype.Service;

public interface BeerService {
    BeerDto getBeerById(UUID beerId);
}
