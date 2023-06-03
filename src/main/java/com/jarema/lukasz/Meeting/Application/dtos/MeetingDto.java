package com.jarema.lukasz.Meeting.Application.dtos;

import com.jarema.lukasz.Meeting.Application.enums.Status;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Getter
@Setter
public class MeetingDto {
    private Long id;
    @NotEmpty(message = "Content could not be empty")
    private String contentOfMeeting;
    @FutureOrPresent(message = "The start of meeting date can not be from the past")
    @NotNull
    private LocalDateTime startOfMeeting;
    @AssertTrue(message = "The end of meeting date must be later than the start of meeting")
    private boolean isEndOfMeetingAfterStartOfMeeting() {
        if (startOfMeeting == null || endOfMeeting == null) {
            return false;
        }
        return endOfMeeting.isAfter(startOfMeeting);
    }
    @NotNull
    private LocalDateTime endOfMeeting;
    private Status status;
    @NotNull(message = "Please chose at least one of our employee")
    private List<Long> employeeIds;
    private Visitor visitor;
}
