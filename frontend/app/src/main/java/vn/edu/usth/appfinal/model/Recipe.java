package vn.edu.usth.appfinal.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Recipe implements Serializable {
    private long id;
    private String title;
    private String description;
    private String imageUrl;
    private int prepTime;
    private int cookTime;
    private int servings;
    private int calories;
    private double proteinG;
    private double carbsG;
    private double fatG;
    private boolean isFavorite;
    private Long userId;
    private String authorName;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Instruction> instructions = new ArrayList<>();

    public Recipe() {
        this.servings = 1;
    }

    public Recipe(long id, String title, int cookTime, int calories) {
        this();
        this.id = id;
        this.title = title;
        this.cookTime = cookTime;
        this.calories = calories;
    }

    public static Recipe fromJson(JSONObject json) {
        if (json == null) return null;
        Recipe recipe = new Recipe();
        recipe.setId(json.optLong("id", 0));
        recipe.setTitle(json.optString("title", ""));

        String desc = json.isNull("description") ? "" : json.optString("description", "");
        if ("null".equalsIgnoreCase(desc)) desc = "";
        recipe.setDescription(desc);

        String img = json.isNull("imageUrl") ? "" : json.optString("imageUrl", "");
        if ("null".equalsIgnoreCase(img)) img = "";
        recipe.setImageUrl(img);

        recipe.setPrepTime(json.optInt("prepTime", 0));
        recipe.setCookTime(json.optInt("cookTime", 0));
        recipe.setServings(json.optInt("servings", 1));
        recipe.setCalories(json.optInt("calories", 0));
        recipe.setProteinG(json.optDouble("proteinG", 0.0));
        recipe.setCarbsG(json.optDouble("carbsG", 0.0));
        recipe.setFatG(json.optDouble("fatG", 0.0));
        recipe.setFavorite(json.optBoolean("favorite", false));

        String author = json.isNull("authorName") ? "" : json.optString("authorName", "");
        if ("null".equalsIgnoreCase(author)) author = "";
        recipe.setAuthorName(author);

        if (json.has("userId") && !json.isNull("userId")) {
            recipe.setUserId(json.optLong("userId"));
        } else {
            recipe.setUserId(null);
        }

        // Ingredients
        JSONArray ingArray = json.optJSONArray("ingredients");
        if (ingArray != null) {
            for (int i = 0; i < ingArray.length(); i++) {
                JSONObject ingObj = ingArray.optJSONObject(i);
                if (ingObj != null) {
                    recipe.getIngredients().add(Ingredient.fromJson(ingObj));
                }
            }
        }

        // Instructions (Backend RecipeDetailDto uses "instruction" singular, but can also be "instructions")
        JSONArray instArray = json.optJSONArray("instruction");
        if (instArray == null) {
            instArray = json.optJSONArray("instructions");
        }
        if (instArray != null) {
            for (int i = 0; i < instArray.length(); i++) {
                JSONObject instObj = instArray.optJSONObject(i);
                if (instObj != null) {
                    recipe.getInstructions().add(Instruction.fromJson(instObj));
                }
            }
        }

        return recipe;
    }

    public JSONObject toRequestJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("title", title != null ? title.trim() : "");
            json.put("description", description != null ? description.trim() : "");
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                json.put("imageUrl", imageUrl.trim());
            } else {
                json.put("imageUrl", JSONObject.NULL);
            }
            json.put("prepTime", prepTime);
            json.put("cookTime", cookTime);
            json.put("servings", servings > 0 ? servings : 1);
            json.put("calories", calories);
            json.put("proteinG", proteinG);
            json.put("carbsG", carbsG);
            json.put("fatG", fatG);

            JSONArray ingArray = new JSONArray();
            if (ingredients != null) {
                for (Ingredient ing : ingredients) {
                    if (ing != null && ing.getName() != null && !ing.getName().trim().isEmpty()) {
                        ingArray.put(ing.toJson());
                    }
                }
            }
            json.put("ingredients", ingArray);

            JSONArray instArray = new JSONArray();
            if (instructions != null) {
                for (int i = 0; i < instructions.size(); i++) {
                    Instruction inst = instructions.get(i);
                    if (inst != null && inst.getDescription() != null && !inst.getDescription().trim().isEmpty()) {
                        JSONObject obj = new JSONObject();
                        obj.put("stepNumber", inst.getStepNumber() > 0 ? inst.getStepNumber() : (i + 1));
                        obj.put("description", inst.getDescription().trim());
                        instArray.put(obj);
                    }
                }
            }
            json.put("instruction", instArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
