// path: src/main/java/infra/Logger.java
package infra;

public class Logger {
    public static void info(String msg) {
        System.out.println("INFO: " + msg);
    }

    public static void error(String msg, Throwable t) {
        System.err.println("ERROR: " + msg);
        if (t != null) {
            t.printStackTrace();
        }
    }
}
