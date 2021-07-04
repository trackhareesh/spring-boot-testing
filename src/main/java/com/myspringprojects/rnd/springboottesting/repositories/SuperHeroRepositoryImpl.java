package com.myspringprojects.rnd.springboottesting.repositories;


import com.myspringprojects.rnd.springboottesting.domain.SuperHero;
import com.myspringprojects.rnd.springboottesting.exception.NonExistingHeroException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Maintains a ArrayList of superheroes. This implementation is only for testing.
 */
@Component
public class SuperHeroRepositoryImpl implements SuperHeroRepository {

    private final List<SuperHero> superHeroes;

    public SuperHeroRepositoryImpl() {
        this.superHeroes = new ArrayList<>();
        superHeroes.add(new SuperHero("Jean", "Grey", "Phoenix"));
        superHeroes.add(new SuperHero("Bruce", "Wayne", "Batman"));
        superHeroes.add(new SuperHero("Susan", "Storm", "Invisible woman"));
        superHeroes.add(new SuperHero("Peter", "Parker", "Spiderman"));
    }

    @Override
    public SuperHero getSuperHero(int id) {
        if (id > superHeroes.size()) throw new NonExistingHeroException();
        return superHeroes.get(id - 1);
    }

    @Override
    public Optional<SuperHero> getSuperHero(String name) {
        return superHeroes.stream().filter(h -> h.getHeroName().equals(name)).findAny();
    }

    @Override
    public void saveSuperHero(SuperHero superHero) {
        superHeroes.add(superHero);
    }
}
