package genakoganovich.mybot;

import org.sikuli.script.App;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import java.io.File;
import java.util.Arrays;

public class AppLauncher {
    public static void main(String[] args) {
        try {
            // ВАЖНО: Инициализируем конфигурацию в самом начале
            Config.initialize();
            
            // Теперь все классы проекта могут использовать ImagePath
            
            String baseDir = Config.get("gspace.base.dir", "c:\\Geomage\\gSpace");
            String exeName = Config.get("gspace.exe.name", "gSpace.exe");
            int splashWait = Config.getInt("gspace.splash.wait", 3000);
            int mainWait = Config.getInt("gspace.main.window.wait", 5000);
            
            System.out.println("Base directory: " + baseDir);
            
            String appPath = findLatestBuild(baseDir, exeName);
            
            if (appPath == null) {
                System.out.println("ERROR: No valid gSpace build found!");
                return;
            }
            
            System.out.println("Starting: " + appPath);
            
            App myApp = new App(appPath);
            myApp.open();
            
            Thread.sleep(splashWait);
            System.out.println("Waiting for main window...");
            Thread.sleep(mainWait);
            
            myApp.focus();
            Region appWindow = myApp.window();
            
            if (appWindow != null) {
                Screen activeScreen = (Screen) appWindow.getScreen();
                System.out.println("SUCCESS: Application on screen #" + activeScreen.getID());
                System.out.println("Window location: " + appWindow.x + "," + appWindow.y);
            } else {
                System.out.println("ERROR: Failed to get application window.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String findLatestBuild(String baseDir, String exeName) {
        File dir = new File(baseDir);
        
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Directory does not exist: " + baseDir);
            return null;
        }
        
        File[] buildDirs = dir.listFiles((d, name) -> {
            File f = new File(d, name);
            return f.isDirectory() && name.startsWith("gSpace_");
        });
        
        if (buildDirs == null || buildDirs.length == 0) {
            System.err.println("No gSpace_* directories found");
            return null;
        }
        
        System.out.println("Found " + buildDirs.length + " build(s):");
        for (File f : buildDirs) {
            System.out.println("  - " + f.getName());
        }
        
        Arrays.sort(buildDirs, (f1, f2) -> {
            int b1 = extractBuildNumber(f1.getName());
            int b2 = extractBuildNumber(f2.getName());
            return Integer.compare(b2, b1);
        });
        
        File latestDir = buildDirs[0];
        System.out.println("Latest build: " + latestDir.getName());
        
        File exeFile = new File(latestDir, exeName);
        
        if (!exeFile.exists()) {
            System.err.println(exeName + " not found in " + latestDir.getAbsolutePath());
            return null;
        }
        
        return exeFile.getAbsolutePath();
    }
    
    private static int extractBuildNumber(String folderName) {
        try {
            return Integer.parseInt(folderName.replace("gSpace_", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}