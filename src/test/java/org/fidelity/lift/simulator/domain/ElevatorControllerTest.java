package org.fidelity.lift.simulator.domain;

import org.assertj.core.api.Assertions;
import org.fidelity.lift.simulator.enums.Direction;
import org.fidelity.lift.simulator.enums.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ElevatorControllerTest {

    @Mock
    private Building building;

    @InjectMocks
    private ElevatorController elevatorController;


    @Test
    void testTryAssingingRequest_for_building_with_1Elevator_withIdleState() {

        ExternalRequest externalRequest = new ExternalRequest(Direction.UP, 0);
        ArrayList<Elevator> elevatorList = new ArrayList<>();
        elevatorList.add(new Elevator());
        Mockito.lenient().when(building.getElevatorList()).thenReturn(elevatorList);

        boolean requestAssinged = elevatorController.tryAssigningRequest(building, externalRequest);

        Assertions.assertThat(requestAssinged).isEqualTo(true);
    }

    @Test
    void testTryAssingingRequest_for_building_with_1Elevator_withOppositeInitialState() {

        ExternalRequest externalRequest = new ExternalRequest(Direction.UP, 0);

        ArrayList<Elevator> elevatorList = new ArrayList<>();

        //setting state to opposite direction
        Elevator elevator = new Elevator();
        elevator.setCurrentState(State.MOVING, Direction.UP, 5);
        elevatorList.add(elevator);

        Mockito.lenient().when(building.getElevatorList()).thenReturn(elevatorList);

        boolean requestAssinged = elevatorController.tryAssigningRequest(building, externalRequest);
        Assertions.assertThat(requestAssinged).isEqualTo(false);
    }

    @Test
    void testTryAssingingRequest_for_building_with_2Elevator_1ElevatorIdle_2ElevatorOppositeDirection() {

        ExternalRequest externalRequest = new ExternalRequest(Direction.UP, 0);

        ArrayList<Elevator> elevatorList = new ArrayList<>();
        //setting state to opposite direction
        Elevator elevator = new Elevator();
        elevator.setCurrentState(State.MOVING, Direction.UP, 5);
        elevatorList.add(elevator);

        //adding idle elevator
        elevatorList.add(new Elevator());

        Mockito.lenient().when(building.getElevatorList()).thenReturn(elevatorList);

        boolean requestAssinged = elevatorController.tryAssigningRequest(building, externalRequest);
        Assertions.assertThat(requestAssinged).isEqualTo(true);
    }

    @Test
    void testGetclosestElevator_forbuild_with_2Elevator() {

        ExternalRequest externalRequest = new ExternalRequest(Direction.UP, 0);

        ArrayList<Elevator> elevatorList = new ArrayList<>();
        Elevator elevator1 = new Elevator();
        elevator1.setCurrentState(State.MOVING, Direction.UP, 5);
        elevatorList.add(elevator1);

        Elevator elevator2 = new Elevator();
        elevator2.setCurrentState(State.MOVING, Direction.UP, 9);
        elevatorList.add(elevator2);

        Mockito.lenient().when(building.getElevatorList()).thenReturn(elevatorList);
        Mockito.lenient().when(building.getHighestFloor()).thenReturn(10);

        Elevator elevator = elevatorController.getclosestElevator(building, externalRequest);

        Assertions.assertThat(elevator).isEqualTo(elevator1);
    }
}