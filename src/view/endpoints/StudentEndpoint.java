package view.endpoints;

import com.google.gson.Gson;
import logic.StudentController;
import security.Digester;
import shared.ReviewDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Et rollebaseret interface udstillet til klienten. Rollen er studerende (student).
 */
@Path("/api/student")
public class StudentEndpoint extends UserEndpoint {

    /**
     * En metode til at tilføje et review på en enkelt lektion i form af en JSON String.
     * @param json Et review objekt i form af en JSON String.
     * @return En JSON String med status på tilføjelsen.
     */
    @POST
    @Consumes("application/json")
    @Path("/review")
    public Response addReview(String json) {

        try {
            Gson gson = new Gson();
            ReviewDTO review = new Gson().fromJson(Digester.decrypt(json), ReviewDTO.class);

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
        catch (Exception e) {
            return null;
        }
    }

    /**
     * En metode til at slette et review på en enkelt lektion i form af en JSON String.
     * @param reviewId Identifier på et review.
     * @param userId Identifier på en bruger.
     * @return En JSON String med status på sletningen.
     */
    @DELETE
    @Consumes("application/json")
    @Path("/review/{reviewId}/{userId}")
    public Response deleteReview(@PathParam("reviewId") String reviewId, @PathParam("userId") String userId)
    {
        try {
            Gson gson = new Gson();

            StudentController studentCtrl = new StudentController();

            String reviewIdDecrypt = Digester.decrypt(reviewId);
            int reviewIdDecrypt1 = Integer.valueOf(reviewIdDecrypt);

            String userIdDecrypt = Digester.decrypt(userId);
            int userIdDecrypt1 = Integer.valueOf(userIdDecrypt);

            boolean isDeleted = studentCtrl.softDeleteReview(userIdDecrypt1, reviewIdDecrypt1);

            if (isDeleted) {
                String toJson = gson.toJson(Digester.encrypt(gson.toJson(isDeleted)));
                System.out.println("Slettet");

                return successResponse(200, toJson);
            } else {
                return errorResponse(404, "Failed. Couldn't delete the chosen review.");
            }
        }
        catch (Exception e) {
            return null;
        }
    }
}
