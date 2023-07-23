package dev.aj.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Visit extends BaseEntity {

    private LocalDate visitDate;

    private String description;

    @OneToOne
    private Pet pet;

    @Builder
    public Visit(Long id, LocalDate visitDate, String description, Pet pet) {
        super(id);
        this.visitDate = visitDate;
        this.description = description;
        this.pet = pet;
    }
}
