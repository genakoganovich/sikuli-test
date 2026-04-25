package genakoganovich.mybot;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageLoader {
    public List<String> loadPngFromFolder(String folderName) {
        List<String> imageList = new ArrayList<>();
        String baseImagePath = Config.get("image.dir", "images");
        File folder = new File(baseImagePath, folderName);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
            if (files != null) {
                Arrays.sort(files);
                for (File file : files) {
                    imageList.add(folderName + "/" + file.getName());
                }
            }
        }
        return imageList;
    }
}