package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.SupportDto;
import com.jarema.lukasz.Meeting.Application.enums.SupportStatus;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Role;
import com.jarema.lukasz.Meeting.Application.models.Support;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.repositories.RoleRepository;
import com.jarema.lukasz.Meeting.Application.repositories.SupportRepository;
import com.jarema.lukasz.Meeting.Application.security.SecurityConfig;
import com.jarema.lukasz.Meeting.Application.services.EmailService;
import com.jarema.lukasz.Meeting.Application.services.SupportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class LoginControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private SupportService supportService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SupportRepository supportRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void LoginController_SupportSave() throws Exception {
        //given
        SupportDto supportDto = new SupportDto();
        supportDto.setEmailAddress("example@test.com");
        supportDto.setMessage("Dzień dobry.\nCzy mogę prosić o zmianę hasła? Nie pamiętam starego.\nDziękuję");

        Role role1 = new Role();
        role1.setName("EMPLOYEE");
        roleRepository.save(role1);
        Role role2 = new Role();
        role2.setName("ADMINISTRATOR");
        roleRepository.save(role2);
        Employee employee1 = Employee.builder()
                .name("Wiesława").surname("Więcek").emailAddress("wieswie@gmail.com")
                .role(Collections.singleton(role1)).department("IT")
                .build();
        employeeRepository.save(employee1);
        Employee employee2 = Employee.builder()
                .name("Marcin").surname("Adanek").emailAddress("marada@gmail.com")
                .role(Collections.singleton(role2)).department("HR")
                .build();
        employeeRepository.save(employee2);
        //when
        mockMvc.perform(post("/support").with(csrf())
                .flashAttr("support", supportDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        //then
        List<Support> savedSupports = supportRepository.findAll();
        assertThat(savedSupports).hasSize(1);
        Support savedSupport = savedSupports.get(0);
        assertThat(savedSupport.getMessage()).isEqualTo(supportDto.getMessage());
        assertThat(savedSupport.getEmailAddress()).isEqualTo(supportDto.getEmailAddress());
    }

    @Test
    public void supportService_SaveSupport_Success() {
        //given
        SupportDto supportDto = new SupportDto();
        supportDto.setEmailAddress("example@test.com");
        supportDto.setMessage("Dzień dobry.\nCzy mogę prosić o zmianę hasła? Nie pamiętam starego.\nDziękuję");

        //when
        Support savedSupport = supportService.saveSupport(supportDto);

        //then
        assertNotNull(savedSupport.getId());
        assertEquals(supportDto.getEmailAddress(), savedSupport.getEmailAddress());
        assertEquals(supportDto.getMessage(), savedSupport.getMessage());
        assertEquals(SupportStatus.OPEN, savedSupport.getSupportStatus());
    }
}
