package com.goit.http;

/*Задание 3

Дополните программу методом, который будет выводить все открытые задачи для пользователя Х.
https://jsonplaceholder.typicode.com/users/1/todos.
Открытыми считаются все задачи, у которых completed = false.
*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Main3 {
    public static void main(String[] args) throws IOException, InterruptedException {
        GetAllTodos src = new GetAllTodos();
        src.getAllOpenToDosByUserId(1);
    }
}

public class GetAllTodos {
    HttpClient client = HttpClient.newHttpClient();

    public void getAllOpenToDosByUserId(int userId) throws IOException, InterruptedException {
        String allTodosJson = getAllTodosByUserId(userId);
        List<ToDosJPH> allOpenTodos = getAllOpenToDos(getAllTodos(allTodosJson));
        String jsonFilePath = "src/main/resources/" + "user-" + userId + "-open_todos.json";
        createJsonWithTodos(allOpenTodos, jsonFilePath);
    }

    private String getAllTodosByUserId(int userId) throws IOException, InterruptedException {
        String uri = "https://jsonplaceholder.typicode.com/users/" + userId + "/todos";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private ToDosJPH[] createTodosFromJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, ToDosJPH[].class);
    }

    private List<ToDosJPH> getAllTodos(String jsonTodos) {
        return new ArrayList<>(Arrays.asList(createTodosFromJson(jsonTodos)));
    }

    private List<ToDosJPH> getAllOpenToDos(List<ToDosJPH> todos) {
        List<ToDosJPH> result = new ArrayList<>();
        for (ToDosJPH element : todos) {
            if (!element.completed) {
                result.add(element);
            }
        }
        return result;
    }

    private void createJsonWithTodos(List<ToDosJPH> todos, String jsonFilePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String outputString = gson.toJson(todos.toArray());
        try (FileWriter output = new FileWriter(jsonFilePath)) {
            output.write(outputString);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

}

class ToDosJPH {
    int userId;
    int id;
    String title;
    boolean completed;

    public ToDosJPH(int userId, int id, String title, boolean completed) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "ToDos{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                '}';
    }
}
