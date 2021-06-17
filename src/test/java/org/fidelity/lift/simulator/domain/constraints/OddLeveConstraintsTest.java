package org.fidelity.lift.simulator.domain.constraints;

import org.fidelity.lift.simulator.domain.InternelRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OddLeveConstraintsTest {

    private OddLeveConstraints oddLeveConstraints;

    @BeforeEach
    void name() {
        oddLeveConstraints = new OddLeveConstraints();
    }

    @Test
    void testConstaintForInternalRequest() {

        InternelRequest internelRequest = new InternelRequest(1);
        Boolean result = oddLeveConstraints.evalute(internelRequest);
        assert result == true;
    }
}