package view.endpoints;

import com.google.gson.Gson;
import logic.StudentController;
import logic.TeacherController;
import logic.UserController;
import security.Digester;
import shared.CourseDTO;
import shared.LectureDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by Kasper on 19/10/2016.
 */
@Path("/api/teacher")
public class TeacherEndpoint extends UserEndpoint {
    @DELETE
    @Consumes("application/json")
    @Path("/review/{reviewId}")
    public Response deleteReviewTeacher(@PathParam("reviewId") String reviewId) {
        Gson gson = new Gson();

        String deleteReviewTeacher = Digester.decrypt(reviewId);
        int intergerDeleteReviewTeacher = Integer.valueOf(deleteReviewTeacher);

        TeacherController teacherCtl = new TeacherController();

        boolean isDeleted = teacherCtl.softDeleteReviewTeacher(intergerDeleteReviewTeacher);

        if (isDeleted) {
            String toJson = gson.toJson(Digester.encrypt(gson.toJson(isDeleted)));
            System.out.println("Slettet Teacher");

            return successResponse(200, toJson);
        } else {
            return errorResponse(404, "Failed. Couldn't delete the chosen review.");
        }
    }

    @GET
    @Consumes("applications/json")
    @Produces("application/json")
    @Path("/course/participant/{courseId}")
    public Response getCourseParticipant(@PathParam("courseId") String courseId) {

        String courseParticipantDecrypt = Digester.decrypt(courseId);
        int integerCoursesParticipant = Integer.valueOf(courseParticipantDecrypt);

        TeacherController teacherController = new TeacherController();
        int participant = teacherController.getCourseParticipants(integerCoursesParticipant);

        if (participant !=0) {
            String returnString = String.valueOf(participant);
            return successResponse(200, returnString);
        } else {
            return errorResponse(404, "Failed. Couldn't get lectures.");
        }
    }
    @GET
    @Consumes("applications/json")
    @Produces("application/json")
    @Path("/course/average/{name}")
    public Response getAverageForLecture(@PathParam("name") String name) {

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
}