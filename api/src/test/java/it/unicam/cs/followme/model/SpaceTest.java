package it.unicam.cs.followme.model;

import org.junit.jupiter.api.Test;

import java.rmi.MarshalledObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpaceTest {

    private final Robot<Direction> robot1 =new Robot<>(new Direction(3,2,1));
    private final Robot<Direction> robot2 =new Robot<>(new Direction(0,0,1));
    private final Robot<Direction> robot3 =new Robot<>(new Direction(1,3,1));
    private final Shape circle = new Circle(1,"C");
    private final Shape rectangle = new Rectangle(2,4,"R");
    private final Coordinate coord1 = new Coordinate(0,0);
    private final Coordinate coord2 = new Coordinate(0,1);
    private final Coordinate coord3 = new Coordinate(1,2);
    private final Coordinate coord4 = new Coordinate(2,2);
    private final Coordinate coord5 = new Coordinate(1.56,1.89);
    private final Coordinate coord6 = new Coordinate(0.5, 0.5);

    @Test
    void getEntityCoordinate() {
        Map<Robot<Direction>, Coordinate> map = new HashMap<>();
        map.put(robot1, coord1);
        map.put(robot2, coord2);
        Space<Robot<Direction>, Shape> space = new Space<>(map, new HashMap<>());
        assertEquals(space.getCoordinates(robot1), coord1);
        assertEquals(space.getCoordinates(robot2), coord2);
    }

    @Test
    void getLabels() {
        Map<Shape, Coordinate> map = new HashMap<>();
        map.put(circle, coord1); // 0,0
        Space<Robot<Direction>, Shape> space = new Space<>(new HashMap<>(), map);
        List<Shape> list = space.getLabels(coord5);
        assertEquals(0, list.size());
        list = space.getLabels(coord6);
        assertEquals(1, list.size());
    }

    @Test
    void getLabelsEntity() {
        Map<Shape, Coordinate> shapeMap = new HashMap<>();
        Map<Robot<Direction>, Coordinate> entityMap = new HashMap<>();
        shapeMap.put(circle, coord4);
        shapeMap.put(rectangle, coord2);
        entityMap.put(robot1, coord6);
        entityMap.put(robot2, coord5);
        Space<Robot<Direction>, Shape> space = new Space<>(entityMap, shapeMap);
        assertEquals(1, space.getLabels(robot1).size());
        assertEquals(2,space.getLabels(robot2).size());
        assertEquals("R",space.getLabels(robot1).get(0).getLabel());
    }


    @Test
    void apply() {
        Map<Robot<Direction>,Coordinate> r=new HashMap<>();
        r.put(robot1,coord1);
        Environment<Robot<Direction>,Shape> space = new Space<>(r,new HashMap<>());
        robot1.setDirection(new Direction(0,1,1));
        space.apply(x->x.replaceEntityCoordinate(robot1,
                DirectionApply.DIRECTION_APPLY.apply(
                        robot1.getDirection(),
                        x.getCoordinates(robot1),
                        1.0)));
        assertEquals(new Coordinate(0,1),space.getCoordinates(robot1));

    }

    @Test
    void getEntitySignalLabel() {
        Map<Robot<Direction>, Coordinate> entityMap = new HashMap<>();
        Map<Shape,Coordinate> shapeMap = new HashMap<>();
        entityMap.put(robot1, coord1);
        entityMap.put(robot2, coord5);
        shapeMap.put(rectangle, coord2);
        shapeMap.put(circle, coord1);
        Space<Robot<Direction>, Shape> space = new Space<>(entityMap,shapeMap);
        robot2.signalLabel("r2");
        robot1.signalLabel("r1");
        assertEquals(1, space.getEntitySignalLabel("r2", rectangle, coord2).keySet().size());
        assertEquals(1, space.getEntitySignalLabel("r1", circle, coord1).keySet().size());

    }

    @Test
    void getEntityMap() {
        Map<Robot<Direction>,Coordinate> r=new HashMap<>();
        r.put(robot1,coord4);
        Environment<Robot<Direction>,Shape> s= new Space<>(r,new HashMap<>());
        assertNotSame(r, s.getEntityMap());
        assertEquals(r,s.getEntityMap());
    }

    @Test
    void replaceEntityCoordinate() {
        Map<Robot<Direction>, Coordinate> map = new HashMap<>();
        map.put(robot1, coord4);
        Space<Robot<Direction>, Shape> space = new Space<>(map, new HashMap<>());
        space.replaceEntityCoordinate(robot1, coord5);
        assertNotEquals(space.getCoordinates(robot1), coord4);
        assertNotEquals(space.getCoordinates(robot1), coord3);
    }

    @Test
    void getCoordinates() {
        Map<Robot<Direction>, Coordinate> map = new HashMap<>();
        map.put(robot3, coord3);
        map.put(robot1, coord4);
        Space<Robot<Direction>, Shape> space = new Space<>(map, new HashMap<>());
        assertEquals(space.getCoordinates(robot3), coord3);
        assertEquals(space.getCoordinates(robot1), coord4);

    }
}