package org.example.dsassignment3_4.view;

import java.util.*;

class User {
    private String userId;
    private String name;
    private Set<User> friends;
    private Queue<String> posts;

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.friends = new HashSet<>();
        this.posts = new LinkedList<>();
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public Set<User> getFriends() { return friends; }
    public Queue<String> getPosts() { return posts; }

    public void addFriend(User user) {
        this.friends.add(user);
        user.getFriends().add(this);  // Bidirectional friendship
    }

    public void removeFriend(User user) {
        this.friends.remove(user);
        user.getFriends().remove(this);
    }

    public void addPost(String post) {
        if (posts.size() >= 10) {  // Limit to the latest 10 posts
            posts.poll();
        }
        posts.offer(post);
    }
}

class SocialNetworkGraph {
    private Map<String, User> users;

    public Map<String, User> getUsers() {return users;}

    public SocialNetworkGraph() {
        this.users = new HashMap<>();
    }

    public void addUser(String userId, String name) {
        users.putIfAbsent(userId, new User(userId, name));
    }

    public void addFriend(String userId1, String userId2) {
        User user1 = users.get(userId1);
        User user2 = users.get(userId2);
        if (user1 != null && user2 != null && !user1.equals(user2)) {
            user1.addFriend(user2);
            System.out.println("Friendship created between " + user1.getName() + " and " + user2.getName());
        } else {
            System.out.println("One or both users not found.");
        }
    }

    public void removeFriend(String userId1, String userId2) {
        User user1 = users.get(userId1);
        User user2 = users.get(userId2);
        if (user1 != null && user2 != null) {
            user1.removeFriend(user2);
            System.out.println("Friendship removed between " + user1.getName() + " and " + user2.getName());
        } else {
            System.out.println("One or both users not found.");
        }
    }

    public Set<User> getMutualFriends(String userId1, String userId2) {
        User user1 = users.get(userId1);
        User user2 = users.get(userId2);
        if (user1 == null || user2 == null) return Set.of();

        Set<User> mutualFriends = new HashSet<>(user1.getFriends());
        mutualFriends.retainAll(user2.getFriends());
        return mutualFriends;
    }

    public Set<User> suggestFriends(String userId) {
        User user = users.get(userId);
        if (user == null) return Set.of();

        Set<User> suggestions = new HashSet<>();
        for (User friend : user.getFriends()) {
            for (User friendOfFriend : friend.getFriends()) {
                if (!user.getFriends().contains(friendOfFriend) && !friendOfFriend.equals(user)) {
                    suggestions.add(friendOfFriend);
                }
            }
        }
        return suggestions;
    }

    public void displayAllUsers() {
        System.out.println("Users in the Social Network:");
        for (User user : users.values()) {
            System.out.println("- " + user.getName() + " (ID: " + user.getUserId() + ")");
        }
    }

    public void displayUserPosts(String userId) {
        User user = users.get(userId);
        if (user != null) {
            System.out.println("Posts by " + user.getName() + ":");
            for (String post : user.getPosts()) {
                System.out.println("- " + post);
            }
        } else {
            System.out.println("User not found.");
        }
    }

    public void addUserPost(String userId, String post) {
        User user = users.get(userId);
        if (user != null) {
            user.addPost(post);
            System.out.println("Post added for " + user.getName());
        } else {
            System.out.println("User not found.");
        }
    }

    public void displayNetwork() {
        System.out.println("Social Network Connections:");
        for (User user : users.values()) {
            System.out.print(user.getName() + " -> ");
            for (User friend : user.getFriends()) {
                System.out.print(friend.getName() + " ");
            }
            System.out.println();
        }
    }

    // BFS traversal (display connections level-wise)
    public void bfsTraversal(String startUserId) {
        User startUser = users.get(startUserId);
        if (startUser == null) {
            System.out.println("User not found.");
            return;
        }
        
        Set<User> visited = new HashSet<>();
        Queue<User> queue = new LinkedList<>();
        queue.offer(startUser);
        visited.add(startUser);
        
        System.out.println("BFS Traversal from " + startUser.getName() + ":");
        while (!queue.isEmpty()) {
            User current = queue.poll();
            System.out.print(current.getName() + " -> ");
            for (User friend : current.getFriends()) {
                if (!visited.contains(friend)) {
                    queue.offer(friend);
                    visited.add(friend);
                }
            }
        }
        System.out.println();
    }
}

public class SocialNetworkApp {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SocialNetworkGraph network = new SocialNetworkGraph();

        while (true) {
            System.out.println("\nSocial Network Menu:");
            System.out.println("1. Add User");
            System.out.println("2. Add Friend");
            System.out.println("3. Remove Friend");
            System.out.println("4. Display Mutual Friends");
            System.out.println("5. Suggest Friends");
            System.out.println("6. Add User Post");
            System.out.println("7. Display User Posts");
            System.out.println("8. Display Network Connections");
            System.out.println("9. BFS Traversal");
            System.out.println("10. Display All Users");
            System.out.println("11. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addUser(network);
                case 2 -> addFriend(network);
                case 3 -> removeFriend(network);
                case 4 -> displayMutualFriends(network);
                case 5 -> suggestFriends(network);
                case 6 -> addUserPost(network);
                case 7 -> displayUserPosts(network);
                case 8 -> network.displayNetwork();
                case 9 -> bfsTraversal(network);
                case 10-> network.displayAllUsers();
                case 11 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void addUser(SocialNetworkGraph network) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        if(network.getUsers().containsKey(userId)){
            System.out.println("Duplicate id not allowed!");
            return;
        }
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        network.addUser(userId, name);
        System.out.println("User added successfully.");
    }

    private static void addFriend(SocialNetworkGraph network) {
        System.out.print("Enter user ID 1: ");
        String userId1 = scanner.nextLine();
        System.out.print("Enter user ID 2: ");
        String userId2 = scanner.nextLine();
        network.addFriend(userId1, userId2);
    }

    private static void removeFriend(SocialNetworkGraph network) {
        System.out.print("Enter user ID 1: ");
        String userId1 = scanner.nextLine();
        System.out.print("Enter user ID 2: ");
        String userId2 = scanner.nextLine();
        network.removeFriend(userId1, userId2);
    }

    private static void displayMutualFriends(SocialNetworkGraph network) {
        System.out.print("Enter user ID 1: ");
        String userId1 = scanner.nextLine();
        System.out.print("Enter user ID 2: ");
        String userId2 = scanner.nextLine();
        Set<User> mutualFriends = network.getMutualFriends(userId1, userId2);
        System.out.println("Mutual Friends:");
        for (User user : mutualFriends) {
            System.out.println("- " + user.getName());
        }
    }

    private static void suggestFriends(SocialNetworkGraph network) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        Set<User> suggestions = network.suggestFriends(userId);
        System.out.println("Friend Suggestions:");
        for (User user : suggestions) {
            System.out.println("- " + user.getName());
        }
    }

    private static void addUserPost(SocialNetworkGraph network) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter post content: ");
        String post = scanner.nextLine();
        network.addUserPost(userId, post);
    }

    private static void displayUserPosts(SocialNetworkGraph network) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        network.displayUserPosts(userId);
    }

    private static void bfsTraversal(SocialNetworkGraph network) {
        System.out.print("Enter start user ID: ");
        String userId = scanner.nextLine();
        network.bfsTraversal(userId);
    }
}
