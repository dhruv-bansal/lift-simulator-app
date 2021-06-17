package org.fidelity.lift.simulator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class Building {

    private List<Elevator> elevatorList;

    private int lowestFloor;

    private int highestFloor;

}
