package it.unicam.cs.followme.controller;

import it.unicam.cs.followme.model.*;
import it.unicam.cs.followme.parsing.ProgramFlowController;
import it.unicam.cs.followme.parsing.SimulationFlowController;
import it.unicam.cs.followme.parsing.SimulationFlowControllerBuilder;
import it.unicam.cs.followme.utilities.FollowMeParser;
import it.unicam.cs.followme.utilities.FollowMeParserException;
import it.unicam.cs.followme.file.EnvironmentFileLoader;
import it.unicam.cs.followme.file.EnvironmentLoader;
import it.unicam.cs.followme.file.ProgramFileLoader;
import it.unicam.cs.followme.file.ProgramLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controllore he prende input dalla vista e li converte in comandi per il modello
 * @param <E>
 * @param <S>
 */
public class Controller <E extends ProgrammableEntity<Direction>, S extends Shape>{
    private Environment<E, S> environment; // l'ambiente su cui lavorerà il controller
    public final static double timeUnit = 1.0; // unità di tempo di partenza
    public double now_timeUnit; // tempo attuale
    private final EnvironmentFileLoader<E, S> enviromantLoad; // si occupa di caricare il file dell'ambiente
    private final ProgramFileLoader<SimulationFlowController<E, S>, E, S> programLoad; // si occupa di di aricare il file dell'ambiente
    private List<SimulationFlowController<E, S>> controllerList; // lista di controllori

    /**
     * Costruttore del controllore
     *
     * @param el file dell'ambiente
     * @param pl file del porgramma
     */
    public Controller(EnvironmentFileLoader<E, S> el, ProgramFileLoader<SimulationFlowController<E, S>, E, S> pl) {
        this.enviromantLoad = el;
        this.programLoad = pl;
        this.environment = new Space<>(new HashMap<>(), new HashMap<>());
        this.now_timeUnit = timeUnit;
        this.controllerList = new ArrayList<>();

    }

    public Controller(EnvironmentFileLoader<E, S> envloader,
                      ProgramFileLoader<SimulationFlowController<E, S>, E, S> progloader, int time_unit) {
        this(envloader, progloader);
        this.now_timeUnit = time_unit;
    }

    /**
     * Restituisce un controllore
     */
    public static Controller<ProgrammableEntity<Direction>, Shape> getController() {
        SimulationFlowControllerBuilder<ProgrammableEntity<Direction>, Shape> builder = new SimulationFlowControllerBuilder<>();
        FollowMeParser parser = new FollowMeParser(builder);
        return new Controller<>(new EnvironmentLoader<>(parser), new ProgramLoader<>(builder));
    }

    /**
     * Imposta la timeUnit per tutti i controllori
     *
     * @param timeUnit nuova timeUnit
     */
    public void set_TimeUnit(double timeUnit) {
        this.now_timeUnit = timeUnit;
        this.controllerList.forEach(x -> x.setTimeUnit(timeUnit));
    }

    /**
     * Metodo che manda avanti la simulazizone
     */
    public void nextCommand() {
        this.controllerList.stream()
                .map(ProgramFlowController::nextCommand)
                .forEach(this.environment::apply);
    }

    /**
     * @return mappa delle entità nell'ambiente con le relative coordinate
     */
    public Map<E, Coordinate> getProgrammableEntity() {
        return this.environment.getEntityMap();
    }

    /**
     * @return mappe delle figure nell'ambiente con le relative coordinate
     */
    public Map<S, Coordinate> getShapes() {
        return this.environment.getShapes();
    }

    /**
     * @return la timeUnit attuale
     */
    public double getTimeUnit() {
        return this.now_timeUnit;
    }

    /**
     * Resetta il tutto
     */
    public void clear() {
        this.controllerList.clear();
        this.now_timeUnit = timeUnit;
        this.environment = new Space<>(new HashMap<>(), new HashMap<>());
    }

    /**
     * Genera un nuovo ambiente
     *
     * @param shapes file contente le shapes
     * @param map    mappa delle entità
     */
    public void generateEnvironment(File shapes, Map<E, Coordinate> map)
            throws FollowMeParserException, IOException {
        this.environment = this.enviromantLoad.getEnvironment(shapes, map);
    }

    /**
     * Genera il programma delle entità
     *
     * @param entities file col programma e le entità
     */
    public void generateProgram(File entities)
            throws FollowMeParserException, IOException {
        this.controllerList = this.programLoad.getFlowControllers(entities, this.environment);
    }

    /**
     * Prende in input i file di ambiente e programma e inizializza il controller
     *
     * @param figure  file con la shapes
     * @param program file del programma
     * @param map     mappa delle entità
     */
    public void createController(File figure, File program, Map<E, Coordinate> map)
            throws FollowMeParserException, IOException {
        this.generateEnvironment(figure, map);
        this.generateProgram(program);
    }
}
