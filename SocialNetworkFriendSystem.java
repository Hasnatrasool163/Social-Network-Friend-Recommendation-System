package org.example.dsassignment3_4.dao;

import java.util.*;

public class SocialNetworkFriendSystem {
    static final int MAX_USERS = 100;
    static String[] users = new String[MAX_USERS];
    static int[][] graph = new int[MAX_USERS][MAX_USERS]; // Adjacency matrix
    static int userCount = 0;
    static LinkedList<String>[] posts = new LinkedList[MAX_USERS]; // Posts for each user

    public static void main(String[] args) {
        for (int i = 0; i < MAX_USERS; i++) {
            posts[i] = new LinkedList<>();
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Social Network Friend Recommendation System ---");
            System.out.println("1. Add User");
            System.out.println("2. Add Friend");
            System.out.println("3. Delete Friend");
            System.out.println("4. Display All Users");
            System.out.println("5. Display Friends");
            System.out.println("6. Display Mutual Friends");
            System.out.println("7. Recommend Friends");
            System.out.println("8. Add Post");
            System.out.println("9. Display Posts");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> addUser(scanner);
                case 2 -> addFriend(scanner);
                case 3 -> deleteFriend(scanner);
                case 4 -> displayAllUsers();
                case 5 -> displayFriends(scanner);
                case 6 -> displayMutualFriends(scanner);
                case 7 -> recommendFriends(scanner);
                case 8 -> addPost(scanner);
                case 9 -> displayPosts(scanner);
                case 10 -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    static void addUser(Scanner scanner) {
        if (userCount >= MAX_USERS) {
            System.out.println("Maximum user limit reached!");
            return;
        }
        System.out.print("Enter user name: ");
        scanner.nextLine(); // Consume newline
        String name = scanner.nextLine();
        users[userCount++] = name;
        System.out.println("User added with ID: " + (userCount - 1));
    }

    static void addFriend(Scanner scanner) {
        System.out.print("Enter User ID 1: ");
        int u1 = scanner.nextInt();
        System.out.print("Enter User ID 2: ");
        int u2 = scanner.nextInt();
        if (isValidUser(u1) && isValidUser(u2)) {
            graph[u1][u2] = 1;
            graph[u2][u1] = 1;
            System.out.println("Friendship created between " + users[u1] + " and " + users[u2]);
        } else {
            System.out.println("Invalid User IDs!");
        }
    }

    static void deleteFriend(Scanner scanner) {
        System.out.print("Enter User ID 1: ");
        int u1 = scanner.nextInt();
        System.out.print("Enter User ID 2: ");
        int u2 = scanner.nextInt();
        if (isValidUser(u1) && isValidUser(u2)) {
            graph[u1][u2] = 0;
            graph[u2][u1] = 0;
            System.out.println("Friendship removed between " + users[u1] + " and " + users[u2]);
        } else {
            System.out.println("Invalid User IDs!");
        }
    }

    static void displayAllUsers() {
        System.out.println("Users in the network:");
        for (int i = 0; i < userCount; i++) {
            System.out.println("ID: " + i + " Name: " + users[i]);
        }
    }

    static void displayFriends(Scanner scanner) {
        System.out.print("Enter User ID: ");
        int userId = scanner.nextInt();
        if (isValidUser(userId)) {
            System.out.println("Friends of " + users[userId] + ":");
            for (int i = 0; i < userCount; i++) {
                if (graph[userId][i] == 1) {
                    System.out.println(users[i]);
                }
            }
        } else {
            System.out.println("Invalid User ID!");
        }
    }

    static void displayMutualFriends(Scanner scanner) {
        System.out.print("Enter User ID 1: ");
        int u1 = scanner.nextInt();
        System.out.print("Enter User ID 2: ");
        int u2 = scanner.nextInt();
        if (isValidUser(u1) && isValidUser(u2)) {
            System.out.println("Mutual friends between " + users[u1] + " and " + users[u2] + ":");
            for (int i = 0; i < userCount; i++) {
                if (graph[u1][i] == 1 && graph[u2][i] == 1) {
                    System.out.println(users[i]);
                }
            }
        } else {
            System.out.println("Invalid User IDs!");
        }
    }

    static void recommendFriends(Scanner scanner) {
        System.out.print("Enter User ID for recommendation: ");
        int userId = scanner.nextInt();
        if (isValidUser(userId)) {
            Set<Integer> recommendations = new HashSet<>();
            for (int i = 0; i < userCount; i++) {
                if (graph[userId][i] == 1) { // Friends of the user
                    for (int j = 0; j < userCount; j++) {
                        if (graph[i][j] == 1 && graph[userId][j] == 0 && j != userId) {
                            recommendations.add(j);
                        }
                    }
                }
            }
            System.out.println("Friend recommendations for " + users[userId] + ":");
            for (int id : recommendations) {
                System.out.println(users[id]);
            }
        } else {
            System.out.println("Invalid User ID!");
        }
    }

    static void addPost(Scanner scanner) {
        System.out.print("Enter User ID: ");
        int userId = scanner.nextInt();
        if (isValidUser(userId)) {
            System.out.print("Enter post content: ");
            scanner.nextLine(); // Consume newline
            String content = scanner.nextLine();
            posts[userId].add(content);
            System.out.println("Post added for " + users[userId]);
        } else {
            System.out.println("Invalid User ID!");
        }
    }

    static void displayPosts(Scanner scanner) {
        System.out.print("Enter User ID to view posts: ");
        int userId = scanner.nextInt();
        if (isValidUser(userId)) {
            System.out.println("Posts by " + users[userId] + ":");
            for (String post : posts[userId]) {
                System.out.println("- " + post);
            }
        } else {
            System.out.println("Invalid User ID!");
        }
    }

    static boolean isValidUser(int id) {
        return id >= 0 && id < userCount;
    }
}
