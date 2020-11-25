package msh.gert.control.auth;

import java.util.HashMap;

import msh.gert.core.model.auth.Client;
import msh.gert.core.model.auth.ResourceOwner;
import msh.gert.db.DataCollection;
import msh.gert.db.authorization.ClientCollection;
import msh.gert.db.exceptions.DBException;
import msh.gert.db.filter.QueryFilter;

public class ClientController {
    
    public HashMap<String, String> createClient(String name, int resourceOwnerId) throws DBException {
        //Preparing the model components for persistency
        ResourceOwner resourceOwner = new ResourceOwner();
        resourceOwner.setId(resourceOwnerId);

        Client client = new Client();
        client.setName(name);
        client.setResourceOwner(resourceOwner);

        try {
            //Asks to the collection to persist the client object
            DataCollection<Client> clientCollection = new ClientCollection();
            boolean clientCreationResult = clientCollection.create(client);
            
            //If the creation was successfull, we need to get the new client information to send it as a response
            if(clientCreationResult) {
                //First we need to create a filter from the data that we have
                QueryFilter filter = clientCollection.getFilter(client);
                
                //Then we query for the client complete data
                client = clientCollection.get(filter);

                //Now we fill the return data
                HashMap<String, String> clientCredentials = new HashMap<String, String>();
                clientCredentials.put("client_id", client.getId());
                clientCredentials.put("client_secret", client.getSecret());

                return clientCredentials;
            } else {
                return null;
            }
            
        } catch(DBException exception){
            String errorMessage = exception.getMessage();
            return null;
        }
    }
}
