package org.fan.phonebooking;

import org.fan.phonebooking.repository.model.PhoneBookingTicket;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PhoneBookService {
    Flux<PhoneBookingTicket> getAllPhones(boolean onlyAvailable);
    Mono<PhoneBookingTicket> bookPhone(int id, String user);
    Mono<PhoneBookingTicket> releasePhone(int id, String user);
}
