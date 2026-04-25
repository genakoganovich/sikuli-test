package genakoganovich.mybot;

import org.sikuli.script.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
    
    // Описание одного шага теста
    private static class Step {
        Function<Context, Region> regionBuilder; // Функция создания региона
        boolean click;                            // Нужно ли кликать
        int sleep;                                // Ожидание после (мс)
        double timeout;                           // Таймаут поиска (сек)

        Step(Function<Context, Region> reg, boolean click, int sleep, double timeout) {
            this.regionBuilder = reg;
            this.click = click;
            this.sleep = sleep;
            this.timeout = timeout;
        }
    }

    // Контекст, который передается от шага к шагу
    private static class Context {
        Screen screen;
        Match lastMatch;
        Region lastWindow; // Сохраняем "окно", чтобы под-элементы искались в нем
    }

    public boolean execute(Screen appScreen) {
        List<String> images = loader.loadPngFromFolder(testFolderName);
        List<Step> steps = prepareSteps();

        if (images.size() < steps.size()) return false;

        Context ctx = new Context();
        ctx.screen = appScreen;

        try {
            for (int i = 0; i < steps.size(); i++) {
                Step step = steps.get(i);
                String imagePath = images.get(i);

                // 1. Создаем регион для этого шага
                Region searchRegion = step.regionBuilder.apply(ctx);
                visual.highlightMatch(searchRegion, 0.5); // Показываем регион поиска

                // 2. Ищем элемент
                Match found = matcher.findElement(searchRegion, imagePath, step.timeout);
                if (found == null) return false;

                visual.highlightMatch(found, 0.5); // Показываем найденный элемент
                ctx.lastMatch = found;

                // Специальная логика: если это был заголовок (шаг 0, 5, 7), запоминаем его как "окно"
                if (i == 0 || i == 5 || i == 7) ctx.lastWindow = found;

                // 3. Кликаем, если нужно
                if (step.click) {
                    found.click();
                    if (step.sleep > 0) Thread.sleep(step.sleep);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private List<Step> prepareSteps() {
        List<Step> s = new ArrayList<>();
        
        // Шаг 1: Заголовок View Manager (ищем на всем экране)
        s.add(new Step(ctx -> ctx.screen, false, 0, 5.0));
        
        // Шаг 2: Seismic Data (ищем под заголовком)
        s.add(new Step(ctx -> ctx.lastMatch.below(600), false, 0, 3.0));
        
        // Шаг 3: Стрелка (расширяем регион Seismic Data и кликаем)
        s.add(new Step(ctx -> createExpandedRegion(ctx.lastMatch, 30, 10, 5, 5), true, 1000, 2.0));
        
        // Шаг 4: Проверка раскрытия (ищем в окне View Manager)
        s.add(new Step(ctx -> ctx.lastWindow.below(600), false, 0, 3.0));
        
        // Шаг 5: Под-элемент (клик)
        s.add(new Step(ctx -> ctx.lastWindow.below(600), true, 2000, 3.0));
        
        // Шаг 6: Заголовок Seismic Files (весь экран)
        s.add(new Step(ctx -> ctx.screen, false, 0, 5.0));
        
        // Шаг 7: Кнопка в заголовке (клик)
        s.add(new Step(ctx -> ctx.lastMatch, true, 1500, 3.0));
        
        // Шаг 8: Заголовок Диалога (весь экран)
        s.add(new Step(ctx -> ctx.screen, false, 0, 5.0));
        
        // Шаг 9: Кнопка в диалоге (ищем в регионе "вправо-вниз до конца")
        s.add(new Step(ctx -> {
            int w = ctx.screen.w - (ctx.lastMatch.x - ctx.screen.x);
            int h = ctx.screen.h - (ctx.lastMatch.y - ctx.screen.y);
            return new Region(ctx.lastMatch.x, ctx.lastMatch.y, w, h);
        }, true, 0, 3.0));

        return s;
    }

    private Region createExpandedRegion(Region base, int l, int r, int u, int d) {
        return new Region(base.x - l, base.y - u, base.w + l + r, base.h + u + d);
    }
}