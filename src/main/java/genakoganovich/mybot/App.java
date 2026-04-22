package genakoganovich.mybot;

import org.sikuli.script.Screen;
import org.sikuli.script.Key;
import org.sikuli.script.ImagePath; // <-- Не забудьте этот импорт!

public class App {

    public static void main(String[] args) {
        String imgPath = System.getProperty("image.dir", "images");
        ImagePath.setBundlePath(imgPath);
        
        String target = "open_project.png";
        
        // 1. Ищем экран
        Screen screen = findScreenWith(target);
        
        if (screen == null) {
            System.out.println("Screen with '" + target + "' not found on any monitor.");
            return;
        }

        // 2. Делаем работу
        try {
            screen.click(target);
            screen.wait(0.5);
            screen.type("demo_project.gsp" + Key.ENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===============================================
    // Вспомогательный метод (наш собственный инструмент)
    // ===============================================
    public static Screen findScreenWith(String imageName) {
        for (int i = 0; i < Screen.getNumberScreens(); i++) {
            Screen s = new Screen(i);
            if (s.exists(imageName, 1.0) != null) {
                return s; // Сразу возвращаем найденный экран
            }
        }
        return null; // Если цикл прошел и ничего не нашел
    }
}
    