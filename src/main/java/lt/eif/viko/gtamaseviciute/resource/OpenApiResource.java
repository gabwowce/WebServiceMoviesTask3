package lt.eif.viko.gtamaseviciute.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lt.eif.viko.gtamaseviciute.OpenApiGenerator;


@Path("/openapi.json")
public class OpenApiResource {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        try {
            String json = MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(OpenApiGenerator.getOpenAPI());
            return Response.ok(json).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity("{\"error\":\"Failed to generate OpenAPI\"}")
                    .build();
        }
    }
}
