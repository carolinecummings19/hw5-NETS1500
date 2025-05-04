import java.util.List;
import java.util.Map;

public class RecipeRanking {
    private String name;
    private List<String> ingredientsMatching;
    private List<String> ingredientsMissing;
    private List<String> ingredientsUnused;

    public RecipeRanking(String name, List<String> ingredientsMatching, List<String> ingredientsMissing, List<String> ingredientsUnused){
        this.name = name;
        this.ingredientsMatching = ingredientsMatching;
        this.ingredientsMissing = ingredientsMissing;
        this.ingredientsUnused = ingredientsUnused;
    }

    public String getName(){
        return name;
    }

    public List<String> getIngredientsMatching(){
        return ingredientsMatching;
    }

    public List<String> getIngredientsMissing(){
        return ingredientsMissing;
    }

    public List<String> getIngredientsUnused(){
        return ingredientsUnused;
    }

    public int getNumMatching(){
        return ingredientsMatching.size();
    }

    public int getNumMissing(){
        return ingredientsMissing.size();
    }

    public int getNumUnused(){
        return ingredientsUnused.size();
    }

    public Double calculateScore(){
        //calculating the score based on the ratio of number of matching, missing, and unused ingredients
        Double numMatching = Double.valueOf(ingredientsMatching.size());
        int numMissing = ingredientsMissing.size();
        int numUnused = ingredientsUnused.size();
        //used cosing similarities rule -- translated to this setting it would be: numMatching/sqrt(numMatching + numUnused)*sqrt(numMatching = numMissing)
        return numMatching/(Math.sqrt(numMatching + numMissing)* Math.sqrt(numMatching + numUnused));
        //return numMatching;
    }
}
