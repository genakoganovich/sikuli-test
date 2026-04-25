package genakoganovich.mybot;

import java.io.File;
import java.util.Arrays;

public class BuildService {
    
    public String findLatestBuildPath(String baseDir, String exeName) {
        File dir = new File(baseDir);
        
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }
        
        File[] buildDirs = dir.listFiles((d, name) -> {
            File f = new File(d, name);
            return f.isDirectory() && name.startsWith("gSpace_");
        });
        
        if (buildDirs == null || buildDirs.length == 0) {
            return null;
        }
        
        // Сортировка по номеру билда (от большего к меньшему)
        Arrays.sort(buildDirs, (f1, f2) -> {
            int b1 = extractBuildNumber(f1.getName());
            int b2 = extractBuildNumber(f2.getName());
            return Integer.compare(b2, b1);
        });
        
        File latestDir = buildDirs[0];
        File exeFile = new File(latestDir, exeName);
        
        return exeFile.exists() ? exeFile.getAbsolutePath() : null;
    }
    
    private int extractBuildNumber(String folderName) {
        try {
            return Integer.parseInt(folderName.replace("gSpace_", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}