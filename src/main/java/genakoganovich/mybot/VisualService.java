package genakoganovich.mybot;

import org.sikuli.script.Region; // Изменили импорт с Match на Region

public class VisualService {
    // Теперь метод принимает Region, что позволяет подсвечивать и Match, и обычные области
    public void highlightMatch(Region region, double seconds) {
        if (region != null) {
            region.highlight(seconds);
        }
    }
}