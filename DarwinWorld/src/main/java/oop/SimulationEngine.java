package oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine{
    private final List<Simulation> simulationList = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final List<Thread> asyncSimulationList = new ArrayList<>();
    public SimulationEngine(List<Simulation> simulationList){
        for(Simulation simulation : simulationList){
            this.simulationList.add(simulation);
        }
    }
    public void runSync(){
        for(Simulation simulation : simulationList){
            simulation.run();
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
    public void awaitSimulationsEnd() {
        for (Thread thread : asyncSimulationList) {
            while (thread.isAlive()){}
        }
        for(Thread thread : asyncSimulationList){
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
