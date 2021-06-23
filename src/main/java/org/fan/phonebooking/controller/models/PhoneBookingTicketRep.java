package org.fan.phonebooking.controller.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.fan.phonebooking.repository.model.Status;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhoneBookingTicketRep {
    private int id;
    private String name;
    private Status status;
    private String bookedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private OffsetDateTime lastBookTime;
}
