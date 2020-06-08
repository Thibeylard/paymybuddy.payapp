package com.paymybuddy.payapp.services;

import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class ClockServiceImpl implements ClockService {
    /**
     * Current ClockServiceImpl zoneId. If null, now() returns default zone DateTime.
     */
    private ZoneId zoneId;

    /**
     * @see ClockService
     */
    @Override
    public ZonedDateTime now() {
        return zoneId == null ? ZonedDateTime.now() : ZonedDateTime.now(zoneId);

    }

    /**
     * @see ClockService
     */
    @Override
    public void setZone(ZoneId zoneId) {
        this.zoneId = zoneId;
    }
}
