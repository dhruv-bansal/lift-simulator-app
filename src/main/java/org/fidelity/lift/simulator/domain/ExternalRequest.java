package org.fidelity.lift.simulator.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.fidelity.lift.simulator.enums.Direction;

@Getter
@Setter
@AllArgsConstructor
public class ExternalRequest extends Request {

    private Direction direction;

    private int floorRequestedFrom;

    @Override
    public String toString() {
        return "ExternalRequest{" +
                "direction=" + direction +
                ", floorRequestedFrom=" + floorRequestedFrom +
                '}';
    }
}
