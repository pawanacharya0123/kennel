package com.kennel.backend.pets;

import com.kennel.backend.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/pets")
public class PetController {
    private final PetRepository petRepository;

    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

//    @AuthenticationPrincipal UserPrincipal userPrincipal
    @GetMapping
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @PostMapping
    public Pet createPet(@RequestBody Pet pet) {
        System.out.println("Received: " + pet.getName()+ ", " + pet.getType() + ", " + pet.getAge());
        return petRepository.save(pet);
    }

}
