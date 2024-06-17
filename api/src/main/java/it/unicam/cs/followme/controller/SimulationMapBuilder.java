package it.unicam.cs.followme.controller;

import it.unicam.cs.followme.model.Coordinate;
import it.unicam.cs.followme.model.Direction;
import it.unicam.cs.followme.model.ProgrammableEntity;
import it.unicam.cs.followme.model.Robot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Genera una mappa di robot
 */
public class SimulationMapBuilder {

    /**
     * Genera mappa di entità
     * @param n numero di entità da generare
     * @param random serve per generare casualmente
     * @return mappa di enità con posizioni casuali
     */
    public Map<ProgrammableEntity<Direction>, Coordinate> getMap(int n, Random random) {
        Map<ProgrammableEntity<Direction>,Coordinate> map = new HashMap<>();
        for(int i=0; i<n;i++)
            map.put(new Robot<>(
                            new Direction(random.nextDouble(),random.nextDouble(),random.nextDouble())),
                    new Coordinate(random.nextDouble()*random.nextInt(-10,10),
                            random.nextDouble()*random.nextInt(-10, 10))
            );
        return map;
    }

    /**
     * Genera mappa di entità
     * @param n numro di robot da generare
     * @return mappa di entità con posizioni casuali
     */
    public Map<ProgrammableEntity<Direction>, Coordinate> getMap(int n){
        return getMap(n, new Random());
    }
}
