package sample.performance;

import atomspace.performance.triple.RandomTripleGraph;
import atomspace.performance.triple.TripleGraph;

public class PerformanceTripleGraph {

    public static TripleGraph getGraph(int N) {
        int subjectsNumber = N;
        int predicatesNumber = N / 2;
        int objectsNumber = N;
        int predicatesPerSubjectNumber = N / 4;

        return new RandomTripleGraph(
                subjectsNumber,
                predicatesNumber,
                objectsNumber,
                predicatesPerSubjectNumber);

    }
}
