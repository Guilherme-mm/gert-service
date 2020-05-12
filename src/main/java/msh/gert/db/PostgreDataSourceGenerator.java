package msh.gert.db;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.sql.DataSource;

@Singleton
@DataSourceDefinition(
    className = "org.postgresql.ds.PGSimpleDataSource",
    name = "java:app/jdbc/primary",
    serverName="postgres",
    portNumber=3306,
    user = "postgres",
    password = "dev_password_1508",
    databaseName = "gert"
)
public class PostgreDataSourceGenerator {

	DataSource ds;
	
	public DataSource getDatasource() {
		return ds;
	}
}
