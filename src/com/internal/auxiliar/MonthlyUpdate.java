package com.internal.auxiliar;

import com.entities.Consumer;

import java.util.List;

public final class MonthlyUpdate {
    private List<Consumer> newConsumers;
    private List<CostChange> costsChanges;

    public List<Consumer> getNewConsumers() {
        return newConsumers;
    }

    public void setNewConsumers(final List<Consumer> newConsumers) {
        this.newConsumers = newConsumers;
    }

    public List<CostChange> getCostsChanges() {
        return costsChanges;
    }

    public void setCostsChanges(final List<CostChange> costsChanges) {
        this.costsChanges = costsChanges;
    }

    @Override
    public String toString() {
        return "MonthlyUpdate{" + "newConsumers=" + newConsumers
                + ", costsChanges=" + costsChanges + '}';
    }
}
