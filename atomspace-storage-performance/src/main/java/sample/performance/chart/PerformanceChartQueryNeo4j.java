package sample.performance.chart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


public class PerformanceChartQueryNeo4j extends Application {


    static final double xMin = 20;
    static final double xMax = 80;

    // ============================
    // Atomspace
    // create

//    // create
//    static final double[][] DATA_PREDICATE = {
//            {20, 3.43},
//            {40, 12.46},
//            {60, 29.70},
//            {80, 49.72},
//    };
//
//    static final double[][] DATA_EVALUATION = {
//            {20, 4.73},
//            {40, 18.65},
//            {60, 41.79},
//            {80, 82.18},
//    };
//
//    static final double yMin = 3;
//    static final double yMax = 85;
//
//    // query
//    static final double[][] DATA_PREDICATE = {
//            {20, 3.58},
//            {40, 3.72},
//            {60, 4.21},
//            {80, 4.42},
//    };
//
//    static final double[][] DATA_EVALUATION = {
//            {20, 4.62},
//            {40, 5.58},
//            {60, 5.93},
//            {80, 7.97},
//    };
//
//    static final double yMin = 3;
//    static final double yMax = 8;

//    // Neo4j
//    static final double[][] DATA_NATIVE = {
//            {20, 20.25},
//            {40, 18.25},
//            {60, 17.25},
//            {80, 21.50},
//    };
//
//    static final double[][] DATA_PREDICATE = {
//            {20, 37.25},
//            {40, 62.75},
//            {60, 91.25},
//            {80, 154.00},
//    };
//
//    static final double[][] DATA_EVALUATION = {
//            {20, 69.75},
//            {40, 239.50},
//            {60, 462.75},
//            {80, 808.25},
//    };
//
//    static final double xMin = 20;
//    static final double xMax = 80;
//
//    static final double yMin = 10;
//    static final double yMax = 810;

    // ============================
    // Neo4j Java API
//    // create
//    static final String TITLE = "Create requests";
//    static final double[][] DATA_NATIVE = {
//            {20, 31.00},
//            {40, 46.50},
//            {60, 81.25},
//            {80, 162.75},
//    };
//
//    static final double[][] DATA_PREDICATE = {
//            {20, 40.00},
//            {40, 73.00},
//            {60, 122.25},
//            {80, 233.25},
//    };
//
//    static final double[][] DATA_EVALUATION = {
//            {20, 62.75},
//            {40, 128.75},
//            {60, 330.25},
//            {80, 856.75},
//    };
//
//    static final double yMin = 30;
//    static final double yMax = 860;

    // query
    static final String TITLE = "Query requests";
    static final double[][] DATA_NATIVE = {
            {20, 5.00},
            {40, 4.50},
            {60, 5.00},
            {80, 6.75},
    };

    static final double[][] DATA_PREDICATE = {
            {20, 6.75},
            {40, 6.75},
            {60, 8.00},
            {80, 8.00},
    };

    static final double[][] DATA_EVALUATION = {
            {20, 11.25},
            {40, 13.00},
            {60, 11.75},
            {80, 11.75},
    };

    static final double yMin = 4;
    static final double yMax = 14;

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle(TITLE);


        final NumberAxis xAxis = new NumberAxis(xMin, xMax, 20);
        final NumberAxis yAxis = new NumberAxis(yMin, yMax, 100);

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        yAxis.setLabel("Time(ms)");
        xAxis.setLabel("Number of subjects in triple graph");

        lineChart.setTitle("Chart");


        lineChart.getData().addAll(getSeries("Native", DATA_NATIVE));
        lineChart.getData().addAll(getSeries("Predicate", DATA_PREDICATE));
        lineChart.getData().addAll(getSeries("Evaluation", DATA_EVALUATION));

        Scene scene = new Scene(lineChart, 500, 300);
        stage.setScene(scene);
        stage.show();
    }


    static XYChart.Series getSeries(String name, double[][] values) {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);

        for (int i = 0; i < values.length; i++) {
            series.getData().add(new XYChart.Data(values[i][0], values[i][1]));
        }
        return series;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
