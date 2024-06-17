package it.unicam.cs.followme.model;

import java.util.List;
import java.util.Map;

public interface Environment<E extends ProgrammableEntity<Direction>, S extends Shape> {

    /**
     * Genera una lista contenente le label che contengono la posizione passata come parametro
     * @param position la posizione sulla quale estrarre le label
     * @return lista delle label che contengono quella coordinata
     */
    List<S> getLabels(Coordinate position);

    /**
     * Genera una lista contentnte le label che contengono l'entità passata come paramtro
     * @param entity entità sulla quale estrerre la label
     * @return lista delle label che contengono quell'entità
     */
    List<S> getLabels(E entity);

    /**
     * Viene applicato un comando all'ambiente
     * @param command comando da applicare all'ambiente
     */
    void apply(Command<E,S> command);

    /**
     * Restituisce una mappa delle entità che segnalano la label all'interno dell'ambiente
     * @param label la label che segnalano
     * @param shape l'area in cui cercare le entità
     * @param position il centro della figura
     * @return mappa di enità con le relative coordinate
     */
    Map<E, Coordinate> getEntitySignalLabel(String label, Shape shape, Coordinate position);

    /**
     * Restituisce la mappa delle entità con le relative coordinate
     */
    Map<E,Coordinate> getEntityMap();

    /**
     * Restituisce la mappa delle figure con le relative coordinate (riferite al centro della figura)
     */
    Map<S, Coordinate> getShapes();

    /**
     * Cambia la mappa dell'ambiente con un'altra mappa passata come parametro
     * @param entity l'entità
     * @param c coordinata dell'entità
     * @return true se è stata cambiata false altrimenti
     */
    boolean replaceEntityCoordinate(E entity, Coordinate c);

    /**
     * @param entity l'entità della quale si voglione conoscere le coordinate
     * @return le coordinante dell'entità
     */
    Coordinate getCoordinates(E entity);




}
