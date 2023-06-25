package com.jarema.lukasz.Meeting.Application.repositories;

import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Collections;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EmployeeRepositoryUnitTest {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void EmployeeRepository_SearchEmployeesByNameOrSurname_ReturnEmployee() {
        //given
        Role role = roleRepository.findByName("EMPLOYEE");
        Employee employee1 = Employee.builder()
                .name("Marcin").surname("Adanek").emailAddress("marada@gmail.com")
                .role(Collections.singleton(role)).department("HR")
                .build();
        Employee employee2 = Employee.builder()
                .name("Wiesława").surname("Więcek").emailAddress("wieswie@gmail.com")
                .role(Collections.singleton(role)).department("IT")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        //when
        List<Employee> employees = employeeRepository.searchEmployeesByNameOrSurname("mar");
        //then
        Assertions.assertThat(employees.size()).isEqualTo(1);
        Assertions.assertThat(employees.get(0)).isEqualTo(employee1);
    }

    @Test
    public void EmployeeRepository_UpdateEmployeePassword_ReturnEmployee() {
        //given
        Role role = roleRepository.findByName("EMPLOYEE");
        Employee employee1 = Employee.builder()
                .name("Marcin").surname("Adanek").emailAddress("marada@gmail.com")
                .role(Collections.singleton(role)).department("HR").password("123456")
                .build();
        employeeRepository.save(employee1);
        //when
        employeeRepository.updateEmployeePassword("abcdef", employee1.getId());
        entityManager.refresh(employee1);
        //then
        Assertions.assertThat(employee1.getPassword()).isEqualTo("abcdef");
    }

    @Test
    public void EmployeeRepository_FindAllEmployeesSortedBySurnameAscending_ReturnEmployee() {
        //given
        Role role = roleRepository.findByName("EMPLOYEE");
        Employee employee1 = Employee.builder()
                .name("Wiesława").surname("Więcek").emailAddress("wieswie@gmail.com")
                .role(Collections.singleton(role)).department("IT")
                .build();
        Employee employee2 = Employee.builder()
                .name("Marcin").surname("Adanek").emailAddress("marada@gmail.com")
                .role(Collections.singleton(role)).department("HR")
                .build();


        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        //when
        List<Employee> employees = employeeRepository.findAllEmployeesSortedBySurnameAscending();
        //then
        Assertions.assertThat(employees.size()).isEqualTo(2);
        Assertions.assertThat(employees.get(0)).isEqualTo(employee2);
    }

    @Test
    public void EmployeeRepository_FindAllByRoleAdministrator_ReturnEmployee() {
        //given
        Role role1 = new Role();
        role1.setName("EMPLOYEE");
        Role role2 = new Role();
        role2.setName("ADMINISTRATOR");
        Employee employee1 = Employee.builder()
                .name("Wiesława").surname("Więcek").emailAddress("wieswie@gmail.com")
                .role(Collections.singleton(role1)).department("IT")
                .build();
        Employee employee2 = Employee.builder()
                .name("Marcin").surname("Adanek").emailAddress("marada@gmail.com")
                .role(Collections.singleton(role2)).department("HR")
                .build();
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        //when
        List<Employee> employees = employeeRepository.findAllByRoleAdministrator();
        //then
        Assertions.assertThat(employees.size()).isEqualTo(1);
        Assertions.assertThat(employees.get(0)).isEqualTo(employee2);
    }
}
