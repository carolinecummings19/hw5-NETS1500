# Recipe Generator
### HW 5: Implementation Project
Caroline Cummings 
Ashley Tang

## Overview

We created a Java application that generates and filters recipes from AllRecipes based on user-inputted dietary restrictions, ingredients, max total time for cooking, and minimum number of servings desired. Our main idea with this project was to make help users plan recipes, especially for those with dietary restrictions/allergies.

We developed an interface using Swing where the user can select dietary preferences (vegetarian, vegan, keto, gluten-free, kosher, or none), filter by total cooking time and number of servings, and input ingredients they want to use. After clicking "Generate Recipes," the 10 top-ranking recipes will appear and the user can expand and collapse each one to see the full details, ingredients, and directions. 

We obtained our recipes by scraping the [AllRecipes website](https://www.allrecipes.com/recipes-a-z-6735880), specifically their A-Z page where it lists recipe categories from A to Z. We followed links for each dietary category we offer to our user and store each in a separate folder. On each recipe page, we pull the Ingredients, Directions, Nutritional Facts, and Details (cook time, prep time, servings, etc) and populate a `.txt` for each recipe. 

We then use these `.txt` files to search for the user-inputted ingredients and preferences. 

## To Run

The GUI can be run using the `RecipeInterface.java` file by clicking the green triangle run button in IntelliJ. 

You can also run the main method in `Main.java` to see the same functionality in the console. 

If you would like to see our webscraper run, you can delete the `recipes` folder and rerun the main method in `Main.java`. This will take around 10 minutes to scrape all the recipes we originally had. 

## Project Structure

```
/recipes/vegan
/recipes/vegetarian
/recipes/glutenfree
/recipes/keto
/recipes/kosher
src/
 ├── RecipeInterface.java   // Main GUI interface
 ├── RecipeScraper.java     // Handles scraping and recipe filtering
 ├── RecipeRanking.java     // Represents ranked recipe details
 └── Main.java             // Running the same functionality in the console
```

---

## Features

* **Dietary Filtering**
  Select from vegan, vegetarian, gluten-free, keto, kosher, or no restrictions.

* **Ingredient Matching**
  Enter a list of comma-separated ingredients you want to use.

* **Time + Servings Filtering**
  Set an upper limit on total time and minimum number of servings.

* **Recipe Ranking**
  Finds and ranks recipes by how well they match your inputs.

* **Expandable Results**
  Each result can be expanded/collapsed to view the full recipe, directions, nutrition facts, and missing ingredients.

---

## How It Works

The program scrapes recipes and saves them as `.txt` files into folders by diet.

When the user applies filters, the backend ranks recipes using ingredient matching and time/serving constraints.

The frontend (Java Swing) displays the top results, letting users explore each one interactively.

---

## Example `.txt` Recipe Format

```
Ingredients:
- 1 cup flour
- 2 eggs
- ...

Directions:
- Preheat oven...
- Mix ingredients...

Nutrition Facts:
250 Calories 10g Fat ...

Recipe Details:
Prep Time: 10 mins
Cook Time: 30 mins
Total Time: 40 mins
Servings: 4
```
---

## Future Plans

In future iterations of this project, we would like to pull more recipes and have more options for the user. Additionally, we could also add the options to list ingredients to avoid (like potential allergens) and only output recipes without those ingredients. 
