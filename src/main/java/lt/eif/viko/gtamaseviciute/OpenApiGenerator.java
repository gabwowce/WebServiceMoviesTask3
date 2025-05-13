package lt.eif.viko.gtamaseviciute;

import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import java.util.Collections;
import java.util.Set;

public class OpenApiGenerator {

    private static OpenAPI OPEN_API;  // cache

    public static synchronized OpenAPI getOpenAPI() {
        if (OPEN_API != null) return OPEN_API;   // jau turime

        // ► 1. Bazinė API informacija
        Info info = new Info()
                .title("Movie API")
                .version("1.0")
                .description("JAX‑RS service for movies & actors");

        // ► 2. Swagger konfigūracija
        SwaggerConfiguration cfg = new SwaggerConfiguration()
                .openAPI(new OpenAPI().info(info))
                .resourcePackages(Collections.singleton("lt.eif.viko.gtamaseviciute.resource"))

                .prettyPrint(true);

        try {
            // ► 3. Pastatome kontekstą Jaxrs būdu (be servlet)
            OPEN_API = new JaxrsOpenApiContextBuilder<>()
                    .openApiConfiguration(cfg)
                    .buildContext(true)  // registruoja į globalų lokatorių
                    .read();             // generuoja OpenAPI iš @Path resursų

            return OPEN_API;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to generate OpenAPI", e);
        }
    }
}
