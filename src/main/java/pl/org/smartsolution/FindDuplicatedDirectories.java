package pl.org.smartsolution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FindDuplicatedDirectories {

    private static final String[] PUNCTUATION_MARKS = { "_", "'", ",", "/", "\"", "\\." };

    public static void main(String[] args) {
        String rootDirectoryPath = "C:\\test";
        File rootDirectory = new File(rootDirectoryPath);
        Map<String, List<String>> directoriesByName = new HashMap<>();
        collectDirectories(rootDirectory, directoriesByName);
        saveDuplicatesToFile(directoriesByName, rootDirectoryPath);
    }

    private static void collectDirectories(File root, Map<String, List<String>> directoriesByName) {
        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String name = normalizeDirectoryName(file.getName());
                    if (directoriesByName.containsKey(name)) {
                        directoriesByName.get(name).add(file.getAbsolutePath());
                    } else {
                        List<String> paths = new ArrayList<>();
                        paths.add(file.getAbsolutePath());
                        directoriesByName.put(name, paths);
                    }
                    collectDirectories(file, directoriesByName);
                }
            }
        }
    }


    private static String normalizeDirectoryName(String directoryName) {
        for (String mark : PUNCTUATION_MARKS) {
            directoryName = directoryName.replaceAll(Pattern.quote(mark), "");
        }
        return directoryName;
    }

    private static void saveDuplicatesToFile(Map<String, List<String>> directoriesByName, String rootDirectoryPath) {
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(rootDirectoryPath + File.separator + "DuplicatedDirectories.txt"));
            for (String name : directoriesByName.keySet()) {
                List<String> paths = directoriesByName.get(name);
                if (paths.size() > 1) {
                    for (String path : paths) {
                        writer.write(path);
                        writer.newLine();
                    }
                    writer.newLine();
                }
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file");
            e.printStackTrace();
        }
    }

}
