package com.paymybuddy.payapp.dtos;

import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

public class BillDTO {
    /**
     * Bill ID
     */
    private final int id;
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
     * Total of all transactions commission between startDate and endDate. Null at creation.
     */
    @Nullable
    private BigDecimal total;

    public BillDTO(int id, int userID, ZonedDateTime creationDate, ZonedDateTime startDate, ZonedDateTime endDate) {
        this.id = id;
        this.userID = userID;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public Optional<BigDecimal> getTotal() {
        return Optional.ofNullable(total);
    }

    public void setTotal(@Nullable BigDecimal total) {
        this.total = total;
    }
}
