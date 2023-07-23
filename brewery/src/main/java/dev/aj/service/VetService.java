package dev.aj.service;

import dev.aj.domain.model.Vet;
import java.util.Set;

public interface VetService {
    Set<Vet> findAll();

    Vet findById(Long id);

    Vet save(Vet vet);
}
