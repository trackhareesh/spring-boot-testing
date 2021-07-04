package com.myspringprojects.rnd.springboottesting.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Spring Controller Acceptance Test")
class WelcomeControllerAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Given the web app, When /welcome is invoked without the name param, Then Hello Stranger is " +
            "returned")
    void testWelcomeWithoutReqParam() throws Exception {

        mockMvc.perform(get("/welcome"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Welcome Stranger!")));

    }

    @Test
    @DisplayName("Given the web app, When /welcome is invoked with a request param, Then Welcome <reqParam> is returned")
    void testWelcomeWithReqParam() throws Exception {

        mockMvc.perform(get("/welcome?name=Hareesh"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Welcome Hareesh!")));

    }
}