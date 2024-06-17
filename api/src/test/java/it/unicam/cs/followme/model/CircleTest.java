package it.unicam.cs.followme.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircleTest {

    Coordinate c1 = new Coordinate(0,0);
    Coordinate c2 = new Coordinate(0,1);
    Coordinate c3 = new Coordinate(0.5,0.5);
    Coordinate c4 = new Coordinate(3,2);
    Coordinate c5 = new Coordinate(1,1);
    Circle circle = new Circle( 1, "Cerchio");

    @Test
    void isContained() {
        assertTrue(circle.isContained(c1));
        assertTrue(circle.isContained(c2));
        assertTrue(circle.isContained(c3));
        assertFalse(circle.isContained(c4));
        assertFalse(circle.isContained(c5));
    }
}