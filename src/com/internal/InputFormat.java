package com.internal;

import com.entities.Consumer;
import com.entities.Distributor;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.internal.auxiliar.CostChange;
import com.internal.auxiliar.MonthlyUpdate;

import java.util.List;

public final class InputFormat {
    private int numberOfTurns;
    private InternalData internalData;
    private List<MonthlyUpdate> monthlyUpdates;

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setNumberOfTurns(final int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public InternalData getInternalData() {
        return internalData;
    }

    @JsonSetter("initialData")
    public void setInternalData(final InternalData internalData) {
        this.internalData = internalData;
    }

    public List<MonthlyUpdate> getMonthlyUpdates() {
        return monthlyUpdates;
    }

    public void setMonthlyUpdates(final List<MonthlyUpdate> monthlyUpdates) {
        this.monthlyUpdates = monthlyUpdates;
    }

    /**
     * @param turn applies the coresponding update
     */
    public void applyUpdate(final int turn) {
        MonthlyUpdate monthlyUpdate = monthlyUpdates.get(turn);

        List<Consumer> consumers = internalData.getConsumers();

        consumers.addAll(monthlyUpdate.getNewConsumers());

        for (CostChange costChange : monthlyUpdate.getCostsChanges()) {
            int distributorId = costChange.getId();
            Distributor distributor = internalData.distributorById(
                    distributorId);

            int newInfrastructureCost = costChange.getInfrastructureCost();
            int newProductionCost = costChange.getProductionCost();
            distributor.setInfrastructureCost(newInfrastructureCost);
            distributor.setProductionCost(newProductionCost);
        }

    }

    @Override
    public String toString() {
        return "InputFormat{" + "numberOfTurns=" + numberOfTurns
                + ",\n initialData=" + internalData + ",\n monthlyUpdates="
                + monthlyUpdates + '}';
    }
}
