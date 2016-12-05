package view.endpoints;

import com.google.gson.Gson;
import logic.TeacherController;
import security.Digester;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Et rollebaseret interface udstillet til klienten. Rollen er lærer (teacher).
 */
@Path("/api/teacher")
public class TeacherEndpoint extends UserEndpoint {

    /**
     * En metode til at slette et review for en enkelt lektion i form af en JSON String.
     * @param reviewId Identifier på det review man ønsker at slette.
     * @return En JSON String med en status på sletning.
     */
    @DELETE
    @Consumes("application/json")
    @Path("/review/{reviewId}")
    public Response deleteReviewTeacher(@PathParam("reviewId") String reviewId) {

        try {
            Gson gson = new Gson();
            String deleteReviewTeacher = Digester.decrypt(reviewId);
            int intergerDeleteReviewTeacher = Integer.valueOf(deleteReviewTeacher);

            TeacherController teacherCtl = new TeacherController();

            // soft deletion
            boolean isDeleted = teacherCtl.softDeleteReviewTeacher(intergerDeleteReviewTeacher);

            if (isDeleted) {
                String toJson = gson.toJson(Digester.encrypt(gson.toJson(isDeleted)));
                System.out.println("Slettet Teacher");
                return successResponse(200, toJson);
            } else {
                return errorResponse(404, "Failed. Couldn't delete the chosen review.");
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * En metode til at hente deltagere på et enkelt kursus i form af en JSON String.
     * @param courseId Identifier på kurset man ønsker at hente deltagere for.
     * @return En JSON String med deltagere for det valgte kursus.
     */
    @GET
    @Consumes("applications/json")
    @Produces("application/json")
    @Path("/course/participant/{courseId}")
    public Response getCourseParticipant(@PathParam("courseId") String courseId) {

        try {
            String courseParticipantDecrypt = Digester.decrypt(courseId);
            int integerCoursesParticipant = Integer.valueOf(courseParticipantDecrypt);

            TeacherController teacherController = new TeacherController();
            int participant = teacherController.getCourseParticipants(integerCoursesParticipant);

            if (participant != 0) {
                String returnString = String.valueOf(participant);
                return successResponse(200, returnString);
            } else {
                return errorResponse(404, "Failed. Couldn't get lectures.");
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * En metode til at hente review gennemsnittet på et enkelt kursus i form af en JSON String.
     * @param name Identifier på kurset man ønsker at hente review gennemsnit for.
     * @return En JSON String med review gennemsnit for det valgte kursus.
     */
    @GET
    @Consumes("applications/json")
    @Produces("application/json")
    @Path("/course/average/{name}")
    public Response getAverageReviewForCourse(@PathParam("name") String name) {

        try {
            String courseParticipantDecrypt = Digester.decrypt(name);
            TeacherController teacherController = new TeacherController();
            double averageCourses = 0;
            averageCourses = teacherController.calculateAverageRatingOnCourse(courseParticipantDecrypt);

            if (averageCourses !=0) {
                String returnString = String.valueOf(averageCourses);
                return successResponse(200, returnString);
            } else {
                return errorResponse(404, "Failed. Couldn't get lectures.");
            }
        }
        catch (Exception e) {
            return null;
        }
    }
}