package it.unicam.cs.followme.file;

import it.unicam.cs.followme.model.*;
import it.unicam.cs.followme.parsing.ShapeFactory;
import it.unicam.cs.followme.utilities.FollowMeParser;
import it.unicam.cs.followme.utilities.FollowMeParserException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class EnvironmentLoader<E extends ProgrammableEntity<Direction>>
        implements EnvironmentFileLoader<E, Shape> {

    private final FollowMeParser parser;

    private final ShapeFactory shapeFactory; // variabile che conterrà le figure dell'ambiente


    public EnvironmentLoader(FollowMeParser parser){
        this.parser = parser;
        this.shapeFactory = new ShapeFactory();
    }

    /**
     * Prende in input un ambiente e costruisce lo spazio
     * @param shape file dell'ambiente
     * @param map mappa di entità con le relative coordinate
     * @return il nuovo spazio generato
     */
    @Override
    public Environment<E,Shape> getEnvironment(File shape, Map<E, Coordinate> map)
            throws FollowMeParserException, IOException{
        return new Space<>(map, shapeFactory.buildMap(this.parser.parseEnvironment(shape)));
    }

    /**
     * Prende in input un path e costruisce lo spazio
     * @param shape file delel'ambiente
     * @param map mappa di entità con le relative coordinate
     * @return il nuovo spazio generato
     */
    @Override
    public Environment<E,Shape> getEnvironment(Path shape, Map<E,Coordinate> map)
        throws FollowMeParserException, IOException{
        return new Space<>(map, shapeFactory.buildMap(this.parser.parseEnvironment(shape)));
    }


}
