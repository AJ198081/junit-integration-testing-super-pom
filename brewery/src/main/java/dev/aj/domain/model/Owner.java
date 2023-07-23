package dev.aj.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
public class Owner extends Person {

    private String streetAddress;
    private String city;
    private String telephone;

    @OneToMany
    private Set<Pet> pets = new HashSet<>();

    @Builder
    public Owner(Long id, String firstName, String lastName, String streetAddress, String city, String telephone, Set<Pet> pets) {
        super(id, firstName, lastName);
        this.streetAddress = streetAddress;
        this.city = city;
        this.telephone = telephone;
        this.pets = pets;
    }
}
