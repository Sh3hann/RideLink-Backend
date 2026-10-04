package com.ridelink.ride.service;

import com.ridelink.ride.exception.InvalidStateTransitionException;
import com.ridelink.ride.model.RideStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Component
public class RideStateMachine {

    // Documented Valid State Transitions for RideLink Lifecycle
    private static final Map<RideStatus, Set<RideStatus>> VALID_TRANSITIONS = new EnumMap<>(RideStatus.class);

    static {
        VALID_TRANSITIONS.put(RideStatus.REQUESTED, EnumSet.of(RideStatus.ASSIGNED, RideStatus.CANCELLED));
        VALID_TRANSITIONS.put(RideStatus.ASSIGNED, EnumSet.of(RideStatus.ACCEPTED, RideStatus.CANCELLED, RideStatus.REQUESTED));
        VALID_TRANSITIONS.put(RideStatus.ACCEPTED, EnumSet.of(RideStatus.IN_PROGRESS, RideStatus.CANCELLED));
        VALID_TRANSITIONS.put(RideStatus.IN_PROGRESS, EnumSet.of(RideStatus.COMPLETED));
        VALID_TRANSITIONS.put(RideStatus.COMPLETED, Collections.emptySet());
        VALID_TRANSITIONS.put(RideStatus.CANCELLED, Collections.emptySet());
    }

    /**
     * Checks if a transition from currentStatus to targetStatus is permitted.
     */
    public boolean canTransition(RideStatus currentStatus, RideStatus targetStatus) {
        if (currentStatus == null || targetStatus == null) {
            return false;
        }
        Set<RideStatus> allowed = VALID_TRANSITIONS.getOrDefault(currentStatus, Collections.emptySet());
        return allowed.contains(targetStatus);
    }

    /**
     * Validates transition and throws InvalidStateTransitionException if illegal.
     */
    public void validateTransition(RideStatus currentStatus, RideStatus targetStatus) {
        if (!canTransition(currentStatus, targetStatus)) {
            Set<RideStatus> allowed = VALID_TRANSITIONS.getOrDefault(currentStatus, Collections.emptySet());
            throw new InvalidStateTransitionException(currentStatus, targetStatus, allowed);
        }
    }

    /**
     * Returns the set of permitted next statuses from the given status.
     */
    public Set<RideStatus> getAllowedTransitions(RideStatus currentStatus) {
        if (currentStatus == null) return Collections.emptySet();
        return Collections.unmodifiableSet(VALID_TRANSITIONS.getOrDefault(currentStatus, Collections.emptySet()));
    }
}
