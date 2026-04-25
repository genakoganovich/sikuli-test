package genakoganovich.mybot;

import org.sikuli.script.Match;

public class VisualService {
    public void highlightMatch(Match match, double seconds) {
        if (match != null) {
            match.highlight(seconds);
        }
    }
}