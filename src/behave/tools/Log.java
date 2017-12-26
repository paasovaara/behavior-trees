package behave.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//TODO use some proper logging framework
public class Log {
    private static boolean m_logDebug = false;
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss:SSS");

    public static void debug(String msg) {
        if (m_logDebug)
            print("debug", msg);
    }

    public static void info(String msg) {
        print("info", msg);
    }

    public static void warning(String msg) {
        print("warning", msg);
    }

    public static void error(String msg) {
        print("error", msg);
    }

    private static void print(String level, String msg) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " [" + level + "] : " + msg);
    }
}
