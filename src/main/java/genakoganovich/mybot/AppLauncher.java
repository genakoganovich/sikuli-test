package genakoganovich.mybot;

import org.sikuli.script.App;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

public class AppLauncher {
    public static void main(String[] args) {
        try {
            App myApp = new App("c:\\Geomage\\gSpace\\gSpace_30420\\gSpace.exe");
            
            System.out.println("Launching application...");
            myApp.open();
            
            Thread.sleep(3000);
            
            System.out.println("Waiting for main window...");
            Thread.sleep(5000); // Увеличьте время, если нужно
            
            myApp.focus(); // Активируем окно
            Region appWindow = myApp.window();
            
            if (appWindow != null) {
                Screen activeScreen = (Screen) appWindow.getScreen();
                System.out.println("Application opened on screen #" + activeScreen.getID());
                System.out.println("Window location: " + appWindow.x + "," + appWindow.y);
            } else {
                System.out.println("Failed to get application window.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}