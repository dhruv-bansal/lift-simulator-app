package org.fidelity.lift.simulator.domain;

import com.sun.tools.javac.util.List;
import org.assertj.core.api.Assertions;
import org.fidelity.lift.simulator.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.internal.runners.InternalRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class ElevatorTest {

    private Elevator elevator;

    @BeforeEach
    void setUp() {
        elevator = new Elevator();
    }

    @Test
    void testIdealStateHandling_with_1External_And_1InternalRequest() {

        ExternalRequest externalRequest = new ExternalRequest(Direction.UP, 0);
        InternelRequest internelRequest = new InternelRequest(5);
        elevator.setCurrentFloor(new AtomicInteger(2));

        elevator.getExternalRequests().add(externalRequest);
        elevator.getInternelRequests().add(internelRequest);

        elevator.idealStateHandling(externalRequest, internelRequest);

        assert elevator.getElevatorRequestLogs().size() == 2;

        // first internal request will be served
        Assertions.assertThat(internelRequest)
                .isEqualTo(elevator.getElevatorRequestLogs().remove());

        // second external request will be served
        Assertions.assertThat(externalRequest)
                .isEqualTo(elevator.getElevatorRequestLogs().remove());

    }

    @Test
    void testDirectStateHandling_with_1External_And_1InternalRequest_test1() {

        ExternalRequest externalRequest = new ExternalRequest(Direction.UP, 0);
        InternelRequest internelRequest = new InternelRequest(5);
        elevator.setCurrentFloor(new AtomicInteger(2));

        elevator.getExternalRequests().add(externalRequest);
        elevator.getInternelRequests().add(internelRequest);

        elevator.directHandling(externalRequest, internelRequest, Direction.UP, false);

        assert elevator.getElevatorRequestLogs().size() == 1;

        // first internal request will be served
        Assertions.assertThat(internelRequest)
                .isEqualTo(elevator.getElevatorRequestLogs().remove());

    }

    @Test
    void testDirectStateHandling_with_1External_And_1InternalRequest_test2() {

        ExternalRequest externalRequest = new ExternalRequest(Direction.DOWN, 5);
        InternelRequest internelRequest = new InternelRequest(3);
        elevator.setCurrentFloor(new AtomicInteger(5));

        elevator.getExternalRequests().add(externalRequest);
        elevator.getInternelRequests().add(internelRequest);

        elevator.directHandling(externalRequest, internelRequest, Direction.UP, false);

        assert elevator.getElevatorRequestLogs().size() == 1;

        // first internal request will be served
        Assertions.assertThat(externalRequest)
                .isEqualTo(elevator.getElevatorRequestLogs().remove());
    }

    @Test
    void testDirectStateHandling_with_1External_And_1InternalRequest_test3() {

        ExternalRequest externalRequest = new ExternalRequest(Direction.UP, 1);
        InternelRequest internelRequest = new InternelRequest(3);
        elevator.setCurrentFloor(new AtomicInteger(5));

        elevator.getExternalRequests().add(externalRequest);
        elevator.getInternelRequests().add(internelRequest);

        elevator.directHandling(externalRequest, internelRequest, Direction.UP, false);

        assert elevator.getElevatorRequestLogs().size() == 1;

        // first internal request will be served
        Assertions.assertThat(externalRequest)
                .isEqualTo(elevator.getElevatorRequestLogs().remove());
    }


}
