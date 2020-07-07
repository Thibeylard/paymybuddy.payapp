package com.paymybuddy.payapp.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

public class Bill {
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

    public Bill(int userID, ZonedDateTime creationDate, ZonedDateTime startDate, ZonedDateTime endDate) {
        this.id = null;
        this.userID = userID;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = null;
    }

    @JsonCreator
    public Bill(@JsonProperty("id") int id,
                @JsonProperty("userID") int userID,
                @JsonProperty("creationDate") ZonedDateTime creationDate,
                @JsonProperty("startDate") ZonedDateTime startDate,
                @JsonProperty("endDate") ZonedDateTime endDate,
                @JsonProperty("total") @NotNull BigDecimal total) {
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
