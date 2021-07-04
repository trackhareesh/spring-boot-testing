package com.myspringprojects.rnd.springboottesting.controllers;

import com.myspringprojects.rnd.springboottesting.domain.SuperHero;
import com.myspringprojects.rnd.springboottesting.repositories.SuperHeroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * This is equivalent to the @WebMvcTest scenario, where a partial Spring Context was populated. But, in this case,
 * the full Spring Context is loaded. But, the Web Environment is still a MOCK. (i.e. no web server is started). Hence,
 * we cannot use a RestTemplate to fire Http Requests. Still we use the MockMvc.perform method to fire requests
 * against the mock web server set up by the tests. Here, we need to specify the @AutoConfigureMockMvc to initialize
 * a MockMvc object.
 *
 */
@AutoConfigureJsonTesters
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DisplayName("Spring Boot Unit Test with the full Spring Context and a Mock Web Server.")
class SuperHeroControllerMockMvcWithFullContextTest {

    // Since the webserver environment is not real in this case
    @Autowired
    private MockMvc mockMvc;

    // Mock bean is equivalent to Mockito Mock. But, in addition to creating a mock, the @MockBean annotation
    // includes this object in the spring context as well.
    @MockBean
    private SuperHeroRepository superHeroRepository;

    // Can be autowired once we use the AutoConfigureJsonTesters at class level.
    @Autowired
    private JacksonTester<SuperHero> jsonSuperHero;


    @Test
    @DisplayName("Given a superhero ID exists in the system, When GET /supeheroes/{id} " +
            "is invoked with that ID, Then the corresponding SuperHero is returned.")
    void canRetrieveByIdWhenExists() throws Exception {

        // Firstly, stub the getSuperHero() method on the SuperHeroRepository so that we can use it
        // in the unit test.
        given(superHeroRepository.getSuperHero(2))
                .willReturn(new SuperHero("Rob", "Mannon", "RobotMan"));

        MockHttpServletResponse response = mockMvc.perform(
                get("/superheroes/2")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Perform the final assertions.
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(
                        jsonSuperHero.write(
                                new SuperHero("Rob", "Mannon", "RobotMan")
                        ).getJson()
                );

        // Also verify that the getSuperHero method is invoked on the repository.
        // We use the BDDMockito verification methods then and should, instead of Mockito.verify.
        // It has the same effect, but more readable.
        then(superHeroRepository).should().getSuperHero(2);

    }
}