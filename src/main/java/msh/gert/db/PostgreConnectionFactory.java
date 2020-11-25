package msh.gert.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.ds.PGConnectionPoolDataSource;

public class PostgreConnectionFactory {
    private static PostgreConnectionFactory instance;
    private PGConnectionPoolDataSource dataSource;
    private String[] serverNames;
    private int[] serverPorts;

    private PostgreConnectionFactory() {
        this.serverNames = new String[1];
        this.serverPorts = new int[1];
        this.serverNames[0] = "postgres";
        this.serverPorts[0] = 5432;

        this.dataSource =  new PGConnectionPoolDataSource();
        this.dataSource.setServerNames(this.serverNames);
        this.dataSource.setPortNumbers(this.serverPorts);
        this.dataSource.setDatabaseName("postgres");
        this.dataSource.setUser("postgres");
        this.dataSource.setPassword("dev_password_1508");
        this.dataSource.setLoginTimeout(20);
        this.dataSource.setSocketTimeout(20);
    }

    public static PostgreConnectionFactory getInstance(){
        if(instance == null){
            instance = new PostgreConnectionFactory();
        }

        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection con = this.dataSource.getConnection();
        return con;
    }
}
