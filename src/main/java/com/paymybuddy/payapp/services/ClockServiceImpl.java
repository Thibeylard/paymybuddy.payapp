package com.paymybuddy.payapp.services;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class ClockServiceImpl implements ClockService {
    /**
     * Current ClockServiceImpl zoneId.
     */
    private ZoneId zoneId;

    public ClockServiceImpl() {
        this.zoneId = ZonedDateTime.now().getZone();
    }

    /**
     * @see ClockService
     */
    @Override
    public ZonedDateTime now() {
        return ZonedDateTime.now(zoneId);

    }

    /**
     * @see ClockService
     */
    @Override
    public ZoneId getZone() {
        return this.zoneId;
    }

    /**
     * @see ClockService
     */
    @Override
    public void setZone(@NotNull final ZoneId zoneId) {
        this.zoneId = zoneId;
    }
}
