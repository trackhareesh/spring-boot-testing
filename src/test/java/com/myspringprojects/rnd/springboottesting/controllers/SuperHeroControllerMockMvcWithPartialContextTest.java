package com.myspringprojects.rnd.springboottesting.controllers;

import com.myspringprojects.rnd.springboottesting.domain.SuperHero;
import com.myspringprojects.rnd.springboottesting.repositories.SuperHeroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
 * In this class, we use the @WebMvcTest instead of building the MockMvc using the MockMvcBuilders,
 * as in the case of standalone test.
 *
 * The most important difference of this approach is that we didn’t need to explicitly load the
 * surrounding logic since there is a partial Spring context in place. If we create new filters,
 * new controller advices, or any other logic participating in the request-response process, we
 * will get them automatically injected in our test. Therefore, we don’t need to take care of the
 * manual configuration here. There is no fine-grain control over what to use in our test, but
 * it’s closer to what happens in reality. When we run our application, all this stuff is
 * there by default.
 *
 * This is in fact a minimal integration test, as we depend on the loaded Spring Context, instead
 * of selectively specifying the Filters and ControllerAdvice classes to be used.
 *
 */
@AutoConfigureJsonTesters
@WebMvcTest(SuperHeroController.class)
@DisplayName("Spring Boot Unit Test with Partial Spring Context populated (with relevant beans)")
class SuperHeroControllerMockMvcWithPartialContextTest {

    // Here, we can autowire the MockMvc, unlike the standalone case. In that case, we used the MockMvcBuilders
    // class to configure the MockMvc instance. Here, since we use the @WebMvcTest annotation at class level,
    // the spring context will be started with the controller.
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