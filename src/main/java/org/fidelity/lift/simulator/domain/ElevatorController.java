package org.fidelity.lift.simulator.domain;

import lombok.Getter;
import org.fidelity.lift.simulator.enums.Direction;
import org.fidelity.lift.simulator.enums.State;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a main controller class corresponds to the central
 * controller that controls all the elevators
 */
@Getter
public class ElevatorController implements Runnable {

    private Building building;
    private Queue<ExternalRequest> requests = new LinkedList<>();

    public ElevatorController(Building building) {
        this.building = building;
    }

    public ElevatorController accept(ExternalRequest request) {
        this.requests.add(request);
        return this;
    }

    public void assignRequest(Building building) {

        while (true) {

            if (!requests.isEmpty()) {

                ExternalRequest request = requests.remove();
                Boolean requestAssigned = tryAssigningRequest(building, request);

                // if request is not assigned
                if (!requestAssigned) {
                    Elevator elevator = getclosestElevator(building, request);
                    if (elevator != null) {
                        elevator.acceptExternalRequest(request);
                    }
                }
            }
        } // while true
    }

    /**
     * This method is called if by algorithm request can't be assigned to
     * any elevator then request is assigned to closed elevator
     *
     * @param building
     * @param request
     */
    Elevator getclosestElevator(Building building, ExternalRequest request) {
        // think of better logic
        List<Elevator> elevatorList = building.getElevatorList();

        Elevator closestElevator = null;
        int shortestElevatorDistance = building.getHighestFloor();

        for (Elevator elevator : elevatorList) {
            boolean canAccept = elevator.canAccept(request);
            int elevatorDistance = Math.abs(elevator.getCurrentFloor().get() - request.getFloorRequestedFrom());
            if (canAccept && elevatorDistance < shortestElevatorDistance) {
                closestElevator = elevator;
                shortestElevatorDistance = elevatorDistance;
            }
            // find closest elevator and assign request
        }

        return closestElevator;
    }

    boolean tryAssigningRequest(Building building, ExternalRequest request) {

        List<Elevator> elevatorList = building.getElevatorList();
        boolean requestAssigned = false;

        for (Elevator elevator : elevatorList) {
            boolean canAccept = elevator.canAccept(request);

            if (canAccept) {
                if (elevator.getCurrentDirection() == Direction.UP &&
                        elevator.getCurrentFloor().get() < request.getFloorRequestedFrom()) {
                    requestAssigned = true;
                }
                if (elevator.getCurrentDirection() == Direction.DOWN &&
                        elevator.getCurrentFloor().get() > request.getFloorRequestedFrom()) {
                    requestAssigned = true;
                }
                if (elevator.getState() == State.IDLE) {
                    requestAssigned = true;
                }
            }
            if (requestAssigned) {
                elevator.acceptExternalRequest(request);
                break;
            }
            // if nobody accepts the request the give request
        }
        return requestAssigned;
    }

    /**
     * Initialize the controller and related elevators
     *
     * @return
     */
    public ElevatorController init() {
        Thread controllerThread = new Thread(this);
        // controllerThread.setDaemon(true);
        controllerThread.start(); // start all elevator
        return this;
    }

    @Override
    public void run() {


        List<Elevator> elevatorList = building.getElevatorList();

        // this will start all elevators
        elevatorList.stream().forEach(elevator -> {
            Thread elevatorThread = new Thread(elevator);
            // elevatorThread.setDaemon(true);
            elevatorThread.start();
        });
        assignRequest(this.building);

    }
}
