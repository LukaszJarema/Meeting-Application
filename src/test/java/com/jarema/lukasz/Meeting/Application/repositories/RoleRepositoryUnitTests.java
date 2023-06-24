package com.jarema.lukasz.Meeting.Application.repositories;

import com.jarema.lukasz.Meeting.Application.models.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleRepositoryUnitTests {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void RoleRepository_FindByName_ReturnRole() {
        //given
        Role role1 = Role.builder()
                .name("ADMINISTRATOR")
                .build();
        roleRepository.save(role1);
        //when
        Role role = roleRepository.findByName("ADMINISTRATOR");
        //then
        Assertions.assertThat(role.getName()).isEqualTo("ADMINISTRATOR");
    }
}
