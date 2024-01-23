package oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine{
    private final List<Simulation> simulationList = new ArrayList<>();
    private final List<Thread> asyncSimulationList = new ArrayList<>();
    public SimulationEngine(List<Simulation> simulationList){
        for(Simulation simulation : simulationList){
            this.simulationList.add(simulation);
        }
    }

    public void runAsync(){
        for(Simulation simulation : simulationList){
            asyncSimulationList.add(new Thread(simulation));
        }
        for(Thread thread : asyncSimulationList){
            thread.start();
        }
    }

}
