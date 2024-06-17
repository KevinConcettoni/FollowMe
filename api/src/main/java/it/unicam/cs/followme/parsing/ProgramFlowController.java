package it.unicam.cs.followme.parsing;

import it.unicam.cs.followme.model.Command;
import it.unicam.cs.followme.model.Direction;
import it.unicam.cs.followme.model.ProgrammableEntity;
import it.unicam.cs.followme.model.Shape;

public interface ProgramFlowController<E extends ProgrammableEntity<Direction>, S extends Shape> {

    /**
     * @return il comando successivo
     */
    Command<E,S> nextCommand();

    /**
     * @return true se non ci sono più comandi da eseguire
     */
    boolean isEnd();

    /**
     * sostituisce la time unit con quella specificata
     * @param timeUnit la nuova time unit
     */
    void setTimeUnit(double timeUnit);

}
