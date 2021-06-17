package org.fidelity.lift.simulator.integrationtest;

import org.assertj.core.api.Assertions;
import org.fidelity.lift.simulator.domain.*;
import org.fidelity.lift.simulator.domain.constraints.EvenLevelConstraints;
import org.fidelity.lift.simulator.domain.constraints.OddLeveConstraints;
import org.fidelity.lift.simulator.enums.Direction;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


class LiftSimulatorIntegrationsTest {

    @Test
    void testSimulator_with2Elevator_and1ExternalAnd1InternalRequest() throws InterruptedException {

        Elevator elevator1 = new Elevator();

        List<Elevator> elevatorList = new ArrayList<>();
        elevator1.setElevatorIdentifier(1);
        elevator1.setConstraints(new OddLeveConstraints());
        elevatorList.add(elevator1);

        Building building = new Building(elevatorList, -1, 10);
        ElevatorController elevatorController = new ElevatorController(building);

        // initializing complete ecosystem.
        elevatorController.init();

        ExternalRequest externalRequest = new ExternalRequest(Direction.UP, 2);
        ExternalRequest externalRequest1 = new ExternalRequest(Direction.DOWN, 7);
        InternelRequest internelRequest = new InternelRequest(5);
        InternelRequest internelRequest1 = new InternelRequest(3);


        elevatorController
                .accept(externalRequest);
        elevatorController.accept(externalRequest1);

        elevator1.acceptInternelRequest(internelRequest);
        elevator1.acceptInternelRequest(internelRequest1);

        Thread.sleep(3000);
        System.out.println(elevator1.getElevatorRequestLogs().size());

        System.out.println(elevator1.getElevatorRequestLogs());
        assert elevator1.getElevatorRequestLogs().size() == 3; // as  oddlevel contraint is present

        Assertions.assertThat(elevator1.getElevatorRequestLogs().remove()).isEqualTo(internelRequest);
        Assertions.assertThat(elevator1.getElevatorRequestLogs().remove()).isEqualTo(externalRequest1);
        Assertions.assertThat(elevator1.getElevatorRequestLogs().remove()).isEqualTo(internelRequest1);
    }

    @Test
    void testSimulator_withMultipleElevator_andMultipleExternalAndMultipleInternalRequest() throws InterruptedException {

        Elevator elevator1 = new Elevator();
        Elevator elevator2 = new Elevator();

        List<Request> elevator1Request = new ArrayList<>();
        List<Request> elevator2Request = new ArrayList<>();

        List<Elevator> elevatorList = new ArrayList<>();
        elevator1.setElevatorIdentifier(1);
        elevator1.setConstraints(new OddLeveConstraints());
        elevatorList.add(elevator1);
        elevator2.setElevatorIdentifier(2);
        elevator2.setConstraints(new EvenLevelConstraints());

        elevatorList.add(elevator2);

        Building building = new Building(elevatorList, -1, 10);
        ElevatorController elevatorController = new ElevatorController(building);

        // initializing complete ecosystem.
        elevatorController.init();

        ExternalRequest externalRequest = new ExternalRequest(Direction.DOWN
                , 2);
        ExternalRequest externalRequest1 = new ExternalRequest(Direction.UP, 7);
        InternelRequest internelRequest = new InternelRequest(3);
        InternelRequest internelRequest1 = new InternelRequest(4);

        elevator1Request.add(internelRequest);
        elevator1Request.add(externalRequest1);

        elevator2Request.add(internelRequest1);
        elevator2Request.add(externalRequest);

        elevatorController
                .accept(externalRequest);
        elevatorController.accept(externalRequest1);

        elevator1.acceptInternelRequest(internelRequest);

        elevator2.acceptInternelRequest(internelRequest1);

        Thread.sleep(1000);


        assert elevator1.getElevatorRequestLogs().size() == 2;
        assert elevator2.getElevatorRequestLogs().size() == 2;

        Assertions.assertThat(elevator1.getElevatorRequestLogs()).hasSameElementsAs(elevator1Request);
        Assertions.assertThat(elevator2.getElevatorRequestLogs()).hasSameElementsAs(elevator2Request);

    }

}