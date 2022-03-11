package com.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonPropertyOrder({"id", "isBankrupt", "budget"})
public final class Consumer implements Entity {
    private int id;
    private int budget;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int monthlyIncome;
    private boolean isBankrupt;

    /**
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * @return
     */
    @JsonProperty("budget")
    public int getBudget() {
        return budget;
    }

    /**
     * @param budget
     */
    @JsonSetter("initialBudget")
    public void setBudget(final int budget) {
        this.budget = budget;
    }

    /**
     * @param monthlyIncome
     */
    public void setMonthlyIncome(final int monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    /**
     * @return
     */
    @JsonGetter("isBankrupt")
    public boolean isBankrupt() {
        return isBankrupt;
    }

    /**
     * @param bankrupt
     */
    @JsonSetter("isBankrupt")
    public void setBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    /**
     * applies the monthly income for the consumer
     */
    public void applyIncome() {
        budget += monthlyIncome;
    }


    /**
     * pays to the distributor
     *
     * @param sumOfMoney - the sum of money the consumer has to pay
     */
    public void payed(final int sumOfMoney) {
        if (sumOfMoney >= 0) {
            this.budget -= sumOfMoney;
        }
    }

    @Override
    public String toString() {
        return "Consumer{" + "id=" + id + ", budget=" + budget
                + ", monthlyIncome=" + monthlyIncome + ", isBankrupt="
                + isBankrupt + '}';
    }
}
