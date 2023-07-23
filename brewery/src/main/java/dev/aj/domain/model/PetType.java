package dev.aj.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class PetType extends BaseEntity {
    private String name;

    @Builder
    public PetType(Long id, String name) {
        super(id);
        this.name = name;
    }
}
