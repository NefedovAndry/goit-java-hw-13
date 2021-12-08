package com.goit.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        String jsonPathUserFromApp = "src/main/resources/user_from_app_demo.json";
        String jsonPathUserInput = "src/main/resources/user_demo.json";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("https://pingponggoit.herokuapp.com/createUser")) /*getDefaultUser*/
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofFile(Paths.get(jsonPathUserInput)))
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        System.out.println("Request #1");
        System.out.println("Headers " + response1.headers());
        System.out.println("Body " + response1.body());
        System.out.println("Status " + response1.statusCode());

        createJsonFromString(response1.body(), jsonPathUserFromApp);
        User user1 = createUserFromJson(response1.body());
        System.out.println(user1);

        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(URI.create("https://pingponggoit.herokuapp.com/getUsers"))
                .GET()
                .build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        System.out.println("Request #3");
        System.out.println(response3.body());

        List<User> usersBeforeDeleting = new ArrayList<>(Arrays.asList(createUsersFromJson(response3.body())));
        System.out.println(usersBeforeDeleting);

        String userUri = "https://pingponggoit.herokuapp.com/removeUser";
        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(URI.create(userUri))
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofFile(Paths.get(jsonPathUserFromApp)))
                .build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        System.out.println("Request #4");
        System.out.println(response4.body());

        HttpRequest request5 = HttpRequest.newBuilder()
                .uri(URI.create("https://pingponggoit.herokuapp.com/getUsers"))
                .GET()
                .build();
        HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
        System.out.println("Request #5");
        System.out.println(response5.body());
        List<User> usersAfterDeleting = new ArrayList<>(Arrays.asList(createUsersFromJson(response5.body())));
        System.out.println(usersAfterDeleting);

        String userPUTUri = "https://pingponggoit.herokuapp.com/overwrite?id=722";
        HttpRequest request6 = HttpRequest.newBuilder()
                .uri(URI.create(userPUTUri))
                .header("Content-Type", "application/json")
//                .method("DELETE", HttpRequest.BodyPublishers.ofFile(Paths.get(jsonPathUserFromApp)))
                .PUT(HttpRequest.BodyPublishers.ofFile(Paths.get(jsonPathUserInput)))
                .build();
        HttpResponse<String> response6 = client.send(request6, HttpResponse.BodyHandlers.ofString());
        System.out.println("Request #6");
        System.out.println(response4.body());

        HttpRequest request7 = HttpRequest.newBuilder()
                .uri(URI.create("https://pingponggoit.herokuapp.com/getUsers"))
                .GET()
                .build();
        HttpResponse<String> response7 = client.send(request7, HttpResponse.BodyHandlers.ofString());
        System.out.println("Request #7");
        System.out.println(response7.body());
        List<User> usersAfterPutting = new ArrayList<>(Arrays.asList(createUsersFromJson(response7.body())));
        System.out.println(usersAfterPutting);


//        String userUri = "https://pingponggoit.herokuapp.com/getUserById?id=" + user1.getId();
//        HttpRequest request2 = HttpRequest.newBuilder()
//                .uri(URI.create(userUri))
//                .GET()
//                .build();
//        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
//        User user2 = createUserFromJson(response2.body());
//        System.out.println(user2);

    }

    public static void createJsonFromString(String str, String jsonFilePath) {
        User user = createUserFromJson(str);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String outputString = gson.toJson(user);
        try (FileWriter output = new FileWriter(jsonFilePath)) {
            output.write(outputString);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public static User createUserFromJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, User.class);
    }

    public static User[] createUsersFromJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, User[].class);
    }
}

class User {
    private final int id;
    private final String name;
    private final String surname;
    private final int salary;
    private final String gender;

    public User(int id, String name, String surname, int salary, String gender) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.salary = salary;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{" + "\r\n" +
                "  id='" + id + "', " + "\r\n" +
                "  name='" + name + "', " + "\r\n" +
                "  surname='" + surname + "', " + "\r\n" +
                "  salary='" + salary + "', " + "\r\n" +
                "  gender='" + gender + '\'' + "\r\n" +
                '}';
    }
}