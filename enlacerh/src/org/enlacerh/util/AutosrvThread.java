/*
 *  Copyright (C) 2016 ABAS CORP, C.A.

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los terminos de la util.licencia Pública General GNU publicada 
    por la Fundacion para el Software Libre, ya sea la version 3 
    de la util.licencia, o (a su eleccion) cualquier version posterior.

    Este programa se distribuye con la esperanza de que sea útil, pero 
    SIN GARANTiA ALGUNA; ni siquiera la garantia implicita 
    MERCANTIL o de APTITUD PARA UN PROPoSITO DETERMINADO. 
    Consulte los detalles de la util.licencia Pública General GNU para obtener 
    una informacion mas detallada. 

    Deberia haber recibido una copia de la util.licencia Pública General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.
 */
package org.enlacerh.util;

/*
 *  Esta clase controla todos los archivos de subida y procesamiento de txt al servidos
 */
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class AutosrvThread extends Thread  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;




	// Constructor
	public AutosrvThread(String opcAutoSrv, String cia, String txtName, String anio, String mes, String opcdelete) {
		this.opcAutoSrv=opcAutoSrv;
		this.cia=cia;
		this.txtName=txtName;
		this.anio=anio;
		this.mes=mes;
		this.opcdelete=opcdelete;
	}

	// Primitives
	private static final String RUTA_EXTERNO = File.separator + "externos" + File.separator + "autoext";
	PntExternos externos = new PntExternos(); //Objeto para procesar archivos externos
	String anio = ""; //Año a subir autoservicio
	String mes = "S"; //Mes subir autoservicio
	String opcAutoSrv; //Opciones a subir
	String cia; //Compañia a procesar
	String txtName; //Nombre del txt
	String ruta; //Ruta donde esta ubicado
	String opcdelete; //Opciones de borrado
	ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); //Toma ruta real de la aplicación
	Utils util = new Utils();
	

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Variables seran utilizadas para capturar mensajes de errores de Oracle y
	// parametros de metodos
	FacesMessage msj = null; 
	PntGenerica consulta = new PntGenerica();
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Pool de conecciones JNDI()
	Connection con;
	PreparedStatement pstmt = null;

  
	   
    
	    /**
	     * Borra autos02
	     **/
	    private void deleteAutos (String tabla, String codcia, String opc, String vanio, String vmes) throws  NamingException {  
	    	try {
	    		Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(Utils.JNDI);
	        	con = ds.getConnection();

	        	String query = "";
	        	if(opc.equals("0")){
	        	query = "DELETE FROM " + tabla +" WHERE PCODCIA = '" + codcia + "' AND ANIO = " + Integer.parseInt(vanio) + " and MES = " + Integer.parseInt(vmes) ;
	        	} else {
	        	query = "DELETE FROM " + tabla +" WHERE PCODCIA = '" + codcia + "'" ;
	        	}
	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);		            
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                //System.out.println("Registros:" + pstmt.getUpdateCount());	
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	            pstmt.close();
	            con.close();     
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    
		   
	
///////////////////////////////////////////////MULTRITHREAD PARALLEL PROGRAMING./////////////////////////////////////////////////////
///////////////////////////////////////////////CODIGO DE APROVECHAMIENTO DE LOS CORES DEL EQUIPO////////////////////////////////////


	    /**
        * Rutina utilizada para procesar multithread, procesa todos los autoservicios
        * . Los parámetros son pasados por el constructor de la clase
	     **/    
   public void procAutosrv() throws NamingException, IOException{
	  deleteAutos(opcAutoSrv, cia, opcdelete, anio, mes); //Elimina registros anteriores 
	 //Procesa el archivo de texto
	 externos.pntExtThread(extContext.getRealPath(RUTA_EXTERNO)+ File.separator + cia, cia+"_"+txtName+anio+mes, opcAutoSrv, Utils.JNDI); //Procesa nóminas //Procesa nóminas    
     }


   public void run(){
	   try {
		   //System.out.println("Thread start");
	   procAutosrv();
	   } catch (NamingException | IOException e) {
	   e.printStackTrace();
	   }
	   }

}
