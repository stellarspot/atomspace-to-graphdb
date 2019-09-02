package sample.performance.neo4j;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


public class PerformanceChartCreateNeo4j extends Application {


    static final String TITLE = "Create requests";

    static final double[][] DATA_NATIVE = {
            {20, 69.25},
            {40, 106.00},
            {60, 216.25},
            {80, 399.75},
    };

    static final double[][] DATA_PREDICATE = {
            {20, 69.25},
            {40, 549.00},
            {60, 2291.25},
            {80, 6539.75},
    };

    static final double[][] DATA_EVALUATION = {
            {20, 159.75},
            {40, 1614.50},
            {60, 7217.75},
            {80, 22171.00},
    };

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle(TITLE);


        double xMin = 20;
        double xMax = 80;

        double yMin = 60;
        double yMax = 22200;


        final NumberAxis xAxis = new NumberAxis(xMin, xMax, 20);
        final NumberAxis yAxis = new NumberAxis(yMin, yMax, 1000);

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        yAxis.setLabel("Time(ms)");
        xAxis.setLabel("Number of subjects in triple graph");

        lineChart.setTitle("Chart");


        lineChart.getData().addAll(getSeries("Native", DATA_NATIVE));
        lineChart.getData().addAll(getSeries("Predicate", DATA_PREDICATE));
        lineChart.getData().addAll(getSeries("Evaluation", DATA_EVALUATION));

        Scene scene = new Scene(lineChart, 1000, 800);
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
