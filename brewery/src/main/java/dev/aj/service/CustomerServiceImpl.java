package dev.aj.service;

import dev.aj.domain.model.CustomerDto;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Override
    public CustomerDto getCustomerById(UUID customerId) {
        return CustomerDto.builder()
                          .id(customerId)
                          .firstName("AJ")
                          .lastName("B")
                          .build();
    }
}
