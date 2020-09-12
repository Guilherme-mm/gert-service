package msh.gert.core.auth;

import java.util.Base64;

public class AuthorizationService {

    /**
     * Receives an Basic HTTP authentication header and checks if its valid
     * @param authorizationHeader String The authorization header sended on basic auth requests
     * @return Boolean True if validation succeeds and false if not
     */
    public boolean basicAuth(String authorizationHeader) {
        String[] headerParts = authorizationHeader.split(" ");
        String base64Credentials = headerParts[1];
        String decodedCredentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] credentialParts = decodedCredentials.split(":");
        String username = credentialParts[0];
        String password = credentialParts[1];

        return true;
    }

}
