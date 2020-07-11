package com.paymybuddy.payapp.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class ClockServiceImpl implements ClockService {

    /**
     * ClockServiceImpl zoneId.
     */
    private final String ZONE_ID;

    public ClockServiceImpl(@Value("${default.zoneID}") String zoneID) {
        this.ZONE_ID = zoneID;
    }

    /**
     * @see ClockService
     */
    public ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.of(ZONE_ID));
    }

    /**
     * @see ClockService
     */
    public ZoneId getZone() {
        return ZoneId.of(ZONE_ID);
    }
}
