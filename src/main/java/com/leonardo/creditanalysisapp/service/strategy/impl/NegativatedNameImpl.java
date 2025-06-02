package com.leonardo.creditanalysisapp.service.strategy.impl;

import java.util.Random;

import com.leonardo.creditanalysisapp.domain.Proposal;
import com.leonardo.creditanalysisapp.exception.StrategyException;
import com.leonardo.creditanalysisapp.service.strategy.IPointCalc;
import com.leonardo.creditanalysisapp.statics.Messages;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Strategy implementation that checks if a user's name is in a negative credit
 * list. Currently uses a random simulation for demonstration purposes. In a
 * real application, this would connect to an external credit check service.
 */
@Order(2)
@Component
@Slf4j
public class NegativatedNameImpl implements IPointCalc {

    private static final int PASSING_POINTS = 100;
    private final Random random = new Random();

    @Override
    public int calc(Proposal proposal) {
        String userName = proposal.getUser().getName() + " " + proposal.getUser().getLastName();
        log.debug("Checking negativated name status for user: {}", userName);

        if (isNameNegativated()) {
            String errorMessage = Messages.NEGATIVATED_NAME.formatted(userName);
            log.info("User {} has a negativated credit status", userName);
            throw new StrategyException(errorMessage);
        }

        log.debug("User {} passed negativated name check", userName);
        return PASSING_POINTS;
    }

    /**
     * Simulates checking if a name is on a negative credit list. In a real
     * implementation, this would call an external credit check service.
     *
     * @return true if the name is negativated, false otherwise
     */
    protected boolean isNameNegativated() {
        return random.nextBoolean();
    }
}
