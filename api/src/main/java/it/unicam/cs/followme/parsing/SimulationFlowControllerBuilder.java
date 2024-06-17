package it.unicam.cs.followme.parsing;

import it.unicam.cs.followme.model.*;
import it.unicam.cs.followme.utilities.RobotCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che si occupa della gestione dei comandi delle entità che si muovono nell'ambiente
 * Quando un comando viene "parsato" viene invocato il metodo dello specifico comando
 */
public class SimulationFlowControllerBuilder<E extends ProgrammableEntity<Direction>, S extends Shape>
        implements ProgramFlowControllerBuilder<SimulationFlowController<E,S>, E, S> {
    private List<Pair<RobotCommand, String[]>> listCommand; // lista di comandi da eseguire

    /**
     * Costruttore del metodo
     */
    public SimulationFlowControllerBuilder(){
        this.listCommand = new ArrayList<>();
    }

    /**
     * Metodo che indica l'inizio del parsing dei comandi
     */
    @Override
    public void parsingStarted() {
        this.listCommand = new ArrayList<>();
    }

    /**
     * Metodo che indica la fine del parsing
     */
    @Override
    public void parsingDone() {
        this.listCommand.add(new Pair<>(null, new String[0]));
    }

    @Override
    public void moveCommand(double[] args) {
        this.listCommand.add(new Pair<>(RobotCommand.MOVE, toString(args)));
    }


    @Override
    public void moveRandomCommand(double[] args) {
        this.listCommand.add(new Pair<>(RobotCommand.MOVE, toString(args)));
    }

    @Override
    public void signalCommand(String label) {
        this.listCommand.add(new Pair<>(RobotCommand.SIGNAL, new String[]{label}));
    }

    @Override
    public void unsignalCommand(String label) {
        this.listCommand.add(new Pair<>(RobotCommand.UNSIGNAL, new String[]{label}));
    }

    @Override
    public void followCommand(String label, double[] args) {
        this.listCommand.add(new Pair<>(RobotCommand.FOLLOW,
                new String[]{label, toString(args)[0], toString(args)[1]}));
    }

    @Override
    public void stopCommand() {
        this.listCommand.add(new Pair<>(RobotCommand.STOP, new String[0]));
    }

    @Override
    public void waitCommand(int s) {
            this.listCommand.add(new Pair<>(RobotCommand.SKIP,new String[]{String.valueOf(s),"0"}));
    }

    @Override
    public void repeatCommandStart(int n) {
        this.listCommand.add(new Pair<>(RobotCommand.REPEAT,new String[]{String.valueOf(n),"0"}));
    }

    @Override
    public void untilCommandStart(String label) {
        this.listCommand.add(new Pair<>(RobotCommand.UNTIL,new String[]{label}));
    }
    @Override
    public void doForeverStart() {
        this.listCommand.add(new Pair<>(RobotCommand.FOREVER,new String[0]));
    }
    @Override
    public void doneCommand() {
        this.listCommand.add(new Pair<>(RobotCommand.DONE,new String[0]));
    }

    /**
     * @param programmables i program flow controller builder
     * @param e l'ambiente con il quale costruire i programmi
     * @return ritorna una lista di ProgramFlowController per quel determinato
     */

    @Override
    public List<SimulationFlowController<E,S>> buildFlowControllers(List<E> programmables, Environment<E,S> e){
        List<SimulationFlowController<E,S>> list = new ArrayList<>(programmables.size());
        for(E p : programmables){
            ArrayList<Pair<RobotCommand,String[]>>list1 = new ArrayList<>(this.listCommand.size());
            for(Pair<RobotCommand,String[]> pair: this.listCommand){
                list1.add(new Pair<>(pair.getKey(),pair.getValue().clone()));
            }
            list.add(new SimulationFlowController<>(e,p,list1));
        }
        return list;//questa implementazione evita che ogni oggetto abbia la stessa lista
    }

    private String[] toString(double[] a){
        String[] s = new String[a.length];
        for (int i = 0; i < a.length; i++)
            s[i] = String.valueOf(a[i]);
        return s;
    }
}
