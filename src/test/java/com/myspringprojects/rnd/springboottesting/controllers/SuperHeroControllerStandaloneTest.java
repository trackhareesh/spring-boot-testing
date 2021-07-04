package com.myspringprojects.rnd.springboottesting.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myspringprojects.rnd.springboottesting.domain.SuperHero;
import com.myspringprojects.rnd.springboottesting.exception.NonExistingHeroException;
import com.myspringprojects.rnd.springboottesting.repositories.SuperHeroRepository;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@DisplayName("Spring Boot Standalone Controller Test -  No webserver started")
class SuperHeroControllerStandaloneTest {

    private MockMvc mockMvc;

    @Mock
    private SuperHeroRepository superHeroRepository;

    @InjectMocks
    private SuperHeroController superHeroController;

    // This object will be initialized by the JacksonTester.initFields call in the setup() method below.
    private JacksonTester<SuperHero> jsonSuperHero;

    @BeforeEach
    public void setup() {

        JacksonTester.initFields(this, new ObjectMapper());

        // Since the Spring context is not initialized, as we're testing the controller in standalone mode, the
        // MockMvc object has to be initialized by specifying the controller under test, the Controller advice class
        // and the filters to be applied.
        mockMvc = MockMvcBuilders.standaloneSetup(superHeroController)
                .setControllerAdvice(new SuperHeroExceptionHandler())
                .addFilters(new SuperHeroFilter())
                .build();
    }


    @Test
    @DisplayName("Given a superhero ID exists in the system, When GET /supeheroes/{id} " +
            "is invoked with that ID, Then the corresponding SuperHero is returned.")
    void canRetrieveByIdWhenExists() throws Exception {

        // First, mock the repository behaviour, when invoking the getSuperHero method by Id.
        given(superHeroRepository.getSuperHero(2))
                .willReturn(new SuperHero("Rob", "Mannon", "RobotMan"));

        // Simulate the Http Request by invoking the MockMvc.perform() method with the URL: '/superheroes/2'
        // and get the response from the MockMvc. The MovkMvc layer will return the response constructed using
        // the SuperHero object returned from the Controller method. Hence, if we test the json returned, we can
        // validate that the controller is returning the correct object from the request handler method.
        MockHttpServletResponse response = mockMvc.perform(
                get("/superheroes/2").accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Finally, do the assertions.
        SuperHero superHero = new SuperHero("Rob", "Mannon", "RobotMan");
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(jsonSuperHero.write(superHero).getJson());
    }

    @Test
    @DisplayName("Given a superhero ID does not exist in the system, When GET /superheroes/{id} " +
            "is invoked with that ID, Then HTTP 404 is returned as the status and the response content is empty")
    public void shouldReturn404WhenRetrievingByNonExistentId() throws Exception {

        // Firstly, mock the SuperHeroRepository.getSuperHero() method to throw the
        // NonExistingHeroException when invoked with a value of 2.
        given(superHeroRepository.getSuperHero(2))
                .willThrow(new NonExistingHeroException());

        // Next fire the request using MockMvc.
        MockHttpServletResponse response = mockMvc.perform(get("/superheroes/2"))
                .andReturn().getResponse();

        // Finally do the required assertions.
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    @DisplayName("Given a super hero name exists in the system, When GET /superheroes?name=<Name> is invoked " +
            "with that name, Then the corresponding SuperHero details is returned in the response")
    public void canRetrieveByNameWhenExists() throws Exception {

        // Firstly stub the getSuperHero(String) method on the SuperHeroRepository, so that the
        // test can be performed.
        given(superHeroRepository.getSuperHero("RobotMan"))
                .willReturn(Optional.of(new SuperHero("Rob", "Mannon", "RobotMan")));

        // Next, fire the request using MockMvc
        MockHttpServletResponse response = mockMvc.perform(
                get("/superheroes?name=RobotMan")
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Finally, do the assertions.
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonSuperHero.write(
                        new SuperHero("Rob", "Mannon", "RobotMan")
                ).getJson()
        );

    }

    @Test
    @DisplayName("Given a name does not exist in the system, When '/superheroes?name=<Name> is invoked " +
            "with that name, Then an HTTP 200 OK response is returned with an empty response content")
    void shouldReturn200WithEmptyResponseWhenNoNameExists() throws Exception {
        // Firstly stub the getSuperHero(String) method on the SuperHeroRepository, so that the
        // test can be performed.
        given(superHeroRepository.getSuperHero("RobotMan"))
                .willReturn(Optional.empty());

        // Next, fire the request using MockMvc
        MockHttpServletResponse response = mockMvc.perform(
                get("/superheroes?name=RobotMan")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Finally, do the assertions.
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("null");
    }

    @Test
    @DisplayName("Given new SuperHero Details, When POST /superheroes is invoked with the JSON " +
            "representation of the SuperHero, then an HTTP 201 is retruned.")
    void canCreateANewSuperHero() throws Exception {

        // Invoke the POST /superheroes method with a new Super Hero details JSON in the request body.
        MockHttpServletResponse response = mockMvc.perform(
                post("/superheroes/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSuperHero.write(
                                new SuperHero("Rob", "Mannon", "RobotMan")).getJson()
                )
        ).andReturn().getResponse();

        // Do the assertion.
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        // Also verify if the saveSuperHero methdo was invoked on the repository exactly once
        verify(superHeroRepository, times(1))
                .saveSuperHero(new SuperHero("Rob", "Mannon", "RobotMan"));
    }


    /**
     * Here, we verify the filter logic, by checking the response header. If the header is set correctly, then
     * the filter is executed.
     *
     * @throws Exception exception during perform() method.
     *
     */
    @Test
    @DisplayName("When any request is executed against the system, Then the returned HTTP response " +
            "contains the X-SUPERHERO-APP header with a value of super-header")
    public void headerIsPresent() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get("/superheroes/2")
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getHeaders("X-SUPERHERO-APP")).containsOnly("super-header");
    }


}