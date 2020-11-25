package msh.gert.db.authorization;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import msh.gert.core.model.auth.ResourceOwner;
import msh.gert.db.DataCollection;
import msh.gert.db.PostgreCollection;
import msh.gert.db.exceptions.DBException;
import msh.gert.db.filter.QueryFilter;
import msh.gert.db.filter.sql.SQLFilterBuilder;

public class ResourceOwnerCollection extends PostgreCollection implements DataCollection<ResourceOwner> {
    public ResourceOwnerCollection() throws DBException {
        super("authentication", "resource_owner_tb");
    }

    /**
     * Looks for a resource owner that matches the provided filter on the database. 
     * 
     * @param filter | QueryFilter | The conditions that will be taken into account on the query
     * @return | ResourceOwner | The first resource owner that matches the passed filter
     * @throws DBException If a problem occurs while interacting with the database
     * @throws IllegalArgumentException If no filter is provided
     */
    @Override
    public ResourceOwner get(QueryFilter filter) throws DBException, IllegalArgumentException {
        //Mounts the sql
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM ").append(this.getSchema()).append('.').append(this.getTable());

        //Checks for the presence of a filter
        if(filter != null){
            stringBuilder.append(" WHERE ").append(filter.toString());
        } else {
            throw new IllegalArgumentException("A null query filter was received");
        }

        //Generates the final sql string
        String sql = stringBuilder.toString();

        //Try to execute the query and mount the response
        try {
            PreparedStatement prepStmt = this.prepareStatement(sql, null);
            ResultSet queryResult = prepStmt.executeQuery();

            if (queryResult.next()) {
                ResourceOwner findedResourceOwner = new ResourceOwner();
                findedResourceOwner.setName(queryResult.getString("name"));
                findedResourceOwner.setPassword(queryResult.getString("password"));
                findedResourceOwner.setUsername(queryResult.getString("username"));
    
                return findedResourceOwner;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage(), e);
        }
    }

    /**
     * @TODO
     */
    public boolean create(ResourceOwner resourceOwner) throws DBException {
        return false;
    }

    /**
     * Generates the proper query filter from a resource owner bean object to be used on the database interaction methods of this class
     * 
     * @param resourceOwner | ResourceOwner | The bean object wich properties values will be used to generate the filter
     * @return | QueryFilter | A filter object to be used on querying methods of this class
     */
    @Override
    public QueryFilter getFilter(ResourceOwner resourceOwner) {
        Map<String, Object> queryParameters = new HashMap<String, Object>();

        if (resourceOwner.getName() != null) {
            queryParameters.put("name", resourceOwner.getName());
        }
        if (resourceOwner.getId() != 0) {
            queryParameters.put("id", resourceOwner.getId());
        }
        if (resourceOwner.getPassword() != null) {
            queryParameters.put("password", resourceOwner.getPassword());
        }
        if (resourceOwner.getUsername() != null) {
            queryParameters.put("username", resourceOwner.getUsername());
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
