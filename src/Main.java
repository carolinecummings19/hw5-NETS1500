import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Call scraper(s)
        String folderPath = "recipes/keto";
        File folder = new File(folderPath);

        if (!folder.exists() || folder.listFiles() == null || folder.listFiles().length == 0) {
            System.out.println("Recipes folder is empty or does not exist. Starting scrape...");
            RecipeScraper.getVeganRecipes();
            RecipeScraper.getVegetarianRecipes();
            RecipeScraper.getGlutenFreeRecipes();
            RecipeScraper.getKetoRecipes();
            RecipeScraper.getKosherRecipes();
        } else {
            System.out.println("Recipes folder already populated. Skipping scrape.");
        }

        // Create a Scanner to enable interactivity
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello!");

        //asking the user if they have dietary restrictions
        System.out.println("Do you have any dietary restrictions? (y/n)");
        String answer = scanner.nextLine().toLowerCase();

        //if the user has dietary restrictions, ask them to specify
        if(answer.equals("y") || answer.equals("yes")){
            System.out.println("Please specify: ");
            System.out.println("1. Vegetarian");
            System.out.println("2. Gluten-Free");
            System.out.println("3. Keto");
            System.out.println("4. Vegan");
            System.out.println("5. Kosher");
            System.out.print("Your choice: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Skipping dietary restrictions.");
            }

            switch (choice) {
                case 1:
                    System.out.println("You selected Vegetarian.");
                    break;
                case 2:
                    System.out.println("You selected Gluten-Free.");
                    break;
                case 3:
                    System.out.println("You selected Keto.");
                    break;
                case 4:
                    System.out.println("You selected Vegan.");
                    break;
                case 5:
                    System.out.println("You selected Kosher.");
                    break;
                default:
                    System.out.println("Invalid selection. Skipping dietary restrictions.");
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
//            System.out.println(answerComponents[i]);
        }
        return answerComponents;
    }
}