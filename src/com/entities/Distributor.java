package com.entities;

import com.Constants;
import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"id", "budget", "bankrupt", "contracts"})
public final class Distributor implements Entity {
    private int id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int contractLength;
    private int budget;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int infrastructureCost;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int productionCost;
    private boolean bankrupt;
    @JsonIgnore
    private int currentConsumersNo;
    @JsonIgnore
    private int temporaryCurrentConsumersNo;
    private List<Contract> contracts = new ArrayList<>();
    @JsonIgnore
    private int contractPrice;

    public int getContractPrice() {
        return contractPrice;
    }

    public int getTemporaryCurrentConsumersNo() {
        return temporaryCurrentConsumersNo;
    }


    public void setCurrentConsumersNo(final int currentConsumersNo) {
        this.currentConsumersNo = currentConsumersNo;
    }

    @JsonGetter("isBankrupt")
    public boolean isBankrupt() {
        return bankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        this.bankrupt = bankrupt;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @JsonGetter("budget")
    public int getBudget() {
        return budget;
    }

    @JsonSetter("initialBudget")
    public void setBudget(final int budget) {
        this.budget = budget;
    }

    @JsonGetter("contracts")
    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(final List<Contract> contracts) {
        this.contracts = contracts;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(final int contractLength) {
        this.contractLength = contractLength;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    @JsonSetter("initialInfrastructureCost")
    public void setInfrastructureCost(final int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public int getProductionCost() {
        return productionCost;
    }

    @JsonSetter("initialProductionCost")
    public void setProductionCost(final int productionCost) {
        this.productionCost = productionCost;
    }

    /**
     * inc
     */
    public void incConsumers() {
        temporaryCurrentConsumersNo++;
    }

    /**
     * dec
     */
    public void decConsumers() {
        temporaryCurrentConsumersNo--;
    }


    /**
     * updates the contract price for this month
     */
    public void updateContractPrice() {
        int number = 0;

        for (Contract contract : contracts) {
            int months = contract.getRemainedContractMonths();
            if (months >= 0) {
                number++;
            }
        }

        number = contracts.size();

        int profit = Math.toIntExact(
                Math.round(Math.floor(Constants.PROFIT_FACTOR
                        * productionCost)));
        int price = 0;
        if (number == 0) {
            price = infrastructureCost + productionCost + profit;
        } else {
            price = Math.toIntExact(Math.round(
                    Math.floor(infrastructureCost / number)
                            + productionCost + profit));
        }

        contractPrice = price;
    }

    /**
     * @param contract contract to add
     */
    public void addContract(final Contract contract) {
        this.contracts.add(contract);
    }

    /**
     * @param sumOfMoney money received
     */
    public void gotPayed(final int sumOfMoney) {
        if (sumOfMoney >= 0) {
            this.budget += sumOfMoney;
        }
    }

    /**
     * @param sumOfMoney to pay
     */
    public void pays(final int sumOfMoney) {
        if (sumOfMoney >= 0) {
            this.budget -= sumOfMoney;
        }
    }

    @Override
    public String toString() {
        return "Distributor{" + "id=" + id + ", contractLength="
                + contractLength + ", budget=" + budget
                + ", infrastructureCost="
                + infrastructureCost + ", productionCost=" + productionCost
                + ", contracts=" + contracts + '}';
    }
}
