import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Call scraper(s)
        String folderPath = "recipes";
        File folder = new File(folderPath);

        String[] subfolderPaths = {"recipes/vegan","recipes/vegetarian","recipes/glutenfree","recipes/keto","recipes/kosher"};

        //if (!folder.exists() || folder.listFiles() == null || folder.listFiles().length == 0) {
              //  System.out.println("Recipes folder is empty or does not exist. Starting scrape...");

            for(String subfolderPath : subfolderPaths){
                File subfolder = new File("recipes/" + subfolderPath);

                if(!subfolder.exists() || subfolder.listFiles() == null || subfolder.listFiles().length == 0) {
                    if (subfolderPath.contains("vegan")) {
                        RecipeScraper.getVeganRecipes();
                    }
                    if (subfolderPath.contains("vegetarian")) {
                        RecipeScraper.getVegetarianRecipes();
                    }
                    if (subfolderPath.contains("glutenfree")) {
                        RecipeScraper.getGlutenFreeRecipes();
                    }
                    if (subfolderPath.contains("keto")) {
                        RecipeScraper.getKetoRecipes();
                    }
                    if (subfolderPath.contains("kosher")) {
                        RecipeScraper.getKosherRecipes();
                    }
                } else{
                    System.out.println(subfolderPath + " folder exists, skipping scrape");
                }
            }
        //} else {
          //System.out.println("Recipes folder already populated. Skipping scrape.");
        //}

        // Create a Scanner to enable interactivity
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello!");
        String dietChosen = null;

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
                    dietChosen = "vegetarian";
                    break;
                case 2:
                    System.out.println("You selected Gluten-Free.");
                    dietChosen = "glutenfree";
                    break;
                case 3:
                    System.out.println("You selected Keto.");
                    dietChosen = "keto";
                    break;
                case 4:
                    System.out.println("You selected Vegan.");
                    dietChosen = "vegan";
                    break;
                case 5:
                    System.out.println("You selected Kosher.");
                    dietChosen = "kosher";
                    break;
                default:
                    System.out.println("Invalid selection. Skipping dietary restrictions.");
            }
        }
        System.out.println("Would you like to filter by recipe details such as prep time, total time, and servings? (y/n)");
        answer = scanner.nextLine().toLowerCase();
        String[] requirements = null;
        if(answer.equals("yes") || answer.equals("y")){
            System.out.println("What would you like to filter? Please separate all with a comma\n    For example: Total Time < 30 mins,Servings > 4");
            answer = scanner.nextLine();
            requirements = answer.split(",");
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

        List<File> recipesWithRequirements = null;
        if (requirements != null) {
            System.out.println("And that match the criteria: ");
            for(String requirement: requirements){
                System.out.println(" - " + requirement);
            }
            recipesWithRequirements = RecipeScraper.filterRecipeDetails(Arrays.asList(requirements), dietChosen);
        }

        HashMap<RecipeRanking, Double> rankedRecipes = RecipeScraper.rankRecipesByIngredients(Arrays.asList(ingredientList), dietChosen, recipesWithRequirements);


        List<Map.Entry<RecipeRanking, Double>> sorted = new ArrayList<>(rankedRecipes.entrySet());
        sorted.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));


        // Print the top 10 results
        int count = 0;
        for (Map.Entry<RecipeRanking, Double> entry : sorted) {
            if (count++ >= 10) break;
            System.out.println("\n" + count + ". " + entry.getKey().getName() + " -> " + entry.getValue());
            for(String recipeDetail: entry.getKey().getRecipeDetails().keySet()){
                System.out.println(" - " + recipeDetail + ": " + entry.getKey().getRecipeDetails().get(recipeDetail)[0] + " " + entry.getKey().getRecipeDetails().get(recipeDetail)[1]);
            }
            String fullRecipeText = entry.getKey().getRecipe();
            System.out.println(fullRecipeText);
            System.out.println("Ingredients missing: ");
            for(String ingredient: entry.getKey().getIngredientsMissing()){
                System.out.println(ingredient);
            }
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

    /**
     * Splits user input to get the ingredients.
     * @param answer
     * @return
     */
    public static String[] splitIntoParts(String answer){
        if (answer.length() == 0) return null;
        String[] answerComponents = answer.split(",|;|and");

        for(int i = 0; i < answerComponents.length; i++){
            if(answerComponents[i].charAt(0) == ' '){
                answerComponents[i] = answerComponents[i].substring(1);
            }
            if(answerComponents[i].charAt(answerComponents[i].length() -1) == ' '){
                answerComponents[i] = answerComponents[i].substring(0,answerComponents[i].length()-1);
            }
        }
        return answerComponents;
    }
}
