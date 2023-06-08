package com.jarema.lukasz.Meeting.Application.repositories;

import com.jarema.lukasz.Meeting.Application.models.Visitor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    Visitor findByEmailAddress(String emailAddress);
    @Modifying
    @Transactional
    @Query("UPDATE Visitor v SET v.password=:password WHERE v.id=:id")
    void updateVisitorPassword(String password, Long id);
    @Modifying
    @Query("UPDATE Visitor v SET v.accountNonLocked = 'true' WHERE v.id = :visitorId")
    void activateVisitor(@Param("visitorId") Long visitorId);
    @Modifying
    @Query("UPDATE Visitor v SET v.accountNonLocked = 'false' WHERE v.id = :visitorId")
    void deactivateVisitor(@Param("visitorId") Long visitorId);
}
