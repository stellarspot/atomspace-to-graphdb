package atomspace.performance.storage.scheme;

import atomspace.performance.storage.TripleModel;
import atomspace.performance.triple.TripleGraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class TripleSchemeModel implements TripleModel {

    protected final String filePrefix;
    protected final TripleGraph tripleGraph;

    public TripleSchemeModel(String filePrefix, TripleGraph tripleGraph) {
        this.filePrefix = filePrefix;
        this.tripleGraph = tripleGraph;
    }

    protected void saveToFile(String fileSuffix, StringBuilder builder) {
        String fileName = String.format("%s-%s.scm", filePrefix, fileSuffix);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(builder.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
