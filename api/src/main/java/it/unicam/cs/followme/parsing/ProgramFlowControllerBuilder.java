package it.unicam.cs.followme.parsing;

import it.unicam.cs.followme.model.Direction;
import it.unicam.cs.followme.model.Environment;
import it.unicam.cs.followme.model.ProgrammableEntity;
import it.unicam.cs.followme.model.Shape;
import it.unicam.cs.followme.utilities.FollowMeParserHandler;

import java.util.List;

public interface ProgramFlowControllerBuilder<C extends ProgramFlowController<E,S>,
    E extends ProgrammableEntity<Direction>, S extends Shape> extends FollowMeParserHandler {

    List<C> buildFlowControllers (List<E> entities, Environment<E,S> env);
}
