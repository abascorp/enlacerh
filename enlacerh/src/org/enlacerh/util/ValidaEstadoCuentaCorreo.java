package org.enlacerh.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@ManagedBean
@ViewScoped
public class ValidaEstadoCuentaCorreo extends Utils {

	public ValidaEstadoCuentaCorreo() throws SQLException, NamingException, IOException {
		estadomail();
	}
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	
    private String estadomail = "";
	
	/**
	 * @return the estadomail
	 */
	public String getEstadomail() {
		if(estadomail.equals("1")){
			estadomail = getMessage("mailver");
		} else {
			estadomail = getMessage("mailnover");
		}
		return estadomail;
	}

	/**
	 * @param estadomail the estadomail to set
	 */
	public void setEstadomail(String estadomail) {
		this.estadomail = estadomail;
	}

	/**
	 * Leer registros en la tabla
	 * @throws NamingException 
	 * @throws IOException 
	 **/ 	
		public void estadomail() throws SQLException, NamingException, IOException {
	 try {	
		Context initContext = new InitialContext();     
			DataSource ds = (DataSource) initContext.lookup(JNDI);
	
			con = ds.getConnection();
			//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
     		DatabaseMetaData databaseMetaData = con.getMetaData();
     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
     		
     		String query = null;
     		
     		switch ( productName ) {
            case "Oracle":
            	query = "SELECT estadomail('" + login.toUpperCase() + "') from dual";
                 break;
            case "PostgreSQL":
            	query = "SELECT estadomail('" + login.toUpperCase() + "')";
                 break;
      		}

	    
	    pstmt = con.prepareStatement(query);
	    //System.out.println(query);
	
	     r =  pstmt.executeQuery();
	    
	    
	    while (r.next()){
	    	estadomail = r.getString(1);
	    }
	 } catch (SQLException e){
	     e.printStackTrace();    
	 }
	    //Cierra las conecciones
	    pstmt.close();
	    con.close();
	    r.close();
	
		}
	
}
