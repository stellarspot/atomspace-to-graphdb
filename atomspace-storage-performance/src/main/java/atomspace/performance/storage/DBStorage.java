package atomspace.performance.storage;

import java.io.Closeable;

public interface DBStorage extends Closeable {

    void clearDB();
}
