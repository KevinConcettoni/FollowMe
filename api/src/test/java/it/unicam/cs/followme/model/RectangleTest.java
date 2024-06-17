package it.unicam.cs.followme.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RectangleTest {
    Coordinate c1 = new Coordinate(0, 0);
    Coordinate c2 = new Coordinate(0, 1);
    Coordinate c3 = new Coordinate(0.5, 0.5);
    Coordinate c4 = new Coordinate(3, 2);
    Rectangle r = new Rectangle(1, 1, "R1");

    @Test
    void isContained() {
        assertTrue(r.isContained(c3));
        assertFalse(r.isContained(c2));
        assertTrue(r.isContained(c1));
    }
}