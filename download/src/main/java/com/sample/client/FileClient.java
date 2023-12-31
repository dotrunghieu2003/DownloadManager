package com.sample.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sample.utils.ConstantManager.DOWNLOAD_DIRECTORY;

public class FileClient {


    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 8080);

             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));){

            Scanner scanner = new Scanner(System.in);

            String userInput = getAction(scanner);
            while ((userInput != null && !userInput.isEmpty())) {
                System.out.println("userInput:" + userInput);
                out.println(userInput);
                out.flush();

                String[] command = userInput.split(" ");
                switch (command[0]) {
                    case "index":
                        listFiles(in);
                        break;
                    case "get":
                        if (command.length > 1) {
                            downloadFiles(in, command[1]);
                        }
                        break;
                    case "quit":
                    case "q":
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println(in.readLine());
                        break;
                }
                userInput = getAction(scanner);
            }

        } catch (Exception ex) {
            ex.fillInStackTrace();
        }

    }

    private static String getAction(Scanner scanner){
        System.out.print("Action:");
        String input = scanner.nextLine();
        return input;
    }

    private static void listFiles(BufferedReader in) throws IOException {
        Stream<String> lines = in.lines();
        System.out.println(lines.collect(Collectors.toList()));

        System.out.println("Action completed.");
    }

    private static void downloadFiles(BufferedReader in, String filename) throws IOException {
        String response = in.readLine();
        if (response.equals("ok")) {
            File directory = new File(DOWNLOAD_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = DOWNLOAD_DIRECTORY + File.separator + filename;
            System.out.println("Handle downloadFiles: "+ fileName);
            try (FileWriter fileWriter = new FileWriter(fileName);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                String content;
                while ((content = in.readLine()) != null && !content.equals("error")) {
                    bufferedWriter.write(content);
                    bufferedWriter.newLine();
                }
                System.out.println("Downloaded: " + filename);
            }
        } else {
            System.out.println("File not found on server: " + filename);
        }
    }
}

