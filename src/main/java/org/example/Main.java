package org.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main extends HttpServlet {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHENTICATION_HEADER_VALUE = "yMHw4UidZM1Hha82AZtMjI50bYCfC3sdX7vvB";
    private static final String BASE_URL = "https://api.hotpot.ai/";
    private static final String ART_GENERATOR_ENDPOINT = "make-art";
    private static final String FILE_TO = "src/main/resources/01.png";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder(
                            new URI(BASE_URL + ART_GENERATOR_ENDPOINT))
                    .header(AUTHORIZATION_HEADER, AUTHENTICATION_HEADER_VALUE)
                    .header("Content-Type", "text/plain;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(req.getParameter("inputText") + req.getParameter("style")))
                    .build();
            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            Files.copy(response.body(), Paths.get(FILE_TO));
            int statusCode = response.statusCode();
            resp.setContentType("text/html");
            resp.setStatus(statusCode);
            PrintWriter out = resp.getWriter();
            out.println("<p><img src="src/main/resources/01.png"></p>");
            out.println(<br></br>);
            out.println("<p><a href="index.html">Home</a></p>");
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }
}
