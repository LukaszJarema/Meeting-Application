package com.jarema.lukasz.Meeting.Application.repositories;

import com.jarema.lukasz.Meeting.Application.models.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupportRepository extends JpaRepository<Support, Long> {
    @Query("SELECT s FROM Support s ORDER BY s.createdAt ASC")
    List<Support> findAllSupportTicketSortedByCreatedDateAscending();
}
