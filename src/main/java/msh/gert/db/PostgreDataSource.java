package msh.gert.db;

import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.sql.DataSource;
import javax.ws.rs.Produces;

@Singleton
@DataSourceDefinition(
    name = "java:app/gert/postgresDatasource",
    className = "org.postgresql.ds.PGConnectionPoolDataSource",
    serverName="postgres",
    portNumber=3306,
    user = "postgres",
    password = "dev_password_1508",
    databaseName = "postgres",
    initialPoolSize = 10,
    minPoolSize = 10,
    maxPoolSize = 50
)
public class PostgreDataSource {
    
    @Resource(lookup = "java:app/gert/postgresDatasource")
	DataSource ds;
    
    @Produces
	public DataSource getDatasource() {
		return ds;
	}
}
