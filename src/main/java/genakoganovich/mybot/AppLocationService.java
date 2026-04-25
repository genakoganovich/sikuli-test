package genakoganovich.mybot;

import org.sikuli.script.Match;
import org.sikuli.script.Screen;
import java.util.List;

public class AppLocationService {
    private final ImageLoader imageLoader;
    private final ImageMatcher imageMatcher;
    private final VisualService visualService;

    public AppLocationService() {
        this.imageLoader = new ImageLoader();
        this.imageMatcher = new ImageMatcher();
        this.visualService = new VisualService();
    }

    public Screen findApplicationScreen() {
        String logoFolder = Config.get("app.logo.folder", "001_logo");
        List<String> logoImages = imageLoader.loadPngFromFolder(logoFolder);

        if (logoImages.isEmpty()) return null;

        int screenCount = Screen.getNumberScreens();

        for (int i = 0; i < screenCount; i++) {
            Screen screen = new Screen(i);
            for (String logoPath : logoImages) {
                Match match = imageMatcher.findElement(screen, logoPath, 2.0);
                if (match != null) {
                    visualService.highlightMatch(match, 2.0);
                    return screen;
                }
            }
        }
        return null;
    }
}