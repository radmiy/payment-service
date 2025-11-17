package com.radmiy.handler;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestHandler {

    public String handle(BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();

        if (firstLine != null && !firstLine.isEmpty()) {
            String[] parts = firstLine.split(" ");
            System.out.println(String.join(" ", parts));
            if (parts.length >= 2) {
                if (parts[0].equals("GET")) {
                    return parts[1];
                }
            }
        }

        return null;
    }
}
