package org.fidelity.lift.simulator.domain.constraints;

import org.fidelity.lift.simulator.domain.ExternalRequest;
import org.fidelity.lift.simulator.domain.InternelRequest;
import org.fidelity.lift.simulator.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvenLevelConstraintsTest {

    private EvenLevelConstraints evenLevelConstraints;

    @BeforeEach
    void setUp() {
        evenLevelConstraints = new EvenLevelConstraints();
    }

    @Test
    void testConstaintForInternalRequest() {

        InternelRequest internelRequest = new InternelRequest(1);
        Boolean result = evenLevelConstraints.evalute(internelRequest);

        assert result == false;

    }

    @Test
    void testConstaintForExternalRequest() {

        ExternalRequest externalRequest = new ExternalRequest(Direction.UP, 2);
        Boolean result = evenLevelConstraints.evalute(externalRequest);

        assert result == true;

    }
}