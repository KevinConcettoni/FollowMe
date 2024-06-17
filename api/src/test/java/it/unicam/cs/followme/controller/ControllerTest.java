package it.unicam.cs.followme.controller;

import it.unicam.cs.followme.model.*;
import it.unicam.cs.followme.utilities.FollowMeParserException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    Controller<ProgrammableEntity<Direction>,Shape> controller = Controller.getController();
    File robot = new File("src/test/resources/RobotFile");
    File env = new File("src/test/resources/EnvironmentFile");
    @Test
    void next() throws IOException, FollowMeParserException {
        Map<ProgrammableEntity<Direction>,Coordinate> m = new HashMap<>();
        Robot<Direction> r1 =new Robot<>(new Direction(1,0,1));
        Robot<Direction> r2 =new Robot<>(new Direction(0,1,1));
        m.put(r1,new Coordinate(0,0));
        m.put(r2,new Coordinate(0, 1));
        controller.createController(env,robot,m);
        controller.nextCommand();
        assertEquals(new Coordinate(1,0),controller.getProgrammableEntity().get(r1));
        assertEquals(new Coordinate(1,1),controller.getProgrammableEntity().get(r2));
        controller.nextCommand();
        assertEquals(new Coordinate(0,0),controller.getProgrammableEntity().get(r1));
        assertEquals(new Coordinate(0,1),controller.getProgrammableEntity().get(r2));
    }

    @Test
    void clear() throws IOException, FollowMeParserException{
        controller.createController(env, robot, new SimulationMapBuilder().getMap(10));
        assertEquals(2,controller.getShapes().size());
        controller.set_TimeUnit(2);
        controller.clear();
        assertEquals(0,controller.getProgrammableEntity().size());
        assertEquals(0,controller.getShapes().size());
        assertEquals(Controller.timeUnit,controller.getTimeUnit());
    }

    @Test
    void cerateController() throws IOException, FollowMeParserException{
        Map<ProgrammableEntity<Direction>,Coordinate> map = new HashMap<>();
        Robot<Direction> r1 =new Robot<>(new Direction(1,0,1));
        Robot<Direction> r2 =new Robot<>(new Direction(0,1,1));
        map.put(r1, new Coordinate(0,0));
        map.put(r2, new Coordinate(1,0));
        controller.createController(env, robot, map);
        assertEquals(map,controller.getProgrammableEntity());
        assertEquals(2,controller.getShapes().size());
    }
}