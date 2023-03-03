package animals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
class Node {
    String value;
    boolean isAnimal;
    Node left;
    Node right;

    public Node() {
    }

    public Node(String value) {
        this.value = value;
        this.right = null;
        this.left = null;
    }

    public Node(String value, boolean isAnimal) {
        this.value = value;
        this.isAnimal = isAnimal;
        this.right = null;
        this.left = null;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getLeft() {
        return this.left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return this.right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @JsonIgnore
    public boolean isAnimal() {
        return this.isAnimal;
    }

    @JsonIgnore
    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    public void setIsAnimal(boolean isAnimal) {
        this.isAnimal = isAnimal;
    }
}

class BinaryTree {
    Node root;

    BinaryTree(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return this.root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}

public class Main {
    static List<String> affirmatives = Arrays.asList("y", "yes", "yeah", "yep", "sure", "right", "affirmative", "correct", "indeed", "you bet", "exactly", "you said it");
    static List<String> negatives = Arrays.asList("n", "no", "no way", "nah", "nope", "negative", "i don't think so, yeah no", "i don't think so", "yeah no");
    static List<String> clarifications = Arrays.asList(
            "I'm not sure I caught you: was it yes or no?",
            "Funny, I still don't understand, is it yes or no?",
            "Oh, it's too complicated for me: just tell me yes or no.",
            "Could you please simply say yes or no?",
            "Oh, no, don't try to confuse me: say yes or no.");
    static List<String> farewells = Arrays.asList(
            "Have a nice day!",
            "Goodbye!",
            "See you later!",
            "Bye!",
            "Until next time!");

    public static void main(String[] args) {
        Locale currentLocale = Locale.getDefault();

        System.out.println(currentLocale.getDisplayLanguage()); // English
        System.out.println(currentLocale.getDisplayCountry());    // United States

        System.out.println(currentLocale.getLanguage());        // en
        System.out.println(currentLocale.getCountry());            // US

        ObjectMapper objectMapper;
        String fileName = "animals.";
        if (args.length >= 1 && args[1].equalsIgnoreCase("xml")) {
            objectMapper = new XmlMapper();
            fileName = fileName + "xml";
        } else if (args.length >= 1 && args[1].equalsIgnoreCase("yaml")) {
            objectMapper = new YAMLMapper();
            fileName = fileName + "yaml";
        } else {
            objectMapper = new JsonMapper();
            fileName = fileName + "json";
        }

        displayGreeting();

        // If file doesn't exist, create new BinaryTree from scratch:
        File file = new File(fileName);
        Node root;
        BinaryTree gameTree;

        if (file.exists() && file.length() > 0) {
            // Load from the file:
            try {
                root = objectMapper.readValue(new File(fileName), Node.class);
                gameTree = new BinaryTree(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("I want to learn about animals.");
            System.out.println("Which animal do you like most?");
            String animal1 = getAnimal();
            System.out.println();
            gameTree = new BinaryTree(new Node(animal1));
        }

        System.out.println("Welcome to the animal expert system!");
        System.out.println();

        // Continually show the menu and get a selection from the user until they want to exit
        int selection;
        do {
            selection = displayOptions();
            switch (selection) {
                case 1: {
                    System.out.println("Playing the guessing game.");
                    Node currentNode = gameTree.getRoot();

                    do {
                        if (currentNode == gameTree.getRoot()) {
                            startGame();
                        }
                        if (!currentNode.isLeaf()) {
                            while (!currentNode.isLeaf()) {
                                currentNode = playRound(gameTree, currentNode);
                            }
                        }
                        currentNode = playRound(gameTree, currentNode);
                    } while (getYesNoResponse("Would you like to play again?"));
                    break;
                }
                case 2: {
                    System.out.println("Listing all the animals.");
                    listAnimals(gameTree.getRoot());
                    break;
                }
                case 3: {
                    System.out.println("Enter the animal:");
                    String animalToFind = getAnimal();
                    List<Node> path = getPath(gameTree.getRoot(), animalToFind);
                    if (path.size() == 0) {
                        System.out.printf("No facts about the %s%n", animalToFind.split(" ")[1]);
                    } else {
                        System.out.printf("Facts about the %s:%n", animalToFind.split(" ")[1]);
                        printPathFacts(path);
                    }
                    break;
                }
                case 4: {
                    System.out.println("The Knowledge Tree stats\n");

                    System.out.printf("- root node                    %s%n", convertQuestionToFact(gameTree.getRoot().getValue(), true));
                    printNumberOfNodesStats(gameTree.getRoot());
                    List<List<Node>> paths = getPaths(gameTree.getRoot());
                    printHeightAndDepthStats(paths);
                    break;
                }
                case 5: {
                    printKnowledgeTree(gameTree.getRoot());
                    break;
                }
                case 0: {
                    break;
                }
                default: {
                    System.out.println("Valid selections: 0 - 5.");
                }
            }
        } while (selection != 0);


        sayFarewell();
        // Save the game by saving the root node
        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(fileName), gameTree.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printKnowledgeTree(Node root) {
        System.out.println();

        Deque<Map.Entry<Node, Integer>> queue = new ArrayDeque<>();
        queue.add(Map.entry(root, 0));


        while (!queue.isEmpty()) {
            Map.Entry<Node, Integer> curEntry = queue.pop();

            Node curNode = curEntry.getKey();
            int curLevel = curEntry.getValue();

            StringBuilder line = new StringBuilder("   ");
            line.append(" ".repeat(Math.max(0, curLevel)));
            line.append(curNode.getValue());
            if (curNode.isLeaf()) {
                System.out.println(line);
            } else {
                System.out.println(line);
                if (curNode.getLeft() != null) {
                    queue.push(Map.entry(curNode.getLeft(), curLevel + 1));
                }
                if (curNode.getRight() != null) {
                    queue.push(Map.entry(curNode.getRight(), curLevel + 1));
                }
            }
        }
    }

    private static void printHeightAndDepthStats(List<List<Node>> paths) {
        int treeHeight = 0;
        int minAnimalDepth = Integer.MAX_VALUE;
        double sumOfAnimalDepths = 0;

        for (List<Node> path : paths) {
            treeHeight = Math.max(treeHeight, path.size() - 1);
            minAnimalDepth = Math.min(minAnimalDepth, path.size() - 1);
            sumOfAnimalDepths += path.size() - 1;
        }

        System.out.printf("- height of the tree           %d%n", treeHeight);
        System.out.printf("- minimum animal's depth       %d%n", minAnimalDepth);
        System.out.printf("- average animal's depth       %.1f%n", sumOfAnimalDepths / paths.size());
    }

    private static void printNumberOfNodesStats(Node root) {
        Deque<Node> queue = new ArrayDeque<>();
        queue.add(root);

        int totalNodes = 0;
        int totalAnimals = 0;
        int totalStatements = 0;

        while (!queue.isEmpty()) {
            Node curNode = queue.poll();
            if (curNode.isLeaf()) {
                totalAnimals++;
            } else {
                if (curNode.getLeft() != null) {
                    queue.add(curNode.getLeft());
                }
                if (curNode.getRight() != null) {
                    queue.add(curNode.getRight());
                }
                totalStatements++;
            }
            totalNodes++;
        }

        System.out.printf("- total number of nodes        %d%n", totalNodes);
        System.out.printf("- total number of animals      %d%n", totalAnimals);
        System.out.printf("- total number of statements   %d%n", totalStatements);
    }

    private static void printPathFacts(List<Node> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            Node curNode = path.get(i);
            boolean yes = curNode.getRight().equals(path.get(i + 1));

            String fact = convertQuestionToFact(curNode.getValue(), yes);
            System.out.println(" - " + fact);
        }
    }

    private static String convertQuestionToFact(String value, boolean yes) {
        String[] question = value.split(" ");
        question[question.length - 1] = question[question.length - 1].replaceAll("[?!.]", "");

        StringBuilder fact = new StringBuilder();
        if (question[0].equalsIgnoreCase("can")) {
            // handle Can question conversion
            if (yes) {
                // It can ...
                fact.append("It can ");
            } else {
                // It can't ...
                fact.append("It can't ");
            }
            fact.append(String.join(" ", Arrays.copyOfRange(question, 2, question.length)));
        } else if (question[0].equalsIgnoreCase("does")) {
            // handle Does question conversion
            if (yes) {
                fact.append("It has ");
                // It has ...
            } else {
                fact.append("It doesn't have ");
                // It doesn't have ...
            }
            fact.append(String.join(" ", Arrays.copyOfRange(question, 3, question.length)));
        } else {
            // handle Is question conversion
            if (yes) {
                fact.append("It is ");
                // It is ...
            } else {
                fact.append("It isn't ");
                // It isn't ...
            }
            fact.append(String.join(" ", Arrays.copyOfRange(question, 2, question.length)));
        }
        fact.append(".");

        return fact.toString();
    }

    // Recursive function to find paths from the root node to every leaf node
    private static void getPath(Node node, String animalName, List<Node> path, List<Node> result) {
        // base case
        if (node == null) {
            return;
        }

        // include the current node to the path
        path.add(node);

        // if the leaf node we are looking for is found, return
        if (node.isLeaf() && node.getValue().equalsIgnoreCase(animalName)) {
            result.addAll(path);
            return;
        }

        // recur for the left and right subtree
        getPath(node.left, animalName, path, result);
        getPath(node.right, animalName, path, result);

        // backtrack: remove the current node after the left, and right subtree are done
        path.remove(path.size() - 1);
    }

    // The main function to print paths from the root node to every leaf node
    private static List<Node> getPath(Node node, String animalName) {
        // list to store root-to-leaf path
        List<Node> path = new ArrayList<>();
        List<Node> result = new ArrayList<>();
        getPath(node, animalName, path, result);
        return result;
    }

    private static void getPaths(Node node, Deque<Node> path, List<List<Node>> questions) {
        // base case
        if (node == null) {
            return;
        }

        // include the current node to the path
        path.addLast(node);

        // if a leaf node is found, print the path
        if (node.isLeaf()) {
            List<Node> pathList = new ArrayList<>(path);
            questions.add(pathList);
        }

        // recur for the left and right subtree
        getPaths(node.left, path, questions);
        getPaths(node.right, path, questions);

        // backtrack: remove the current node after the left, and right subtree are done
        path.removeLast();
    }

    // The main function to print paths from the root node to every leaf node
    private static List<List<Node>> getPaths(Node node) {
        // list to store root-to-leaf path
        Deque<Node> path = new ArrayDeque<>();
        List<List<Node>> questions = new ArrayList<>();
        getPaths(node, path, questions);
        return questions;
    }

    private static void listAnimals(Node root) {
        if (root == null) {
            System.out.println("I don't know any animals. :(");
            return;
        }

        List<String> animalNames = new ArrayList<>();
        Deque<Node> queue = new ArrayDeque<>();
        queue.add(root);

        System.out.println("Here are the animals I know:");

        while (!queue.isEmpty()) {
            Node curNode = queue.poll();
            if (curNode.isLeaf()) {
                String animalName = curNode.getValue().split(" ")[1];
                animalNames.add(animalName);
            } else {
                if (curNode.getLeft() != null) {
                    queue.add(curNode.getLeft());
                }
                if (curNode.getRight() != null) {
                    queue.add(curNode.getRight());
                }
            }
        }

        animalNames.sort(String::compareToIgnoreCase);

        for (String animalName : animalNames) {
            System.out.printf(" - %s%n", animalName);
        }
    }

    private static int displayOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What do you want to do:");
        System.out.println();
        System.out.println("1. Play the guessing game");
        System.out.println("2. List of all animals");
        System.out.println("3. Search for an animal");
        System.out.println("4. Calculate statistics");
        System.out.println("5. Print the Knowledge Tree");
        System.out.println("0. Exit");
        return scanner.nextInt();
    }

    private static void sayFarewell() {
        System.out.println(farewells.get(getRandIndex(farewells.size())));
    }

    private static Node playRound(BinaryTree gameTree, Node currentNode) {
        if (currentNode.isLeaf()) {
            // If root is animal, ask the user if this animal is correct
            boolean animalIsCorrect = getYesNoResponse(String.format("Is it %s?", currentNode.getValue()));
            // Animals will always have null at left and right (animals are always leaves)
            // If the animal is correct, thank the player for playing and ask if they want to play again
            if (animalIsCorrect) {
                System.out.println("Nice! The correct animal has been found.\n");
            } else {
                // If animal is incorrect, ask for distinguishing question
                // Save reference to animal name (current node's value)
                String currentAnimal = currentNode.getValue();

                System.out.println("I give up. What animal do you have in mind?");
                String actualAnimal = getAnimal();
                List<String> fact = getFact(currentAnimal, actualAnimal);

                boolean isCorrectForAnimal2 = getCorrectForAnimal2(actualAnimal);
                String endOfStatement = String.join(" ", fact.subList(2, fact.size()));

                // Get opposite of second word in fact String (has/can/is) for animal1 and anima
                String verb = fact.get(1);
                String verbOpposite = "";
                String question = "";
                switch (verb) {
                    case "can": {
                        verbOpposite = "can't";
                        question = "Can it " + endOfStatement + "?";
                        break;
                    }
                    case "has": {
                        verbOpposite = "doesn't have";
                        question = "Does it have " + endOfStatement + "?";
                        break;
                    }
                    case "is": {
                        verbOpposite = "isn't";
                        question = "Is it " + endOfStatement + "?";
                        break;
                    }
                }

                // Replace node's value with distinguishing question, set node to non-animal
                // currentNode.setIsAnimal(false);
                currentNode.setValue(question);

                // Print out the 2 facts
                // Set new node with animal that is yes to question to right
                // Set new node with animal that is no to question to left
                System.out.println("I have learned the following facts about animals:");
                String animal1Name = currentAnimal.split(" ")[1];
                String animal2Name = actualAnimal.split(" ")[1];
                if (isCorrectForAnimal2) {
                    currentNode.setRight(new Node(actualAnimal));
                    currentNode.setLeft(new Node(currentAnimal));
                    System.out.printf(" - The %s %s %s.%n", animal1Name, verbOpposite, endOfStatement);
                    System.out.printf(" - The %s %s %s.%n", animal2Name, verb, endOfStatement);
                } else {
                    currentNode.setRight(new Node(currentAnimal));
                    currentNode.setLeft(new Node(actualAnimal));
                    System.out.printf(" - The %s %s %s.%n", animal1Name, verb, endOfStatement);
                    System.out.printf(" - The %s %s %s.%n", animal2Name, verbOpposite, endOfStatement);
                }

                // Print out the distinguishing question
                System.out.println("I can distinguish these animals by asking the question:");
                System.out.println(" - " + question);
                System.out.println("Nice! I've learned so much about animals!");
                System.out.println();

            }
            return gameTree.getRoot();
        } else {
            // If root is question, ask the user this question and go left/right node based on answer
            boolean animalFitsQuestion = getYesNoResponse(currentNode.getValue());
            if (animalFitsQuestion) {
                currentNode = currentNode.getRight();
            } else {
                currentNode = currentNode.getLeft();
            }
        }

        return currentNode;
    }

    private static boolean getYesNoResponse(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(question);
        String input = scanner.nextLine().toLowerCase().trim().replaceAll("[!.]$", "");
        while (!affirmatives.contains(input) && !negatives.contains(input)) {
            System.out.println("Enter in yes or no:");
            input = scanner.nextLine();
        }

        return affirmatives.contains(input);
    }

    private static void startGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("You think of an animal, and I guess it.");
        System.out.println("Press enter when you're ready.");
        scanner.nextLine();
    }

    private static boolean getCorrectForAnimal2(String animal) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Is the statement correct for %s?%n", animal);
        String input = scanner.nextLine().toLowerCase().trim().replaceAll("[!.]$", "");
        while (!affirmatives.contains(input) && !negatives.contains(input)) {
            System.out.println("Enter in yes or no:");
            input = scanner.nextLine();
        }

        return affirmatives.contains(input);
    }

    private static boolean isInvalid(List<String> fact) {
        List<String> validSecondWords = Arrays.asList("can", "has", "is");
        // Verify the string has at least three words
        if (fact.size() < 3) return true;
        // Verify the string starts with It can/has/is
        if (!fact.get(0).equalsIgnoreCase("it")) return true;
        // Verify the second word is can, has, or is
        if (!validSecondWords.contains(fact.get(1).toLowerCase())) return true;

        return false;
    }

    private static void printFactInstructions() {
        System.out.println("The sentence should satisfy one of the following templates:");
        System.out.println("- It can ...");
        System.out.println("- It has ...");
        System.out.println("- It is a/an ...");
        System.out.println();
    }

    private static List<String> getFact(String animal1, String animal2) {
        Scanner scanner = new Scanner(System.in);

        System.out.printf("Specify a fact that distinguishes %s from %s.%n", animal1, animal2);

        List<String> fact;

        do {
            printFactInstructions();
            fact = Arrays.asList(scanner.nextLine().split(" "));
        } while (isInvalid(fact));

        fact.set(fact.size() - 1, fact.get(fact.size() - 1).replaceAll("[^a-zA-Z]", ""));
        return fact;
    }

    private static int getRandIndex(int upperBound) {
        Random rand = new Random();
        return rand.nextInt(upperBound);
    }

    private static String getResponse() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String response = scanner.nextLine().trim().toLowerCase();
            char lastChar = response.charAt(response.length() - 1);
            if (lastChar == '!' || lastChar == '.') {
                response = response.substring(0, response.length() - 1);

            }

            if (affirmatives.contains(response)) {
                return "Yes";
            }
            if (negatives.contains(response)) {
                return "No";
            }
            System.out.println(clarifications.get(getRandIndex(clarifications.size())));
        }
    }

