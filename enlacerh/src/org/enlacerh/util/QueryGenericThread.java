/**
 * 
 */
package org.enlacerh.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * @author andres
 * Clase para generar querys genéricos utiliando los hilos de la computadora
 *
 */
public class QueryGenericThread extends Thread {

	/**
	 * 
	 */
	String pool;
	String query;
	
	public QueryGenericThread(String pool, String query) {
		this.pool = pool;
		this.query = query;
	}
	
	public void run() {	
		try {
			//int nbThreads =  Thread.getAllStackTraces().keySet().size();			
			//System.out.println("Número de hilos: " + nbThreads);
			
			Context initContext = new InitialContext();
			DataSource ds = (DataSource) initContext.lookup(pool);
			
			Connection con =  ds.getConnection();
            PreparedStatement pstmt = null;
            
            pstmt = con.prepareStatement(query);
            System.out.println(query);
            
            pstmt.executeUpdate();
            
            pstmt.close();
            con.close();
            		
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
    }
	
	
	

}
