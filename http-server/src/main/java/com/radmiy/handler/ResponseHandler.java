package com.radmiy.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.radmiy.handler.ResponseHandler.STATUS.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ResponseHandler {

    private static final String FILES_DIR = "http-server\\static";
    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("html", "text/html; charset=UTF-8");
        MIME_TYPES.put("css", "text/css; charset=UTF-8");
        MIME_TYPES.put(null, "application/octet-stream");
    }

    public void handle(String requestPath, BufferedWriter outputStream) {
        try {
            String fileName = requestPath.equals("/") || requestPath.equals("/index.html") ? "index.html" : requestPath;

            if (fileName.startsWith("/")) {
                fileName = fileName.substring(1);
            }

            final Path staticPath = Paths.get(FILES_DIR).toAbsolutePath().normalize();
            final Path filePath = staticPath.resolve(fileName).normalize();

            if (!filePath.startsWith(staticPath)) {
                sendError(outputStream, STATUS_403, "Путь не разрешен.");
                return;
            }

            final File file = filePath.toFile();
            if (file.exists() && !file.isDirectory()) {
                sendFoundFile(outputStream, file);
            } else {
                sendError(outputStream, STATUS_404, "Страничка не найдена.");
            }

        } catch (IOException e) {
            System.err.println("Ошибка при обработке запроса: " + e.getMessage());
            try {
                sendError(outputStream, STATUS_500, "Ошибка сервера.");
            } catch (IOException ignored) {
            }
        }
    }

    private void sendFoundFile(BufferedWriter writer, File file) throws IOException {
        final String fileContent = Files.readString(file.toPath(), UTF_8);
        send(writer, STATUS_200, fileContent, getMimeType(file.getName()));
    }

    private void sendError(BufferedWriter writer, STATUS status, String bodyMessage) throws IOException {
        final String htmlBody = "<h1>" + status + "</h1><p>" + bodyMessage + "</p>";
        send(writer, status, htmlBody, MIME_TYPES.get("html"));
    }

    private void send(BufferedWriter writer, STATUS status, String bodyMessage, String mimeType) throws IOException {
        final long htmlBodyLen = bodyMessage.getBytes(UTF_8).length;

        final String response = "HTTP/1.1 " + status.code +
                "\r\n" + "Content-Type: " + mimeType +
                "; charset=UTF-8\r\nContent-Length: " +
                htmlBodyLen + "\r\nConnection: close\r\n\r\n";

        writer.write(response);
        writer.write(bodyMessage);
        writer.flush();
    }

    private String getMimeType(String fileName) {
        final int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            final String extension = fileName.substring(dotIndex + 1).toLowerCase();
            return MIME_TYPES.getOrDefault(extension, MIME_TYPES.get(null));
        }
        return MIME_TYPES.get(null);
    }

    enum STATUS {
        STATUS_200("200 OK"),
        STATUS_403("403 Forbidden"),
        STATUS_404("404 Not Found"),
        STATUS_500("500 Internal Server Error");

        private final String code;

        STATUS(String code) {
            this.code = code;
        }
    }

}
