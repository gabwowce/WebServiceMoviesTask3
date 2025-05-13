package lt.eif.viko.gtamaseviciute.config;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {

    public AppConfig() {
        // Vienas (!) tikslus paketas, kuriame yra tavo @Path klasės
        packages("lt.eif.viko.gtamaseviciute.resource");

        // Swagger/OpenAPI endpointams pakanka šių dviejų
        register(OpenApiResource.class);
        register(AcceptHeaderOpenApiResource.class);
    }
}
