package de.alexanderwodarz.code.swifud.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.json.JSONObject;

@AllArgsConstructor
@Getter
@Builder
public class Item {
    private String description;
    private int amount;
    private double price;

    public JSONObject build() {
        return new JSONObject().put("description", description).put("amount", amount).put("price", price);
    }

}
