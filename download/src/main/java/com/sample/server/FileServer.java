package com.sample.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {


    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080); // Change the port number if needed

            System.out.println("Server Listen on Port:8080 ...");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        String[] tokens = inputLine.split(" ");
                        String command = tokens[0];

                        System.out.println("command:" + command);

                        switch (command) {
                            case "index":
                                DownloadHandler.listFiles(out);
                                break;
                            case "get":
                                System.out.println("file to get:" + tokens[1]);
                                DownloadHandler.getFile(out, tokens[1]);
                                break;
                            case "quit":
                            case "q":
                                out.println("Server closing connection");
                                clientSocket.close();
                                break;
                            default:
                                out.println("unknown command");
                                break;
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }

}
