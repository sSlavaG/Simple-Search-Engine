package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    static Map<String, ArrayList<Integer>> wordMap = new HashMap<>();
    static ArrayList<String> arrayOfLines = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {

        String filePath = args[1];

        File textFile = new File(filePath);
        Scanner scanner = new Scanner(textFile);

        while (scanner.hasNext()) {
            String lineToAdd = scanner.nextLine();
            arrayOfLines.add(lineToAdd);
        }

        createMap();

        displayUserMenu();
        scanner.close();
        textFile.delete();

    }

    public static void createMap() {
        ArrayList<Integer> mapList;

        for (int i = 0; i < arrayOfLines.size(); i++) {
            String[] wordArray = arrayOfLines.get(i).split(" ");

            for (String word : wordArray) {
                mapList = new ArrayList<>();

                if (!wordMap.containsKey(word.toLowerCase())) {
                    mapList.add(i);
                    wordMap.put(word.toLowerCase(), mapList);
                } else {
                    mapList = wordMap.get(word.toLowerCase());
                    mapList.add(i);
                    wordMap.put(word.toLowerCase(), mapList);
                }
            }
        }

    }

    public static void displayUserMenu() {
        System.out.println("=== Menu ===");
        System.out.println("1. Find a person \n2. Print all people \n0. Exit");

        Scanner scanner = new Scanner(System.in);
        String menuChoice = scanner.nextLine();


        while (!menuChoice.equals("0")) {
            switch(menuChoice) {
                case "1":
                    System.out.println("Select a matching strategy: ALL, ANY, NONE");
                    String strategyMethod = scanner.nextLine();
                    StrategyImplementor implementor = new StrategyImplementor();

                    if (strategyMethod.equals("ALL")) {
                        implementor.setMethod(new StrategyALL());
                        implementor.implementStrategy();

                    } else if (strategyMethod.equals("ANY")) {
                        implementor.setMethod(new StrategyANY());
                        implementor.implementStrategy();

                    } else if (strategyMethod.equals("NONE")) {
                        implementor.setMethod(new StrategyNONE());
                        implementor.implementStrategy();
                    }
                    break;

                case "2":
                    printAllPeople();
                    break;

                default:
                    System.out.println("Incorrect option. Try Again!");
                }

                System.out.println("=== Menu ===");
                System.out.println("1. Find a person \n2. Print all people \n0. Exit");
                menuChoice = scanner.nextLine();
            }
        }

    public static void printAllPeople() {
        System.out.println("=== List of people ===");
        for (String i : arrayOfLines) {
            System.out.println(i);
        }
    }
}

interface StrategyInterface {
    void findPerson();
}

class StrategyALL implements StrategyInterface {
    @Override
    public void findPerson() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter data to search: ");
        String query = scanner.nextLine().toLowerCase();
        String[] queryList = query.split(" ");

        Set<Integer> intersectionSet = new HashSet<>(Main.wordMap.getOrDefault(queryList[0], new ArrayList<>()));

        for (int i = 0; i < queryList.length; i++) {
            intersectionSet.retainAll(new HashSet<>(Main.wordMap.getOrDefault(queryList[i], new ArrayList<>())));
        }

        System.out.println(intersectionSet.size() + " matching query is found: ");

        for (Integer index : intersectionSet) {
            System.out.println(Main.arrayOfLines.get(index));
        }

    }
}

class StrategyANY implements StrategyInterface {

    @Override
    public void findPerson() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter data to search: ");
        String query = scanner.nextLine().toLowerCase();
        String[] queryList = query.split(" ");

        Set<Integer> unionSet = new HashSet<>(Main.wordMap.getOrDefault(queryList[0], new ArrayList<>()));

        for (int i = 0; i < queryList.length; i++) {
            unionSet.addAll(new HashSet<>(Main.wordMap.getOrDefault(queryList[i], new ArrayList<>())));
        }

        System.out.println(unionSet.size() + " matching query is found: ");

        for (Integer index : unionSet) {
            System.out.println(Main.arrayOfLines.get(index));
        }

    }
}

class StrategyNONE implements StrategyInterface {
    @Override
    public void findPerson() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter data to search: ");
        String query = scanner.nextLine().toLowerCase();
        String[] queryList = query.split(" ");

        Set<Integer> unionSet = new HashSet<>(Main.wordMap.getOrDefault(queryList[0], new ArrayList<>()));

        for (int i = 0; i < queryList.length; i++) {
            unionSet.addAll(new HashSet<>(Main.wordMap.getOrDefault(queryList[i], new ArrayList<>())));
        }

        System.out.println(unionSet.size() + " matching query is found: ");

        for (int i = 0; i < Main.arrayOfLines.size(); i++) {
            if (!unionSet.contains(i)) {
                System.out.println(Main.arrayOfLines.get(i));
            }

        }

    }

}

class StrategyImplementor {
    private StrategyInterface queryMethod;

    public void setMethod(StrategyInterface queryMethod) {
        this.queryMethod = queryMethod;
    }

    public void implementStrategy() {
        this.queryMethod.findPerson();
    }
}