package it.unicam.cs.followme.controller;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SimulationMapBuilderTest {

    @Test
    void getMap() {
        SimulationMapBuilder map = new SimulationMapBuilder();
        assertEquals(5, map.getMap(5).size());
    }

    @Test
    void getMapRandom() {
        SimulationMapBuilder map = new SimulationMapBuilder();
        assertEquals(7, map.getMap(7,new Random()).size());
    }
}