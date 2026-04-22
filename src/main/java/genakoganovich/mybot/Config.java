package genakoganovich.mybot;

import org.sikuli.script.ImagePath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Properties properties;
    private static boolean initialized = false;
    
    // Приватный конструктор - синглтон
    private Config() {}
    
    /**
     * Инициализация конфигурации проекта.
     * Вызывается один раз при старте приложения.
     */
    public static void initialize() {
        if (initialized) {
            return;
        }
        
        properties = new Properties();
        
        // Загружаем config.properties
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            System.out.println("✓ Configuration loaded from config.properties");
        } catch (IOException e) {
            System.out.println("⚠ Config file not found, using defaults");
        }
        
        // Настраиваем глобальный путь к изображениям для SikuliX
        setupImagePath();
        
        initialized = true;
        System.out.println("✓ Project configuration initialized");
    }
    
    /**
     * Настройка глобального пути к изображениям для SikuliX
     */
    private static void setupImagePath() {
        // Приоритет: VM arg > config.properties > default
        String imagePath = System.getProperty("image.dir");
        
        if (imagePath == null || imagePath.isEmpty()) {
            imagePath = properties.getProperty("image.dir", "images");
        }
        
        File imageDir = new File(imagePath);
        
        // Создаем папку, если не существует
        if (!imageDir.exists()) {
            System.out.println("Creating image directory: " + imagePath);
            imageDir.mkdirs();
        }
        
        // Устанавливаем глобальный путь для ВСЕГО проекта
        ImagePath.setBundlePath(imagePath);
        
        System.out.println("✓ SikuliX image path set to: " + imageDir.getAbsolutePath());
        System.out.println("  Current bundle path: " + ImagePath.getBundlePath());
    }
    
    /**
     * Получить значение из конфигурации
     */
    public static String get(String key) {
        return get(key, null);
    }
    
    /**
     * Получить значение из конфигурации с дефолтным значением
     */
    public static String get(String key, String defaultValue) {
        if (properties == null) {
            initialize();
        }
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Получить числовое значение из конфигурации
     */
    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}