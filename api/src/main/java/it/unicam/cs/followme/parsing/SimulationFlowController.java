package it.unicam.cs.followme.parsing;

import it.unicam.cs.followme.model.*;
import it.unicam.cs.followme.utilities.RobotCommand;

import java.util.*;
import java.util.function.Function;

public class SimulationFlowController<E extends ProgrammableEntity<Direction>, S extends Shape>
    implements ProgramFlowController<E,S> {
    private final List<Pair<RobotCommand, String[]>> listCommand; // lista dei comandi da eseguire
    Environment<E,S> enviroment; // l'ambiente di riferimento
    public static final double timeUnit = 1.0; // il tempo di default nella simulazione
    private double now_timeUnit; // il tempo attuale nella simulazione
    E entity; // entità che eseguirà i comandi
    private final Stack<Integer> stack; // stack di indici
    private int step; // numero di passi

    public SimulationFlowController(Environment<E,S> e, E entity, List<Pair<RobotCommand, String[]>> l, double t){
        if (e == null || entity == null || l == null)
            throw new NullPointerException("Parametri nulli");
        this.enviroment = e;
        this.entity = entity;
        this.listCommand = l;
        this.now_timeUnit = t;
        this.stack = new Stack<>();
    }

    public SimulationFlowController(Environment<E,S> e, E entity, List<Pair<RobotCommand, String[]>> l){
        this(e,entity,l,timeUnit);
    }

    /**
     * @return true se la simulazione è terminata, false altrimenti
     */
    public boolean isEnd(){
        return this.step == this.listCommand.size()-1;
    }

    private Command<E,S> baseCommand(RobotCommand c, String[] s){
        return switch (c){
            case SIGNAL,UNSIGNAL -> signal(s[0]);
            case SKIP -> skip(s);
            case MOVE -> moveCommand(s);
            case FOLLOW -> follow(s[0], s);
            default -> null;
        };
    }

    /**
     * @param c comando assegnato ad un robot
     * @return true se il comando è un comando di loop
     */
    private boolean isLoop(RobotCommand c){
        return switch(c){
            case REPEAT, UNTIL, FOREVER, DONE -> true;
            default -> false;
        };
    }

    private Command<E,S> loopCommand(RobotCommand c, String[] s){
        return switch (c){
            case FOREVER -> doForever();
            case DONE -> done();
            case REPEAT -> repeat(s);
            case UNTIL -> until(s[0]);
            default -> null;
        };
    }

    public Command<E,S> nextCommand(){
        if (isEnd())
            return x -> {};
        RobotCommand c = this.listCommand.get(this.step).getKey();
        String[] s = this.listCommand.get(this.step).getValue();
        if (isLoop(c))
            return loopCommand(c,s);
        else
            return baseCommand(c,s);
    }

    private Command<E,S> doForever(){
        this.stack.push(this.step);
        this.step++;
        return nextCommand();
    }

    private Command<E,S> done(){
        this.step = this.stack.pop();
        return nextCommand();
    }

    private Command<E,S> repeat(String[] s){
        List<Integer> list = parseToN(s, Integer::parseInt);
        if(list.get(0)>list.get(1)){
            this.listCommand.get(this.step).getValue()[1]= Integer.toString(list.get(1)+1);
            this.stack.push(this.step++);
            return nextCommand();
        }
        this.listCommand.get(this.step).getValue()[1]= Integer.toString(0);
        jump();
        return nextCommand();
    }
    private Command<E,S> until(String label){
        if(this.enviroment.getLabels(this.entity).stream().map(Shape::getLabel)
                .filter(label::equals).toList().isEmpty())
            this.stack.push(this.step++);
        else
            jump();
        return nextCommand();
    }
    private Command<E,S> move(){
        return e-> e.replaceEntityCoordinate(
                this.entity, DirectionApply.DIRECTION_APPLY.apply(
                        this.entity.getDirection(), e.getCoordinates(this.entity), this.now_timeUnit
                ));
    }
    private Command<E,S> signal(String l){
        if(this.listCommand.get(this.step++).getKey()==RobotCommand.SIGNAL)
            this.entity.signalLabel(l);
        else
            this.entity.unsignalLabel(l);
        return e -> {};//non deve muovere il robot
    }

    private Command<E,S> skip(String[] s){
        int x = Integer.parseInt(s[0]);
        int y = Integer.parseInt(s[1]);
        if(x <= y){
            this.listCommand.get(this.step).getValue()[1]="0";
            this.step++;
            return nextCommand();
        }else {
            this.listCommand.get(this.step).getValue()[1]=Integer.toString(y+1);
            return move();
        }
    }

    private Command<E,S> follow(String l,String[] s) {
        this.step++;
        List<Double> p = List.of(Double.parseDouble(s[1]),Double.parseDouble(s[2]));
        Map<E,Coordinate> map= enviroment.getEntitySignalLabel(l,new Circle(p.get(0),""),
                enviroment.getCoordinates(this.entity));
        if(!map.isEmpty()){
            double x= map.values().stream().map(Coordinate::getX).reduce(0.0,Double::sum)/map.size();
            double y= map.values().stream().map(Coordinate::getY).reduce(0.0,Double::sum)/map.size();
            Coordinate c= RelativeCoordinate.getRelative( new Coordinate(x,y),this.enviroment.getCoordinates(this.entity));
            if(c.getX()==0&&c.getY()==0)moveRandom(-p.get(0), p.get(0), -p.get(0), p.get(0), p.get(1));
            else entity.setDirection(new Direction(c,p.get(1)));
        } else moveRandom( -p.get(0), p.get(0), -p.get(0), p.get(0), p.get(1) );
        return move();
    }

    /*
    salta al prossimo comdando
     */
    private void jump(){
        int count=1;
        for(this.step++; this.step < this.listCommand.size() && count > 0;this.step++)
            if(isLoop(this.listCommand.get(this.step).getKey()))
                if(this.listCommand.get(this.step).getKey()==RobotCommand.DONE)
                    count--;
                else
                    count++;
    }

    private void moveRandom(double x1, double x2, double y1, double y2, double s){
        this.entity.setDirection(
                new Direction(RelativeCoordinate.getRelative(
                        new Coordinate(getRandom(x1,x2), getRandom(y1,y2)), this.enviroment.getCoordinates(this.entity)
        ),s));
    }
    private Command<E,S> moveCommand(String[] s){
        List<Double> p = parseToN(s,Double::parseDouble);
        this.step++;
        /* se non è move (che accetta 3 parametri)
            allora è move random (5 parametri)*/
        if(p.size()==3)
            this.entity.setDirection(new Direction(p.get(0),p.get(1),p.get(2)) );
        else
            moveRandom(p.get(0),p.get(1),p.get(2),p.get(3),p.get(4));
        return move();
    }
    private double getRandom(double n1, double n2){
        Random r = new Random();
        return r.nextDouble(n1,n2);
    }

    private <N extends Number> List<N> parseToN (String[]a , Function<String,N> f){
        return Arrays.stream(a)
                .map(f).toList();
    }

    public void setTimeUnit(double timeUnit){
        this.now_timeUnit = timeUnit;
    }

}
