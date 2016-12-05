package view.endpoints;

import com.google.gson.Gson;
import logic.UserController;
import security.Digester;
import shared.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Et interface udstillet til klienten.
 */

@Path("/api")
public class UserEndpoint {

    /**
     * En metode til at hente lektioner for et enkelt kursus i form af en JSON String.
     * @param code Fagkoden på det kursus man ønsker at hente.
     * @return En JSON String med lektioner for det valgte kursus.
     */
    @GET
    @Consumes("applications/json")
    @Produces("application/json")
    @Path("/lecture/{code}")
    public Response getLectures(@PathParam("code") String code) {

        try {
            String lecturesDecrypt = Digester.decrypt(code);

            UserController userCtrl = new UserController();
            ArrayList<LectureDTO> lectures = userCtrl.getLectures(lecturesDecrypt);

            if (!lectures.isEmpty()) {
                return successResponse(200, lectures);
            } else {
                return errorResponse(404, "Failed. Couldn't get lectures.");
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * En metode til at hente kurser for et enkelt studie i form af en JSON String.
     * @param shortname navnet på det studie man ønsker at hente.
     * @return En JSON String med kurser for det valgte studie.
     */
    @GET
    @Path("/study/{shortname}")
    public Response getStudy(@PathParam("shortname") String shortname) {

        try {
            UserController userCtrl = new UserController();
            ArrayList<StudyDTO> studies = userCtrl.getStudies(shortname);

            if (!studies.isEmpty()) {
                return successResponse(200, studies);
            } else {
                return errorResponse(404, "Failed. Couldn't get studies.");
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * En metode til at hente de kurser en bruger er tilmeldt i form af en JSON String.
     * @param userId Identfier på den bruger man ønsker at hente kurser for.
     * @return En JSON String med kurser for den valgte bruger.
     */
    @GET
    @Path("/course/{userId}")
    public Response getCourses(@PathParam("userId") String userId) {

        try {
            String courseDecrypt = Digester.decrypt(userId);
            int integerCourses = Integer.valueOf(courseDecrypt);

            UserController userCtrl = new UserController();
            ArrayList<CourseDTO> courses = userCtrl.getCourses(integerCourses);

            if (!courses.isEmpty()) {
                return successResponse(200, courses);
            } else {
                return errorResponse(404, "Failed. Couldn't get courses.");
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * En metode til at hente reviews for en lektion i form af en JSON String.
     * @param lectureId Identfier på den lektion man ønsker at hente reviews for.
     * @return En JSON String med reviews for den valgte lektion.
     */
    @GET
    @Consumes("applications/json")
    @Path("/review/{lectureId}")
    public Response getReviews(@PathParam("lectureId") String lectureId) {

        try {
            String reviewDecrypt = Digester.decrypt(lectureId);
            int intergerReview = Integer.valueOf(reviewDecrypt);

            UserController userCtrl = new UserController();
            ArrayList<ReviewDTO> reviews = userCtrl.getReviews(intergerReview);

            if (!reviews.isEmpty()) {
                return successResponse(200, reviews);
            } else {
                return errorResponse(404, "Failed. Couldn't get reviews.");
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * En metode til at hente brugerens egne reviews i form af en JSON String.
     * @param userId Identfier på den user man ønsker at hente reviews for.
     * @return En JSON String med brugerens reviews.
     */
    @GET
    @Consumes("applications/json")
    @Path("/reviews/{userId}")
    public Response getReviewsFromUserId(@PathParam("userId") String userId) {

        try {
            String reviewFromUserIdDecrypt = Digester.decrypt(userId);
            int intergerReviewFromUserId = Integer.valueOf(reviewFromUserIdDecrypt);

            UserController userCtrl = new UserController();
            ArrayList<ReviewDTO> reviews = userCtrl.getReviewsFromUserId(intergerReviewFromUserId);

            if (!reviews.isEmpty()) {
                return successResponse(200, reviews);
            } else {
                return errorResponse(404, "Failed. Couldn't get reviews.");
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * En metode til at logge ind.
     * @param data mail og password på brugeren.
     * @return en JSON String med brugerens login.
     */
    @POST
    @Consumes("application/json")
    @Path("/login")
    public Response login(String data) {

        try {
            UserDTO user = new Gson().fromJson(Digester.decrypt(data), UserDTO.class);
            UserController userCtrl = new UserController();

            if (user != null) {
                UserDTO login = userCtrl.login(user.getCbsMail(), user.getPassword());
                return successResponse(200, login);
            } else {
                return errorResponse(401, "Couldn't login. Try again!");
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * En metode der returnerer en fejl meddelelse i form af en JSON String.
     * @param status Nummer på status.
     * @param message Beskrivelse af status.
     * @return En fejlbesked indeholdende status.
     */
    protected Response errorResponse(int status, String message) {
        return Response.status(status).entity(new Gson().toJson(Digester.encrypt("{\"message\": \"" + message + "\"}"))).build();
    }

    /**
     * En metode der returnerer en success meddelelse i form af en JSON String.
     * @param status Nummer på status.
     * @param data Status objekt.
     * @return En besked ved success indeholdende status.
     */
    protected Response successResponse(int status, Object data) {
        return Response.status(status).entity((Digester.encrypt(new Gson().toJson(data)))).build();
    }
}
