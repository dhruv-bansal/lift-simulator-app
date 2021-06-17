package org.fidelity.lift.simulator.domain.constraints;

import org.fidelity.lift.simulator.domain.ExternalRequest;
import org.fidelity.lift.simulator.domain.InternelRequest;
import org.fidelity.lift.simulator.domain.Request;

public class EvenLevelConstraints extends Constraints {

    @Override
    public Boolean evalute(Request request) {

        ExternalRequest externalRequest = request instanceof ExternalRequest ? (ExternalRequest) request : null;
        InternelRequest internelRequest = request instanceof InternelRequest ? (InternelRequest) request : null;

        if (internelRequest != null && internelRequest.getRequestedFloor() % 2 == 0)
            return true;

        if(externalRequest != null && externalRequest.getFloorRequestedFrom() % 2 == 0)
            return true;
        return false;
    }
}
