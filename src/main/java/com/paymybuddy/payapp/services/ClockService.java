package com.paymybuddy.payapp.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public interface ClockService {
    /**
     * Get current ZonedDateTime.
     *
     * @return ZoneDateTime object
     */
    ZonedDateTime now();

    /**
     * Set ClockService zoneId attribute.
     */
    void setZone(ZoneId zoneId);
}
