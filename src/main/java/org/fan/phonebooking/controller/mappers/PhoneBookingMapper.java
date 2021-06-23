package org.fan.phonebooking.controller.mappers;

import org.fan.phonebooking.controller.models.PhoneBookingTicketRep;
import org.fan.phonebooking.repository.model.PhoneBookingTicket;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PhoneBookingMapper {
    PhoneBookingMapper INSTANCE = Mappers.getMapper( PhoneBookingMapper.class );

    PhoneBookingTicketRep phoneBookingToPhoneBookingRep(PhoneBookingTicket ticket);

}
