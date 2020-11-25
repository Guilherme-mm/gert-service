package msh.gert.db.authorization;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msh.gert.core.model.auth.Client;
import msh.gert.core.model.auth.ResourceOwner;
import msh.gert.db.DataCollection;
import msh.gert.db.PostgreCollection;
import msh.gert.db.exceptions.DBException;
import msh.gert.db.filter.QueryFilter;
import msh.gert.db.filter.sql.SQLFilterBuilder;

public class ClientCollection extends PostgreCollection implements DataCollection<Client> {
    public ClientCollection() throws DBException {
        super("authentication", "client_tb");
    }

    @Override
    public Client get(QueryFilter filter) throws DBException {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("SELECT * FROM ").append(this.getSchema()).append('.').append(this.getTable());

        if(filter != null){
            stringBuilder.append(" WHERE ").append(filter.toString());
        }

        String sql = stringBuilder.toString();

        try {
            PreparedStatement prepStmt = this.prepareStatement(sql, null);
            ResultSet queryResult = prepStmt.executeQuery();

            if (queryResult.next()) {
                Client findedClient = new Client();
                findedClient.setId(queryResult.getString("id"));
                findedClient.setSecret(queryResult.getString("secret"));
                findedClient.setName(queryResult.getString("name"));

                ResourceOwner clientResourceOwner = new ResourceOwner();
                if (queryResult.getInt("resource_owner") != 0) {
                    try {
                        clientResourceOwner.setId(queryResult.getInt("resource_owner"));
                        ResourceOwnerCollection resourceOwnerCollection = new ResourceOwnerCollection();
                        QueryFilter resourceOwnerFilter = resourceOwnerCollection.getFilter(clientResourceOwner);
                        clientResourceOwner = resourceOwnerCollection.get(resourceOwnerFilter);
                    } catch (DBException exception) {
                        throw new DBException("Failed to retrieve client resource owner data", exception);
                    }
                }

                return findedClient;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public boolean create(Client client) throws DBException {
        List<Object> queryParameters = new ArrayList<Object>();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("INSERT INTO ").append(this.getSchema()).append(".").append(this.getTable()).append(" (");

        boolean hasSetparameter = false;

        if (client.getName() != null) {
            stringBuilder.append("name");
            queryParameters.add(client.getName());
            hasSetparameter = true;
        }

        if (client.getSecret() != null) {
            if (hasSetparameter) {
                stringBuilder.append(", secret");
            } else {
                stringBuilder.append("secret");
                hasSetparameter = true;
            }

            queryParameters.add(client.getSecret());
        }

        if (client.getResourceOwner() != null) {
            if (hasSetparameter) {
                stringBuilder.append(", resource_owner");
            } else {
                stringBuilder.append("resource_owner");
            }

            queryParameters.add(client.getResourceOwner().getId());
        }

        stringBuilder.append(") VALUES (");

        int counter = 1;
        for (Object parameter : queryParameters) {
            if (counter < queryParameters.size()) {
                stringBuilder.append("?,");
            } else {
                stringBuilder.append("?)");
            }

            counter++;
        }

        String sql = stringBuilder.toString();

        try {
            PreparedStatement prepStmt = this.prepareStatement(sql, queryParameters);
            int modifiedCount = prepStmt.executeUpdate();

            if (modifiedCount > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public QueryFilter getFilter(Client client) {        
        Map<String, Object> queryParameters = new HashMap<String, Object>();

        if(client.getId() != null) {
            queryParameters.put("id", client.getId());
        }
        if(client.getName() != null) {
            queryParameters.put("name", client.getName());
        }
        if(client.getSecret() != null) {
            queryParameters.put("secret", client.getSecret());
        }
        if(client.getResourceOwner() != null) {
            queryParameters.put("resource_owner", client.getResourceOwner().getId());
        }

        SQLFilterBuilder filterBuilder = new SQLFilterBuilder();

        int parametersQuantity = queryParameters.size();
        int iterationCounter = 1;
        for (Map.Entry<String, Object> parameter : queryParameters.entrySet()) {
            if(iterationCounter == parametersQuantity){
                filterBuilder = filterBuilder.addCondition(parameter.getKey(), "=", parameter.getValue(), null);
            } else {
                filterBuilder = filterBuilder.addCondition(parameter.getKey(), "=", parameter.getValue(), "AND");
            }
            iterationCounter++;
        }

        return filterBuilder.build();
    }
}