    private static String getAnimal() {
        Scanner scanner = new Scanner(System.in);
        String[] inputLine = scanner.nextLine().toLowerCase().split(" ");

        if (inputLine.length == 0) {
            return "";
        }

        if (inputLine[0].equals("the")) {
            inputLine = Arrays.copyOfRange(inputLine, 1, inputLine.length);
        }

        if (inputLine[0].equals("a") || inputLine[0].equals("an")) {
            return String.join(" ", inputLine);
        }

        String VOWELS = "aeiouAEIOU";
        if (VOWELS.indexOf(inputLine[0].charAt(0)) == -1) {
            return "a " + String.join(" ", inputLine);
        } else {
            return "an " + String.join(" ", inputLine);
        }
    }

    private static void displayGreeting() {
        LocalTime time = LocalTime.now();

        LocalTime fiveAM = LocalTime.of(5, 0, 0);
        LocalTime twelvePM = LocalTime.of(12, 0, 0);
        LocalTime sixPM = LocalTime.of(18, 0, 0);

        if (time.isBefore(fiveAM) || (time.equals(sixPM) || time.isAfter(sixPM))) {
            System.out.println("Good evening!");
        } else if (time.equals(fiveAM) || (time.isAfter(fiveAM) && time.isBefore(twelvePM))) {
            System.out.println("Good morning!");
        } else {
            System.out.println("Good afternoon!");
        }
        System.out.println();
    }
}
