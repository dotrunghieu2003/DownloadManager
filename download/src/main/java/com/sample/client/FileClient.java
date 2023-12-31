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
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);) {
            socket.setSoTimeout(3000);
            Scanner scanner = new Scanner(System.in);

            String userInput = getAction(scanner);
            while ((userInput != null && !userInput.isEmpty())) {
                System.out.println("userInput:" + userInput);
                out.println(userInput);

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input), 10000);

                String[] command = userInput.split(" ");
                switch (command[0]) {
                    case "index":
                        listFiles(reader);
                        break;
                    case "get":
                        if (command.length > 1) {
                            downloadFiles(reader, command[1]);
                        }
                        break;
                    case "quit":
                    case "q":
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println(reader.readLine());
                        break;
                }
                userInput = getAction(scanner);
            }

        } catch (Exception ex) {
            ex.fillInStackTrace();
        }

    }

    private static String getAction(Scanner scanner) {
        System.out.print("Action:");
        String input = scanner.nextLine();
        return input;
    }

    private static void listFiles(BufferedReader in) throws IOException {
        String data = null;
        try {
            do {
                data = in.readLine();
                System.out.println(data);
            } while (data != null);
        } catch (Exception ex) {
            System.out.println("Action completed.");
        }

    }

    private static void downloadFiles(BufferedReader in, String filename) throws IOException {
        String response = in.readLine();

        if (response.equals("ok")) {
            File directory = new File(DOWNLOAD_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = DOWNLOAD_DIRECTORY + File.separator + filename;
            System.out.println("Handle downloadFiles: " + fileName);
            try (FileWriter fileWriter = new FileWriter(fileName);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                String content;
                try {
                    while ((content = in.readLine()) != null && !content.equals("error")) {
                        bufferedWriter.write(content);
                        bufferedWriter.newLine();
                    }
                } catch (Exception ex) {
                    System.out.println("Completed Downloaded: " + filename);
                }
            }
        } else {
            System.out.println("File not found on server: " + filename);
        }

    }
}

