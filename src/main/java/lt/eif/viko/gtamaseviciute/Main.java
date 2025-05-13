package lt.eif.viko.gtamaseviciute;

import jakarta.ws.rs.core.UriBuilder;
import lt.eif.viko.gtamaseviciute.config.AppConfig;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.net.URI;

public class Main {
    public static void main(String[] args) throws IOException {
        URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(8080).build();

        // 1. Startuojame Jersey + Grizzly
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, new AppConfig(), false);

        // 2. Pridedame Swagger UI statinius failus
        ClassLoader cl = Main.class.getClassLoader();
        HttpHandler swagger = new CLStaticHttpHandler(
                cl, "/META-INF/resources/webjars/swagger-ui/5.12.2/");

        server.getServerConfiguration().addHttpHandler(swagger, "/swagger-ui");

        // 3. Paleidžiame serverį
        server.start();


        // 4. Blokuojame giją, kol vartotojas paspaus ENTER
        System.in.read();
        server.shutdownNow();
    }
}
