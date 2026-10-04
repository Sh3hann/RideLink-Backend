package com.ridelink.ride.service;

import com.ridelink.ride.exception.InvalidStateTransitionException;
import com.ridelink.ride.model.RideStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RideStateMachine Unit Tests")
class RideStateMachineTest {

    private RideStateMachine stateMachine;

    @BeforeEach
    void setUp() {
        stateMachine = new RideStateMachine();
    }

    @ParameterizedTest(name = "Valid Transition: {0} -> {1}")
    @CsvSource({
            "REQUESTED, ASSIGNED",
            "REQUESTED, CANCELLED",
            "ASSIGNED, ACCEPTED",
            "ASSIGNED, CANCELLED",
            "ASSIGNED, REQUESTED",
            "ACCEPTED, IN_PROGRESS",
            "ACCEPTED, CANCELLED",
            "IN_PROGRESS, COMPLETED"
    })
    @DisplayName("Should allow valid ride state transitions")
    void testValidTransitions(RideStatus from, RideStatus to) {
        assertTrue(stateMachine.canTransition(from, to),
                () -> String.format("Expected transition from %s to %s to be valid", from, to));
        assertDoesNotThrow(() -> stateMachine.validateTransition(from, to));
    }

    @ParameterizedTest(name = "Invalid Transition: {0} -> {1}")
    @CsvSource({
            "REQUESTED, COMPLETED",
            "REQUESTED, IN_PROGRESS",
            "ASSIGNED, COMPLETED",
            "ACCEPTED, COMPLETED",
            "IN_PROGRESS, REQUESTED",
            "IN_PROGRESS, CANCELLED",
            "COMPLETED, REQUESTED",
            "COMPLETED, IN_PROGRESS",
            "COMPLETED, CANCELLED",
            "CANCELLED, REQUESTED",
            "CANCELLED, ACCEPTED"
    })
    @DisplayName("Should reject invalid ride state transitions and throw InvalidStateTransitionException")
    void testInvalidTransitions(RideStatus from, RideStatus to) {
        assertFalse(stateMachine.canTransition(from, to),
                () -> String.format("Expected transition from %s to %s to be invalid", from, to));

        InvalidStateTransitionException ex = assertThrows(
                InvalidStateTransitionException.class,
                () -> stateMachine.validateTransition(from, to)
        );

        assertEquals(from, ex.getCurrentStatus());
        assertEquals(to, ex.getTargetStatus());
        assertTrue(ex.getMessage().contains("Invalid ride status transition"));
    }

    @Test
    @DisplayName("Should return empty allowed transitions for terminal states")
    void testTerminalStates() {
        Set<RideStatus> completedTransitions = stateMachine.getAllowedTransitions(RideStatus.COMPLETED);
        assertTrue(completedTransitions.isEmpty(), "COMPLETED must be a terminal state with no allowed transitions");

        Set<RideStatus> cancelledTransitions = stateMachine.getAllowedTransitions(RideStatus.CANCELLED);
        assertTrue(cancelledTransitions.isEmpty(), "CANCELLED must be a terminal state with no allowed transitions");
    }

    @Test
    @DisplayName("Null parameters should safely evaluate to false")
    void testNullSafety() {
        assertFalse(stateMachine.canTransition(null, RideStatus.ACCEPTED));
        assertFalse(stateMachine.canTransition(RideStatus.REQUESTED, null));
        assertFalse(stateMachine.canTransition(null, null));
        assertTrue(stateMachine.getAllowedTransitions(null).isEmpty());
    }
}
