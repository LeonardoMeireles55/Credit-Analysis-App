package com.leonardo.creditanalysisapp.service.strategy.impl;

import java.util.Random;

import com.leonardo.creditanalysisapp.domain.Proposal;
import com.leonardo.creditanalysisapp.exception.StrategyException;
import com.leonardo.creditanalysisapp.service.strategy.IPointCalc;
import com.leonardo.creditanalysisapp.statics.Messages;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScoreNameImpl implements IPointCalc {

    Random random = new Random();

    @Order(3)
    @Override
    public int calc(Proposal proposal) {

        int score = score();

        if (score < 200) {
            throw new StrategyException(
                    String.format(Messages.LOW_SCORE_NAME,
                            proposal.getUser().getName() + " " + proposal.getUser().getLastName()));
        }

        if (score <= 400) {
            return 150;
        }
        if (score <= 650) {
            return 200;
        }

        return 220;
    }

    private int score() {
        return random.nextInt(0, 1000);
    }
}
