package genakoganovich.mybot;

import org.sikuli.script.App;
import org.sikuli.script.Region;

public class AppLauncher {
    
    public App launch(String appPath, int waitTimeMs) {
        try {
            App myApp = new App(appPath);
            myApp.open();
            
            // Ожидание загрузки приложения
            Thread.sleep(waitTimeMs);
            
            myApp.focus();
            return myApp;
        } catch (Exception e) {
            return null;
        }
    }

    public Region getAppWindow(App app) {
        if (app == null) return null;
        return app.window();
    }
}