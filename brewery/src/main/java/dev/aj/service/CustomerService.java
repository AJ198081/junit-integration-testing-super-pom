package dev.aj.service;

import dev.aj.domain.model.CustomerDto;
import java.util.UUID;

public interface CustomerService {
    CustomerDto getCustomerById(UUID customerId);
}
