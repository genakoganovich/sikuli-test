package genakoganovich.mybot;

import org.sikuli.script.Screen;
import org.sikuli.script.ImagePath; // Добавили импорт ImagePath

public class App {
    public static void main(String[] args) {
        
        // 1. НАСТРОЙКА: Говорим SikuliX, где лежат наши картинки
        // Слово "images" означает, что папка лежит рядом с корнем проекта
        ImagePath.setBundlePath("images"); 
        
        System.out.println("Программа запущена! Ищу в папке: " + ImagePath.getBundlePath());

        Screen screen = new Screen();

        try {
            // 2. Теперь мы можем писать просто имя файла!
            String imageName = "test.png";
            
            System.out.println("Жду картинку...");
            screen.wait(imageName, 5.0);
            
            screen.click(imageName);
            System.out.println("Успешно кликнул!");
            
        } catch (Exception e) {
            System.out.println("\n--- ОШИБКА ---");
            e.printStackTrace(); 
        }
    }
}