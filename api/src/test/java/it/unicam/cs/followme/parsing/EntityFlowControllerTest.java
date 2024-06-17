package it.unicam.cs.followme.parsing;

import it.unicam.cs.followme.model.*;
import it.unicam.cs.followme.utilities.FollowMeParser;
import it.unicam.cs.followme.utilities.FollowMeParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityFlowControllerTest {

    private final Robot<Direction> r1 =new Robot<>(new Direction(3,0,1));
    private final Robot<Direction>r2 =new Robot<>(new Direction(1,1,1));
    private final Robot<Direction>r3 =new Robot<>(new Direction(1,2,1));
    private final Shape c = new Circle(1,"A");
    private final Shape r = new Rectangle(2,4,"B");
    private final Coordinate c1 = new Coordinate(0,0);
    private final Coordinate c2 = new Coordinate(0,1);
    private final Coordinate c3 = new Coordinate(0.5,0.5);
    private final Coordinate c4 = new Coordinate(3,2);
    private final Coordinate c5 = new Coordinate(1.01593,1.54909);

    @Test
    void repeatTest() throws FollowMeParserException {
        Robot<Direction>r1 =new Robot<>(new Direction(3,0,1));
        Map<Robot<Direction>,Coordinate> m=new HashMap<>();
        m.put(r1,c1);
        Environment<Robot<Direction>,Shape> e = new Space<>(m,new HashMap<>());
        SimulationFlowControllerBuilder<Robot<Direction>,Shape> handler = new SimulationFlowControllerBuilder<>();
        FollowMeParser p= new FollowMeParser(handler);
        p.parseRobotProgram("REPEAT 4\nMOVE 0 1 1\nDONE\n");
        SimulationFlowController<Robot<Direction>,Shape> a = handler.buildFlowControllers(List.of(r1),e).get(0);
        Command<Robot<Direction>,Shape> c= a.nextCommand();
        int cont=0;
        while(!a.isEnd()){
            e.apply(c);
            c=a.nextCommand();
            cont++;
        }
        assertEquals(4,cont);
        assertEquals(new Coordinate(0,4),e.getCoordinates(r1));
    }

    @Test
    public void untilTest() throws FollowMeParserException{
        Robot<Direction>r1 =new Robot<>(new Direction(3,0,1));
        Map<Robot<Direction>,Coordinate> m =new HashMap<>();
        Map<Shape,Coordinate> f =new HashMap<>();
        m.put(r1,c1);
        f.put(c,new Coordinate(0,5));
        Environment<Robot<Direction>,Shape> e = new Space<>(m,f);
        SimulationFlowControllerBuilder<Robot<Direction>,Shape> handler = new SimulationFlowControllerBuilder<>();
        FollowMeParser p= new FollowMeParser(handler);
        p.parseRobotProgram("UNTIL A\nMOVE 0 1 1\nDONE\n");
        SimulationFlowController<Robot<Direction>,Shape> a = handler.buildFlowControllers(List.of(r1),e).get(0);
        Command<Robot<Direction>,Shape> c= a.nextCommand();
        while(!a.isEnd()){
            e.apply(c);
            c=a.nextCommand();
        }
        assertEquals(new Coordinate(0,4),e.getCoordinates(r1));
    }
    @Test
    public void untilRepeatTest() throws FollowMeParserException, IOException {
        Robot<Direction>r1 =new Robot<>(new Direction(3,0,1));
        Map<Robot<Direction>,Coordinate> m =new HashMap<>();
        Map<Shape,Coordinate> f =new HashMap<>();
        m.put(r1,c1);
        f.put(c,new Coordinate(0,5));
        Environment<Robot<Direction>,Shape> e = new Space<>(m,f);
        SimulationFlowControllerBuilder<Robot<Direction>,Shape> handler = new SimulationFlowControllerBuilder<>();
        FollowMeParser p= new FollowMeParser(handler);
        p.parseRobotProgram(Path.of("src/test/resources/UntilCommand"));
        SimulationFlowController<Robot<Direction>,Shape> a = handler.buildFlowControllers(List.of(r1),e).get(0);
        Command<Robot<Direction>,Shape> c= a.nextCommand();
        int count =0;
        while(!e.getCoordinates(r1).equals(new Coordinate(0,4))){
            e.apply(c);
            c=a.nextCommand();
            count++;
        }
        assertEquals(new Coordinate(0,4),e.getCoordinates(r1));
        assertEquals(12,count);//numero di operazioni fatte
    }

    @Test
    public void doForeverTest() throws FollowMeParserException{
        Robot<Direction>r1 =new Robot<>(new Direction(3,0,1));
        Map<Robot<Direction>,Coordinate> m =new HashMap<>();
        m.put(r1,c1);
        Environment<Robot<Direction>,Shape> e = new Space<>(m,new HashMap<>());
        SimulationFlowControllerBuilder<Robot<Direction>,Shape> handler = new SimulationFlowControllerBuilder<>();
        FollowMeParser p= new FollowMeParser(handler);
        p.parseRobotProgram("DO FOREVER \n MOVE 0 -1 1 \n DONE ");
        SimulationFlowController<Robot<Direction>,Shape> a = handler.buildFlowControllers(List.of(r1),e).get(0);
        for(int i = 0 ; i < 20 ;i++)
            e.apply(a.nextCommand());
        assertEquals(new Coordinate(0,-20),e.getCoordinates(r1));
    }
    @Test
    public void followTest() throws FollowMeParserException, IOException{
        Robot<Direction>r1 =new Robot<>(new Direction(3,0,1));
        Robot<Direction>r2 =new Robot<>(new Direction(1,1,1));
        Map<Robot<Direction>,Coordinate> m =new HashMap<>();
        m.put(r1,c1);
        m.put(r2,new Coordinate(1,1));
        Map<Shape,Coordinate> fi = new HashMap<>();
        fi.put(new Circle(0.2,"A"),new Coordinate(1,1));
        Environment<Robot<Direction>,Shape> e = new Space<>(m,fi);
        SimulationFlowControllerBuilder<Robot<Direction>,Shape> handler = new SimulationFlowControllerBuilder<>();
        FollowMeParser p= new FollowMeParser(handler);
        p.parseRobotProgram(Path.of("src/test/resources/FollowCommand"));
        List<SimulationFlowController<Robot<Direction>,Shape>> a = handler.buildFlowControllers(List.of(r1,r2),e);
        a.stream().map(ProgramFlowController::nextCommand).forEach(e::apply);
        assertEquals(new Coordinate(1,1),e.getCoordinates(r2));
        while(!r1.getLabel().equals("A"))
            e.apply(a.get(0).nextCommand());
        assertEquals("A",r1.getLabel());

    }

    @Test
    void continueTest() throws FollowMeParserException, IOException{
        Robot<Direction>r1 =new Robot<>(new Direction(3,0,1));
        Map<Robot<Direction>,Coordinate> m =new HashMap<>();
        m.put(r1,c1);
        Environment<Robot<Direction>,Shape> e = new Space<>(m,new HashMap<>());
        SimulationFlowControllerBuilder<Robot<Direction>,Shape> handler = new SimulationFlowControllerBuilder<>();
        FollowMeParser p= new FollowMeParser(handler);
        p.parseRobotProgram(Path.of("src/test/resources/SkipCommand"));
        List<SimulationFlowController<Robot<Direction>,Shape>> a =
                handler.buildFlowControllers(List.of(r1),e);
        for(int i=0; i<3;i++){
            e.apply(a.get(0).nextCommand());
            assertEquals(new Coordinate(i+1,0),e.getCoordinates(r1));
        }
    }
}
