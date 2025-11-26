package com.radmiy;

import com.radmiy.handler.RequestHandler;
import com.radmiy.handler.ResponseHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("server started");

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("client connected");

                    try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                        String requestedFile = "";

                        while (!in.ready()) ;
                        while (in.ready()) {
                            final RequestHandler requestHandler = new RequestHandler();
                            requestedFile = requestHandler.handle(in);

                            if (requestedFile != null && !requestedFile.isEmpty()) {
                                final ResponseHandler responseHandler = new ResponseHandler();
                                responseHandler.handle(requestedFile, out);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
