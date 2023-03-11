package de.alexanderwodarz.code.swifud.application.model;

import lombok.Builder;
import org.json.JSONObject;

@Builder
public class InvoiceAddress {

    private String line1, line2, line3, line4;

    public JSONObject build() {
        JSONObject build = new JSONObject();
        if (line1 != null && line1.length() > 0)
            build.put("line1", line1);
        if (line2 != null && line2.length() > 0)
            build.put("line2", line2);
        if (line3 != null && line3.length() > 0)
            build.put("line3", line3);
        if (line4 != null && line4.length() > 0)
            build.put("line4", line4);
        return build;
    }

}
