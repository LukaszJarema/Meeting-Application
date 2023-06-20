package com.jarema.lukasz.Meeting.Application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.services.EmailService;
import com.jarema.lukasz.Meeting.Application.services.SupportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class LoginControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private SupportService supportService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private EmailService emailService;

    @Test
    void mainPageGet() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("main-page"))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    void mainPagePost() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("main-page"))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    void employeeLoginPageGet() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    void accessDeniedPageGet() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/access-denied"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("access-denied"))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    void accountDisabledPageGet() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/accountDisabled"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("account-disabled"))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
    }

    @Test
    void logoutPageGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/logout"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

    @Test
    void supportFormGet() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/support"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("support"))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);
    }
}