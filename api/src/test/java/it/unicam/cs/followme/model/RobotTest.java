package it.unicam.cs.followme.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    Robot<Direction> robot = new Robot<>(new Direction(1,1,1));

    @Test
    void signalLabel() {
        // verifico che la label sia del formato giusto
        robot.signalLabel("SbAgLiAtA-!"); // c'è il !
        assertNotEquals("SbAgLiAtA-!", robot.getLabel());
        robot.signalLabel("CoRReTTa_777");
        assertEquals("CoRReTTa_777", robot.getLabel());
        robot.signalLabel("corretta_");
        assertEquals("corretta_", robot.getLabel());
    }

    // altri test superflui ?
}