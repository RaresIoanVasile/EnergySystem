package com.internal;

import com.entities.Consumer;
import com.entities.Distributor;

import java.util.ArrayList;
import java.util.List;

public final class InternalData {
    private List<Consumer> consumers;
    private List<Distributor> distributors;

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(final List<Consumer> consumers) {
        this.consumers = consumers;
    }

    public List<Distributor> getDistributors() {
        return distributors;
    }

    public void setDistributors(final List<Distributor> distributors) {
        this.distributors = distributors;
    }

    /**
     * @return remaining customers (that are not bankrupt)
     */
    public List<Consumer> remainingConsumers() {
        List<Consumer> consumers = new ArrayList<>();

        for (Consumer consumer : this.consumers) {
            if (!consumer.isBankrupt()) {
                consumers.add(consumer);
            }
        }

        return consumers;
    }

    /**
     * @return remaining distributors (that are not bankrupt)
     */
    public List<Distributor> remainingDistributors() {
        List<Distributor> distributors = new ArrayList<>();

        for (Distributor distributor : this.distributors) {
            if (!distributor.isBankrupt()) {
                distributors.add(distributor);
            }
        }

        return distributors;
    }

    /**
     * @param id distributor
     * @return distributor
     */
    Distributor distributorById(final int id) {
        for (Distributor distributor : distributors) {
            if (distributor.getId() == id) {
                return distributor;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "InitialData{" + "consumers=" + consumers + ",\n distributors="
                + distributors + '}';
    }
}
