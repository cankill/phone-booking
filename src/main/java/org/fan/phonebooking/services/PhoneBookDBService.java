package org.fan.phonebooking.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.fan.phonebooking.PhoneBookService;
import org.fan.phonebooking.repository.PhoneBookServiceRepository;
import org.fan.phonebooking.repository.model.PhoneBookingTicket;
import org.fan.phonebooking.repository.model.Status;
import org.fan.phonebooking.services.exceptions.PhoneNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Log4j2
@RequiredArgsConstructor
@Component
public class PhoneBookDBService implements PhoneBookService {
    @NonNull
    private final PhoneBookServiceRepository phoneBookRepo;

    @Override
    public Flux<PhoneBookingTicket> getAllPhones(boolean onlyAvailable) {
        return onlyAvailable
                ? phoneBookRepo.findAllByStatus(Status.AVAILABLE)
                : phoneBookRepo.findAll();
    }

    @Override
    public Mono<PhoneBookingTicket> bookPhone(int id, String user) {
        return phoneBookRepo.findById(id)
                .flatMap(ticket -> {
                    if (ticket.getStatus() != Status.AVAILABLE) {
                        var msg = String.format("Phone '%s' was reserved at '%s' by '%s'",
                                ticket.getName(), ticket.getLastBookTime(), ticket.getBookedBy());
                        return Mono.error(new IllegalArgumentException(msg));
                    }

                    var version = ticket.getLastBookTime();
                    ticket.setStatus(Status.BOOKED);
                    ticket.setBookedBy(user);
                    ticket.setLastBookTime(OffsetDateTime.now());

                    return phoneBookRepo.bookPhone(ticket.getId(), ticket.getBookedBy(), ticket.getLastBookTime(), version)
                            .flatMap(count -> {
                                if (count == 0) {
                                    var msg = String.format("Can't reserve a phone '%s' try again...", ticket.getName());
                                    return Mono.error(new IllegalArgumentException(msg));
                                }

                                return Mono.just(ticket);
                            });
                })
                .switchIfEmpty(Mono.error(new PhoneNotFoundException(id)));
    }

    @Override
    public Mono<PhoneBookingTicket> releasePhone(int id, String user) {
        return phoneBookRepo.findById(id)
                .flatMap(ticket -> {
                    if (ticket.getStatus() != Status.BOOKED) {
                        var msg = String.format("Phone '%s' is not reserved", ticket.getName());
                        return Mono.error(new IllegalArgumentException(msg));
                    }

                    if (!StringUtils.equalsIgnoreCase(user, ticket.getBookedBy())) {
                        var msg = String.format("Phone '%s' is not reserved on you", ticket.getName());
                        return Mono.error(new IllegalArgumentException(msg));
                    }

                    var version = ticket.getLastBookTime();
                    ticket.setStatus(Status.AVAILABLE);
                    ticket.setBookedBy(null);
                    ticket.setLastBookTime(OffsetDateTime.now());

                    return phoneBookRepo.releasePhone(ticket.getId(), ticket.getLastBookTime(), version)
                            .flatMap(count -> {
                                if (count == 0) {
                                    var msg = String.format("Can't release a phone '%s' try again...", ticket.getName());
                                    return Mono.error(new IllegalArgumentException(msg));
                                }

                                return Mono.just(ticket);
                            });
                })
                .switchIfEmpty(Mono.error(new PhoneNotFoundException(id)));
    }
}
