package com.jarema.lukasz.Meeting.Application.repositories;

import com.jarema.lukasz.Meeting.Application.enums.Status;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Meeting;
import com.jarema.lukasz.Meeting.Application.models.Role;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MeetingRepositoryUnitTests {
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    TestEntityManager entityManager;

    @Test
    public void MeetingRepository_FindByStartOfMeetingBetweenAndVisitor_ReturnMeetings() {
        //given
        Role role = roleRepository.findByName("EMPLOYEE");
        Visitor visitor1 = Visitor.builder()
                .name("Andrzej").surname("Mądry")
                .build();
        Visitor visitor2 = Visitor.builder()
                .name("Anna").surname("Warchoł")
                .build();
        Employee employee1 = Employee.builder()
                .name("Marcin").surname("Adanek").emailAddress("marada@gmail.com")
                .role(Collections.singleton(role)).department("HR")
                .build();
        Employee employee2 = Employee.builder()
                .name("Wiesława").surname("Więcek").emailAddress("wieswie@gmail.com")
                .role(Collections.singleton(role)).department("IT")
                .build();
        visitorRepository.save(visitor1);
        visitorRepository.save(visitor2);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        Meeting meeting1 = Meeting.builder()
                .contentOfMeeting("test").startOfMeeting(LocalDateTime.of
                        (2024, 06, 23, 12, 0))
                .endOfMeeting(LocalDateTime.of(2024, 06, 23, 13, 0))
                .visitor(visitor1).employees(Arrays.asList(employee1, employee2))
                .build();
        Meeting meeting2 = Meeting.builder()
                .contentOfMeeting("test2").startOfMeeting(LocalDateTime.of
                        (2024, 06, 24, 12, 0))
                .endOfMeeting(LocalDateTime.of(2024, 06, 24, 13, 0))
                .visitor(visitor1).employees(Arrays.asList(employee1, employee2))
                .build();
        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);
        //when
        List<Meeting> meetings = meetingRepository.findByStartOfMeetingBetweenAndVisitor(LocalDateTime.of
                        (2024, 06, 23, 10, 0),
                LocalDateTime.of(2024, 06, 23, 23, 59),
                visitor1, Sort.by(Sort.Direction.DESC, "startOfMeeting"));
        //then
        Assertions.assertThat(meetings.size()).isEqualTo(1);
        Assertions.assertThat(meetings.get(0).getContentOfMeeting()).isEqualTo("test");
    }

    @Test
    public void MeetingRepository_FindByStartOfMeetingBetween_ReturnMeeting() {
        //given
        Role role = roleRepository.findByName("EMPLOYEE");
        Visitor visitor1 = Visitor.builder()
                .name("Andrzej").surname("Mądry")
                .build();
        Visitor visitor2 = Visitor.builder()
                .name("Anna").surname("Warchoł")
                .build();
        Employee employee1 = Employee.builder()
                .name("Marcin").surname("Adanek").emailAddress("marada@gmail.com")
                .role(Collections.singleton(role)).department("HR")
                .build();
        Employee employee2 = Employee.builder()
                .name("Wiesława").surname("Więcek").emailAddress("wieswie@gmail.com")
                .role(Collections.singleton(role)).department("IT")
                .build();
        visitorRepository.save(visitor1);
        visitorRepository.save(visitor2);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        Meeting meeting1 = Meeting.builder()
                .contentOfMeeting("test").startOfMeeting(LocalDateTime.of
                        (2024, 06, 23, 12, 0))
                .endOfMeeting(LocalDateTime.of(2024, 06, 23, 13, 0))
                .visitor(visitor1).employees(Arrays.asList(employee1, employee2))
                .build();
        Meeting meeting2 = Meeting.builder()
                .contentOfMeeting("test2").startOfMeeting(LocalDateTime.of
                        (2024, 06, 24, 12, 0))
                .endOfMeeting(LocalDateTime.of(2024, 06, 24, 13, 0))
                .visitor(visitor1).employees(Arrays.asList(employee1, employee2))
                .build();
        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);
        //when
        List<Meeting> meetings = meetingRepository.findByStartOfMeetingBetween(LocalDateTime.of
                        (2024, 06, 23, 0, 0),
                LocalDateTime.of(2024, 06, 23, 23, 59),
                Sort.by(Sort.Direction.DESC, "startOfMeeting"));
        //then
        Assertions.assertThat(meetings.size()).isEqualTo(1);
        Assertions.assertThat(meetings.get(0).getContentOfMeeting()).isEqualTo("test");
    }

    @Test
    public void MeetingRepository_UpdateMeetingStatus_ReturnMeeting() {
        //given
        Role role = roleRepository.findByName("EMPLOYEE");
        Visitor visitor1 = Visitor.builder()
                .name("Andrzej").surname("Mądry")
                .build();
        Visitor visitor2 = Visitor.builder()
                .name("Anna").surname("Warchoł")
                .build();
        Employee employee1 = Employee.builder()
                .name("Marcin").surname("Adanek").emailAddress("marada@gmail.com")
                .role(Collections.singleton(role)).department("HR")
                .build();
        Employee employee2 = Employee.builder()
                .name("Wiesława").surname("Więcek").emailAddress("wieswie@gmail.com")
                .role(Collections.singleton(role)).department("IT")
                .build();
        visitorRepository.save(visitor1);
        visitorRepository.save(visitor2);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        Meeting meeting1 = Meeting.builder()
                .contentOfMeeting("test").startOfMeeting(LocalDateTime.of
                        (2024, 06, 23, 12, 0))
                .endOfMeeting(LocalDateTime.of(2024, 06, 23, 13, 0))
                .visitor(visitor1).employees(Arrays.asList(employee1, employee2)).status(Status.REJECTED)
                .build();
        Meeting meeting2 = Meeting.builder()
                .contentOfMeeting("test2").startOfMeeting(LocalDateTime.of
                        (2024, 06, 24, 12, 0))
                .endOfMeeting(LocalDateTime.of(2024, 06, 24, 13, 0))
                .visitor(visitor1).employees(Arrays.asList(employee1, employee2)).status(Status.WAITING)
                .build();
        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);
        //when
        meetingRepository.updateMeetingStatus(Status.APPROVED, meeting1.getId());
        meetingRepository.updateMeetingStatus(Status.REJECTED, meeting2.getId());
        entityManager.refresh(meeting1);
        entityManager.refresh(meeting2);
        //then
        Assertions.assertThat(meeting1.getStatus()).isEqualTo(Status.APPROVED);
        Assertions.assertThat(meeting2.getStatus()).isEqualTo(Status.REJECTED);
    }

    @Test
    public void MeetingRepository_FindAllMeetingsSortedByStartDateDescending_ReturnMeeting() {
        //given
        Role role = roleRepository.findByName("EMPLOYEE");
        Visitor visitor1 = Visitor.builder()
                .name("Andrzej").surname("Mądry")
                .build();
        Visitor visitor2 = Visitor.builder()
                .name("Anna").surname("Warchoł")
                .build();
        Employee employee1 = Employee.builder()
                .name("Marcin").surname("Adanek").emailAddress("marada@gmail.com")
                .role(Collections.singleton(role)).department("HR")
                .build();
        Employee employee2 = Employee.builder()
                .name("Wiesława").surname("Więcek").emailAddress("wieswie@gmail.com")
                .role(Collections.singleton(role)).department("IT")
                .build();
        visitorRepository.save(visitor1);
        visitorRepository.save(visitor2);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        Meeting meeting1 = Meeting.builder()
                .contentOfMeeting("test").startOfMeeting(LocalDateTime.of
                        (2024, 06, 23, 12, 0))
                .endOfMeeting(LocalDateTime.of(2024, 06, 23, 13, 0))
                .visitor(visitor1).employees(Arrays.asList(employee1, employee2)).status(Status.REJECTED)
                .build();
        Meeting meeting2 = Meeting.builder()
                .contentOfMeeting("test2").startOfMeeting(LocalDateTime.of
                        (2024, 06, 24, 12, 0))
                .endOfMeeting(LocalDateTime.of(2024, 06, 24, 13, 0))
                .visitor(visitor1).employees(Arrays.asList(employee1, employee2)).status(Status.WAITING)
                .build();
        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);
        //when
        List<Meeting> meetings = meetingRepository.findAllMeetingsSortedByStartDateDescending();
        //then
        Assertions.assertThat(meetings.size()).isEqualTo(2);
        Assertions.assertThat(meetings.get(0).getContentOfMeeting()).isEqualTo("test2");
    }
}
