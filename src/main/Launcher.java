package main;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Cross-platform launcher for LWJGL/GLFW games.
 *
 * On macOS, GLFW requires -XstartOnFirstThread, which must be present from
 * the very first JVM thread. This launcher detects macOS and relaunches the
 * JVM with that flag when it is missing.
 *
 * On Windows and Linux, Game.main() is called directly.
 */
public class Launcher {

    public static void main(String[] args) throws Exception {
        if (isMacOS() && !isFirstThreadFlagSet()) {
            relaunchWithFirstThread(args);
        } else {
            Game.main(args);
        }
    }

    // -------------------------------------------------------------------------

    private static boolean isMacOS() {
        String os = System.getProperty("os.name", "").toLowerCase();
        return os.contains("mac") || os.contains("darwin");
    }

    private static boolean isFirstThreadFlagSet() {
        return ManagementFactory.getRuntimeMXBean()
                .getInputArguments()
                .contains("-XstartOnFirstThread");
    }

    private static void relaunchWithFirstThread(String[] gameArgs) throws Exception {
        // Resolve java executable from java.home — more reliable than ProcessHandle
        String javaHome = System.getProperty("java.home");
        String javaExe = javaHome + File.separator + "bin" + File.separator + "java";

        List<String> cmd = new ArrayList<>();
        cmd.add(javaExe);
        cmd.add("-XstartOnFirstThread");

        // Forward all original JVM flags except -XstartOnFirstThread (avoid duplicate)
        for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            if (!arg.equals("-XstartOnFirstThread")) {
                cmd.add(arg);
            }
        }

        // Re-add classpath
        String cp = System.getProperty("java.class.path");
        if (cp != null && !cp.isEmpty()) {
            cmd.add("-cp");
            cmd.add(cp);
        }

        cmd.add(Launcher.class.getName());

        for (String arg : gameArgs) {
            cmd.add(arg);
        }

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.inheritIO();
        // Explicitly set working directory so the child JVM can resolve it
        pb.directory(new File(System.getProperty("user.dir")));

        Process process = pb.start();
        int exitCode = process.waitFor();
        System.exit(exitCode);
    }
}
