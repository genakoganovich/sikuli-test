package genakoganovich.mybot;

import org.sikuli.script.App;
import org.sikuli.script.Screen;
import org.sikuli.basics.Settings; 

public class ImageRecognitionTest {

    public static void main(String[] args) {
        // Создаем экземпляр класса и запускаем логику
        new ImageRecognitionTest().run();
    }

    public void run() {
        try {
            initializeEnvironment();

            String appPath = findLatestAppPath();
            if (appPath == null) return;

            App myApp = launchApplication(appPath);
            if (myApp == null) return;

            Screen appScreen = findApplicationScreen();
            if (appScreen == null) return;

            // Здесь начинается основная работа с найденным экраном
            startTesting(appScreen);

        } catch (Exception e) {
            // Ошибки поглощаются или записываются в лог-файл (не в stdout)
        }
    }

    /**
     * Шаг 0: Настройка конфигурации и библиотек
     */
    private void initializeEnvironment() {
        Settings.ActionLogs = false;
        Settings.InfoLogs = false;
        Settings.DebugLogs = false;
        Config.initialize();
    }

    /**
     * Шаг 1: Получение пути к последнему билду
     */
    private String findLatestAppPath() {
        BuildService buildService = new BuildService();
        String baseDir = Config.get("gspace.base.dir", "c:\\Geomage\\gSpace");
        String exeName = Config.get("gspace.exe.name", "gSpace.exe");
        return buildService.findLatestBuildPath(baseDir, exeName);
    }

    /**
     * Шаг 2: Запуск приложения и ожидание окна
     */
    private App launchApplication(String appPath) {
        AppLauncher launcher = new AppLauncher();
        int totalWait = Config.getInt("gspace.splash.wait", 3000) + 
                        Config.getInt("gspace.main.window.wait", 5000);
        
        return launcher.launch(appPath, totalWait);
    }

    /**
     * Шаг 3: Определение монитора, на котором открылось приложение
     */
    private Screen findApplicationScreen() {
        AppLocationService locationService = new AppLocationService();
        return locationService.findApplicationScreen();
    }

    /**
     * Шаг 4: Точка входа для самих тестов
     */
    private void startTesting(Screen appScreen) {
        // Логика тестирования элементов GUI
    }
}