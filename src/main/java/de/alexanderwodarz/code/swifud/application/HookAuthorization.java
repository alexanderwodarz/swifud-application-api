package de.alexanderwodarz.code.swifud.application;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.alexanderwodarz.code.web.rest.RequestData;
import de.alexanderwodarz.code.web.rest.authentication.AuthenticationFilter;
import de.alexanderwodarz.code.web.rest.authentication.AuthenticationFilterResponse;
import de.alexanderwodarz.code.web.rest.authentication.CorsResponse;

public class HookAuthorization extends AuthenticationFilter {

    public static AuthenticationFilterResponse doFilter(RequestData request) {
        String auth = request.getAuthorization();
        if (auth == null || auth.length() == 0)
            return AuthenticationFilterResponse.UNAUTHORIZED();
        try {
            JWT.require(Algorithm.HMAC256(Application.secret)).withIssuer("Swifud").build().verify(auth.split(" ")[1]);
        } catch (Exception e) {
            return AuthenticationFilterResponse.UNAUTHORIZED();
        }
        return AuthenticationFilterResponse.OK();
    }

    public static CorsResponse doCors(RequestData data) {
        CorsResponse response = new CorsResponse();
        response.setOrigin("*");
        return response;
    }

}
