package org.esfinge.querybuilder.jdbc;

import java.sql.Connection;

public interface DatabaseConnectionProvider {

	public Connection getConnection();

}
 