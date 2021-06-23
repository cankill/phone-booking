package org.fan.phonebooking.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhoneBookingTicket {
    @Id
    private int id;
    private String name;
    private Status status;
    private String bookedBy;
    private OffsetDateTime lastBookTime;
}
