package org.pso.model;

import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

public final class DataSet {
    private static DataSet INSTANCE;
    @Getter
    private List<City> cities;
    @Getter
    private Map<City, Map<City, Double>> distanceMatrix;

    private DataSet() {
    }

    public static DataSet getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DataSet();
        }
        return INSTANCE;
    }

    public int getNumberOfCities() {
        return cities.size();
    }

    public void calculateDistanceMatrix() {
        distanceMatrix = cities.stream()
                .collect(Collectors.toMap(
                        (city) -> city,
                        (city) -> cities.stream()
                                .filter(element -> !Objects.equals(element.getIndex(), city.getIndex()))
                                .collect(Collectors.toMap(
                                        otherCity -> otherCity,
                                        city::calculateDistance
                                ))));
    }

    public void writeDistances() {
        distanceMatrix.forEach((city, distanceMatrix) -> distanceMatrix.forEach(
                (otherCity, distance) ->
                        System.out.printf(
                                "%s %s %.2f",
                                city.getIndex(),
                                otherCity.getIndex(),
                                distance)));
    }

    public void loadDataFromCSV(String fileName) throws IOException {
        Reader in = new FileReader(fileName);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .parse(in);
        cities = new ArrayList<>();
        for (CSVRecord record : records) {
            cities.add(City.builder()
                    .index(Integer.valueOf(record.get(0)))
                    .X(Double.valueOf(record.get(1)))
                    .Y(Double.valueOf(record.get(2)))
                    .build());
        }
    }
}
