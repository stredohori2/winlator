package com.pubg.imobile.xconnector;

import java.io.IOException;

public interface XStreamLock extends AutoCloseable {
    void close() throws IOException;
}
