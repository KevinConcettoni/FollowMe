package it.unicam.cs.followme.file;

import it.unicam.cs.followme.model.*;
import it.unicam.cs.followme.utilities.FollowMeParserException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * Interfaccia che consente la costruzione di un'ambiente preso in input un file o un path
 * @param <E> l'entità dell'ambiente
 * @param <S> le figure dell'ambiente
 */
public interface EnvironmentFileLoader<E extends ProgrammableEntity<Direction>, S extends Shape> {

    /**
     * @param shape path delle figure
     * @param entityMap mappa delle entità
     * @return l'ambiente generato dal path
     */
    Environment<E,S> getEnvironment(Path shape, Map<E, Coordinate> entityMap)
        throws IOException, FollowMeParserException;

    /**
     * @param shape file delle figure
     * @param entityMap mappa delle entità
     * @return l'ambiente generato dal file
     */
    Environment<E,S> getEnvironment(File shape, Map<E,Coordinate> entityMap)
        throws IOException, FollowMeParserException;
}
