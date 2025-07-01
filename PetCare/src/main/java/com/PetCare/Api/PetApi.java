package com.PetCare.Api;

import com.PetCare.Entity.Pet;
import com.PetCare.Service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetApi {
    @Autowired
    private PetService petService;

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or #customerId == principal.id")
    public ResponseEntity<List<Pet>> getPetsForCustomer(@PathVariable int customerId) {
        List<Pet> pets = petService.getPetsByCustomer(customerId);
        return ResponseEntity.ok(pets);
    }

    @PostMapping
    public ResponseEntity<String> createPet(@RequestBody Pet pet) {
        petService.createPet(pet);
        return ResponseEntity.ok("Pet profile created successfully.");
    }

    @PutMapping("/{petId}")
    public ResponseEntity<String> updatePet(@PathVariable int petId, @RequestBody Pet pet) {
        pet.setPetId(petId);
        try {
            petService.updatePet(pet);
            return ResponseEntity.ok("Pet profile updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{petId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePet(@PathVariable int petId) {
        petService.deletePet(petId);
        return ResponseEntity.ok("Pet profile deleted successfully.");
    }
}
