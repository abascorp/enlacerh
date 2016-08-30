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

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@ManagedBean
@RequestScoped
public class ValidaReg extends Utils {

	public ValidaReg() {

	}

	private String idTemp = "";
	FacesMessage msj = null; 
	
	
    
	/**
	 * @return the idTemp
	 */
	public String getIdTemp() {
		return idTemp;
	}

	/**
	 * @param idTemp the idTemp to set
	 */
	public void setIdTemp(String idTemp) {
		this.idTemp = idTemp;
	}
	
	//Coneccion a base de datos
	//Pool de conecciones JNDIFARM
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;

	
	public void getId(){
		
		//System.out.println("Validando usuario: " + idTemp);
		
		try {
        	Context initContext = new InitialContext();     
        	DataSource ds = (DataSource) initContext.lookup(JNDI);
        	con = ds.getConnection();

            String query = "update autos01";
            	   query += " set mail_validado='1'";
                   query += " where coduser = (select coduser from temp_registro where idtemp='" + idTemp + "')";
            //System.out.println(query);
            pstmt = con.prepareStatement(query);

            try {
                //Avisando
                pstmt.executeUpdate();
                if(pstmt.getUpdateCount()==0){
                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("reg3"), "");
                } else {
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("reg2"), "");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL,  e.getMessage(), "");
            }
            deleteId();
            pstmt.close();
            con.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().addMessage(null, msj); 
    	
    }
	
	
    private void deleteId(){
		
		//System.out.println("Validando usuario: " + idTemp);
		
		try {
        	Context initContext = new InitialContext();     
        	DataSource ds = (DataSource) initContext.lookup(JNDI);
        	con = ds.getConnection();

            String query = "delete from temp_registro where idtemp='" + idTemp + "'";
            //System.out.println(query);
            pstmt = con.prepareStatement(query);

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
    





}
