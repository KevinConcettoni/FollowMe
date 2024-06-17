package it.unicam.cs.followme.file;


import it.unicam.cs.followme.model.Direction;
import it.unicam.cs.followme.model.Environment;
import it.unicam.cs.followme.model.ProgrammableEntity;
import it.unicam.cs.followme.model.Shape;
import it.unicam.cs.followme.parsing.SimulationFlowController;
import it.unicam.cs.followme.utilities.FollowMeParserException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Consente la costrizione di una lista di controllori preso in input un file
 * contente tutti i movimenti delle entità
 * @param <C> controller
 * @param <E> entità
 * @param <S> forme
 */
public interface ProgramFileLoader<C extends SimulationFlowController<E,S>,
        E extends ProgrammableEntity<Direction>, S extends Shape>{

    List<C> getFlowControllers(File program, Environment<E,S> env)
        throws IOException, FollowMeParserException;

    List<C> getFlowControllers(Path program, Environment<E,S> env)
        throws IOException, FollowMeParserException;

}
