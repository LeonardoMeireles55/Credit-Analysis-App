package com.leonardo.creditanalysisapp.exception;

import java.io.Serial;

/**
 * Exception thrown when a credit analysis strategy encounters an error that
 * should disqualify a proposal.
 */
public class StrategyException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String strategyName;

    /**
     * Creates a new strategy exception with a message
     *
     * @param message The error message
     */
    public StrategyException(String message) {
        super(message);
        this.strategyName = "Unknown";
    }

    /**
     * Creates a new strategy exception with a message and identifies the
     * strategy that threw it
     *
     * @param message The error message
     * @param strategyName The name of the strategy throwing the exception
     */
    public StrategyException(String message, String strategyName) {
        super(message);
        this.strategyName = strategyName;
    }

    /**
     * Creates a new strategy exception with a message and cause
     *
     * @param message The error message
     * @param cause The underlying cause
     */
    public StrategyException(String message, Throwable cause) {
        super(message, cause);
        this.strategyName = "Unknown";
    }

    /**
     * Creates a new strategy exception with a message, cause, and strategy name
     *
     * @param message The error message
     * @param cause The underlying cause
     * @param strategyName The name of the strategy throwing the exception
     */
    public StrategyException(String message, Throwable cause, String strategyName) {
        super(message, cause);
        this.strategyName = strategyName;
    }

    /**
     * Gets the name of the strategy that threw this exception
     *
     * @return The strategy name
     */
    public String getStrategyName() {
        return strategyName;
    }
}
