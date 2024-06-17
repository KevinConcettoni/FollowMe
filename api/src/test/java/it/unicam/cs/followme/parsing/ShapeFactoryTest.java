package it.unicam.cs.followme.parsing;

import it.unicam.cs.followme.model.*;
import it.unicam.cs.followme.utilities.FollowMeParser;
import it.unicam.cs.followme.utilities.FollowMeParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShapeFactoryTest {

    @Test
    void buildMap() throws IOException, FollowMeParserException {
        ShapeFactory sb = new ShapeFactory();
        FollowMeParser parser = new FollowMeParser(new SimulationFlowControllerBuilder<>());
        //Coordinate Cerchio 0,0
        //Coordinate Rettangolo 1,1
        Map<Shape, Coordinate> map = sb.buildMap(
                parser.parseEnvironment(Path.of("src/test/resources/EnvironmentFile")));
        Space<Robot<Direction>, Shape> space = new Space<>(new HashMap<>(), map);
        assertEquals("R", space.getLabels(new Coordinate(1,1)).get(0).getLabel());
        assertEquals("R", space.getLabels(new Coordinate(2,2)).get(0).getLabel());



    }
}