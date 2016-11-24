package view.endpoints;

import com.google.gson.Gson;
import logic.StudentController;
import security.Digester;
import shared.ReviewDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by Kasper on 19/10/2016.
 */

@Path("/api/student")
public class StudentEndpoint extends UserEndpoint {

    @POST
    @Consumes("application/json")
    @Path("/review")
    public Response addReview(String json) {

        Gson gson = new Gson();
        ReviewDTO review = new Gson().fromJson(json, ReviewDTO.class);

        StudentController studentCtrl = new StudentController();
        boolean isAdded = studentCtrl.addReview(review);

        if (isAdded) {
            //String toJson = gson.toJson(Digester.encrypt(gson.toJson(isAdded)));
            String toJson = gson.toJson((gson.toJson(isAdded)));
            System.out.println("Tilføjet");

            return successResponse(200, toJson);

        } else {
            return errorResponse(404, "Failed. Couldn't get reviews.");
        }
    }

    @DELETE
    @Consumes("application/json")
    @Path("/review/{reviewId}/{userId}")
    public Response deleteReview(@PathParam("reviewId") int reviewId, @PathParam("userId") int userId)
    {
        Gson gson = new Gson();

        StudentController studentCtrl = new StudentController();

        boolean isDeleted = studentCtrl.softDeleteReview(userId, reviewId);

        if (isDeleted) {
            String toJson = gson.toJson(Digester.encrypt(gson.toJson(isDeleted)));
            System.out.println("Slettet");

            return successResponse(200, toJson);
        } else {
            return errorResponse(404, "Failed. Couldn't delete the chosen review.");
        }
    }
}