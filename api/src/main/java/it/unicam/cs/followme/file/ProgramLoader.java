package it.unicam.cs.followme.file;

import it.unicam.cs.followme.model.Direction;
import it.unicam.cs.followme.model.Environment;
import it.unicam.cs.followme.model.ProgrammableEntity;
import it.unicam.cs.followme.model.Shape;
import it.unicam.cs.followme.parsing.SimulationFlowControllerBuilder;
import it.unicam.cs.followme.parsing.SimulationFlowController;
import it.unicam.cs.followme.utilities.FollowMeParser;
import it.unicam.cs.followme.utilities.FollowMeParserException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ProgramLoader<E extends ProgrammableEntity<Direction>, S extends Shape>
    implements ProgramFileLoader<SimulationFlowController<E,S>, E, S> {

    private final FollowMeParser parser;
    private final SimulationFlowControllerBuilder<E,S> builder;

    public ProgramLoader(SimulationFlowControllerBuilder<E,S> parser){
        this.builder = parser;
        this.parser = new FollowMeParser(parser);
    }

    /**
     * @param file il file del programma
     * @param e l'ambiente di riferimento
     * @return lista dei flow controller
     */
    @Override
    public List<SimulationFlowController<E,S>> getFlowControllers (File file, Environment<E,S> e)
        throws FollowMeParserException, IOException{
        this.parser.parseRobotProgram(file );
        return this.builder.buildFlowControllers(e.getEntityMap().keySet().stream().toList(), e);
    }

    /**
     *
     * @param path il path del programma
     * @param e l'ambiente di riferimento
     * @return lista di flow controller
     */
    @Override
    public List<SimulationFlowController<E,S>> getFlowControllers (Path path, Environment<E,S> e)
            throws FollowMeParserException, IOException{
        this.parser.parseRobotProgram(path);
        return this.builder.buildFlowControllers(e.getEntityMap().keySet().stream().toList(), e);
    }



}
