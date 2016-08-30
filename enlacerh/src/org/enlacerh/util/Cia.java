/*
 *  Copyright (C) 2016 ABAS CORP, C.A.

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los terminos de la Licencia PÃºblica General GNU publicada 
    por la Fundacion para el Software Libre, ya sea la version 3 
    de la Licencia, o (a su eleccion) cualquier version posterior.

    Este programa se distribuye con la esperanza de que sea Ãºtil, pero 
    SIN GARANTiA ALGUNA; ni siquiera la garantia implicita 
    MERCANTIL o de APTITUD PARA UN PROPoSITO DETERMINADO. 
    Consulte los detalles de la Licencia PÃºblica General GNU para obtener 
    una informacion mas detallada. 

    Deberia haber recibido una copia de la Licencia PÃºblica General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.
 */

package org.enlacerh.util;

import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.enlacerh.getset.DatosCianom;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Andres Dominguez 08/02/2009
 */

@ManagedBean
@RequestScoped
public class Cia extends Utils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();


	// Constructor
	public Cia() throws SQLException, NamingException, IOException {
		if (!rq.isRequestedSessionIdValid()) {
			//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
		}  else {
		selectCia();
		}
	}
	

	// Variables seran utilizadas para capturar mensajes de errores de Oracle
	PntGenerica consulta = new PntGenerica();
	private String orden = "1"; // Orden de la consulta
	private int rows; // Registros de tabla
	FacesMessage msj = null;
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); // Usuario
	private String pcodcia = "";
	private String descia = "";
	private String ptipnom = "";
	private String desnom = "";
	private String grupo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("grupo"); //Usuario logeado

	
	// Coneccion a base de datos
	// Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;


	/**
	 * @return the orden
	 */
	public String getOrden() {
		return orden;
	}

	/**
	 * @param orden
	 *            the orden to set
	 */
	public void setOrden(String orden) {
		this.orden = orden;
	}
		
	
	
	/**
	 * @return the ptipnom
	 */
	public String getPtipnom() {
		return ptipnom;
	}

	/**
	 * @param ptipnom the ptipnom to set
	 */
	public void setPtipnom(String ptipnom) {
		this.ptipnom = ptipnom;
	}

	/**
	 * @return the desnom
	 */
	public String getDesnom() {
		return desnom;
	}

	/**
	 * @param desnom the desnom to set
	 */
	public void setDesnom(String desnom) {
		this.desnom = desnom;
	}

	//////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////

	
	/**
	 * Setea compañía Seleccionada
	 * @param next
	 */
	public void setCiaSelected(String cia, String descia){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ciaselected", cia);
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("desciaselected", descia);
    }

	/**
	 * @return the pcodcia
	 */
	public String getPcodcia() {
		return pcodcia;
	}

	/**
	 * @param pcodcia the pcodcia to set
	 */
	public void setPcodcia(String pcodcia) {
		this.pcodcia = pcodcia;
	}

	/**
	 * @return the descia
	 */
	public String getDescia() {
		return descia;
	}

	/**
	 * @param descia the descia to set
	 */
	public void setDescia(String descia) {
		this.descia = descia;
	}

	private List<DatosCianom> list = new ArrayList<DatosCianom>();

	/**
	 * @return the list
	 */
	public List<DatosCianom> getList() {
		return list;
	}


	/**
	 * Leer Datos de compañi­as
	 * 
	 * @throws NamingException
	 * @throws IOException
	 **/
	public void selectCia() throws SQLException, NamingException, IOException {
		
		Context initContext = new InitialContext();
		DataSource ds = (DataSource) initContext.lookup(JNDI);
		con = ds.getConnection();
        
		String query;
		// Consulta
		query  = "SELECT codcia, nomcia2";
		query += " FROM seg007 a, pnt001 b";
		query += " WHERE a.p_codcia=b.codcia";
		query += " and a.grupo=b.grupo";
		query += " and a.p_coduser = '" + login.toUpperCase() + "'";
		query += " and a.grupo = '" + grupo + "'";
		query += " order by " + orden;


		pstmt = con.prepareStatement(query);
		//System.out.println(query);

		r = pstmt.executeQuery();

		while (r.next()) {
			DatosCianom select = new DatosCianom();
			select.setCompania(r.getString(1));
			select.setDescodcia(r.getString(2));

			// Agrega la lista
			list.add(select);
			rows = list.size();
		}
		// Cierra las conecciones
		pstmt.close();
		con.close();
		r.close();

	}
	

	

	private List<DatosCianom> filtro;
	
	

	/**
	 * @return the filtro
	 */
	public List<DatosCianom> getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(List<DatosCianom> filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}
	
    public void busqueda() throws SQLException, NamingException, IOException{
    	list.clear();
    	selectCia();
    }
    
    public void href(String page) throws IOException{
		   FacesContext.getCurrentInstance().getExternalContext().redirect(page + ".xhtml"); 
	   }

}
