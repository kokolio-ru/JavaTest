package sample.controllers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileList {

    public List<String> files = new ArrayList<>();

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    public List<String> getFileList (File dirr, String ext, String inf) throws IOException {
        if (dirr == null)
            return null;
        File[] arr = dirr.listFiles();
        assert arr != null;
        for(File file : arr){
            if(file.isDirectory()){
                getFileList(file, ext, inf);
            } else if (getFileExtension(file).equals(ext)) {
                BufferedInputStream bs = new BufferedInputStream(new FileInputStream(file), 8192 * 4);
                byte[] content = new byte[bs.available()];
                bs.read(content);
                bs.close();
                String[] lines = new String(content, StandardCharsets.UTF_8).split("\n");
                int i = 1;
                loop:
                for (String line : lines) {
                    String[] words = line.split(" ");
                    int j = 1;
                    for (String word : words) {
                        if (word.equalsIgnoreCase(inf)) {
                            files.add(String.valueOf(file));
                            break loop;
                        }
                        j++;
                    }
                    i++;
                }
                }
            }

        if (files != null)
            return files;
        else
            return null;
    }
}
