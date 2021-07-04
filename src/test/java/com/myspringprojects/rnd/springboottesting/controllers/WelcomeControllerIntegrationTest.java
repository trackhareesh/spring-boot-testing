package com.myspringprojects.rnd.springboottesting.controllers;

import com.myspringprojects.rnd.springboottesting.services.WelcomeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WelcomeController.class)
@DisplayName("Spring Integration Test")
class WelcomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WelcomeService welcomeService;

    @Test
    @DisplayName("Given WelcomeController, When /welcome is invoked without the name param, Then Welcome Stranger is " +
            "returned")
    void testWelcomeWithoutReqParam() throws Exception {
        // Mock the service method when 'Stranger' is passed to it.
        Mockito.when(welcomeService.getWelcomeMessage("Stranger"))
                .thenReturn("Welcome Stranger!");

        mockMvc.perform(get("/welcome"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Welcome Stranger!")));

        // Verify if the welcome message was invoked with the parameter 'Stranger'
        verify(welcomeService).getWelcomeMessage("Stranger");

    }

    @Test
    @DisplayName("Given WelcomeController, When /welcome is invoked with a request param, Then Welcome <reqParam> is returned")
    void testWelcomeWithReqParam() throws Exception {

        Mockito.when(welcomeService.getWelcomeMessage("Hareesh"))
                .thenReturn("Welcome Hareesh!");

        mockMvc.perform(get("/welcome?name=Hareesh"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Welcome Hareesh!")));

        // Verify that the WelcomeService was invoked with 'Hareesh' as parameter.
        verify(welcomeService).getWelcomeMessage("Hareesh");

    }
}