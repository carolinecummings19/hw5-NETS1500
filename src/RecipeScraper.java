import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeScraper {
    /*
     * Fetches and returns the Document for a given URL
     */
    public static Document fetchPage(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Failed to fetch page: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to scrape vegan recipes
     */
    public static void getVeganRecipes() {
        String aToZUrl = "https://www.allrecipes.com/recipes-a-z-6735880#alphabetical-list-v";

        // Find the Vegan category link
        Document aToZPage = fetchPage(aToZUrl);
        if (aToZPage == null) return;

        Element veganLink = aToZPage.selectFirst("a:contains(Vegan)");
        if (veganLink == null) {
            System.out.println("Could not find Vegan category link.");
            return;
        }
        String veganUrl = veganLink.absUrl("href");
        System.out.println("Vegan category page: " + veganUrl);

        // Go into each vegan subcategory
        Document veganCategoryPage = fetchPage(veganUrl);
        if (veganCategoryPage == null) return;

        Elements subcategoryLinks = veganCategoryPage.select("div#mntl-taxonomy-nodes_1-0 ul#mntl-taxonomy-nodes__list_1-0 a");
        for (Element subLink : subcategoryLinks) {
            String subUrl = subLink.absUrl("href");
            System.out.println("Subcategory page: " + subUrl);
            processSubcategory(subUrl, "vegan");
        }
    }

    /**
     * Method to scrape vegetarian recipes
     */
    public static void getVegetarianRecipes() {
        String aToZUrl = "https://www.allrecipes.com/recipes-a-z-6735880#alphabetical-list-v";

        // Find the Vegetarian category link
        Document aToZPage = fetchPage(aToZUrl);
        if (aToZPage == null) return;

        Element vegetarianLink = aToZPage.selectFirst("a:contains(Vegetarian)");
        if (vegetarianLink == null) {
            System.out.println("Could not find Vegetarian category link.");
            return;
        }
        String vegetarianUrl = vegetarianLink.absUrl("href");
        System.out.println("Vegetarian category page: " + vegetarianUrl);

        // Go into each vegetarian subcategory
        Document vegetarianCategoryPage = fetchPage(vegetarianUrl);
        if (vegetarianCategoryPage == null) return;

        Elements subcategoryLinks = vegetarianCategoryPage.select("div#mntl-taxonomy-nodes_1-0 ul#mntl-taxonomy-nodes__list_1-0 a");
        for (Element subLink : subcategoryLinks) {
            String subUrl = subLink.absUrl("href");
            System.out.println("Subcategory page: " + subUrl);
            processSubcategory(subUrl, "vegetarian");
        }
    }

    /**
     * Method to scrape en free recipes
     */
    public static void getGlutenFreeRecipes() {
        String aToZUrl = "https://www.allrecipes.com/recipes-a-z-6735880#alphabetical-list-v";

        // Find the gluten free category link
        Document aToZPage = fetchPage(aToZUrl);
        if (aToZPage == null) return;

        Element glutenFreeLink = aToZPage.selectFirst("a:contains(Gluten-Free)");
        if (glutenFreeLink == null) {
            System.out.println("Could not find Gluten-Free category link.");
            return;
        }
        String glutenFreeUrl = glutenFreeLink.absUrl("href");
        System.out.println("Vegetarian category page: " + glutenFreeUrl);

        // Go into each gluten free subcategory
        Document glutenFreeCategoryPage = fetchPage(glutenFreeUrl);
        if (glutenFreeCategoryPage == null) return;

        Elements subcategoryLinks = glutenFreeCategoryPage.select("div#mntl-taxonomy-nodes_1-0 ul#mntl-taxonomy-nodes__list_1-0 a");
        for (Element subLink : subcategoryLinks) {
            String subUrl = subLink.absUrl("href");
            System.out.println("Subcategory page: " + subUrl);
            processSubcategory(subUrl, "glutenfree");
        }
    }

    /**
     * Method to scrape keto recipes
     */
    public static void getKetoRecipes() {
        String aToZUrl = "https://www.allrecipes.com/recipes-a-z-6735880#alphabetical-list-v";

        // Find the keto category link
        Document aToZPage = fetchPage(aToZUrl);
        if (aToZPage == null) return;

        Element ketoLink = aToZPage.selectFirst("a:contains(Keto)");
        if (ketoLink == null) {
            System.out.println("Could not find Keto category link.");
            return;
        }
        String ketoUrl = ketoLink.absUrl("href");
        System.out.println("Keto category page: " + ketoUrl);
        processSubcategory(ketoUrl, "keto");
    }

    /**
     * Method to scrape Kosher recipes
     */
    public static void getKosherRecipes() {
        String aToZUrl = "https://www.allrecipes.com/recipes-a-z-6735880#alphabetical-list-v";

        // Find the Kosher category link
        Document aToZPage = fetchPage(aToZUrl);
        if (aToZPage == null) return;

        Element kosherLink = aToZPage.selectFirst("a:contains(Kosher)");
        if (kosherLink == null) {
            System.out.println("Could not find Kosher category link.");
            return;
        }
        String kosherUrl = kosherLink.absUrl("href");
        System.out.println("Kosher category page: " + kosherUrl);

        // Go into each Kosher subcategory
        Document kosherCategoryPage = fetchPage(kosherUrl);
        if (kosherCategoryPage == null) return;

        Elements subcategoryLinks = kosherCategoryPage.select("div#mntl-taxonomy-nodes_1-0 ul#mntl-taxonomy-nodes__list_1-0 a");
        for (Element subLink : subcategoryLinks) {
            String subUrl = subLink.absUrl("href");
            System.out.println("Subcategory page: " + subUrl);
            processSubcategory(subUrl, "kosher");
        }
    }

    /**
     * Find ingredients that match the requirements
     * @param ingredientList
     * @param diet
     */
    public static HashMap<RecipeRanking, Double> rankRecipesByIngredients(List<String> ingredientList, String diet){
        HashMap<RecipeRanking, Double> rankedRecipes = new HashMap<>();
        String parentDir = "/Users/ashleytang/Documents/NETS 1500/hw5-NETS1500/recipes";
        File[] recipesForDiet;

        if(diet != null) {
            //open the folder matching the diet
            File folder = new File(parentDir, diet);
            recipesForDiet = folder.listFiles();
        } else {
            File folder = new File(parentDir);
            File[] subfolders = folder.listFiles();
            List<File> allFiles = new ArrayList<>();
            for(File subfolder: subfolders){
                allFiles.addAll(Arrays.asList(subfolder.listFiles()));
            }
            recipesForDiet = allFiles.toArray(new File[0]);
        }

        //for each recipe, rank the recipes in terms of how many ingredients match from the list
        for(File recipe: recipesForDiet){

            //keep a count for the number of matching ingredients
            //int numMatchingIngredients = 0;
            //List<String> matchingIngredients = new ArrayList<>();
            List<String> ingredientsInRecipe = getIngredients(recipe);

            //List<String> ingredientsFound = ingredientsUnused.stream().distinct().filter(ingredientList::contains).collect(Collectors.toList());

            //find all ingredients that the user lists and the recipe uses
            List<String> ingredientsMatching = ingredientList.stream()
                    .filter(full -> ingredientsInRecipe.stream().anyMatch(containing -> containing.contains(full)))
                    .collect(Collectors.toList());

            //System.out.println(ingredientsMatching);

            //find all ingredients that the user is missing
            List<String> ingredientsMissing = ingredientsInRecipe.stream()
                    .filter(item -> ingredientList.stream().noneMatch(item::contains))
                    .collect(Collectors.toList());

            //find all ingredients that the user lists but the recipe does not use
            List<String> ingredientsUnused = ingredientList.stream()
                    .filter(full -> ingredientsInRecipe.stream().noneMatch(containing -> containing.contains(full)))
                    .collect(Collectors.toList());

            RecipeRanking recipeRank = new RecipeRanking(recipe.getName(), ingredientsMatching, ingredientsMissing, ingredientsUnused);
            rankedRecipes.put(recipeRank, recipeRank.calculateScore());
        }

        return rankedRecipes;
    }

    public static List<String> getIngredients(File file){
        List<String> ingredients = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            for(int i = 0; i < reader.read(); i++)
            while(line != null && !line.contains("Directions")){
                if(!line.contains("Ingredients") && !line.equals("")){
                    ingredients.add(line);
                }
                line = reader.readLine();
            }
            //System.out.println(ingredients);
            reader.close();
        } catch (IOException e){
            System.out.println("Error reading file: " + e);
        }
        return ingredients;
    }

    /**
     * Find the recipes from the cards on the page.
     * @param subcategoryUrl
     * @param diet
     */
    private static void processSubcategory(String subcategoryUrl, String diet) {
        Document subPage = fetchPage(subcategoryUrl);
        if (subPage == null) return;

        Elements recipeLinks = subPage.select("a[id^=mntl-card-list-items_]");
        for (Element recipeLink : recipeLinks) {
            String recipeUrl = recipeLink.absUrl("href");
            System.out.println("Found recipe: " + recipeUrl);

            scrapeAndSaveRecipe(recipeUrl, diet);
        }
    }

    /**
     * Scrapes a recipe and saves it as a .txt file
     * @param recipeUrl
     * @param diet
     */
    private static void scrapeAndSaveRecipe(String recipeUrl, String diet) {
        try {
            Document recipePage = fetchPage(recipeUrl);
            if (recipePage == null) return;

            // Extract the title
            String title = recipePage.title().replaceAll("[\\\\/:*?\"<>|]", "_");
            String folderPath = "recipes/" + diet;
            File folder = new File(folderPath);
            if (!folder.exists()) folder.mkdirs();

            StringBuilder recipeText = new StringBuilder();

            // Ingredients
            recipeText.append("Ingredients:\n");
            Elements ingredients = recipePage.select("li[class='mm-recipes-structured-ingredients__list-item']");
            if (ingredients.isEmpty()) {
                // fallback if the selector fails
                ingredients = recipePage.select("ul[class='mm-recipes-structured-ingredients__list'] li");
                if (ingredients.size() == 0) {
                    return;
                }
            }

            File file = new File(folderPath + "/" + title + ".txt");
            for (Element ing : ingredients) {
                recipeText.append("- ").append(ing.text()).append("\n");
            }
            recipeText.append("\n");

            // Directions
            recipeText.append("Directions:\n");
            Elements directions = recipePage.select("div[id^='mm-recipes-steps__content_'] li");
            if (directions.size() != 0) {
                for (Element step : directions) {
                    if (step.hasText()) {
                        recipeText.append("- ").append(step.text()).append("\n");
                    }
                }
            } else {
                recipeText.append("Error getting directions.\n");
            }
            recipeText.append("\n");

            // Nutrition Facts
            recipeText.append("Nutrition Facts:\n");
            Elements nutrition = recipePage.select("table[class^='mm-recipes-nutrition-facts-summary__table'] tr td");
            if (nutrition != null) {
                for (Element fact : nutrition) {
                    if (fact.hasText()) {
                        recipeText.append(fact.text()).append(" ");
                    }
                }
            } else {
                recipeText.append("Not available\n");
            }

            //Recipe details -- prep time, cook time, and servings
            recipeText.append("\n\nRecipe Details:\n");
            Elements recipeDetails = recipePage.select("div[class=mm-recipes-details__item]");

            if (recipeDetails != null) {
                for (Element detail : recipeDetails) {
                    if (detail.hasText()) {
                        recipeText.append(detail.text()).append("\n");
                    }
                }
            } else {
                recipeText.append("Not available\n");
            }


            try (FileWriter writer = new FileWriter(file)) {
                writer.write(recipeText.toString());
                System.out.println("Saved recipe: " + file.getPath());
            }

        } catch (IOException e) {
            System.out.println("Failed to save recipe: " + recipeUrl + " Error: " + e.getMessage());
        }
    }
}

