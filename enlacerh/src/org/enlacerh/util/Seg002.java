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

	import org.apache.commons.lang3.StringUtils;
import org.enlacerh.getset.Rol;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
	
	/**
	 *
	 * @author Andres
	 */
	@ManagedBean
	@ViewScoped
	public class Seg002 extends Utils implements Serializable {
	
	private LazyDataModel<Rol> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Rol> getLazyModel() {
			return lazyModel;
		}
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
	public Seg002() {
		if (!rq.isRequestedSessionIdValid()) {
			//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
		}  else {
		lazyModel  = new LazyDataModel<Rol>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
	        @Override
			public List<Rol> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	
	private String codrol = "";
	private String desrol = "";
	private String localgrupo = "";
	private int validarOperacion = 0;//Param guardar para validar si guarda o actualiza
	private Object filterValue = "";
	private List<Rol> list = new ArrayList<Rol>();
	
		/**
	 * @return the codrol
	 */
	public String getCodrol() {
		return codrol;
	}
	
	/**
	 * @param codrol the codrol to set
	 */
	public void setCodrol(String codrol) {
		this.codrol = codrol;
	}
	
	/**
	 * @return the desrol
	 */
	public String getDesrol() {
		return desrol;
	}
	
	/**
	 * @param desrol the desrol to set
	 */
	public void setDesrol(String desrol) {
		this.desrol = desrol;
	}
	
	/**
	 * @return the validarOperacion
	 */
	public int getValidarOperacion() {
		return validarOperacion;
	}
	
	/**
	 * @param validarOperacion the validarOperacion to set
	 */
	public void setValidarOperacion(int validarOperacion) {
		this.validarOperacion = validarOperacion;
	}
	
	
	/**
	 * @return the localgrupo
	 */
	public String getLocalgrupo() {
		return localgrupo;
	}
	
	/**
	 * @param localgrupo the localgrupo to set
	 */
	public void setLocalgrupo(String localgrupo) {
		this.localgrupo = localgrupo;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null; 
	PntGenerica consulta = new PntGenerica();
	Accmnu acc = new Accmnu(); //Objeto tipo acc
	boolean vGacc; //Validador de opciones del menú
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
	     * Inserta roles.
	     **/
	    private void insert()  { 
	       try {
	    	   Context initContext = new InitialContext();     
	       	   DataSource ds = (DataSource) initContext.lookup(JNDI);
	           con = ds.getConnection();
	           
	            String query = "INSERT INTO Seg002 VALUES (trim(?),?,?,'" + getFecha() + "',?,'" + getFecha() + "'," + Integer.parseInt(grupo) + ")";
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, codrol.toUpperCase());
	            pstmt.setString(2, desrol.toUpperCase());
	            pstmt.setString(3, login);
	            pstmt.setString(4, login);
	            
	            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
	            vGacc = acc.valAccmnu("seg01", "insert", login, JNDI);//LLama a la funcion que valida las opciones del rol
	            if (vGacc){
	               msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnAccInsert"), "");
	            }  else {
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                limpiarValores();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), ""); 
	            } catch (SQLException e)  {
	            	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	            }
	            pstmt.close();
	            con.close();
	        } catch (Exception e) {
	        }
	       FacesContext.getCurrentInstance().addMessage(null, msj); 
	    }
	
	    /**
	     * Borra Paises
	     * <p>
	     * Parametros del metodo: String codpai. Pool de conecciones
	     * @throws NamingException 
	     * @throws IOException 
	     **/
	    public void delete() throws NamingException, IOException  {  
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	String[] chkbox = request.getParameterValues("toDelete");
	    	
	    	if(codrol.equals(".")){
	    		codrol = "";
	    	}
	
	    	if (chkbox==null){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("del"), ""); 
	    	} else {
	    	try {
	    		Context initContext = new InitialContext();     
	        	   DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con = ds.getConnection();
	          //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
		 		DatabaseMetaData databaseMetaData = con.getMetaData();
		 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección

		 		String query = "";
	            
	            String param = "'" + StringUtils.join(chkbox, "','") + "'";
	            
	            switch ( productName ) {
		        case "Oracle":
		        	query = "DELETE FROM seg002 WHERE CODROL||GRUPO in (" + param + ")";
		             break;
		        case "PostgreSQL":
		        	query = "DELETE FROM seg002 WHERE CODROL||cast(GRUPO as text) in (" + param + ")";
		             break;
		  		}
	  
	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);
	            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
	            vGacc = acc.valAccmnu("seg01", "delete", login, JNDI);//LLama a la funcion que valida las opciones del rol
	            if (vGacc) {
	            	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnAccDelete"), "");
	            } else {
	            
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                if (pstmt.getUpdateCount() <= 1) {
	                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
	                } else {
	                	msj =  new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDeletes"), "");
	                }
	                limpiarValores();
	            } catch (SQLException e) {
	                msj =   new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	
	            pstmt.close();
	            con.close();
	               }        
	        } catch (Exception e) {
	        }
	    	}
	    	FacesContext.getCurrentInstance().addMessage(null, msj); 
	    	}
	    }
	    
	    
	    /**
	     * Actualiza roles
	     **/
	    private void update()  {
	         try {
	        	 Context initContext = new InitialContext();     
	         	   DataSource ds = (DataSource) initContext.lookup(JNDI);
	             con = ds.getConnection();
	             
	            String query = "UPDATE Seg002";
	             query += " SET desrol = ?, usract = ?, fecact='" + getFecha() + "'";
	             query += " WHERE codrol = ?";
	             query += " and grupo = " + Integer.parseInt(grupo);
	            //System.out.println(query);
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, desrol.toUpperCase());
	            pstmt.setString(2, login);
	            pstmt.setString(3, codrol.toUpperCase());
	
	            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
	            vGacc = acc.valAccmnu("seg01", "update", login, JNDI);//LLama a la funcion que valida las opciones del rol
	            if (vGacc){
	            	msj =   new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnAccUpdate"), "");
	            }  else {
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                desrol="";
	                localgrupo="";
	                validarOperacion = 0;
	                if(pstmt.getUpdateCount()==0){
	                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
	                } else {
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
	                }
	            } catch (SQLException e) {
	            	msj =   new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	                }
	            pstmt.close();
	            con.close();
	               
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	         FacesContext.getCurrentInstance().addMessage(null, msj); 
	    }
	    
	    
	    /**
	     * Genera las operaciones de guardar o modificar
	     * @throws NamingException 
	     * @throws SQLException 
	     * @throws IOException 
	     **/ 
	    public void guardar() throws NamingException, SQLException, IOException{   
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    	if(validarOperacion==0){
	    		insert();
	    	} else {
	    		update();
	    	}
	    	}
	    }
	    
	   
	   
	
		/**
	     * Leer Datos de roles
	     * @throws NamingException 
	     * @throws IOException 
	     **/ 	
	
		public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, NamingException, IOException{
			  		
			Context initContext = new InitialContext();     
	    	DataSource ds = (DataSource) initContext.lookup(JNDI);
	        con = ds.getConnection();
	 	   //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	 		DatabaseMetaData databaseMetaData = con.getMetaData();
	 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección

	 		String query = "";
	 		
	 		switch ( productName ) {
	        case "Oracle":
	        	//Consulta paginada
	        	query = "  select * from ";
	        	query += " ( select query.*, rownum as rn from";
		        query += "  ( SELECT trim(codrol), trim(desrol), trim(grupo)";
		  		query += " FROM SEG002";
		  		query += " where codrol like '" + codrol + "%'";
		  		query += " and codrol||desrol like '%" + ((String) filterValue).toUpperCase() + "%'";
		   		query += " and grupo = '" +  grupo + "'";
		   		query += " order by 1) query";
			    query += " ) where rownum <= " + pageSize ;
			    query += " and rn > (" + first + ")";

	             break;
	        case "PostgreSQL":
	        	//Consulta paginada
		        query = "SELECT trim(codrol), trim(desrol), trim(CAST(grupo AS text))";
		  		query += " FROM SEG002";
		  		query += " where codrol like '" + codrol + "%'";
		  		query += " and codrol||desrol like '%" + ((String) filterValue).toUpperCase() + "%'";
		   		query += " and CAST(grupo AS text) = '" +  grupo + "'";
		   		query += " order by " + sortField.replace("v", "") ;
		        query += " LIMIT " + pageSize;
		        query += " OFFSET " + first;
	             break;          		   
	  		}
	  		  		
	  		
	
	        
	        pstmt = con.prepareStatement(query);
	        //System.out.println(query);
	  		
	        r =  pstmt.executeQuery();
	        
	  	
	        
	        while (r.next()){
	        	Rol select = new Rol();
	        	select.setVcodrol(r.getString(1));
	        	select.setVdesrol(r.getString(2));
	        	select.setVgrupo(r.getString(3));
	        	//Agrega la lista
	        	list.add(select);
	        	rows = list.size();
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
	 		
	 		switch ( productName ) {
	        case "Oracle":
	        	query = "SELECT count_seg002(" + Integer.parseInt(grupo) + ",'" + ((String) filterValue).toUpperCase() +  "') from dual";
	             break;
	        case "PostgreSQL":
	        	query = "SELECT count_seg002(" + Integer.parseInt(grupo) + ",'" + ((String) filterValue).toUpperCase() +  "')";
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
	    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    /////////////////////////////////////OPERACIONES BASICAS//////////////////////////////////////////////////////////////
	    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
	    /**
	     * Limpia los valores
	     **/
		private void limpiarValores(){
			codrol = "";
			desrol = "";	
			validarOperacion = 0;
			localgrupo = "";
		}
		
		
	//public static void main (String args []) throws NamingException{
	//Seg002 a = new Seg002();
	//a.insertSeg002("2|aaa", "", "ADMIN");
	//a.deleteSeg002("'NA'", "");
	//a.selectSeg001("|", 10, 0, "1", "");
	//System.out.println(a.getMsj());
	//}
	
	}
