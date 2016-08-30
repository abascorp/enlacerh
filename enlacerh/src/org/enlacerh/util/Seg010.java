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
	
	/*
	 * To change this template, choose Tools | Templates
	 * and open the template in the editor.
	 */
	import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

	import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.enlacerh.getset.Menuopc;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
	
	
	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Seg010 extends Utils implements Serializable{
	
	private LazyDataModel<Menuopc> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Menuopc> getLazyModel() {
			return lazyModel;
		}
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	
	
	public Seg010() {
		if (!rq.isRequestedSessionIdValid()) {
			//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
		}  else {
		lazyModel  = new LazyDataModel<Menuopc>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
	        @Override
			public List<Menuopc> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
	        	//Filtro
	        	if (filters != null) {
	           	 for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
	           		 String filterProperty = it.next(); // table column name = field name
	                 filterValue = filters.get(filterProperty);
	             //Filtrando por grupo de usuarios    
	             if(filterProperty.equals("vgrupo")){
	                  grupo = (String) filterValue;
	             }
	           	 }
	            }
	        	try { 
	        		//Consulta
					select(first, pageSize,sortField, filterValue);
					//Counter
					counter(filterValue);
					//Contador lazy
					lazyModel.setRowCount(rows);  //Necesario para crear la paginación
				} catch (SQLException | NamingException | IOException e) {	
					e.printStackTrace();
				}             
				return list;  
	        } 
	        
	        
	        //Arregla bug de primefaces
	        @Override
	        /**
			 * Arregla el Issue 1544 de primefaces lazy loading porge generaba un error
			 * de divisor equal a cero, es solamente un override
			 */
	        public void setRowIndex(int rowIndex) {
	            /*
	             * The following is in ancestor (LazyDataModel):
	             * this.rowIndex = rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
	             */
	            if (rowIndex == -1 || getPageSize() == 0) {
	                super.setRowIndex(-1);
	            }
	            else
	                super.setRowIndex(rowIndex % getPageSize());
	        }
	        
		};
		}
	}
	
	private String codopc = "";
	private String desopc = "";
	private String accopc = "";
	private String cia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ciaInput");
	private String param;
	private Object filterValue = "";
	private List<Menuopc> list = new ArrayList<Menuopc>();
	
	
	/**
	 * @return the cia
	 */
	public String getCia() {
		return cia;
	}
	
	/**
	 * @param cia the cia to set
	 */
	public void setCia(String cia) {
		this.cia = cia;
	}
	
	/**
	 * @return the codopc
	 */
	public String getCodopc() {
		return codopc;
	}
	
	/**
	 * @param codopc the codopc to set
	 */
	public void setCodopc(String codopc) {
		this.codopc = codopc;
	}
	
	/**
	 * @return the desopc
	 */
	public String getDesopc() {
		return desopc;
	}
	
	/**
	 * @param desopc the desopc to set
	 */
	public void setDesopc(String desopc) {
		this.desopc = desopc;
	}
	
	
	
	/**
	 * @return the accopc
	 */
	public String getAccopc() {
		return accopc;
	}
	
	/**
	 * @param accopc the accopc to set
	 */
	public void setAccopc(String accopc) {
		this.accopc = accopc;
	}
	
	
	/**
	 * @return the param
	 */
	public String getParam() {
		return param;
	}
	
	/**
	 * @param param the param to set
	 */
	public void setParam(String param) {
		this.param = param;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null; 
	PntGenerica consulta = new PntGenerica();
	boolean vGacc; //Validador de opciones del menú
	private int rows; //Registros de tabla
	//private String tipusr = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipusr"); //Usuario logeado
	private String grupo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("grupo"); //Usuario logeado
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;
	
	     /**
	     * Inserta menu para opciones de roles.
	     **/
	    private void insertSeg010(String numopc, String desopc, String accopc) throws  NamingException {
	    	String[] veccia = cia.split("\\ - ", -1);
	
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con = ds.getConnection();
	
	            String query = "insert into seg010 values ('"+ veccia[0] + "','" + numopc + "','" + desopc + "','" + accopc + "'," + Integer.parseInt(grupo) + ")";
	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	            } catch (SQLException e)  {
	            }           
	            pstmt.close();
	            con.close();
	        } catch (Exception e) {
	        }
	    }
	    
	    /**
	     * Inserta menu para opciones de roles.
	     * @throws NamingException 
	     * @throws IOException 
	     * @throws SQLException 
	     * @throws ClassNotFoundException 
	     **/
	    public void insert() throws NamingException, ClassNotFoundException, SQLException, IOException{
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    	insertSeg010("autos02", "NOMINA", "0");
	    	insertSeg010("autos03", "VACACIONES", "0");
	    	insertSeg010("autos04", "PRESTACIONES", "0");
	    	insertSeg010("autos05", "INTERESES", "0");
	    	insertSeg010("autos06", "PRESTAMOS ACTIVOS", "0");
	    	insertSeg010("autos07", "DISPONIBLE UTILIDADES", "0");
	    	insertSeg010("autos08", "CAMBIAR CONTRASEÑA", "0");
	    	insertSeg010("autos09", "ACTUALIZAR CORREO", "0");
	    	insertSeg010("autos10", "IMPUESTOS", "0");
	    	insertSeg010("autoconst", "CONSTANCIA DE TRABAJO", "0");
	    	insertSeg010("mail", "ENVIAR RECIBOS POR CORREO", "0");
	    	}
	    }
	
	    /**
	     * Elimina menu para opciones de roles.
	     * @throws IOException 
	     **/
	    public void delete() throws  NamingException, IOException {
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    	String[] veccia = cia.split("\\ - ", -1);
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con = ds.getConnection();
	          //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
		 		DatabaseMetaData databaseMetaData = con.getMetaData();
		 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección

		 		String query = "";
		 		
		 		switch ( productName ) {
		        case "Oracle":
		        	query = "DELETE from seg010 WHERE p_codcia ='" + veccia[0].toUpperCase() + "'";
		             break;
		        case "PostgreSQL":
		        	query = "DELETE from seg010 WHERE p_codcia ='" + veccia[0].toUpperCase() + "'";
		             break;
		  		}
	
	            pstmt = con.prepareStatement(query);
	
	            // Antes de ejecutar valida si existe el registro en la base de Datos.
	            consulta.selectPntGenerica("select p_codcia from seg010 where p_codcia ='" + veccia[0].toUpperCase() + "'", JNDI);
	               if (consulta.getRows()==0){
	            	   msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoDelete"), "");
	               } else {
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                if (pstmt.getUpdateCount() <= 1) {
	                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
	                } else {
	                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDeletes"), "");
	                }
	            } catch (SQLException e) {
	            	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	
	            }
	                }
	            pstmt.close();
	            con.close();
	               
	        } catch (Exception e) {
	            e.getMessage();
	        }
	        FacesContext.getCurrentInstance().addMessage(null, msj); 
	    	}
	    }
	    
	    /**
	     * Actualiza acceso a menu para opciones de roles, acceso, insertar, eliminar, modificar
	     **/
	    private void updateSeg010(String accopc) throws  NamingException {
	        String[] veccia = cia.split("\\ - ", -1);
	
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con =  ds.getConnection();
	            
	
	            String query = "UPDATE SEG010 SET accopc = '" + accopc + "'" 
	                    + " WHERE P_CODCIA = ? AND numopc IN (" + param.toLowerCase() + ")" ;
	
	            //System.out.println(query);
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, veccia[0].toUpperCase());
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnUpdate"), "");
	            } catch (SQLException e) {
	                e.printStackTrace();
	                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	
	            pstmt.close();
	            con.close();
	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        FacesContext.getCurrentInstance().addMessage(null, msj);
	    }
	    
	    public void update() throws  NamingException, ClassNotFoundException, SQLException, IOException { 
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    	String accopc = "0";
	    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	String[] paramAcc = request.getParameterValues("toAcc");
	    	String[] paramDacc = request.getParameterValues("toDacc");
	    	if (paramAcc==null && paramDacc==null){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("seg006Up1"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    	//Si opción es 1 muestra acceso
	
	    	if(paramAcc!=null && paramDacc!=null){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("seg006Opc"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	}
	    	if(paramAcc!=null && paramDacc==null){
	    		accopc = "0";
	        		updateSeg010(accopc);
	    	}
	    	if(paramAcc==null && paramDacc!=null){
	    		accopc = "1";
	        		updateSeg010(accopc);
	    	}
	    	}
	    	}
	    }
	
	 
	        
	
	
			/**
	         * Leer Datos de paises
	         * @throws NamingException 
			 * @throws SQLException 
	         * @throws IOException 
	         **/ 	
	      	public void select(int first, int pageSize, String sortField, Object filterValue) throws NamingException, SQLException, IOException  {
	      	
	      		Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	      		con = ds.getConnection();
	      	    //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	     		DatabaseMetaData databaseMetaData = con.getMetaData();
	     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección

	     		String query = "";
	      		
	      		if(cia==null){
	      			cia = " - ";
	            }
	            if(cia==""){
	            	cia = " - ";
	            }
	
	      		String[] veccia = cia.split("\\ - ", -1);
	      		
	      		switch ( productName ) {
	            case "Oracle":
	            	query = "  select * from ";
	            	query += " ( select query.*, rownum as rn from";
	            	query += " ( SELECT  upper(numopc), desopc, case when accopc='0' then '" + getMessage("seg006A") + "' else '" +  getMessage("seg006N") + "' end, grupo, p_codcia";
		       		query += " FROM SEG010 a";
		       		query += " where p_codcia like '" + veccia[0] + "%'"; 
		       		query += " and upper(numopc)||desopc like '%" + ((String) filterValue).toUpperCase() + "%'";
		       	    query += " and grupo = '" +  grupo + "'";
		       	    query += " order by numopc, a.p_codcia" + ") query";
		            query += " ) where rownum <= " + pageSize ;
				    query += " and rn > (" + first + ")";

	                 break;
	            case "PostgreSQL":
	            	query = " SELECT  upper(numopc), desopc, case when accopc='0' then '" + getMessage("seg006A") + "' else '" +  getMessage("seg006N") + "' end, grupo, p_codcia";
		       		query += " FROM SEG010";
		       		query += " where p_codcia like '" + veccia[0] + "%'"; 
		       		query += " and upper(numopc)||desopc like '%" + ((String) filterValue).toUpperCase() + "%'";
		       	    query += " and CAST(grupo AS text) = '" +  grupo + "'";
		       		query += " order by " + sortField.replace("v", "") ;
		            query += " LIMIT " + pageSize;
		            query += " OFFSET " + first;
	                 break;          		   
	      		}
	      		 		
	     		
	      		
	
	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);
	      		
	            ResultSet r =  pstmt.executeQuery();
	            
	            while (r.next()){
	            	Menuopc select = new Menuopc();
	                select.setVnumopc(r.getString(1));
	                select.setVdesopc(r.getString(2));
	                select.setVaccopc(r.getString(3));
	                select.setVgrupo(r.getString(4));
	                select.setVcodcia(r.getString(5));
	            	//Agrega la lista
	            	list.add(select);
	            }
	            //Cierra las conecciones
	            pstmt.close();
	            con.close();
	            r.close();
	
	      	}
	      	
	      	public void buscar() throws NamingException, SQLException, ClassNotFoundException, IOException{
	      		list.clear();
	      		select(1,15,"vnumopc", "");
	      	}
	      	
	  	
	      	/**
	         * Leer registros en la tabla
	         * @throws NamingException 
	         * @throws IOException 
	         **/ 	
	      	public void counter(Object filterValue ) throws SQLException, NamingException, IOException {
	         try {	
	        	Context initContext = new InitialContext();     
	       		DataSource ds = (DataSource) initContext.lookup(JNDI);
	       		
	
	       		con = ds.getConnection();
	       		
	       		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	     		DatabaseMetaData databaseMetaData = con.getMetaData();
	     		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
	     		
	     		String query = null;
	       		
	       		
	       		
	       		if(cia==null){
	      			cia = " - ";
	            }
	            if(cia==""){
	            	cia = " - ";
	            }
	
	      		String[] veccia = cia.split("\\ - ", -1);
	       		
	      		switch ( productName ) {
	            case "Oracle":
	            	query = "SELECT count_seg010(" + Integer.parseInt(grupo) + ",'" + veccia[0] + "','" + ((String) filterValue).toUpperCase() + "') from dual";
	                 break;
	            case "PostgreSQL":
	            	query = "SELECT count_seg010(" + Integer.parseInt(grupo) + ",'" + veccia[0] + "','" + ((String) filterValue).toUpperCase() + "')";
	                 break;
	      		}

	            
	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);
	
	             r =  pstmt.executeQuery();
	            
	            
	            while (r.next()){
	            	rows = r.getInt(1);
	            }
	         } catch (SQLException e){
	             e.printStackTrace();    
	         }
	            //Cierra las conecciones
	            pstmt.close();
	            con.close();
	            r.close();
	
	      	}
	
	/**
	 	 * @return the rows
	 	 */
	 	public int getRows() {
	 		return rows;
	 	}
	
	 	public void reset(){
	 		cia = "";
	    }
	
	
	}
