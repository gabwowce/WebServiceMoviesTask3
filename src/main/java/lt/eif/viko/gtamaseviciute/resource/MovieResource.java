package lt.eif.viko.gtamaseviciute.resource;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lt.eif.viko.gtamaseviciute.dao.MovieDao;
import lt.eif.viko.gtamaseviciute.model.Movie;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.eif.viko.gtamaseviciute.util.OperationResult;

import java.sql.SQLException;
import java.util.List;

@Path("/movies")
@Tag(name = "Movies", description = "Movie CRUD and queries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

    private final MovieDao dao = new MovieDao();

    /** GET /movies -> visi filmai */
    @GET
    @Operation(
            summary = "Get all movies",
            description = "Returns movies with actors",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Movies retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OperationResult.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content)
            }
    )
    public Response getAll() {
        OperationResult<List<Movie>> result = dao.findAll();
        return Response
                .status(result.isSuccess() ? Response.Status.OK : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(result)
                .build();
    }



    /** GET /movies/{id} -> filmas su aktoriais */
    @GET
    @Path("/{id}")
    @Operation(
            summary = "Get movie by ID",
            description = "Returns a single movie",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Movie found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OperationResult.class))),
                    @ApiResponse(responseCode = "404", description = "Movie not found",
                            content = @Content)
            }
    )
    public Response getById(@PathParam("id") int id) {
        OperationResult<Movie> result = dao.findById(id);

        return Response
                .status(result.isSuccess() ? Response.Status.OK : Response.Status.NOT_FOUND)
                .entity(result)
                .build();
    }

    /** POST /movies -> sukuria filmą */
    @POST
    @Operation(
            summary = "Create new movie",
            description = "Creates movie and (optionally) new actors",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Movie created",
                            content = @Content(schema = @Schema(implementation = OperationResult.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content)
            }
    )
    public Response create(Movie movie) {
        OperationResult result = dao.create(movie);

        return Response
                .status(result.isSuccess() ? Response.Status.CREATED : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(result)
                .build();
    }

}