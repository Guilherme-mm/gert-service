package msh.gert.core.auth;

import java.util.Base64;
import java.util.HashMap;

import msh.gert.core.model.auth.ResourceOwner;
import msh.gert.db.authorization.ResourceOwnerCollection;
import msh.gert.db.exceptions.DBException;
import msh.gert.db.filter.QueryFilter;

public class AuthorizationService {

    /**
     * Receives a basic HTTP authentication header and extracts its user and password informations
     * 
     * @param authorizationHeader | String | The authorization header string in the format: 'Authorization <base 64 credentials>'
     * @return HashMap<String, String> A map with the extracted user and password
     */
    private HashMap<String, String> extractCredentialsFromHeader(String authorizationHeader) {
        String[] headerParts = authorizationHeader.split(" ");
        String base64Credentials = headerParts[1];
        String decodedCredentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] credentialParts = decodedCredentials.split(":");
        String username = credentialParts[0];
        String password = credentialParts[1];

        HashMap<String, String> extractedCredentials = new HashMap<String, String>();
        extractedCredentials.put("username", username);
        extractedCredentials.put("password", password);
        return extractedCredentials;
    }

    /**
     * Receives an Basic HTTP authentication header and checks if its valid
     * 
     * @param authorizationHeader | String | The authorization header sended on basic auth requests
     * @return | Boolean | True if authentication succeeds and false if not
     * @throws DBException
     */
    public boolean basicAuth(String authorizationHeader) throws DBException {
        HashMap<String, String> receivedCredentials = this.extractCredentialsFromHeader(authorizationHeader);

        //Filling the ResourceOwner bean
        ResourceOwner resourceOwner = new ResourceOwner();
        resourceOwner.setUsername(receivedCredentials.get("username"));
        resourceOwner.setPassword(receivedCredentials.get("password"));

        //Generates a filter for querying a resource owner with the received credentials
        ResourceOwnerCollection resourceOwnerCollection = new ResourceOwnerCollection();
        QueryFilter filter = resourceOwnerCollection.getFilter(resourceOwner);

        //Looks for a resource owner that matches the created filter
        ResourceOwner findedOwner = resourceOwnerCollection.get(filter);

        //If no resource owner is found, it means that the credentials are not valid
        if(findedOwner == null) {
            return false;
        }

        //A resource owner was found, wich means that the credentials are valid
        return true;
    }
}