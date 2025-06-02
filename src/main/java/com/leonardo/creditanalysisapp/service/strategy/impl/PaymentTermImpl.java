package com.leonardo.creditanalysisapp.service.strategy.impl;

import com.leonardo.creditanalysisapp.domain.Proposal;
import com.leonardo.creditanalysisapp.service.strategy.IPointCalc;

import org.springframework.stereotype.Component;

@Component
public class PaymentTermImpl implements IPointCalc {

    @Override
    public int calc(Proposal proposal) {
        return paymentTermIsLower(proposal);
    }

    private int paymentTermIsLower(Proposal proposal) {
        if (proposal.getPaymentTerm() <= 120) {
            return 80;
        }
        return 0;
    }

}
