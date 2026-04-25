package genakoganovich.mybot;

import org.sikuli.script.Match;
import org.sikuli.script.Region;

public class ImageMatcher {
    public Match findElement(Region region, String imagePath, double timeout) {
        try {
            return region.exists(imagePath, timeout);
        } catch (Exception e) {
            return null;
        }
    }
}