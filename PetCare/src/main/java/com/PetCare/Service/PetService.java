package com.PetCare.Service;

import com.PetCare.Entity.Pet;
import com.PetCare.Repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    public int createPet(Pet pet) {
        return petRepository.save(pet);
    }

    public java.util.List<Pet> getPetsByCustomer(int customerId) {
        return petRepository.findAllByCustomerId(customerId);
    }

    public int updatePet(Pet pet) {
        petRepository.findById(pet.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + pet.getPetId()));
        return petRepository.update(pet);
    }

    public int deletePet(int petId) {
        return petRepository.deleteById(petId);
    }
}
