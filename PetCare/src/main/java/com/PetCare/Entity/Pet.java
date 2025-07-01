package com.PetCare.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("pets")
public class Pet {
    @Id
    private Integer petId;
    private Integer customerId;
    private String name;
    private String species;
    private String breed;
    private LocalDate dateOfBirth;
    private String medicalNotes;
}
