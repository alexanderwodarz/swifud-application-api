package de.alexanderwodarz.code.swifud.application;

import de.alexanderwodarz.code.web.StatusCode;
import de.alexanderwodarz.code.web.rest.ResponseData;
import de.alexanderwodarz.code.web.rest.annotation.RequestBody;
import de.alexanderwodarz.code.web.rest.annotation.RestController;
import de.alexanderwodarz.code.web.rest.annotation.RestRequest;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;

@RestController
public class HookController {

    @RestRequest(path = "/hook", method = "POST", produces = MediaType.APPLICATION_JSON)
    public static ResponseData hook(@RequestBody JSONObject body) {
        return new ResponseData(Application.consumer.apply(body).toString(), StatusCode.OK);
    }

}
