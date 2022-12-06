package org.pso.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Specimen {

    @Getter
    private final DataSet dataSet;
    @Getter
    @Setter
    public List<City> citiesVisitedInOrder;
    @Getter
    @Setter
    public double objectiveFunction;
    @Getter
    @Setter
    //PSO
    public List<Double> currentVelocity;
    @Getter
    @Setter
    public List<Double> bestPosition;
    @Getter
    @Setter
    public List<Double> currentPosition;
    private final Random random;

    public Specimen() {
        dataSet = DataSet.getInstance();
        random = new Random();
        citiesVisitedInOrder = new ArrayList<>();
        objectiveFunction = 0;

        fillTheVisitedCitiesList();
        initializeVelocities();
        initializePosition();
        initiallySetObjectiveFunction();
    }

    private void initializeVelocities() {
        currentVelocity = new ArrayList<>();
        for (int i = 0; i < dataSet.getNumberOfCities(); i++) {
            currentVelocity.add(random.nextDouble() * 2.0 - 1.0); //from -1 to 1
        }
    }

    private void initializePosition() {
        bestPosition = new ArrayList<>();
        for (int i = 0; i < dataSet.getNumberOfCities(); i++) {
            bestPosition.add((double) i);
        }
        currentPosition = new ArrayList<>();
        for (int i = 0; i < dataSet.getNumberOfCities(); i++) {
            currentPosition.add((double) i);
        }
    }

    private void fillTheVisitedCitiesList() {
        citiesVisitedInOrder = new ArrayList<>();

        for (int i = 0; i < dataSet.getCities().size(); i++)
            citiesVisitedInOrder.add(dataSet.getCities().get(i).deepClone());

        Collections.shuffle(citiesVisitedInOrder);
    }

    public void initiallySetObjectiveFunction() {
        var newObjectiveFunction = getTotalDistanceTraveled();
        if (newObjectiveFunction < this.objectiveFunction) {
            bestPosition.clear();
            bestPosition.addAll(currentPosition);

        }
        this.objectiveFunction = newObjectiveFunction;
    }

    private double getTotalDistanceTraveled() {
        double totalDistance = 0;
        for (int i = 0; i < citiesVisitedInOrder.size() - 1; i++) {
            totalDistance += getDistance(citiesVisitedInOrder.get(i).getIndex(),
                    citiesVisitedInOrder.get(i + i).getIndex());
        }

        totalDistance += getDistance(citiesVisitedInOrder.get(citiesVisitedInOrder.size() - 1).getIndex(),
                citiesVisitedInOrder.get(0).getIndex());
        return totalDistance;
    }

    private double getDistance(int sourceCityIdentifier, int destinationCityIdentifier) {
        return dataSet.getDistanceMatrix()
                .get(dataSet.getCities().get(sourceCityIdentifier))
                .get(dataSet.getCities().get(destinationCityIdentifier));
    }

    public Specimen deepClone() {
        Specimen newSpecimen = new Specimen();
        newSpecimen.setCitiesVisitedInOrder(new ArrayList<>());
        newSpecimen.setObjectiveFunction(objectiveFunction);
        newSpecimen.setBestPosition(new ArrayList<>(bestPosition));
        newSpecimen.setCurrentVelocity(new ArrayList<>(currentVelocity));
        newSpecimen.setCurrentPosition(new ArrayList<>(currentPosition));

        for (City city : citiesVisitedInOrder) {
            newSpecimen.getCitiesVisitedInOrder().add(city.deepClone());
        }
        return newSpecimen;
    }

    public void setNewVelocityAndPosition(double w, double c1, double r1, double c2, double r2, Specimen bestResult) {
        for (int i = 0; i < dataSet.getNumberOfCities(); i++) {
            double vel = currentVelocity.get(i);
            double pos = currentPosition.get(i);
            double populationBestLoc = bestPosition.get(i);
            double generallyBestLoc = bestResult.bestPosition.get(i);

            double newVel = (w * vel) + (r1 * c1) * (populationBestLoc - pos) + (r2 * c2) * (generallyBestLoc - pos);

            currentVelocity.set(i, newVel);
            
            double newPos = pos + newVel;
            currentPosition.set(i, newPos);

            swapWithLocation((int) Math.abs(pos - newPos));
        }
    }

    public void swapWithLocation(int diff) {
        if (diff > 10) {
            diff = 10;
        }
        for (int i = 0; i < diff; i++) {
            int random1 = 0;
            int random2 = 0;

            while (random1 == random2)
                random2 = random.nextInt(0, dataSet.getNumberOfCities());

            City city_1 = citiesVisitedInOrder.get(random1);
            City city_2 = citiesVisitedInOrder.get(random2);

            citiesVisitedInOrder.set(random2, city_1);
            citiesVisitedInOrder.set(random1, city_2);

            if (getTotalDistanceTraveled() > objectiveFunction) { //cofnij jeśli to pogarsza wynik
                citiesVisitedInOrder.set(random2, city_2);
                citiesVisitedInOrder.set(random1, city_1);
            }
        }
    }
}
