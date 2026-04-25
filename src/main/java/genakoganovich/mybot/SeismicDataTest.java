package genakoganovich.mybot;

import org.sikuli.script.*;
import java.util.List;

public class SeismicDataTest {
    private final ImageMatcher matcher;
    private final VisualService visual;
    private final ImageLoader loader;
    private final String testFolderName = "005_test_001";

    public SeismicDataTest(ImageMatcher matcher, VisualService visual, ImageLoader loader) {
        this.matcher = matcher;
        this.visual = visual;
        this.loader = loader;
    }

    public boolean execute(Screen appScreen) {
        try {
            List<String> images = loader.loadPngFromFolder(testFolderName);
            if (images.size() < 9) return false;

            // 1. Окно View Manager и список
            Match title = waitAndHighlight(appScreen, images.get(0), 5.0);
            if (title == null) return false;

            Region win = title.below(600);
            Match item = waitAndHighlight(win, images.get(1), 3.0);
            if (item == null) return false;

            // 2. Раскрытие списка (Стрелка)
            if (findAndClick(createExpandedRegion(item, 30, 10, 5, 5), images.get(2), 2.0, 1000) == null) return false;

            // 3. Выбор под-элемента
            if (waitAndHighlight(win, images.get(3), 3.0) == null) return false; // Проверка раскрытия
            if (findAndClick(win, images.get(4), 3.0, 2000) == null) return false;

            // 4. Окно Seismic Files и его кнопка
            Match header = waitAndHighlight(appScreen, images.get(5), 5.0);
            if (header == null) return false;

            if (findAndClick(header, images.get(6), 3.0, 1500) == null) {
                // Запасной поиск кнопки чуть ниже заголовка
                if (findAndClick(header.below(50), images.get(6), 2.0, 1500) == null) return false;
            }

            // 5. Диалог и финальная кнопка
            Match diagHeader = waitAndHighlight(appScreen, images.get(7), 5.0);
            if (diagHeader == null) return false;

            Region diagArea = new Region(diagHeader.x, diagHeader.y, 
                                        appScreen.w - (diagHeader.x - appScreen.x), 
                                        appScreen.h - (diagHeader.y - appScreen.y));
            
            return findAndClick(diagArea, images.get(8), 3.0, 0) != null;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Вспомогательный метод: Найти, подсветить и вернуть Match.
     * Убирает дублирование проверки на null и визуализации.
     */
    private Match waitAndHighlight(Region reg, String img, double timeout) {
        Match m = matcher.findElement(reg, img, timeout);
        if (m != null) {
            visual.highlightMatch(m, 0.5);
        }
        return m;
    }

    /**
     * Вспомогательный метод: Найти, подсветить, кликнуть и подождать.
     */
    private Match findAndClick(Region reg, String img, double timeout, int sleepMs) throws InterruptedException {
        Match m = waitAndHighlight(reg, img, timeout);
        if (m != null) {
            m.click();
            if (sleepMs > 0) Thread.sleep(sleepMs);
        }
        return m;
    }

    private Region createExpandedRegion(Region base, int l, int r, int u, int d) {
        return new Region(base.x - l, base.y - u, base.w + l + r, base.h + u + d);
    }
}