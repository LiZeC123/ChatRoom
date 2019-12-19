package top.lizec.core.proxy;

import java.io.IOException;

public interface ObjectSocket {

    void writeUTF(String string) throws IOException;

    String readUTF() throws IOException;
}
