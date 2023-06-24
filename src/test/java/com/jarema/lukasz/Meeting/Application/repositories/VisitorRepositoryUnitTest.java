package com.jarema.lukasz.Meeting.Application.repositories;

import com.jarema.lukasz.Meeting.Application.models.Role;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class VisitorRepositoryUnitTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void VisitorRepository_SaveAll_ReturnsSavedVisitor() {
        //given
        Visitor visitor = Visitor.builder().name("Adam").surname("Węgrzyn").emailAddress("adaweg@gmail.com")
                .password("abcdef").telephoneNumber("123456789").role((List<Role>) roleRepository.findByName("VISITOR"))
                .accountNonLocked("true").build();
        //when
        Visitor savedVisitor = visitorRepository.save(visitor);
        //then
        Assertions.assertThat(savedVisitor).isNotNull();
        Assertions.assertThat(savedVisitor.getId()).isGreaterThan(0);
    }

    @Test
    public void VisitorRepository_FindAll_ReturnMoreThenOneVisitor() {
        //given
        Visitor visitor1 = Visitor.builder().name("Adam").surname("Węgrzyn").emailAddress("adaweg@gmail.com")
                .password("abcdef").telephoneNumber("123456789").role((List<Role>) roleRepository.findByName("VISITOR"))
                .accountNonLocked("true").build();
        Visitor visitor2 = Visitor.builder().name("Marta").surname("Węgrzyn").emailAddress("marweg@gmail.com")
                .password("abcdefgh").telephoneNumber("123456789").role((List<Role>) roleRepository.findByName("VISITOR"))
                .accountNonLocked("true").build();
        visitorRepository.save(visitor1);
        visitorRepository.save(visitor2);
        //when
        List<Visitor> visitorList = visitorRepository.findAll();
        //then
        Assertions.assertThat(visitorList).isNotNull();
        Assertions.assertThat(visitorList.size()).isEqualTo(2);
    }

    @Test
    public void VisitorRepository_FindById_ReturnVisitor() {
        //given
        Visitor visitor1 = Visitor.builder().name("Adam").surname("Węgrzyn").emailAddress("adaweg@gmail.com")
                .password("abcdef").telephoneNumber("123456789").role((List<Role>) roleRepository.findByName("VISITOR"))
                .accountNonLocked("true").build();
        visitorRepository.save(visitor1);
        //when
        Visitor visitor = visitorRepository.findById(visitor1.getId()).get();
        //then
        Assertions.assertThat(visitor).isNotNull();
    }

    @Test
    public void VisitorRepository_UpdateVisitorPassword_ReturnNewVisitorPassword() {
        //given
        Visitor visitor = Visitor.builder().name("Adam").surname("Węgrzyn").emailAddress("adaweg@gmail.com")
                .password("abcdef").telephoneNumber("123456789").role((List<Role>) roleRepository.findByName("VISITOR"))
                .accountNonLocked("true").build();
        visitorRepository.save(visitor);
        //when
        visitorRepository.updateVisitorPassword("123456", visitor.getId());
        entityManager.refresh(visitor);
        //then
        Assertions.assertThat(visitor.getPassword()).isEqualTo("123456");
    }

    @Test
    public void VisitorRepository_SearchVisitorsByNameOrSurname_ReturnVisitor() {
        //given
        Visitor visitor1 = Visitor.builder().name("Adam").surname("Węgrzyn").emailAddress("adaweg@gmail.com")
                .password("abcdef").telephoneNumber("123456789").role((List<Role>) roleRepository.findByName("VISITOR"))
                .accountNonLocked("true").build();
        Visitor visitor2 = Visitor.builder().name("Marta").surname("Węgrzyn").emailAddress("marweg@gmail.com")
                .password("abcdefgh").telephoneNumber("123456789").role((List<Role>) roleRepository.findByName("VISITOR"))
                .accountNonLocked("true").build();
        visitorRepository.save(visitor1);
        visitorRepository.save(visitor2);
        //when
        List<Visitor> visitors = visitorRepository.searchVisitorsByNameOrSurname("węg");
        //then
        Assertions.assertThat(visitors.size()).isEqualTo(2);
    }

    @Test
    public void VisitorRepository_FindAllVisitorsSortedBySurnameAscending_ReturnVisitors() {
        //given
        Visitor visitor1 = Visitor.builder().name("Wojciech").surname("Ząb").emailAddress("wojzab@gmail.com")
                .password("abcdef").telephoneNumber("123456789").role((List<Role>) roleRepository.findByName("VISITOR"))
                .accountNonLocked("true").build();
        Visitor visitor2 = Visitor.builder().name("Marta").surname("Węgrzyn").emailAddress("marweg@gmail.com")
                .password("abcdefgh").telephoneNumber("123456789").role((List<Role>) roleRepository.findByName("VISITOR"))
                .accountNonLocked("true").build();
        visitorRepository.save(visitor1);
        visitorRepository.save(visitor2);
        //when
        List<Visitor> visitors = visitorRepository.findAllVisitorsSortedBySurnameAscending();
        //then
        Assertions.assertThat(visitors.get(0).getSurname()).isEqualTo("Węgrzyn");
        Assertions.assertThat(visitors.get(1).getSurname()).isEqualTo("Ząb");
    }
}
