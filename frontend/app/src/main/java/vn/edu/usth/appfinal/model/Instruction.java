package vn.edu.usth.appfinal.model;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Instruction implements Serializable {
    private int stepNumber;
    private String description;

    public Instruction() {}

    public Instruction(int stepNumber, String description) {
        this.stepNumber = stepNumber;
        this.description = description;
    }

    public static Instruction fromJson(JSONObject json) {
        if (json == null) return null;
        Instruction item = new Instruction();
        item.setStepNumber(json.optInt("stepNumber", 1));
        String desc = json.isNull("description") ? "" : json.optString("description", "");
        if ("null".equalsIgnoreCase(desc)) desc = "";
        item.setDescription(desc);
        return item;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("stepNumber", stepNumber);
            json.put("description", (description != null && !description.equalsIgnoreCase("null")) ? description : "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
