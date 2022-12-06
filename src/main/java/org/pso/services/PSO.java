package org.pso.services;

import org.pso.model.Specimen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.pso.utils.Utils.Utilities.*;

public class PSO {
    private final CsvStatisticsHolder statisticsHolder = CsvStatisticsHolder.getInstance();

    private int currentGeneration = 0;

    private List<Specimen> population;

    private Specimen bestSolution;

    private final Random random = new Random();

    private double w;
    private double c1;
    private double c2;
    private double r1;
    private double r2;

    public PSO() {
        InitializePopulation();
        InitializeParameters();
    }

    private void InitializePopulation() {
        population = new ArrayList<>(PSO_POPULATION_SIZE);
        for (int i = 0; i < PSO_POPULATION_SIZE; i++)
            population.add(new Specimen());
    }

    private void InitializeParameters() {
        w = W;
        c1 = C1;
        c2 = C2;
    }

    public Specimen psoCycle() {
        statisticsHolder.addNewGenerationStatistics(currentGeneration, population);
        bestSolution = findBest(population).deepClone();

        while (currentGeneration < PSO_ITERATIONS) {
            currentGeneration++;

            var solution = findBest(population).deepClone();


            if (solution.getObjectiveFunction() < bestSolution.getObjectiveFunction()) {
                bestSolution = solution;
            }
            population.forEach(s -> {
                r1 = random.nextDouble();
                r2 = random.nextDouble();

                s.initiallySetObjectiveFunction();
                s.setNewVelocityAndPosition(w, c1, r1, c2, r2, bestSolution);
            });
            System.out.printf("generation: %s: %s%n", currentGeneration, bestSolution.getObjectiveFunction());
        }
        return bestSolution;
    }

    private Specimen findBest(List<Specimen> examinedPopulation) {
        examinedPopulation.sort(Comparator.comparingDouble(Specimen::getObjectiveFunction));
        return examinedPopulation.get(0);
    }
}
