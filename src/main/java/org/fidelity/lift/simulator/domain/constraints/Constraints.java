package org.fidelity.lift.simulator.domain.constraints;

import org.fidelity.lift.simulator.domain.Request;

public abstract class Constraints {

    public abstract Boolean evalute(Request request);
}
