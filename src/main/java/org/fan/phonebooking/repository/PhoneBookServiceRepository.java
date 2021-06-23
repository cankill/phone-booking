package org.fan.phonebooking.repository;

import org.fan.phonebooking.repository.model.PhoneBookingTicket;
import org.fan.phonebooking.repository.model.Status;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

public interface PhoneBookServiceRepository extends ReactiveCrudRepository<PhoneBookingTicket, Integer> {
    @Modifying
    @Query("UPDATE phone_booking_ticket SET status = 'BOOKED', last_book_time = :curDate, booked_by = :user WHERE id = :id AND last_book_time = :prevDate")
    Mono<Integer> bookPhone(@Param(value = "id") int id,
                            @Param(value= "user") String user,
                            @Param(value = "curDate") OffsetDateTime curDate,
                            @Param(value = "prevDate") OffsetDateTime prevDate);

    @Modifying
    @Query("UPDATE phone_booking_ticket SET status = 'AVAILABLE', last_book_time = :curDate, booked_by = null WHERE id = :id AND last_book_time = :prevDate")
    Mono<Integer> releasePhone(@Param(value = "id") int id,
                            @Param(value = "curDate") OffsetDateTime curDate,
                            @Param(value = "prevDate") OffsetDateTime prevDate);

    Flux<PhoneBookingTicket> findAllByStatus(Status available);
}
