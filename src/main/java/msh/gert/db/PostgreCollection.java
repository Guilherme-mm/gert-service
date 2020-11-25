package msh.gert.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import msh.gert.db.exceptions.DBException;

public abstract class PostgreCollection {
    private Connection connection;
    private String schema;
    private String table;

    public PostgreCollection(String schema, String table) throws DBException {
        try {
            PostgreConnectionFactory connectionFactory = PostgreConnectionFactory.getInstance();
            this.connection = connectionFactory.getConnection();
        } catch (SQLException e) {
            throw new DBException("An error has ocurred while trying to acquire a postgre connection", e);
        }

        this.schema = schema;
        this.table = table;
    }
    
    public Connection getConnection(){
        return this.connection;
    }

    public String getSchema() {
        return this.schema;
    }

    public String getTable() {
        return this.table;
    }

    public PreparedStatement prepareStatement(String sql, List<Object> parameters) throws SQLException {
        PreparedStatement pstmt = this.getConnection().prepareStatement(sql);

        if(parameters != null){
            if(parameters.size() > 0) {
                int counter = 1;
    
                for (Object parameter : parameters) {
                    if(parameter.getClass() == Integer.class){
                        pstmt.setInt(counter, (Integer)parameter);
                    }
                    if(parameter.getClass() == String.class) {
                        pstmt.setString(counter, (String)parameter);
                    }
                    if(parameter.getClass() == Long.class){
                        pstmt.setLong(counter, (Long)parameter);
                    }
    
                    counter++;
                }
            }
        }

        return pstmt;
    }
}
