# Recipe Generator
### HW 5: Implementation Project
Caroline Cummings
Ashley Tang

## Overview ##

We created a Java application that generates and filters recipes from AllRecipes based on
user-inputted dietary restrictions, ingredients, max total time for cooking, and minimum
number of servings desired. Our main idea with this project was to make help users plan
recipes, especially for those with dietary restrictions/allergies.

We developed an interface using Swing where the user can select dietary preferences
(vegetarian, vegan, keto, gluten-free, kosher, or none), filter by total cooking time and
number of servings, and input ingredients they want to use. After clicking "Generate
Recipes," the 10 top-ranking recipes will appear and the user can expand and collapse each
one to see the full details, ingredients, and directions.

We obtained our recipes by scraping the AllRecipes website:
https://www.allrecipes.com/recipes-a-z-6735880
Specifically, we used their A-Z page where it lists recipe categories from A to Z. We
followed links for each dietary category we offer to our user and store each in a separate
folder. On each recipe page, we pull the Ingredients, Directions, Nutritional Facts, and
Details (cook time, prep time, servings, etc) and populate a `.txt` for each recipe.

We then use these `.txt` files to search for the user-inputted ingredients and preferences.

## To Run ##

The GUI can be run using the `RecipeInterface.java` file by clicking the green triangle run
button in IntelliJ.

You can also run the main method in `Main.java` to see the same functionality in the console.

If you would like to see our webscraper run, you can delete the `recipes` folder and rerun the
main method in `Main.java`. This will take around 10 minutes to scrape all the recipes we
originally had.

## Features ##

* Dietary Filtering: Select from vegan, vegetarian, gluten-free, keto, kosher, or no
restrictions.

* Ingredient Matching: Enter a list of comma-separated ingredients you want to use.

* Time + Servings Filtering: Set an upper limit on total time and minimum number of servings,
both are optional.

* Recipe Ranking: Finds and ranks recipes by how well they match your inputs. Uses TF-IDF
vector concept and cosine similarities. The vectors used are <1,...,1,1,....,1,0,...,0> and
<1,...,1,0,...,0,1,...,1> where the size of the vectors is equal to the total number of
ingredients in both recipes. Using the rule of cosine similarities, this can be simplified to
(number of matching)/(sqrt(matching + number of unused) + sqrt(number of matching + number of missing)).
The closer this value is to 1, the more similar the recipe is to the user's query.

* Expandable Results: Each result can be expanded/collapsed to view the full recipe, directions,
nutrition facts, and missing ingredients.

  We used Physical Networks (Internet) and Document Search (aka Information Retrieval).

---

## How It Works ##

The program scrapes recipes and saves them as `.txt` files into folders by diet.

When the user applies filters, the backend ranks recipes using ingredient matching and
time/serving constraints.

The frontend (Java Swing) displays the top results, letting users explore each one
interactively.

---

## Who Did What ##

Caroline Cummings implemented the recipe scraping functionality and did the initial
scraping of recipes and stored them locally in the `recipes` folder. She also developed
and desgined the GUI which calls the backend and formats the matched recipes.

Ashley Tang developed the backend of matching recipes to ingredients using cosine
similarity. She also helped scrape the recipe details so that we could also constrain the
search by serving size and total time to make and created the user experience in the console.

Both worked on the Instruction Manual.