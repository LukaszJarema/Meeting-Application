package com.jarema.lukasz.Meeting.Application.repositories;

import com.jarema.lukasz.Meeting.Application.models.Support;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SupportRepositoryUnitTests {
    @Autowired
    private SupportRepository supportRepository;

    @Test
    public void SupportRepository_FindAllSupportTicketSortedByCreatedDateAscending_ReturnSupport(){
        //given
        Support support1 = Support.builder().message("Dzień dobry. Czy mogę prosić o zmianę hasła, aktualne nie działa.")
                .emailAddress("adaweg@gmail.com")
                .createdAt(LocalDateTime.MAX)
                .build();
        Support support2 = Support.builder().message("Dzień dobry. Czy mogę prosić o zmianę hasła, zapomniałem je.")
                .emailAddress("matweg@gmail.com")
                .createdAt(LocalDateTime.now())
                .build();
        supportRepository.save(support1);
        supportRepository.save(support2);
        //when
        List<Support> supports = supportRepository.findAllSupportTicketSortedByCreatedDateAscending();
        //then
        Assertions.assertThat(supports.get(1)).isEqualTo(support2);
        Assertions.assertThat(supports.get(0)).isEqualTo(support1);
    }
}
