package com.tulip.init;

import java.sql.Connection;
import java.sql.Statement;

import com.mchange.v2.c3p0.AbstractConnectionCustomizer;

/**
 * 更新连接池编码方式
 */
public class UTF8MB4ConnectionCustomizer extends AbstractConnectionCustomizer {
	
	@Override
	public void onAcquire(Connection c, String parentDataSourceIdentityToken) throws java.lang.Exception {
		Statement stmt = null;
		try {
			stmt = c.createStatement();
			stmt.executeUpdate("SET names utf8mb4");
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}
}
