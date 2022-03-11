package com.logic;


import com.Constants;
import com.entities.Consumer;
import com.entities.Contract;
import com.entities.Distributor;
import com.factory.ContractFactory;
import com.internal.InputFormat;
import com.internal.InternalData;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private static Simulation instance;
    private InputFormat data;
    private List<Consumer> thisRoundConsumers;
    private List<Distributor> thisRoundDistributors;
    private int currentTurn;
    private boolean hardStop;

    private Simulation() {
    }

    public static Simulation getInstance() {
        if (instance == null) {
            instance = new Simulation();
        }
        return instance;
    }

    public void setData(InputFormat inputData) {
        this.data = inputData;
    }

    public void simulate() {
        currentTurn = -1;
        thisRoundDistributors = null;
        thisRoundConsumers = null;
        hardStop = false;

        // while simulation is not over
        while (!shouldEnd()) {
            simulateRound();
        }

    }

    private boolean shouldEnd() {

        return hardStop || currentTurn >= data.getNumberOfTurns();
    }

    private void simulateRound() {
        if (currentTurn >= 0) {
            data.applyUpdate(currentTurn);
        }

        thisRoundConsumers = data.getInternalData().remainingConsumers();
        thisRoundDistributors = data.getInternalData().remainingDistributors();

        addIncomeToConsumers();
        removeBankruptContracts();
        updateNewPrices();
        removeOldContracts();
        setUpContracts();
        payContracts();
        payCosts();

        if (data.getInternalData().remainingDistributors().size() == 0) {
            hardStop = true;
            return;
        }

        updateNumberOfConsumers();
        updateContractsMonthsLeft();
        currentTurn++;
    }

    private void addIncomeToConsumers() {
        List<Consumer> consumers = this.data.getInternalData().getConsumers();
        for (Consumer consumer : consumers) {
            if (!consumer.isBankrupt()) {
                consumer.applyIncome();
            }
        }
    }

    private void removeBankruptContracts() {
        for (Distributor distributor : thisRoundDistributors) {
            List<Contract> contracts = distributor.getContracts();

            for (int i = 0; i < contracts.size(); i++) {
                Contract contract = contracts.get(i);
                Consumer consumer = consumerById(contract.getConsumerId());

                boolean consumerBankrupt = consumer.isBankrupt();

                if (consumerBankrupt) {

                    distributor.getContracts().remove(i);
                    distributor.decConsumers();
                    i--;
                }
            }
        }
    }

    private void removeOldContracts() {
        for (Distributor distributor : thisRoundDistributors) {
            List<Contract> contracts = distributor.getContracts();

            for (int i = 0; i < contracts.size(); i++) {
                Contract contract = contracts.get(i);
                Consumer consumer = consumerById(contract.getConsumerId());

                boolean isDelayed = contract.isDelayed();
                boolean consumerBankrupt = consumer.isBankrupt();
                int months = contract.getRemainedContractMonths();


                if (!isDelayed){
                    if (months <= 0) {
                        distributor.getContracts().remove(i);
                        distributor.decConsumers();
                        i--;
                        continue;
                    }
                }

                if (consumerBankrupt) {
                    distributor.getContracts().remove(i);
                    distributor.decConsumers();
                    i--;
                }
            }
        }
    }

    private void updateNewPrices() {
        List<Distributor> distributors = data.getInternalData()
                .getDistributors();

        for (Distributor distributor : distributors) {
            distributor.updateContractPrice();
        }
    }

    private void updateNumberOfConsumers() {
        for (Distributor distributor : thisRoundDistributors) {
            distributor.setCurrentConsumersNo(
                    distributor.getTemporaryCurrentConsumersNo());
        }
    }

    private void setUpContracts() {
        for (Consumer consumer : thisRoundConsumers) {
            List<Contract> contracts = contractsOf(consumer);
            // consumer does not have a contract with a distributor
            // so he needs one
            if (contracts.size() == 0) {
                setUpContract(consumer);

                // consumer has a contract with a distributor, but we need to
                // know that contract hasn't expired and the consumer hasn't
                // paid yet
            } else if (contracts.size() == 1) {
                Contract crtContract = contracts.get(0);

                if (crtContract.isDelayed()
                        && crtContract.getRemainedContractMonths() == 0) {
                    setUpContract(consumer);
                } else {
                    // idk
                    //new Exception("this_ex1").printStackTrace();
                }
                // is this even possible?
                // consumer has one unpaid contract and a new contract
            } else {
                // FIXME: ?
                new Exception("this_ex2").printStackTrace();
            }
        }
    }

    private void payContracts() {
        for (Consumer consumer : thisRoundConsumers) {
            int consumerBudget = consumer.getBudget();

            List<Contract> contracts = contractsOf(consumer);
            int contractsNo = contracts.size();

            if (contractsNo == 1) {
                Contract crtContract = contracts.get(0);

                Distributor distributor = distributorById(
                        crtContract.getDistributorId());

                boolean isDelayed = crtContract.isDelayed();
                int oldPrice = crtContract.getPrice();
                int newPrice = Math.toIntExact(
                        Math.round(Math.floor(Constants.PENALTY_FACTOR
                                * oldPrice)));

                if (isDelayed) {
                    if (oldPrice + newPrice > consumerBudget) {
                        consumer.setBankrupt(true);
                    } else {
                        // pay
                        distributor.gotPayed(oldPrice + newPrice);
                        consumer.payed(oldPrice + newPrice);
                    }
                } else {
                    if (oldPrice > consumerBudget) {
                        crtContract.setDelayed(true);
                    } else {
                        distributor.gotPayed(oldPrice);
                        consumer.payed(oldPrice);
                    }
                }
            } else if (contractsNo == 2) {
                // this means that one contracts is delayed and it has to be
                // paid and the other is the current contract

                Contract contract1 = contracts.get(0);
                Contract contract2 = contracts.get(1);


                Contract delayedContract = contract1
                        .isDelayed() ? contract1 : contract2;
                Contract normalContract = contract1
                        .isDelayed() ? contract2 : contract1;

                int delayedPrice = delayedContract.getPrice();
                delayedPrice = Math.toIntExact(
                        Math.round(Math.floor(Constants.PENALTY_FACTOR
                                * delayedPrice)));

                int normalPrice = normalContract.getPrice();

                if (normalPrice + delayedPrice > consumerBudget) {
                    consumer.setBankrupt(true);
                } else {
                    Distributor distributor1 = distributorById(
                            delayedContract.getDistributorId());

                    Distributor distributor2 = distributorById(
                            normalContract.getDistributorId());

                    distributor1.gotPayed(delayedPrice);
                    distributor2.gotPayed(normalPrice);

                    consumer.payed(normalPrice + delayedPrice);
                }
            }
        }
    }

    private void payCosts() {
        for (Distributor distributor : thisRoundDistributors) {
            List<Contract> contracts = distributor.getContracts();

            int consumatorNo = 0;

            for (Contract contract : contracts) {
                if (contract.isDelayed()
                        && contract.getRemainedContractMonths() == 0) {
                    continue;
                }
                consumatorNo++;
            }
            int priceToPay = distributor.getInfrastructureCost()
                    + distributor.getProductionCost() * consumatorNo;


            if (priceToPay > distributor.getBudget()) {
                distributor.pays(priceToPay);
                distributor.setBankrupt(true);
            } else {
                distributor.pays(priceToPay);
            }

        }
    }

    private void updateContractsMonthsLeft() {
        for (Distributor distributor : thisRoundDistributors) {
            List<Contract> contracts = distributor.getContracts();

            for (Contract contract : contracts) {
                int months = contract.getRemainedContractMonths();

                if (months > 0) {
                    contract.decrementMonths();
                }
            }
        }
    }

    private Consumer consumerById(int id) {
        for (Consumer consumer : this.data.getInternalData().getConsumers()) {
            if (consumer.getId() == id) {
                return consumer;
            }
        }

        return null;
    }

    private List<Contract> contractsOf(Consumer consumer) {

        List<Contract> contracts = new ArrayList<>();

        for (Distributor distributor : thisRoundDistributors) {
            List<Contract> contractList = distributor.getContracts();

            // sanity check
            if (distributor.isBankrupt()) {
                // clear contracts
                continue;
            }

            for (Contract contract : contractList) {
                if (contract.getConsumerId() == consumer.getId()) {
                    contracts.add(contract);
                }
            }
        }

        return contracts;
    }

    private void setUpContract(Consumer consumer) {

        int minPrice = Integer.MAX_VALUE;
        Distributor crtDistributor = null;

        for (Distributor distributor : thisRoundDistributors) {
            int price = distributor.getContractPrice();
            if (price < minPrice) {
                minPrice = price;
                crtDistributor = distributor;
            }
        }

        // this should not happen
        // another sanity check
        if (crtDistributor == null) {
            hardStop = true;
            //            new Exception().printStackTrace();
            // TODO: handle me
            return;
        }

        ContractFactory contractFactory = new ContractFactory();

        Contract newContract = contractFactory.getContract(
                Constants.CONSUMER_DISTRIBUTOR_CONTRACT);

        newContract.setConsumerId(consumer.getId());
        newContract.setDistributorId(crtDistributor.getId());
        newContract.setPrice(minPrice);
        newContract.setRemainedContractMonths(
                crtDistributor.getContractLength());

        crtDistributor.addContract(newContract);
    }

    private Distributor distributorById(int id) {
        for (Distributor distributor : this.data.getInternalData()
                .getDistributors()) {
            if (distributor.getId() == id) {
                return distributor;
            }
        }

        return null;
    }

    public InternalData getResult() {
        return data.getInternalData();
    }

}
