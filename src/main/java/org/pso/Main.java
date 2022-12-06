package org.pso;

import org.pso.model.DataSet;
import org.pso.model.Specimen;
import org.pso.services.CsvStatisticsHolder;
import org.pso.services.PSO;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final String TRIVIAL_0 = "src/main/resources/trivial_0.ttp";
    private static final String TRIVIAL_1 = "src/main/resources/trivial_1.ttp";
    private static final String LONG = "src/main/resources/1.ttp";

    //private static final List<CsvTotalResult> results = new List<CsvTotalResult>();
    public static void main(String[] args) throws IOException {
        DataSet dataSet = DataSet.getInstance();
        dataSet.loadDataFromCSV(TRIVIAL_0);
        dataSet.calculateDistanceMatrix();
        //dataSet.writeDistances();
        runPSO();

        System.out.println("==========KONIEC==========");
        new Scanner(System.in).nextLine();
    }

    private static void runPSO() {
        CsvStatisticsHolder.getInstance().reset();
        PSO pso = new PSO();
        Specimen best = pso.psoCycle();
        System.out.printf("FINISH: %s%n", best.getObjectiveFunction());
        //Utils.SavePathSolutionToFile(best);

        double b = best.getObjectiveFunction();
        //double avg = CsvStatisticsHolder.getInstance().getResults().//Average(p => p.averageOfResults);
        //double minavg = CsvStatisticsHolder.getInstance().getResults().//Results.Average(p => p.worstResult));
        //double maxavg = CsvStatisticsHolder.getInstance().getResults().//Average(p => p.bestResult));

        //results.add(new CsvTotalResult(b, maxavg, minavg, avg));
    }
}
