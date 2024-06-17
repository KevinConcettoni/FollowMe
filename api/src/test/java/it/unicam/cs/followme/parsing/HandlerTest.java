package it.unicam.cs.followme.parsing;

import it.unicam.cs.followme.utilities.FollowMeParser;
import it.unicam.cs.followme.utilities.FollowMeParserException;
import org.junit.jupiter.api.Test;
import it.unicam.cs.followme.model.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HandlerTest {

    private final Robot<Direction> robot1 = new Robot<>(new Direction(3,0,1));
    private final Robot<Direction> robot2 = new Robot<>(new Direction(1,0,1));
    private final Coordinate coord1 = new Coordinate(0,0);
    private final Coordinate coord2 = new Coordinate(0,1);

    private Map<Robot<Direction>, Coordinate> robotMap;

    @Test
    void Test1() throws FollowMeParserException, IOException {
        robotMap=new HashMap<>();
        robotMap.put(robot1,coord1);
        Space<Robot<Direction>,Shape> e = new Space<>(robotMap,new HashMap<>());
        SimulationFlowControllerBuilder<Robot<Direction>,Shape> handler
                = new SimulationFlowControllerBuilder<>();
        FollowMeParser parser= new FollowMeParser(handler);
        parser.parseRobotProgram(Path.of("src/test/resources/MoveCommand"));
        List<SimulationFlowController<Robot<Direction>,Shape>> a
                = handler.buildFlowControllers(robotMap.keySet().stream().toList(),e);
        e.apply(a.get(0).nextCommand());
        assertEquals(e.getCoordinates(robot1),coord2);
    }

    @Test
    void Test2() throws FollowMeParserException, IOException{
        robotMap=new HashMap<>();
        robotMap.put(robot2,coord1);
        Space<Robot<Direction>,Shape> e = new Space<>(robotMap,new HashMap<>());
        SimulationFlowControllerBuilder<Robot<Direction>,Shape> handler
                = new SimulationFlowControllerBuilder<>();
        FollowMeParser p= new FollowMeParser(handler);
        p.parseRobotProgram(Path.of("src/test/resources/RepeatCommand"));
        SimulationFlowController<Robot<Direction>,Shape> a =
                handler.buildFlowControllers(List.of(robot2),e).get(0);
        Command<Robot<Direction>,Shape> c= a.nextCommand();
        int cont=0;
        while(!a.isEnd()){
            e.apply(c);
            c = a.nextCommand();
            cont++;
        }
        assertEquals(3,cont);
    }

}
