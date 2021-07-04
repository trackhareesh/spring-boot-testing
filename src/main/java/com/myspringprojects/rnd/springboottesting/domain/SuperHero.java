package com.myspringprojects.rnd.springboottesting.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The SuperHero domain class. Use Lombok @Data annotation to create Getters, Setters, Equals and HashCode.
 * Also, generate a No arguments constructor and an All Arguments constructor.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SuperHero {

    private String firstName;
    private String lastName;
    private String heroName;

}
