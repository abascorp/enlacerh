/*
 *  Copyright (C) 2011  ANDRES DOMINGUEZ

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los terminos de la Licencia Pública General GNU publicada 
    por la Fundacion para el Software Libre, ya sea la version 3 
    de la Licencia, o (a su eleccion) cualquier version posterior.

    Este programa se distribuye con la esperanza de que sea útil, pero 
    SIN GARANTiA ALGUNA; ni siquiera la garantia implicita 
    MERCANTIL o de APTITUD PARA UN PROPoSITO DETERMINADO. 
    Consulte los detalles de la Licencia Pública General GNU para obtener 
    una informacion mas detallada. 

    Deberia haber recibido una copia de la Licencia Pública General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.
 */
package org.opennomina.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.sql.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


/**
 *
 * @author Andres
 */
@ManagedBean(name="seg003Bean")
@ViewScoped
public class Seg003forLogin extends Utils implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//Variables seran utilizadas para capturar mensajes de errores de Oracle

//Variables para select
    private int columns;
    private String[][] arr;    

    
    public Seg003forLogin() {
    }



	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null; 
	PntGenerica consulta = new PntGenerica();
	private String login = ""; //Usuario logeado
	private int rows; //Registros de tabla
	
	

 
    
    /**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}


	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

  
    public void selectLogin(String user, String pool, String tabla) throws NamingException {

        //Pool de conecciones JNDI. Cambio de metodologia de conexion a Bd. Julio 2010
        Context initContext = new InitialContext();
        DataSource ds = (DataSource) initContext.lookup(pool);
        try {
            Statement stmt;
            ResultSet rs;
            Connection con = ds.getConnection();
            //Class.forName(getDriver());
            //con = DriverManager.getConnection(
            //        getUrl(), getUser(), getPass());
            stmt = con.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String query = "SELECT trim(coduser), trim(cluser), trim(mail),  cedula, trim(p_codcia), grupo, trim(nbr), admin";
            query += " FROM " + tabla;
            query += " where coduser = '" + user.toUpperCase() + "'";
            //System.out.println(query);
            try {
                rs = stmt.executeQuery(query);
                rows = 1;
                rs.last();
                rows = rs.getRow();
                //System.out.println("FILAS: " + rows);

                ResultSetMetaData rsmd = rs.getMetaData();
                columns = rsmd.getColumnCount();
                //System.out.println(columns);
                arr = new String[rows][columns];

                int i = 0;
                rs.beforeFirst();
                while (rs.next()) {
                    for (int j = 0; j < columns; j++) {
                        arr[i][j] = rs.getString(j + 1);
                    }
                    i++;
                }
            } catch (SQLException e) {
            	msj =   new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            	FacesContext.getCurrentInstance().addMessage(null, msj);
            }
            stmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
  

	/**
     * @return Retorna el arreglo
     **/
    public String[][] getArray() {
        return arr;
    }

    /**
     * @return Retorna número de filas
     **/
    public int getRows() {
        return rows;
    }

    /**
     * @return Retorna número de columnas
     **/
    public int getColumns() {
        return columns;
    }
	
	
  
    
    
  

}
