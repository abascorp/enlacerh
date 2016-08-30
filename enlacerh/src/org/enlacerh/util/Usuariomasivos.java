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

package org.enlacerh.util;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;


 
/**
 *
 * @author Andres Dominguez 08/05/2012
 */
/**
 * Clase para leer, insertar, modificar y eliminar registros de la tabla
 * autos01 registro de usuarios de autoservicio
 *  */
@ManagedBean
@ViewScoped
public class Usuariomasivos extends Utils  {


	// Constructor
    public Usuariomasivos() {
    }

    
  //Coneccion a base de datos
  //Pool de conecciones JNDI()
  Connection con;
  PreparedStatement pstmt = null;
  ResultSet r;
  StringMD md = new StringMD(); //Objeto encripta
  PntGenerica consulta = new PntGenerica();
   
    /**
     * Actualiza Usuario autoservicio
     **/
    public void update(String cedula, String clave) throws  NamingException {
          try {
        	Context initContext = new InitialContext();     
        	DataSource ds = (DataSource) initContext.lookup("jdbc/nube");
        	con = ds.getConnection();

            String query = "UPDATE AUTOS01MASIVO SET CLUSER = ? WHERE cedula = " + cedula ;
            //System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, md.getStringMessageDigest(clave.toUpperCase(), StringMD.SHA256));


            try {
                //Avisando
                pstmt.executeUpdate();


            } catch (SQLException e) {
                e.printStackTrace();

            }

            pstmt.close();
            con.close();
               

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actmasivo() throws NamingException{
    	System.out.println("Comenzando.................");
    	consulta.selectPntGenerica("select trim(cast(cedula as text)), trim(cluser) from autos01masivo", "jdbc/nube");
    	String[][] tabla = consulta.getArray();
    	int rows = consulta.getRows();
    	for(int i = 0; i < rows; i ++){
    		update(tabla[i][0], tabla[i][1]);
    		System.out.println("Actualizando cedula: " + tabla[i][0]);
    	}
    	System.out.println("Actualización masiva ejecutada");
    }
}




