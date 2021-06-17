package org.fidelity.lift.simulator.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InternelRequest extends Request {

    private int requestedFloor;

    @Override
    public String toString() {
        return "InternelRequest{" +
                "requestedFloor=" + requestedFloor +
                '}';
    }
}
