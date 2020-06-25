package com.paymybuddy.payapp.dtos;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

public class BillDTO {
    /**
     * Bill ID. Null at creation for save.
     */
    @Nullable
    private final Integer id;
    /**
     * Bill User ID
     */
    private final int userID;
    /**
     * Bill creation date
     */
    private final ZonedDateTime creationDate;
    /**
     * Date before which transactions does not enter into account
     */
    private final ZonedDateTime startDate;
    /**
     * Date after which transactions does not enter into account
     */
    private final ZonedDateTime endDate;

    /**
     * Total of all transactions commission between startDate and endDate. Null at creation for save.
     */
    @Nullable
    private BigDecimal total;

    public BillDTO(int userID, ZonedDateTime creationDate, ZonedDateTime startDate, ZonedDateTime endDate) {
        this.id = null;
        this.userID = userID;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = null;
    }

    public BillDTO(int id, int userID, ZonedDateTime creationDate, ZonedDateTime startDate, ZonedDateTime endDate, @NotNull BigDecimal total) {
        this.id = id;
        this.userID = userID;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = total;
    }

    public Optional<Integer> getId() {
        return Optional.ofNullable(id);
    }

    public int getUserID() {
        return userID;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public String getCreationDateAsTimestampWithTimeZoneString() {
        return creationDate.getYear() + "-" + creationDate.getMonthValue() + "-" + creationDate.getDayOfMonth() +
                " " + creationDate.getHour() + ":" + creationDate.getMinute() + ":" + creationDate.getSecond() +
                "+" + (creationDate.getOffset().getTotalSeconds() / 3600);
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public String getStartDateAsTimestampWithTimeZoneString() {
        return startDate.getYear() + "-" + startDate.getMonthValue() + "-" + startDate.getDayOfMonth() +
                " " + startDate.getHour() + ":" + startDate.getMinute() + ":" + startDate.getSecond() +
                "+" + (startDate.getOffset().getTotalSeconds() / 3600);
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public String getEndDateAsTimestampWithTimeZoneString() {
        return endDate.getYear() + "-" + endDate.getMonthValue() + "-" + endDate.getDayOfMonth() +
                " " + endDate.getHour() + ":" + endDate.getMinute() + ":" + endDate.getSecond() +
                "+" + (endDate.getOffset().getTotalSeconds() / 3600);
    }

    public Optional<BigDecimal> getTotal() {
        return Optional.ofNullable(total);
    }

    public void setTotal(@Nullable BigDecimal total) {
        this.total = total;
    }
}
