package com.leonardo.creditanalysisapp.service;

import java.util.List;

import com.leonardo.creditanalysisapp.domain.Proposal;
import com.leonardo.creditanalysisapp.dto.ProposalDTO;
import com.leonardo.creditanalysisapp.exception.StrategyException;
import com.leonardo.creditanalysisapp.mapper.ProposalMapper;
import com.leonardo.creditanalysisapp.service.strategy.IPointCalc;
import com.leonardo.creditanalysisapp.statics.Messages;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditAnalysisService {

    private final List<IPointCalc> pointCalcList;
    private final RabbitNotificationService rabbitNotificationService;
    private final ProposalMapper proposalMapper;

    @Value("${rabbitmq.completed-exchange.exchange}")
    private String exchangePropostaConcluida;

    @Value("${credit.analysis.approval.threshold:250}")
    private int approvalThreshold;

    public void analysis(ProposalDTO proposalDTO) {
        log.info("Starting credit analysis for proposal with value: {}", proposalDTO.getProposalValue());

        Proposal proposal = proposalMapper.toEntity(proposalDTO);

        try {
            int totalPoints = calculateTotalPoints(proposal);
            boolean approved = totalPoints > approvalThreshold;

            proposal.setApproved(approved);
            proposal.setObservation(formatObservation(approved, proposal));

            log.info("Credit analysis completed. Proposal {} with total points: {}",
                    approved ? "APPROVED" : "REJECTED", totalPoints);

        } catch (StrategyException ex) {
            log.warn("Strategy exception during analysis: {}", ex.getMessage());
            proposal.setApproved(false);
            proposal.setObservation(ex.getMessage());
        }

        rabbitNotificationService.notification(exchangePropostaConcluida, proposal);
    }

    private int calculateTotalPoints(Proposal proposal) {
        return pointCalcList.stream()
                .mapToInt(strategy -> strategy.calc(proposal))
                .sum();
    }

    private String formatObservation(boolean approved, Proposal proposal) {
        String template = approved ? Messages.APPROVED_NAME : Messages.NOT_APPROVED_NAME;
        return String.format(template, proposal.getUser().getName() + " " + proposal.getUser().getLastName());
    }
}
