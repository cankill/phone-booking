package org.fan.phonebooking.services.exceptions;

public class PhoneNotFoundException extends RuntimeException {
    public PhoneNotFoundException(int id) {
        super(String.format("The phone with id '%s' was not found", id));
    }
}
