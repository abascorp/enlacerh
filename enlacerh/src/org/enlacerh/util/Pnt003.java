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
	
	import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

	import javax.annotation.PostConstruct;
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
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
	
	 
	/**
	 *
	 * @author Andres Dominguez 08/02/2009
	 */
	/**
	 * Clase para leer, insertar, modificar y eliminar registros de la tabla
	 * Pnt003(Paises)
	 *  */
	@ManagedBean
	@ViewScoped
	public class Pnt003 extends Utils implements Serializable {
		
		private LazyDataModel<Pnt003> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Pnt003> getLazyModel() {
			return lazyModel;
		}
		
		
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	
		
		// Constructor
	    @PostConstruct
		public void init() {
	    	if (!rq.isRequestedSessionIdValid()) {
				//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
				RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
			}  else {
	    	
			lazyModel  = new LazyDataModel<Pnt003>(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 7217573531435419432L;
				
	            @Override
				public List<Pnt003> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
					} catch (SQLException | NamingException e) {	
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
	    
	
	//Campos de la tabla para getter y setter
	private String codpai = "";
	private String despai = "";
	private String localgrupo = "";
	private int validarOperacion = 0;//Param guardar para validar si guarda o actualiza
	private Object filterValue = "";
	private List<Pnt003> list = new ArrayList<Pnt003>();//LIstar paises
	
	
	public String getCodpai() {
		return codpai;
	}
	
	public void setCodpai(String codpai) {
		this.codpai = codpai;
	}
	
	public String getDespai() {
		return despai;
	}
	
	public void setDespai(String despai) {
		this.despai = despai;
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
	     * Inserta Paises.
	     * <p>
	     * Parametros del Metodo: String codpai, String despai. Pool de conecciones y login
		 * @throws NamingException 
	     **/
	    private void insert() throws NamingException {
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con = ds.getConnection();
	
	            String query = "INSERT INTO PNT003 VALUES (" + Integer.parseInt(codpai) + ",?,?,'" + getFecha() + "',?,'" + getFecha() + "'," + Integer.parseInt(grupo) + ")";
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, despai.toUpperCase());
	            pstmt.setString(2, login);
	            pstmt.setString(3, login);
	            //System.out.println(query);
	            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
	            vGacc = acc.valAccmnu("bas01", "insert", login, JNDI);//LLama a la funcion que valida las opciones del rol
	            if (vGacc) {
	            	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnAccInsert"), "");
	            } else {
	            try {
	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
	                limpiarValores();
	            } catch (SQLException e)  {
	                 //e.printStackTrace();
	                 msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	                }//Fin validacion 
	         
	            pstmt.close();
	            con.close();           
	        } catch (Exception e) {
	            e.printStackTrace();
	            msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
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
	    	
	    	if (chkbox==null){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("pnt003DelPai"), "");
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
		        	query = "DELETE FROM PNT003 WHERE codpai||grupo in (" + param + ")";
		             break;
		        case "PostgreSQL":
		        	query = "DELETE FROM PNT003 WHERE CAST(codpai AS text)||CAST(grupo AS text) in (" + param + ")";
		             break;
		  		}

	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);
	            
	            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
	            vGacc = acc.valAccmnu("bas01", "delete", login, JNDI);//LLama a la funcion que valida las opciones del rol
	            if (vGacc) {
	            	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnAccDelete"), "");
	            } else {
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                if (pstmt.getUpdateCount() <= 1) {
	                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
	                } else {
	                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDeletes"), ""); 
	                }
	                limpiarValores();	
	            } catch (SQLException e) {
	                e.printStackTrace();
	                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	
	            pstmt.close();
	            con.close();
	               }
	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    	}
	    	FacesContext.getCurrentInstance().addMessage(null, msj); 
	    	}
	    }
	    
	    
	
	
	    /**
	     * Actualiza Paises
	     **/
	    private void update()  {
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
		        	query = "UPDATE PNT003";
			     	query += " SET DESPAI = ?";
			        query += " , FECACT = '" + getFecha() + "' , USRACT = '" + login + "'";
			        query += " WHERE codpai = trim('" + codpai + "')";
			        query += " and grupo = " + Integer.parseInt(localgrupo);
		             break;
		        case "PostgreSQL":
		        	query = "UPDATE PNT003";
			     	query += " SET DESPAI = ?";
			        query += " , FECACT = '" + getFecha() + "' , USRACT = '" + login + "'";
			        query += " WHERE CAST(codpai AS text) = trim('" + codpai + "')";
			        query += " and grupo = " + Integer.parseInt(localgrupo);
		             break;
		  		}
	
	        	
	            //System.out.println(query);
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, despai.toUpperCase());
	            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
	            vGacc = acc.valAccmnu("bas01", "update", login, JNDI);//LLama a la funcion que valida las opciones del rol
	            if (vGacc) {
	            	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnAccUpdate"), "");
	            } else {
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                if(pstmt.getUpdateCount()==0){
	                	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
	                } else {
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
	                }
	                despai = "";
	                localgrupo="";
	                validarOperacion = 0;
	            } catch (SQLException e) {
	                e.printStackTrace();
	                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL,  e.getMessage(), "");
	            }
	
	            pstmt.close();
	            con.close();
	              
	                }//Fin validacion de licencia
	
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
	     * Leer Datos de paises
	     * @throws NamingException 
	     * @throws IOException 
	     **/ 	
	  	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, NamingException {
	     try {	
	    	 //System.out.println("JNDI: " + JNDI);
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
		  		query += " ( SELECT codpai, trim(despai), grupo";
		        query += " FROM Pnt003";
		        query += " where codpai like '" + codpai + "%'";
		        query += " and codpai||despai like '%" + ((String) filterValue).toUpperCase() + "%'";
		        query += " and grupo = '" +  grupo + "'";
		        query += " order by " + sortField.replace("v", "") + ") query";
			    query += " ) where rownum <= " + pageSize ;
			    query += " and rn > (" + first + ")";

	             break;
	        case "PostgreSQL":
	        	//Consulta paginada
		  		query = "SELECT codpai, trim(despai), grupo";
		        query += " FROM Pnt003";
		        query += " where CAST(codpai AS text) like '" + codpai + "%'";
		        query += " and cast(codpai as text)||despai like '%" + ((String) filterValue).toUpperCase() + "%'";
		        query += " and CAST(grupo AS text) = '" +  grupo + "'";
		        query += " order by " + sortField ;
		        query += " LIMIT " + pageSize;
		        query += " OFFSET " + first;

	             break;          		   
	  		}
	   		
	
	   	
	        
	        pstmt = con.prepareStatement(query);
	        //System.out.println(query);
	
	         r =  pstmt.executeQuery();
	        
	        
	        while (r.next()){
	        	Pnt003 select = new Pnt003();
	        	select.setCodpai(r.getString(1));
	        	select.setDespai(r.getString(2));
	        	select.setLocalgrupo(r.getString(3));
	        	//Agrega la lista
	        	list.add(select);
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
	     * Leer registros en la tabla
	     * @throws NamingException 
	     * @throws IOException 
	     **/ 	
	  	public void counter(Object filterValue ) throws SQLException, NamingException {
	     try {	
	    	Context initContext = new InitialContext();     
	   		DataSource ds = (DataSource) initContext.lookup(JNDI);
	 		
	 		String query = null;
	
	   		con = ds.getConnection();
	   	  //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	 		DatabaseMetaData databaseMetaData = con.getMetaData();
	 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
	 		
	 		switch ( productName ) {
	        case "Oracle":
	        	query = "SELECT count_pnt003(" + Integer.parseInt(grupo) + ",'" + ((String) filterValue).toUpperCase() + "') from dual";
	             break;
	        case "PostgreSQL":
	        	query = "SELECT count_pnt003(" + Integer.parseInt(grupo) + ",'" + ((String) filterValue).toUpperCase() + "')";
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
		
	
		/**
	     * Limpia los valores
	     **/
		public void limpiarValores(){
			codpai = "";
			despai = "";	
			validarOperacion = 0;
			localgrupo = "";
		}
	
	}
	
