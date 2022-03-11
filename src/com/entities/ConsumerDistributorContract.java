package com.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public final class ConsumerDistributorContract implements Contract {
    private int consumerId;
    @JsonIgnore
    private int distributorId;
    private int price;
    private int remainedContractMonths;
    @JsonIgnore
    private boolean delayed;

    public int getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(final int consumerId) {
        this.consumerId = consumerId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public int getRemainedContractMonths() {
        return remainedContractMonths;
    }

    public void setRemainedContractMonths(final int remainedContractMonths) {
        this.remainedContractMonths = remainedContractMonths;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(final boolean delayed) {
        this.delayed = delayed;
    }

    public int getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(final int distributorId) {
        this.distributorId = distributorId;
    }

    /**
     *
     */
    public void decrementMonths() {
        remainedContractMonths--;
    }

    @Override
    public String toString() {
        return "ConsumerDistributorContract{" + "consumerId=" + consumerId
                + ", price=" + price
                + ", remainedContractMonths=" + remainedContractMonths
                + ", delayed=" + delayed + '}';
    }
}
