package org.fan.phonebooking.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fan.phonebooking.controller.mappers.PhoneBookingMapper;
import org.fan.phonebooking.controller.models.PhoneBookingTicketRep;
import org.fan.phonebooking.PhoneBookService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/phone-bookings")
public class PhoneBookController {
    @NonNull
    private final PhoneBookService phoneBookService;

    @GetMapping
    public Flux<PhoneBookingTicketRep> getAllPhoneBookings(@RequestParam(name = "onlyAvailable", required = false) boolean onlyAvailable) {
        log.debug("Get all bookings. Filter by availability: '{}'", onlyAvailable);
        return phoneBookService.getAllPhones(onlyAvailable)
                .map(PhoneBookingMapper.INSTANCE::phoneBookingToPhoneBookingRep);
    }

    @PutMapping("/{id}")
    public Mono<PhoneBookingTicketRep> bookPhone(@PathVariable int id, @NonNull @RequestHeader("user") String user) {
        log.debug("Book a phone id '{}' for user '{}'", id, user);
        return phoneBookService.bookPhone(id, user)
                .map(PhoneBookingMapper.INSTANCE::phoneBookingToPhoneBookingRep);
    }

    @DeleteMapping("/{id}")
    public Mono<PhoneBookingTicketRep> releasePhoneBook(@PathVariable int id, @NonNull @RequestHeader("user") String user) {
        log.debug("Release a phone id '{}' by user '{}'", id, user);
        return phoneBookService.releasePhone(id, user)
                .map(PhoneBookingMapper.INSTANCE::phoneBookingToPhoneBookingRep);
    }

}
