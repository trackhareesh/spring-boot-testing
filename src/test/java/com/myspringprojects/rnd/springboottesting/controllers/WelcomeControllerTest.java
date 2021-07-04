package com.myspringprojects.rnd.springboottesting.controllers;

import com.myspringprojects.rnd.springboottesting.services.WelcomeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.concurrent.WeakConcurrentMap;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Spring Controller Unit Test")
class WelcomeControllerTest {

    @Mock
    private WelcomeService welcomeService;

    @InjectMocks
    private WelcomeController welcomeController;

    @Test
    void testWelcome() {
        Mockito.when(welcomeService.getWelcomeMessage("Hareesh")).thenReturn("Welcome Hareesh!");
        assertEquals("Welcome Hareesh!", welcomeController.welcome("Hareesh"));
    }
}