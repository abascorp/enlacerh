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
	public class Seg006 extends Utils implements Serializable {
	
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
	
	
	public Seg006() {
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
	private String accinsert = "";
	private String accupdate = "";
	private String accdelete = "";
	private String rol = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rol");
	private String opcvista = "1";
	private String param;
	private Object filterValue = "";
	List<Menuopc> list = new ArrayList<Menuopc>();
	
	
	/**
	 * @return the rol
	 */
	public String getRol() {
		return rol;
	}
	
	/**
	 * @param rol the rol to set
	 */
	public void setRol(String rol) {
		this.rol = rol;
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
	 * @return the accinsert
	 */
	public String getAccinsert() {
		return accinsert;
	}
	
	/**
	 * @param accinsert the accinsert to set
	 */
	public void setAccinsert(String accinsert) {
		this.accinsert = accinsert;
	}
	
	/**
	 * @return the accupdate
	 */
	public String getAccupdate() {
		return accupdate;
	}
	
	/**
	 * @param accupdate the accupdate to set
	 */
	public void setAccupdate(String accupdate) {
		this.accupdate = accupdate;
	}
	
	/**
	 * @return the accdelete
	 */
	public String getAccdelete() {
		return accdelete;
	}
	
	/**
	 * @param accdelete the accdelete to set
	 */
	public void setAccdelete(String accdelete) {
		this.accdelete = accdelete;
	}
	
	
	/**
	 * @return the orden
	 */
	public String getOrden() {
		return orden;
	}
	
	/**
	 * @param orden the orden to set
	 */
	public void setOrden(String orden) {
		this.orden = orden;
	}
	
	
	
	/**
	 * @return the opcvista
	 */
	public String getOpcvista() {
		return opcvista;
	}
	
	/**
	 * @param opcvista the opcvista to set
	 */
	public void setOpcvista(String opcvista) {
		this.opcvista = opcvista;
	}
	
	
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
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
	//O genera un error
	private String orden = "5, 1"; //Orden de la consulta
	private int rows; //Registros de tabla
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	private String grupo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("grupo"); //Usuario logeado
	//private String tipusr = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipusr"); //Usuario logeado
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;
	
	     /**
	     * Inserta menu para opciones de roles.
	     * @throws NamingException 
	     * @throws IOException 
	     **/
	    public void insert() throws NamingException, IOException  {
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    	String[] vecrol = rol.split("\\ - ", -1);
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con = ds.getConnection();
	
	            String query = "insert into seg006 " +
	            		" select trim('" + vecrol[0] +  "'), trim(codopc), desopc, '0','0','0','0'," + Integer.parseInt(grupo) + 
	            		" from seg004 " +
	            		" where codopc not in (select trim(numopc)" +
	            		                       " from seg006 " +
	            		                       "  where p_codrol = '" + vecrol[0] + "'" +
	            		                       		" and grupo = " +  Integer.parseInt(grupo) + ")" ;
	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("seg006MnuCgo"), "");
	            } catch (SQLException e)  {
	            	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
	            }           
	            pstmt.close();
	            con.close();
	        } catch (Exception e) {
	        }
	    
	    	FacesContext.getCurrentInstance().addMessage(null, msj);
	    	}
	    }
	    
	    	   
	
	    /**
	     * Elimina menu para opciones de roles.
	     * @throws NamingException 
	     * @throws IOException 
	     **/
	    public void delete() throws NamingException, IOException  {
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    	String[] vecrol = rol.split("\\ - ", -1);
	    	//Valida que los campos no sean nulos
	    	//Chequea que las variables no sean nulas 
	    	if (rol.equals("")){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("seg006El"), "");
	    	} else {
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
		        	query = "DELETE from seg006 WHERE p_codrol ='" + vecrol[0].toUpperCase() + "'";
		             break;
		        case "PostgreSQL":
		        	query = "DELETE from seg006 WHERE p_codrol ='" + vecrol[0].toUpperCase() + "'";
		             break;
		  		}
	
	            pstmt = con.prepareStatement(query);
	
	            // Antes de ejecutar valida si existe el registro en la base de Datos.
	            consulta.selectPntGenerica("select p_codrol from seg006 where p_codrol ='" + vecrol[0].toUpperCase() + "'", JNDI);
	               if (consulta.getRows()==0){
	            	   msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoDelete"), "");
	               } else {
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
	            } catch (SQLException e) {
	            	msj =   new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	                }
	            pstmt.close();
	            con.close();
	            
	        } catch (Exception e) {
	            e.getMessage();
	        }
	    	}
	    	FacesContext.getCurrentInstance().addMessage(null, msj);
	    	}
	    }
	    
	    /**
	     * Actualiza acceso a menu para opciones de roles, acceso, insertar, eliminar, modificar
	     * @param: accopc. Acceso al menú valores 0 y 1
	     * @param: tipo. accopc, accinsert, accupdate, accdelete
	     **/
	    private void updateSeg006(String accopc, String tipo)  {
	    	//Valida que los campos no sean nulos
	    	//Chequea que las variables no sean nulas 
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con =  ds.getConnection();
	
	            String query = "UPDATE SEG006 SET " + tipo + "='" + accopc + "'" 
	                    + " WHERE P_CODROL||numopc IN (" + param.toLowerCase() + ")" ;
	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnUpdate"), "");
	            } catch (SQLException e) {
	                e.printStackTrace();
	                msj =   new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	            pstmt.close();
	            con.close();
	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    	
	    	FacesContext.getCurrentInstance().addMessage(null, msj);
	    }
	    
	    
	    /**
	     * Actualiza acceso a menu para opciones de roles, acceso, insertar, eliminar, modifica,
	     * dependiendo del tipo de opción que se esté cargando
	     * @throws SQLException 
	     * @throws IOException 
	     **/
	    public void update() throws  NamingException, SQLException, IOException { 
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    	String accopc = null;
	    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	String[] paramAcc = request.getParameterValues("toAcc");
	    	String[] paramDacc = request.getParameterValues("toDacc");
	    	if (paramAcc==null && paramDacc==null){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("seg006Up1"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    		opcvista = "1";
	    	} else 	if(paramAcc!=null && paramDacc!=null){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("seg006Opc"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    		opcvista = "1";
	    	} else {
	    		 switch(opcvista) {
	    		 case "1": 
	    			 if(paramAcc!=null && paramDacc==null){
	    		    		accopc = "0";
	    		     } else if(paramAcc==null && paramDacc!=null){
	    		    		accopc = "1";
	    		    	}
	     			 updateSeg006(accopc, "accopc"); //Acceso al menu
	    		     break;
	    		 case "2":
	    			 if(paramAcc!=null && paramDacc==null){
	 		    		accopc = "0";
	 		    	 } else if(paramAcc==null && paramDacc!=null){
	 		    		accopc = "1";
	 		           }
	    			 updateSeg006(accopc, "accinsert"); //Acceso a insertar
	    		     break;
	    		 case "3": 
	    			 if(paramAcc!=null && paramDacc==null){
	 		    		accopc = "0";
	 		    	 } else if(paramAcc==null && paramDacc!=null){
	 		    		accopc = "1";
	 		           }
	    			 updateSeg006(accopc, "accupdate"); //Acceso a modificar
	    		     break;
	    		 case "4": 
	    			 if(paramAcc!=null && paramDacc==null){
	 		    		accopc = "0";
	 		    	 } else if(paramAcc==null && paramDacc!=null){
	 		    		accopc = "1";
	 		    	   }
	    			 updateSeg006(accopc, "accdelete"); //Acceso a eliminar
	    		     break;
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
	  	public void select(int first, int pageSize, String sortField, Object filterValue) throws  NamingException, SQLException, IOException {
	  		
	  		Context initContext = new InitialContext();     
	  		DataSource ds = (DataSource) initContext.lookup(JNDI);
	  		
	  		if(rol==null){
	        	rol = " - ";
	        }
	        if(rol==""){
	        	rol = " - ";
	        }
	  		
	  		String[] vecrol = rol.split("\\ - ", -1);
	  	
	  		con = ds.getConnection();
	  		
	  		 //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	 		DatabaseMetaData databaseMetaData = con.getMetaData();
	 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección

	 		String query = "";
	 		
	 		switch ( productName ) {
	        case "Oracle":
	         	//Si opción es 1 muestra acceso
		 		 switch(opcvista) {
				 case "1":
		 		//Consulta paginada
		 	  		//Consulta paginada
		 			query += " SELECT  case when length(numopc)<=4 then trim(upper(numopc)) else trim(numopc) end, case when length(numopc)<=4 then trim(upper(desopc)) else trim(desopc) end, case when a.accopc='0' then '" + getMessage("seg006A") + "' else '" +  getMessage("seg006N") + "' end, a.grupo";
		 			query += " , trim(a.p_codrol) , b.desrol";
		 			query += " FROM SEG006 A, SEG002 B";
		  		    query += " WHERE a.p_codrol=b.codrol";
		  		    query += " and a.grupo=b.grupo";
		  		    query += " and a.p_codrol like '" + vecrol[0] + "%'"; 
		  		    query += " and upper(a.numopc)||upper(a.desopc) like '%" + ((String) filterValue).toUpperCase() + "%'";
		  		    query += " and a.grupo = '" +  grupo + "'";
		  		    query += " order by 1,5";
		  		 break;
				 case "2":
		 	  		//Consulta paginada
		 	  	  		//Consulta paginada
		 	  			query += " SELECT  case when length(numopc)<=4 then trim(upper(numopc)) else trim(numopc) end, case when length(numopc)<=4 then trim(upper(desopc)) else trim(desopc) end";
		 	   		    query += ", case when accinsert='0' then '" + getMessage("seg006A") + "' else '" +  getMessage("seg006N") + "' end, a.grupo";
		 	   		    query += " , trim(a.p_codrol) , b.desrol";
		 	   		    query += " FROM SEG006 A, SEG002 B";
		  		        query += " WHERE a.p_codrol=b.codrol";
		  		        query += " and a.grupo=b.grupo";
		  		        query += " and a.p_codrol like '" + vecrol[0] + "%'";
		  		        query += " and a.numopc||a.desopc like '%" + filterValue + "%'";
		  		        query += " and a.grupo = '" +  grupo + "'";
		  		        query += " order by 1,5";
		  		 break;
				 case "3": 
		 	   //Si opción es 3 muestra modificar
		 	  		//Consulta paginada
		 	  	  		//Consulta paginada
		 	  			query += " SELECT  case when length(numopc)<=4 then trim(upper(numopc)) else trim(numopc) end, case when length(numopc)<=4 then trim(upper(desopc)) else trim(desopc) end";
		 	   		    query += ", case when accupdate='0' then '" + getMessage("seg006A") + "' else '" +  getMessage("seg006N") + "' end, a.grupo";
		 	   		    query += " , trim(a.p_codrol) , b.desrol";
		 	   		    query += " FROM SEG006 A, SEG002 B";
		  		        query += " WHERE a.p_codrol=b.codrol";
		  		        query += " and a.grupo=b.grupo";
		  		        query += " and a.p_codrol like '" + vecrol[0] + "%'";
		  		        query += " and a.numopc||a.desopc like '%" + filterValue + "%'";
		  	   		    query += " and a.grupo = '" +  grupo + "'";
		  		        query += " order by 1,5";
		  		     break;
				 case "4": 
		 	//Si opción es 4 muestra eliminar
		 	  		//Consulta paginada
		 	  	  		//Consulta paginada
		 	  			query += " SELECT  case when length(numopc)<=4 then trim(upper(numopc)) else trim(numopc) end, case when length(numopc)<=4 then trim(upper(desopc)) else trim(desopc) end";
		 	   		    query += ", case when accdelete='0' then '" + getMessage("seg006A") + "' else '" +  getMessage("seg006N") + "' end, a.grupo";
		 	   		    query += " , trim(a.p_codrol) , b.desrol";
		 	   		    query += " FROM SEG006 A, SEG002 B";
		  		        query += " WHERE a.p_codrol=b.codrol";
		  		        query += " and a.grupo=b.grupo";
		  		        query += " and a.p_codrol like '" + vecrol[0] + "%'"; 
		  		        query += " and a.numopc||a.desopc like '%" + filterValue + "%'";
		  		      query += " and a.grupo = '" +  grupo + "'";
		  		        query += " order by 1,5";
		 	  		}

	             break;
	        case "PostgreSQL":
	         	//Si opción es 1 muestra acceso
		 		 switch(opcvista) {
				 case "1":
		 		//Consulta paginada
		 	  		//Consulta paginada
		 			query += " SELECT  case when char_length(numopc)<=4 then trim(upper(numopc)) else trim(numopc) end, case when char_length(numopc)<=4 then trim(upper(desopc)) else trim(desopc) end, case when a.accopc='0' then '" + getMessage("seg006A") + "' else '" +  getMessage("seg006N") + "' end, a.grupo";
		 			query += " , trim(a.p_codrol) , b.desrol";
		 			query += " FROM SEG006 A, SEG002 B";
		  		    query += " WHERE a.p_codrol=b.codrol";
		  		    query += " and a.grupo=b.grupo";
		  		    query += " and a.p_codrol like '" + vecrol[0] + "%'"; 
		  		    query += " and upper(a.numopc)||upper(a.desopc) like '%" + ((String) filterValue).toUpperCase() + "%'";
		  		    query += " and CAST(a.grupo AS text) = '" +  grupo + "'";
		  		    query += " order by 1,5";
		           query += " LIMIT " + pageSize;
		           query += " OFFSET " + first;
		  		 break;
				 case "2":
		 	  		//Consulta paginada
		 	  	  		//Consulta paginada
		 	  			query += " SELECT  case when char_length(numopc)<=4 then trim(upper(numopc)) else trim(numopc) end, case when char_length(numopc)<=4 then trim(upper(desopc)) else trim(desopc) end";
		 	   		    query += ", case when accinsert='0' then '" + getMessage("seg006A") + "' else '" +  getMessage("seg006N") + "' end, a.grupo";
		 	   		    query += " , trim(a.p_codrol) , b.desrol";
		 	   		    query += " FROM SEG006 A, SEG002 B";
		  		        query += " WHERE a.p_codrol=b.codrol";
		  		        query += " and a.grupo=b.grupo";
		  		        query += " and a.p_codrol like '" + vecrol[0] + "%'";
		  		        query += " and a.numopc||a.desopc like '%" + filterValue + "%'";
		  	   		    query += " and CAST(a.grupo AS text) = '" +  grupo + "'";
		  		        query += " order by 1,5";
		  	            query += " LIMIT " + pageSize;
		  	            query += " OFFSET " + first;
		  		 break;
				 case "3": 
		 	   //Si opción es 3 muestra modificar
		 	  		//Consulta paginada
		 	  	  		//Consulta paginada
		 	  			query += " SELECT  case when char_length(numopc)<=4 then trim(upper(numopc)) else trim(numopc) end, case when char_length(numopc)<=4 then trim(upper(desopc)) else trim(desopc) end";
		 	   		    query += ", case when accupdate='0' then '" + getMessage("seg006A") + "' else '" +  getMessage("seg006N") + "' end, a.grupo";
		 	   		    query += " , trim(a.p_codrol) , b.desrol";
		 	   		    query += " FROM SEG006 A, SEG002 B";
		  		        query += " WHERE a.p_codrol=b.codrol";
		  		        query += " and a.grupo=b.grupo";
		  		        query += " and a.p_codrol like '" + vecrol[0] + "%'";
		  		        query += " and a.numopc||a.desopc like '%" + filterValue + "%'";
		  	   		    query += " and CAST(a.grupo AS text) = '" +  grupo + "'";
		  		        query += " order by 1,5";
		  	            query += " LIMIT " + pageSize;
		  	            query += " OFFSET " + first;
		  		     break;
				 case "4": 
		 	//Si opción es 4 muestra eliminar
		 	  		//Consulta paginada
		 	  	  		//Consulta paginada
		 	  			query += " SELECT  case when char_length(numopc)<=4 then trim(upper(numopc)) else trim(numopc) end, case when char_length(numopc)<=4 then trim(upper(desopc)) else trim(desopc) end";
		 	   		    query += ", case when accdelete='0' then '" + getMessage("seg006A") + "' else '" +  getMessage("seg006N") + "' end, a.grupo";
		 	   		    query += " , trim(a.p_codrol) , b.desrol";
		 	   		    query += " FROM SEG006 A, SEG002 B";
		  		        query += " WHERE a.p_codrol=b.codrol";
		  		        query += " and a.grupo=b.grupo";
		  		        query += " and a.p_codrol like '" + vecrol[0] + "%'"; 
		  		        query += " and a.numopc||a.desopc like '%" + filterValue + "%'";
		  	   		    query += " and CAST(a.grupo AS text) = '" +  grupo + "'";
		  		        query += " order by 1,5";
		  	            query += " LIMIT " + pageSize;
		  	            query += " OFFSET " + first;
		 	  		}
	             break;          		   
	  		}

	  		
	 
	       pstmt = con.prepareStatement(query);
	       //System.out.println(query);
	  		
	         r =  pstmt.executeQuery();
	        
	  		
	               
	        while (r.next()){
	        	Menuopc select = new Menuopc();
	            select.setVnumopc(r.getString(1));
	            select.setVdesopc(r.getString(2));
	            select.setVaccopc(r.getString(3));
	            select.setVgrupo(r.getString(4));
	            select.setVpcodrol(r.getString(5));
	            select.setVdesrol(r.getString(6));
	        	//Agrega la lista
	        	list.add(select);
	        	//rows = list.size();
	        }
	        //Cierra las conecciones
	        pstmt.close();
	        con.close();
	        r.close();
	  	}
	  	
	  	public void buscar() throws NamingException, SQLException, IOException{
	  		list.clear();
	  		select(1, 15, "vdesrol", filterValue);
	  	}
	  
	   /**
	 	 * @return the rows
	 	 */
	 	public int getRows() {
	 		return rows;
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
	   		
	   		if(rol==null){
	        	rol = " - ";
	        }
	        if(rol==""){
	        	rol = " - ";
	        }
	  		
	  		String[] vecrol = rol.split("\\ - ", -1);
	  		
	  		 //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	 		DatabaseMetaData databaseMetaData = con.getMetaData();
	 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
	 		
	 		String query = null;
	 		
	 		switch ( productName ) {
	        case "Oracle":
	        	query = "SELECT count_seg006(" + Integer.parseInt(grupo) + ",'" + ((String) filterValue).toUpperCase() + "','" + vecrol[0] + "') from dual";
	             break;
	        case "PostgreSQL":
	        	query = "SELECT count_seg006(" + Integer.parseInt(grupo) + ",'" + ((String) filterValue).toUpperCase() + "','" + vecrol[0] + "')";
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
	     * Retorna el tipo de acceso de visualización
	     * @return String
	     **/
	     public String accVis(){
	     //Si registros página es mayor a paginación
	     String vLretorno = "";
	   //Si opción es 1 muestra acceso
			if(opcvista.equals("1")){
				vLretorno = getMessage("seg006Opc1");
			}
			//Si opción es 1 muestra insert
			if(opcvista.equals("2")){
				vLretorno = getMessage("seg006Opc2");
			}
			//Si opción es 1 muestra update
			if(opcvista.equals("3")){
				vLretorno = getMessage("seg006Opc3");
			}
			//Si opción es 1 muestra delete
			if(opcvista.equals("4")){
				vLretorno = getMessage("seg006Opc4");
			}
	     return vLretorno;
	     }
	
	
	    public void reset(){
	    	rol = "";
	    }
	
	
	}
