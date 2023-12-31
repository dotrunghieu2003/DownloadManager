package com.sample.server;

import com.sample.utils.ConstantManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DownloadHandler {

    public static void listFiles(PrintWriter out) {
        System.out.println("Handle list Files...");
        try {
            Path paths =  Paths.get(ConstantManager.ROOT_DIRECTORY);
            Files.walk(paths)
                    .filter(Files::isRegularFile)
                    .forEach(path -> out.println(path.getFileName()));
            out.flush();
        } catch (IOException e) {
            out.println("Error listing files");
            e.printStackTrace();
        }
    }

    public static void getFile(PrintWriter out, String filename) {
        System.out.println("Handle get Files...");
        try {
            Path filePath = Paths.get(ConstantManager.ROOT_DIRECTORY, filename);
            if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                out.println("ok");
                Files.lines(filePath).forEach(out::println);
            } else {
                out.println("error");
            }
            out.flush();
        } catch (IOException e) {
            out.println("Error getting file");
        }
    }
}
