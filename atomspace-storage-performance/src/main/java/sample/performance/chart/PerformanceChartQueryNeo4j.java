package sample.performance.chart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


public class PerformanceChartQueryNeo4j extends Application {


    // ============================
    // Atomspace
    // create
//    static final String TITLE = "Create requests";
//    static final String LABEL = "Number of triples";
//
//    static final double[][] DATA_PREDICATE = {
//            {100, 3.34},
//            {200, 7.87},
//            {300, 10.57},
//            {400, 14.36},
//    };
//
//    static final double[][] DATA_EVALUATION = {
//            {100, 5.61},
//            {200, 10.19},
//            {300, 15.44},
//            {400, 18.73},
//    };
//
//    static final double xMin = 100;
//    static final double xMax = 400;
//    static final double xTickUnit = 50;
//
//    static final double yMin = 3;
//    static final double yMax = 20;
//    static final double yTickUnit = 5;

//    // query
//    static final String TITLE = "Query requests";
//    static final String LABEL = "Number of queries";
//
//    static final double[][] DATA_PREDICATE = {
//            {1000, 74},
//            {2000, 162},
//            {3000, 240},
//            {4000, 325},
//    };
//
//    static final double[][] DATA_EVALUATION = {
//            {1000, 105},
//            {2000, 200},
//            {3000, 331},
//            {4000, 399},
//    };
//
//    static final double xMin = 1000;
//    static final double xMax = 4000;
//    static final double xTickUnit = 500;
//
//    static final double yMin = 70;
//    static final double yMax = 400;
//    static final double yTickUnit = 50;

    // ============================
    // Neo4j Cypher
//    // create
//    static final String TITLE = "Create requests";
//    static final String LABEL = "Number of triples";
//    static final double[][] DATA_NATIVE = {
//            {100, 18},
//            {200, 31},
//            {300, 37},
//            {400, 42},
//    };
//
//    static final double[][] DATA_PREDICATE = {
//            {100, 24},
//            {200, 48},
//            {300, 66},
//            {400, 84},
//    };
//
//    static final double[][] DATA_EVALUATION = {
//            {100, 42},
//            {200, 76},
//            {300, 112},
//            {400, 138},
//    };
//
//    static final double xMin = 100;
//    static final double xMax = 400;
//    static final double xTickUnit = 50;
//
//    static final double yMin = 15;
//    static final double yMax = 140;
//    static final double yTickUnit = 20;


//    // query
//    static final String TITLE = "Query requests";
//    static final String LABEL = "Number of queries";
//    static final double[][] DATA_NATIVE = {
//            {100, 19},
//            {200, 35},
//            {300, 52},
//            {400, 73},
//    };
//
//    static final double[][] DATA_PREDICATE = {
//            {100, 25},
//            {200, 46},
//            {300, 65},
//            {400, 95},
//    };
//
//    static final double[][] DATA_EVALUATION = {
//            {100, 645},
//            {200, 1286},
//            {300, 1937},
//            {400, 2543},
//    };
//
//    static final double xMin = 100;
//    static final double xMax = 400;
//    static final double xTickUnit = 50;
//
//    static final double yMin = 19;
//    static final double yMax = 2543;
//    static final double yTickUnit = 300;

    // ============================
    // Neo4j Java API
    // create
//    static final String TITLE = "Create requests";
//    static final String LABEL = "Number of triples";
//    static final double[][] DATA_NATIVE = {
//            {100, 12},
//            {200, 20},
//            {300, 37},
//            {400, 49},
//    };
//
//    static final double[][] DATA_PREDICATE = {
//            {100, 15},
//            {200, 32},
//            {300, 50},
//            {400, 60},
//    };
//
//    static final double[][] DATA_EVALUATION = {
//            {100, 25},
//            {200, 53},
//            {300, 93},
//            {400, 128},
//    };
//
//    static final double xMin = 100;
//    static final double xMax = 400;
//    static final double xTickUnit = 50;
//
//    static final double yMin = 10;
//    static final double yMax = 130;
//    static final double yTickUnit = 20;


//    // query
//    static final String TITLE = "Query requests";
//    static final String LABEL = "Number of queries";
//    static final double[][] DATA_NATIVE = {
//            {1000, 57},
//            {2000, 130},
//            {3000, 187},
//            {4000, 256},
//    };
//
//    static final double[][] DATA_PREDICATE = {
//            {1000, 64},
//            {2000, 142},
//            {3000, 213},
//            {4000, 269},
//    };
//
//    static final double[][] DATA_EVALUATION = {
//            {1000, 69},
//            {2000, 151},
//            {3000, 219},
//            {4000, 287},
//    };
//
//    static final double xMin = 1000;
//    static final double xMax = 4000;
//    static final double xTickUnit = 500;
//
//    static final double yMin = 55;
//    static final double yMax = 300;
//    static final double yTickUnit = 50;

    // ============================
    // Janus Graph

//    // create
//    static final String TITLE = "Create requests";
//    static final String LABEL = "Number of triples";
//    static final double[][] DATA_NATIVE = {
//            {100, 139},
//            {200, 392},
//            {300, 715},
//            {400, 1076},
//    };
//
//    static final double[][] DATA_PREDICATE = {
//            {100, 160},
//            {200, 467},
//            {300, 824},
//            {400, 1242},
//    };
//
//    static final double[][] DATA_EVALUATION = {
//            {100, 235},
//            {200, 665},
//            {300, 1187},
//            {400, 1782},
//    };
//
//    static final double xMin = 100;
//    static final double xMax = 400;
//    static final double xTickUnit = 50;
//
//    static final double yMin = 140;
//    static final double yMax = 1780;
//    static final double yTickUnit = 500;


    // query
    static final String TITLE = "Query requests";
    static final String LABEL = "Number of queries";
    static final double[][] DATA_NATIVE = {
            {100, 45},
            {200, 73},
            {300, 105},
            {400, 133},
    };

    static final double[][] DATA_PREDICATE = {
            {100, 69},
            {200, 111},
            {300, 156},
            {400, 195},
    };

    static final double[][] DATA_EVALUATION = {
            {100, 199},
            {200, 365},
            {300, 513},
            {400, 635},
    };

    static final double xMin = 100;
    static final double xMax = 400;
    static final double xTickUnit = 50;

    static final double yMin = 45;
    static final double yMax = 640;
    static final double yTickUnit = 50;

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle(TITLE);

        final NumberAxis xAxis = new NumberAxis(xMin, xMax, xTickUnit);
        final NumberAxis yAxis = new NumberAxis(yMin, yMax, yTickUnit);

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        yAxis.setLabel("Time(ms)");
        xAxis.setLabel(LABEL);

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
