package com.mathboy11;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.ListCoursesResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class Main {
    private static final String clientId = "CLIENT_ID_GOES_HERE";
    private static final String clientSecret = "CLIENT_SECRET_GOES_HERE";
    private static final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private static final List<String> scopes = Collections.singletonList(ClassroomScopes.CLASSROOM_COURSES_READONLY);

    private static Credential getCreds(NetHttpTransport transport) throws IOException {
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(transport, jsonFactory, clientId, clientSecret, scopes).build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

        Classroom service = new Classroom.Builder(transport, jsonFactory, getCreds(transport))
                .setApplicationName("Youtube Classroom Demo")
                .build();

        ListCoursesResponse response = service.courses().list()
                .setPageSize(10)
                .execute();

        List<Course> courses = response.getCourses();

        for (Course course : courses) {
            System.out.println(course.getName());
        }
    }
}