package com.factory;

import com.Constants;
import com.entities.ConsumerDistributorContract;
import com.entities.Contract;

public final class ContractFactory {
    /**
     * @param type
     * @return a new consumer_distributor contract
     */
    public Contract getContract(final String type) {
        if (type.equals(Constants.CONSUMER_DISTRIBUTOR_CONTRACT)) {
            return new ConsumerDistributorContract();
        }
        return null;
    }
}
