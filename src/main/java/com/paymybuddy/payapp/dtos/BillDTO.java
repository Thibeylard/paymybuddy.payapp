package com.paymybuddy.payapp.dtos;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

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
     * Total of all transactions commission between startDate and endDate
     */
    private final BigDecimal total;

    public BillDTO(int id, int userID, ZonedDateTime creationDate, ZonedDateTime startDate, ZonedDateTime endDate, BigDecimal total) {
        this.id = id;
        this.userID = userID;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = total;
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

    public BigDecimal getTotal() {
        return total;
    }
}
