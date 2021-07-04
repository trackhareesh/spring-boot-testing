package com.myspringprojects.rnd.springboottesting.repositories;


import com.myspringprojects.rnd.springboottesting.domain.SuperHero;

import java.util.Optional;

public interface SuperHeroRepository {

    /**
     * Find the super hero by id. If the super hero does not exist, throw
     * NonExistingHeroException.
     *
     * @param id super hero id.
     *
     * @return The super hero domain object.
     */
    SuperHero getSuperHero(int id);

    /**
     * Retrieves a super hero by name.
     *
     * @param name the name of the super hero.
     *
     * @return the super hero domain object.
     */
    Optional<SuperHero> getSuperHero(String name);

    /**
     * Saves the super hero.
     *
     * @param superHero the super hero domain object.
     */
    void saveSuperHero(SuperHero superHero);

}
