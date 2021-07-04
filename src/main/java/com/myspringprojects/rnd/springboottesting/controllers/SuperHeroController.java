package com.myspringprojects.rnd.springboottesting.controllers;

import com.myspringprojects.rnd.springboottesting.domain.SuperHero;
import com.myspringprojects.rnd.springboottesting.repositories.SuperHeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/superheroes")
public class SuperHeroController {

    @Autowired
    private SuperHeroRepository superHeroRepository;

    @GetMapping("/{id}")
    public SuperHero getSuperHeroById(@PathVariable int id) {
        return superHeroRepository.getSuperHero(id);
    }

    @GetMapping
    public Optional<SuperHero> getSuperHeroByName(@RequestParam("name") String name) {
        return superHeroRepository.getSuperHero(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addSuperHero(@RequestBody SuperHero superHero) {
        superHeroRepository.saveSuperHero(superHero);
    }

}
