package com.jarema.lukasz.Meeting.Application.repositories;

import com.jarema.lukasz.Meeting.Application.enums.Status;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Meeting;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByStartOfMeetingBetweenAndVisitor(LocalDateTime startDateTime, LocalDateTime endDateTime, Visitor visitor, Sort sort);
    List<Meeting> findByStartOfMeetingBetweenAndEmployees(LocalDateTime startOfDay, LocalDateTime endOfDay, Employee employee, Sort sort);
    List<Meeting> findByStartOfMeetingBetween(LocalDateTime startOfDay, LocalDateTime endOfDay, Sort sort);
    @Modifying
    @Transactional
    @Query("UPDATE Meeting m SET m.status=:status WHERE m.id=:id")
    void updateMeetingStatus(Status status, Long id);
    @Query("SELECT m FROM Meeting m ORDER BY m.startOfMeeting DESC")
    List<Meeting> findAllMeetingsSortedByStartDateDescending();
}
