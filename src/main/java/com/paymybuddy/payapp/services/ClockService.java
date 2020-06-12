package com.paymybuddy.payapp.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public interface ClockService {

    /**
     * ClockServiceImpl zoneId.
     */
    @Value("${default.zoneID}")
    String ZONE_ID = "Europe/Paris";

    /**
     * Get current ZonedDateTime.
     *
     * @return ZoneDateTime object
     */
    default ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.of(ZONE_ID));
    }

    /**
     * Get ClockService zoneId attribute.
     */
    default ZoneId getZone() {
        return ZoneId.of(ZONE_ID);
    }
}
