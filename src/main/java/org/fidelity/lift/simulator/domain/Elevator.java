package org.fidelity.lift.simulator.domain;

import com.google.common.collect.EvictingQueue;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fidelity.lift.simulator.domain.constraints.Constraints;
import org.fidelity.lift.simulator.enums.Direction;
import org.fidelity.lift.simulator.enums.State;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Slf4j
@Setter
public class Elevator implements Runnable {

    private Queue<ExternalRequest> externalRequests = new LinkedList<>();
    private Queue<InternelRequest> internelRequests = new LinkedList<>();

    // current state
    private AtomicInteger currentFloor = new AtomicInteger(0);
    private State state = State.IDLE;
    private Direction currentDirection = Direction.UP;

    private Constraints constraints;

    private int elevatorIdentifier;


    // evicting queue to collect request logs service by this elevator
    private
    Queue<Request> elevatorRequestLogs = EvictingQueue.create(10);


    // check if request can be accepted by elevator
    public Boolean canAccept(ExternalRequest request) {
        // check for both external and internal request seperately
        // CASE 1
        if (null != constraints && !constraints.evalute(request)) {
            return false;
        }
        return true;
    }

    public void acceptExternalRequest(ExternalRequest externalRequest) {
        externalRequests.add(externalRequest);
    }

    // this method will direct accept the internal request
    public Boolean acceptInternelRequest(InternelRequest request) {
        if (null != constraints && !constraints.evalute(request)) {
            return false;
        }
        internelRequests.add(request);
        return false;
    }

    public void serveRequest() {

        while (true) {
            ExternalRequest externalRequest = externalRequests.peek();
            InternelRequest internelRequest = internelRequests.peek();

            // figure out which request to server first
            boolean isProcessed = idealStateHandling(externalRequest, internelRequest);

            if (!isProcessed && state == State.MOVING) {

                isProcessed = directHandling(externalRequest, internelRequest, Direction.UP, isProcessed);
                directHandling(externalRequest, internelRequest, Direction.DOWN, isProcessed);
            }

            if (externalRequests.isEmpty() && internelRequests.isEmpty()) {
                state = State.IDLE;
            }
        }

    }

    boolean directHandling(ExternalRequest externalRequest, InternelRequest internelRequest, Direction direction, boolean isProcessed) {

        int externalrequestFloorDiff = externalRequest != null ? currentFloor.get() - externalRequest.getFloorRequestedFrom() : -1;
        int internalrequestFloorDiff = internelRequest != null ? currentFloor.get() - internelRequest.getRequestedFloor() : -1;

        boolean isProcess = false;
        if (!isProcessed && currentDirection == direction) {

            if (internelRequest != null && externalRequest != null) {
                if (externalrequestFloorDiff <= internalrequestFloorDiff) {
                    externalRequestProcessing(externalRequest, direction);
                    isProcess = true;
                } else {
                    processInternalRequest(internelRequest, direction);
                    isProcess = true;
                }
            } else if (internelRequest != null) {
                if (internelRequest.getRequestedFloor() > currentFloor.get()) {
                    currentDirection = Direction.UP;
                } else {
                    currentDirection = Direction.DOWN;
                }
                isProcess = true;
                processInternalRequest(internelRequest, currentDirection);
            } else if (externalRequest != null) {
                if (externalRequest.getFloorRequestedFrom() > currentFloor.get()) {
                    currentDirection = Direction.UP;
                } else {
                    currentDirection = Direction.DOWN;
                }
                isProcess = true;
                externalRequestProcessing(externalRequest, currentDirection);
            }
        }

        return isProcess;
    }

    boolean idealStateHandling(ExternalRequest externalRequest, InternelRequest internelRequest) {
        boolean isProcessed = false;
        if (state == State.IDLE) {
            if (internelRequest != null) {

                if (internelRequest.getRequestedFloor() > currentFloor.get()) {
                    currentDirection = Direction.UP;
                } else {
                    currentDirection = Direction.DOWN;
                }
                processInternalRequest(internelRequest, currentDirection);
                isProcessed = true;
            }
            if (externalRequest != null) {
                if (externalRequest.getFloorRequestedFrom() > currentFloor.get()) {
                    currentDirection = Direction.UP;
                } else {
                    currentDirection = Direction.DOWN;
                }
                externalRequestProcessing(externalRequest, currentDirection);
                isProcessed = true;
            }
        }
        return isProcessed;
    }

    private void processInternalRequest(InternelRequest internelRequest, Direction up) {
        internelRequests.remove();
        currentDirection = up;
        state = State.MOVING;
        updateCurrentFloor(internelRequest.getRequestedFloor());
        logElevatorRequest(internelRequest);
    }

    private void externalRequestProcessing(ExternalRequest externalRequest, Direction down) {
        externalRequests.remove();
        currentDirection = down;
        state = State.MOVING;
        updateCurrentFloor(externalRequest.getFloorRequestedFrom());
        logElevatorRequest(externalRequest);
    }

    @Override
    public void run() {
        this.serveRequest();
    }

    public void logElevatorRequest(Request request) {
        elevatorRequestLogs.add(request);
    }

    /**
     * This method sets the current states in the Elevator
     *
     * @param state
     * @param direction
     * @param currentFloor
     */
    public void setCurrentState(State state, Direction direction, Integer currentFloor) {
        this.state = state;
        direction = direction;
        this.currentFloor = new AtomicInteger(currentFloor);
    }

    private void updateCurrentFloor(int floor) {
        final boolean[] stopped = new boolean[1];
        if (state == State.MOVING) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int currentFloorNo = currentFloor.incrementAndGet();
                    currentFloor.set(currentFloorNo);
                    if (currentFloorNo == floor) {
                        stopped[0] = this.cancel();
                    }
                    if (currentFloorNo == 10) {
                        stopped[0] = this.cancel();
                    }
                }
            }, 2, 14);

            if (stopped[0]) {
                state = State.IDLE;
            }
        }
        if (state == State.IDLE) {
            currentFloor.set(floor);
        }
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "currentFloor=" + currentFloor +
                ", state=" + state +
                ", currentDirection=" + currentDirection +
                '}';
    }

}
