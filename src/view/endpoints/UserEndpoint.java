package view.endpoints;

import com.google.gson.Gson;
import logic.UserController;
import security.Digester;
import shared.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;


@Path("/api")
public class UserEndpoint {

    /**
     * En metode til at hente lektioner for et enkelt kursus i form af en JSON String.
     *
     * @param code Fagkoden på det kursus man ønsker at hente.
     * @return En JSON String
     */
    @GET
    @Consumes("applications/json")
    @Produces("application/json")
    @Path("/lecture/{code}")
    public Response getLectures(@PathParam("code") String code) {
        UserController userCtrl = new UserController();
        ArrayList<LectureDTO> lectures = userCtrl.getLectures(code);

        if (!lectures.isEmpty()) {
            return successResponse(200, lectures);
        } else {
            return errorResponse(404, "Failed. Couldn't get lectures.");
        }
    }
    @GET
    @Path("/study/{shortname}")
    public Response getStudy(@PathParam("shortname") String shortname) {

        Gson gson = new Gson();
        UserController userCtrl = new UserController();
        ArrayList<StudyDTO> studies = userCtrl.getStudies(shortname);

        if (!studies.isEmpty()) {
            return successResponse(200, studies);
        } else {
            return errorResponse(404, "Failed. Couldn't get courses.");
        }
    }

    /**
     * En metode til at hente de kurser en bruger er tilmeldt.
     *
     * @param userId Id'et på den bruger man ønsker at hente kurser for.
     * @return De givne kurser i form af en JSON String.
     */
    @GET
    @Path("/course/{userId}")
    public Response getCourses(@PathParam("userId") int userId) {

        Gson gson = new Gson();
        UserController userCtrl = new UserController();
        ArrayList<CourseDTO> courses = userCtrl.getCourses(userId);

        if (!courses.isEmpty()) {
            return successResponse(200, courses);
        } else {
            return errorResponse(404, "Failed. Couldn't get course.");
        }
    }

    @GET
    @Consumes("applications/json")
    @Path("/review/{lectureId}")
    public Response getReviews(@PathParam("lectureId") int lectureId) {
        Gson gson = new Gson();
        UserController userCtrl = new UserController();
        ArrayList<ReviewDTO> reviews = userCtrl.getReviews(lectureId);

        if (!reviews.isEmpty()) {
            return successResponse(200, reviews);
        } else {
            return errorResponse(404, "Failed. Couldn't get reviews.");
        }
    }
    @GET
    @Consumes("applications/json")
    @Path("/reviews/{userId}")
    public Response getReviewsFromUserId(@PathParam("userId") int userId) {
        Gson gson = new Gson();
        UserController userCtrl = new UserController();
        ArrayList<ReviewDTO> reviews = userCtrl.getReviewsFromUserId(userId);

        if (!reviews.isEmpty()) {
            return successResponse(200, reviews);
        } else {
            return errorResponse(404, "Failed. Couldn't get reviews.");
        }
    }

    @POST
    @Consumes("application/json")
    @Path("/login")
    public Response login(String data) {

        Gson gson = new Gson();
        UserDTO user = new Gson().fromJson(data, UserDTO.class);
        UserController userCtrl = new UserController();

        if (user != null) {
            UserDTO login = userCtrl.login(user.getCbsMail(), user.getPassword());
            return successResponse(200, login);
        } else {
            return errorResponse(401, "Couldn't login. Try again!");
        }
    }

    protected Response errorResponse(int status, String message) {

        //return Response.status(status).entity(new Gson().toJson(Digester.encrypt("{\"message\": \"" + message + "\"}"))).build();
        return Response.status(status).entity(new Gson().toJson("{\"message\": \"" + message + "\"}")).build();
    }

    protected Response successResponse(int status, Object data) {
        Gson gson = new Gson();

        //return Response.status(status).entity(gson.toJson(Digester.encrypt(gson.toJson(data)))).build();
        return Response.status(status).entity(gson.toJson(data)).build();
    }
}
