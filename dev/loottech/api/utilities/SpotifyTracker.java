package dev.loottech.api.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpotifyTracker {
    private static Process powerShellProcess;
    private static BufferedReader reader;
    private static BufferedWriter writer;
    private static final ScheduledExecutorService executor;
    private static String currentTrack;
    private static boolean processActive;

    private static void startPowerShellProcess() {
        try {
            ProcessBuilder pb = new ProcessBuilder(new String[]{"powershell.exe", "-NoProfile", "-Command", "while ($true) {   $title = (Get-Process spotify -ErrorAction SilentlyContinue |     Select-Object -ExpandProperty MainWindowTitle |     Where-Object { $_ -ne 'Spotify' -and $_ -ne 'Default IME' });   if ($title) { Write-Output $title }   Start-Sleep -Milliseconds 500 }"});
            pb.redirectErrorStream(true);
            powerShellProcess = pb.start();
            reader = new BufferedReader((Reader)new InputStreamReader(powerShellProcess.getInputStream()));
            writer = new BufferedWriter((Writer)new OutputStreamWriter(powerShellProcess.getOutputStream()));
            processActive = true;
            new Thread(() -> {
                block3: {
                    try {
                        String line;
                        while (processActive && (line = reader.readLine()) != null) {
                            if (line.trim().isEmpty()) continue;
                            currentTrack = line.trim();
                        }
                    }
                    catch (IOException e) {
                        if (!processActive) break block3;
                        System.err.println("SpotifyTracker read error: " + e.getMessage());
                        SpotifyTracker.restartProcess();
                    }
                }
            }, "SpotifyTracker-Reader").start();
        }
        catch (IOException e) {
            System.err.println("Failed to start PowerShell: " + e.getMessage());
        }
    }

    private static void restartProcess() {
        SpotifyTracker.cleanup();
        SpotifyTracker.startPowerShellProcess();
    }

    private static void checkProcess() {
        if (!processActive || !powerShellProcess.isAlive()) {
            System.err.println("PowerShell process died, restarting...");
            SpotifyTracker.restartProcess();
        }
    }

    public static String getCurrentTrack() {
        return currentTrack;
    }

    public static void cleanup() {
        processActive = false;
        try {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (powerShellProcess != null) {
                powerShellProcess.destroyForcibly();
            }
        }
        catch (IOException e) {
            System.err.println("Cleanup error: " + e.getMessage());
        }
    }

    static {
        executor = Executors.newSingleThreadScheduledExecutor();
        currentTrack = "";
        processActive = false;
        SpotifyTracker.startPowerShellProcess();
        executor.scheduleAtFixedRate(SpotifyTracker::checkProcess, 5L, 5L, TimeUnit.SECONDS);
    }
}
