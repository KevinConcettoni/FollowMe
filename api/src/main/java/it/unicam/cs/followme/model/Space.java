package it.unicam.cs.followme.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Space<E extends ProgrammableEntity<Direction>, S extends Shape> implements Environment<E,S> {

    /**
     * Mappa che tiene traccia delle entità e delle loro coordinate nel piano
     */
    private final Map<E, Coordinate> entities;

    /**
     * Mappa che tiene traccia delle figure nel piano e della loro posizione
     */
    public final Map <S, Coordinate> shapes;

    /**
     * Costruttore dello spazio
     * @param eMap mappa delle entita
     * @param sMap mappa delle figure
     */
    public Space(Map<E,Coordinate> eMap, Map<S,Coordinate> sMap){
        if (eMap == null || sMap == null)
            throw new NullPointerException("Mappa passata nulla");
        this.entities = eMap;
        this.shapes = sMap;
    }

    /**
     * Restituisce le coordinate di un entità nel piano
     * @param entity l'entità di cui si volgiono conoscere le coordinate
     * @return le coordinate dell'entità
     */
    public Coordinate getEntityCoordinate (E entity){
        return this.entities.get(entity);
    }

    @Override
    public List<S> getLabels(Coordinate position) {
        return this.shapes.entrySet().stream()
                .filter(x-> x.getKey().isContained(RelativeCoordinate.getRelative(x.getValue(),position)))
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public List<S> getLabels(E entity) {
        return getLabels(this.getCoordinates(entity));
    }

    @Override
    public void apply(Command<E, S> command) {
        command.apply(this);
    }

    /**
     * Restituisce una lista di entità che segnalano una label all'interno di un'area
     * @param label la label che segnalano
     * @param shape l'area in cui cercare le entità
     * @param position il centro della figura
     * @return la mappa delle entità con le relative coordinate
     */
    @Override
    public Map<E, Coordinate> getEntitySignalLabel(String label, Shape shape, Coordinate position) {
        return this.entities.entrySet().stream()
                .filter(x -> label.equals(x.getKey().getLabel()))
                .filter(x -> shape.isContained(RelativeCoordinate.getRelative(position,x.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<E, Coordinate> getEntityMap() {
        return new HashMap<>(this.entities);
    }

    @Override
    public Map<S, Coordinate> getShapes() {
        return new HashMap<>(this.shapes);
    }


    /**
     * Sostituisce le coordinate di un entità con delle coordinate nuove
     * @param entity l'entità a cui assegnare le nuove coordinate
     * @param c le nuove coordinate
     * @return la mappa aggiornata
     */
    @Override
    public boolean replaceEntityCoordinate(E entity, Coordinate c) {
        if (entity == null || c == null)
            throw new NullPointerException("Parametri nulli");
        this.entities.replace(entity, c);
        return true;
    }

    /**
     * @param entity entità di cui vogliamo conoscere le coordinate
     * @return le coordinate dell'entità
     */
    @Override
    public Coordinate getCoordinates(E entity) {
        return this.entities.get(entity);
    }


}
