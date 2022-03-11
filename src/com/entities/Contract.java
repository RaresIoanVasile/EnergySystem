package com.entities;

public interface Contract {
    /**
     * @return id
     */
    default int getConsumerId() {
        return 0;
    }

    /**
     * @param consumerId set
     */
    default void setConsumerId(int consumerId) {

    }

    /**
     * @return price
     */
    default int getPrice() {
        return 0;
    }

    /**
     * @param price set
     */
    default void setPrice(int price) {

    }

    /**
     * @return months
     */
    default int getRemainedContractMonths() {
        return 0;
    }

    /**
     * @param remainedContractMonths set
     */
    default void setRemainedContractMonths(int remainedContractMonths) {

    }

    /**
     * @return delayed
     */
    default boolean isDelayed() {
        return false;
    }

    /**
     * @param delayed set
     */
    default void setDelayed(boolean delayed) {

    }

    /**
     * @return id
     */
    default int getDistributorId() {
        return 0;
    }

    /**
     * @param distributorId id
     */
    default void setDistributorId(int distributorId) {

    }

    /**
     * dec
     */
    void decrementMonths();
}
