import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Create a Scanner to enable interactivity
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello!");

        //list to keep track of the foods that the user would like to avoid
        List<String> foodsToAvoid = new ArrayList<>();

        //map of diets with the ingredients to avoid
        Map<String, List<String>> diets = new HashMap<>();
        diets.put("vegan", Arrays.asList("meat", "fish", "eggs", "milk", "animal derived ingredients"));
        diets.put("gluten free", Arrays.asList("wheat", "barley", "rye", "triticale", "pasta"));

        //asking the user if they have dietary restrictions
        System.out.println("Do you have any dietary restrictions? (y/n)");
        String answer = scanner.nextLine().toLowerCase();

        //if the user has dietary restrictions, ask them to specify
        if(answer.equals("y") || answer.equals("yes")){
            System.out.println("Please specify");
            answer = scanner.nextLine().toLowerCase();
            String[] answerComponents = splitIntoParts(answer);

            for(String part: answerComponents) {
                while (!diets.containsKey(part) && part != null) {
                    part = doNotUnderstand(part, scanner);
                }

                if (part != null) {
                    System.out.println("You said " + part + " so we are assuming you would like to avoid: ");
                    foodsToAvoid.addAll(diets.get(part));
                    printList(foodsToAvoid);
                }
            }

            System.out.println("Would you like to add any other foods to the list? (y/n)");
            answer = scanner.nextLine();
            while(answer.equals("y") || answer.equals("yes")){
                System.out.println("What other food would you like to avoid?");
                answer = scanner.nextLine();
                foodsToAvoid.add(answer);
                printList(foodsToAvoid);
                System.out.println("Would you like to add any other foods to the list? (y/n)");
                answer = scanner.nextLine();
            }


        }

        System.out.println("What ingredients would you like to use?\nPlease separate each ingredient with a comma");
        String ingredients = scanner.nextLine();
        String[] ingredientList = splitIntoParts(ingredients);
        while(ingredientList == null){
            System.out.println("Error: no ingredients found, please put in your desired ingredients and separate each with a comma");
            ingredients = scanner.nextLine();
            ingredientList = splitIntoParts(ingredients);
        }

        System.out.println("Great, finding recipes with: ");
        for(String ingredient: ingredientList){
            System.out.println(" - " + ingredient);
        }


    }

    public static void printList(List<String> list){
        for(String string: list){
            System.out.println(" - " + string);
        }
    }

    public static String doNotUnderstand(String answer, Scanner scanner){
        String newAnswer = null;
        System.out.println("Sorry, we do not understand what " + answer + " means. Would you like to try again or list the ingredients to avoid?");
        answer = scanner.nextLine().toLowerCase();
        if(answer.contains("try again")){
            System.out.println("Please type your dietary restrictions");
            newAnswer = scanner.nextLine();

        }
        return newAnswer;

    }

    public static String[] splitIntoParts(String answer){
        String[] answerComponents = answer.split(",|;|and");

        for(int i = 0; i < answerComponents.length; i++){
            if(answerComponents[i].charAt(0) == ' '){
                answerComponents[i] = answerComponents[i].substring(1);
            }
            if(answerComponents[i].charAt(answerComponents[i].length() -1) == ' '){
                answerComponents[i] = answerComponents[i].substring(0,answerComponents[i].length()-1);
            }
            System.out.println(answerComponents[i]);
        }
        return answerComponents;
    }
}