package com.myspringprojects.rnd.springboottesting.controllers;

import com.myspringprojects.rnd.springboottesting.domain.SuperHero;
import com.myspringprojects.rnd.springboottesting.repositories.SuperHeroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * Here, we initialize the full Spring Context and start a real web server on a random port.
 * We do not use the MockMvc anymore. Here, we use TestRestTemplate. We can use the usual RestTemplate
 * as well, but the TestRestTemplate has test friendly methods.
 *
 * Here, we have a choice to either use the original Repository object or a mocked one. If we mock the
 * repository using the @MockBean, the actual repository object initialized, will be replaced with the
 * mock bean. If we do not mock the bean, then the original bean will be used, there by making this
 * an acceptance test of the controller methods.
 *
 * The context created is reused across tests, in order to minimize the execution of individual tests.
 *
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Spring Boot Integration Test with Full Context and Actual WebServer started.")
class SuperHeroControllerWithFullContextAcceptanceTest {

    // Mock bean is equivalent to Mockito Mock. But, in addition to creating a mock, the @MockBean annotation
    // includes this object in the spring context as well.
    @MockBean
    private SuperHeroRepository superHeroRepository;

    // Rest template to fire requests.
    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    @DisplayName("Given a superhero ID exists in the system, When GET /supeheroes/{id} " +
            "is invoked with that ID, Then the corresponding SuperHero is returned.")
    void canRetrieveByIdWhenExists() {

        // Firstly, stub the getSuperHero() method on the SuperHeroRepository so that we can use it
        // in the unit test.
        given(superHeroRepository.getSuperHero(2))
                .willReturn(new SuperHero("Rob", "Mannon", "RobotMan"));

        ResponseEntity<SuperHero> response = restTemplate.getForEntity("/superheroes/2", SuperHero.class);

        // Perform the final assertions.
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(new SuperHero("Rob", "Mannon","RobotMan"));

        // Also verify that the getSuperHero method is invoked on the repository.
        // We use the BDDMockito verification methods then and should, instead of Mockito.verify.
        // It has the same effect, but more readable.
        then(superHeroRepository).should().getSuperHero(2);

    }
}