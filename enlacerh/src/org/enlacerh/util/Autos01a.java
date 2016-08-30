	package org.enlacerh.util;
	
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.enlacerh.getset.Registros;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
	
	@ManagedBean
	@ViewScoped
	public class Autos01a extends Utils implements Serializable {
		
	private LazyDataModel<Registros> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Registros> getLazyModel() {
			return lazyModel;
		}
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		@PostConstruct
		public void init() {
			if (!rq.isRequestedSessionIdValid()) {
				//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
				RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
			}  else {
			lazyModel  = new LazyDataModel<Registros>(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 7217573531435419432L;
				
		        @Override
				public List<Registros> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
						selectAutos01a(first, pageSize,sortField, filterValue);
						//Counter
						counterAutos01a(filterValue);
						//Contador lazy
						lazyModel.setRowCount(rows);  //Necesario para crear la paginación
					} catch (SQLException | NamingException  e) {	
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
		
		public void cargarTb() throws SQLException, NamingException{
			//Consulta
			selectAutos01a(1, 15,"", "");
			//Counter
			counterAutos01a("");
		}
		
		//Campos de la tabla para getter y setter
		private String pcodcia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ciacopia");
		private String cedula = "";
		private String mail = "";
		StringMD md = new StringMD();//Objeto encriptador
		private Object filterValue = "";
		private List<Registros> list = new ArrayList<Registros>();
        
        	

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
		 * @return the cedula
		 */
		public String getCedula() {
			return cedula;
		}
	
		/**
		 * @param cedula the cedula to set
		 */
		public void setCedula(String cedula) {
			this.cedula = cedula;
		}
	
		
	
		/**
		 * @return the mail
		 */
		public String getMail() {
			return mail;
		}
	
		/**
		 * @param mail the mail to set
		 */
		public void setMail(String mail) {
			this.mail = mail;
		}
	
	
	
	
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
		FacesMessage msj = null; 
		PntGenerica consulta = new PntGenerica();
		Accmnu acc = new Accmnu(); //Objeto tipo acc
		boolean vGacc; //Validador de opciones del menú
		private int rows; //Registros de tabla
		private String grupo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("grupo"); //Usuario logeado
		private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
		//Coneccion a base de datos
		//Pool de conecciones JNDI
		Connection con;
		PreparedStatement pstmt = null;
		ResultSet r;
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////OPERACIONES BASICAS//////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Limpia los valores
	 **/
	private void limpiarValores(){
		pcodcia = "";
		cedula = "";
		mail = "";
        nombre = "";
        apellido = "";
	}
	
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////CODIGO DE VALIDACION PARA REGISTRO DE USUARIO//////////////////////////////////////////////
			private String nombre = "";
			private String apellido = "";
			private String randomKey = "OPEN"+md.randomKey();
			
			/**
			 * @return the nombre
			 */
			public String getNombre() {
				return nombre;
			}

			/**
			 * @param nombre the nombre to set
			 */
			public void setNombre(String nombre) {
				this.nombre = nombre;
			}

			/**
			 * @return the apellido
			 */
			public String getApellido() {
				return apellido;
			}

			/**
			 * @param apellido the apellido to set
			 */
			public void setApellido(String apellido) {
				this.apellido = apellido;
			}
			
	        /*
	         * Inserta registros en tabla de validación de registro de usuarios
	         */
	        
			public void insertAutos01a(){
				try {
		        	Context initContext = new InitialContext();     
		        	DataSource ds = (DataSource) initContext.lookup(JNDI);
		            con = ds.getConnection();
		            
		            String[] veccodcia = pcodcia.split("\\ - ", -1);
		            
		            //System.out.println(vnombre.toUpperCase());
		            //System.out.println(vapellido.toUpperCase());
		            //System.out.println(vmail.toLowerCase());
		            //System.out.println(randomKey);
		            //System.out.println(Integer.parseInt(cedula));
		            
		            		
		            String query = "INSERT INTO autos01a VALUES (" + Integer.parseInt(cedula) + ",?,?,?,?,'" + getFecha() + "'," + Integer.parseInt(grupo) + ", '0', ?)";
		            //ystem.out.println(query);
		            pstmt = con.prepareStatement(query);
		            pstmt.setString(1, nombre.toUpperCase());
		            pstmt.setString(2, apellido.toUpperCase());
		            pstmt.setString(3, mail.toLowerCase());
		            pstmt.setString(4, randomKey);
		            pstmt.setString(5, veccodcia[0]);
		            
		            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
		            vGacc = acc.valAccmnu("bas05", "insert", login, JNDI);//LLama a la funcion que valida las opciones del rol
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
			
			
			/*
	         * Borra registros en tabla de validación de registro de usuarios
	         */
			public void deleteAutos01a() throws NamingException, IOException{
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
			        	query = "DELETE FROM autos01a WHERE cedula||grupo in (" + param + ")";
			             break;
			        case "PostgreSQL":
			        	query = "DELETE FROM autos01a WHERE CAST(cedula AS text)||cast(grupo as text) in (" + param + ")";
			             break;
			  		}

		            pstmt = con.prepareStatement(query);
		            //System.out.println(query);
		            
		            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
		            vGacc = acc.valAccmnu("bas05", "delete", login, JNDI);//LLama a la funcion que valida las opciones del rol
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
			
			/*
	         * Envía a usuarios registros
	         */
			public void enviarrandomkey(){
				int vrows = 0;
				String[][] vltabla;
				Sendmail vmail = new Sendmail();
				String query = "select trim(mail), trim(codigo_validacion), cedula from autos01a where estatus = '0' and grupo = " + grupo;
				//System.out.println(query);
				try {
					consulta.selectPntGenerica(query, JNDI);
					vrows  = consulta.getRows();
				} catch (NamingException e) {
					e.printStackTrace();
				}
				//System.out.println(vrows);
				if (vrows > 0){
					vltabla = consulta.getArray();
					for (int i = 0; i < vrows; i++){
						//System.out.println(i);
						vmail.enviarRandomkey(vltabla[i][0], vltabla[i][1]);
						update(vltabla[i][2], grupo);
					}
				} else {
					msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("autos01mailno"), "");
					FacesContext.getCurrentInstance().addMessage(null, msj);
				}
			}
			
			
			/*
	         * Envía a usuarios registros
	         */
			public void enviarrandomkeyTrab(){
				int vrows = 0;
				String[][] vltabla;
				Sendmail vmail = new Sendmail();
				String query = "select trim(mail), trim(codigo_validacion), cedula from autos01a where cedula  = '" + cedula + "' and grupo = " + grupo;
				//System.out.println(query);
				try {
					consulta.selectPntGenerica(query, JNDI);
					vrows  = consulta.getRows();
				} catch (NamingException e) {
					e.printStackTrace();
				}
				//System.out.println(vrows);
				if (vrows > 0){
					vltabla = consulta.getArray();
					for (int i = 0; i < vrows; i++){
						//System.out.println(i);
						vmail.enviarRandomkey(vltabla[i][0], vltabla[i][1]);
						update(vltabla[i][2], grupo);
					}
					limpiarValores();
				} else {
					msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("autos01mailno"), "");
					FacesContext.getCurrentInstance().addMessage(null, msj);
				}
			}
			
			/**
		     * Actualiza estatus de envíos
		     **/
		    private void update(String pcedula, String pgrupo)  {
		        try {
		        	Context initContext = new InitialContext();     
		        	DataSource ds = (DataSource) initContext.lookup(JNDI);
		        	con = ds.getConnection();
		        	//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
			 		DatabaseMetaData databaseMetaData = con.getMetaData();
			 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección

			 		String query = "update autos01a set estatus = '1' where cedula = " + pcedula + " and grupo = " + pgrupo;

		        	
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
			
			/**
		     * Leer Datos de paises
		     * @throws NamingException 
		     * @throws IOException 
		     **/ 	
		  	public void selectAutos01a(int first, int pageSize, String sortField, Object filterValue) throws SQLException, NamingException {
		     try {	
		    	 //System.out.println("JNDI: " + JNDI);
		    	Context initContext = new InitialContext();     
		   		DataSource ds = (DataSource) initContext.lookup(JNDI);
		
		   		con = ds.getConnection();
		   	 //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
		 		DatabaseMetaData databaseMetaData = con.getMetaData();
		 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección

		 		String query = "";
		 		
		 		if(pcodcia==null){
		 			pcodcia = " - ";
		        }
		        if(pcodcia==""){
		        	pcodcia = " - ";
		        }
		        
		        String[] veccodcia = pcodcia.split("\\ - ", -1);
		 		
		 		switch ( productName ) {
		        case "Oracle":
		        	//Consulta paginada
		        	query = "  select * from ";
		        	query += " ( select query.*, rownum as rn from";
			  		query += " ( SELECT a.cedula, trim(a.nombre), trim(a.apellido), trim(a.mail), case when a.estatus = '0' then 'NO ENVIADO' else 'ENVIADO' end, a.pcodcia, b.nomcia1, a.grupo";
			        query += " FROM autos01a a, pnt001 b";
			        query += " where a.pcodcia = b.codcia";
			        query += " and a.grupo = b.grupo";
			        query += " and pcodcia like '" + veccodcia[0] + "%'";
			        query += " and a.cedula||a.nombre||a.apellido like '%" + ((String) filterValue).toUpperCase() + "%'";
			        query += " and a.grupo = '" +  grupo + "'";
			        query += " order by " + sortField.replace("v", "") + ") query";
				    query += " ) where rownum <= " + pageSize ;
				    query += " and rn > (" + first + ")";

		             break;
		        case "PostgreSQL":
		        	//Consulta paginada
		        	query += " SELECT a.cedula, trim(a.nombre), trim(a.apellido), trim(a.mail), case when a.estatus = '0' then 'NO ENVIADO' else 'ENVIADO' end, a.pcodcia, b.nomcia1, a.grupo";
			        query += " FROM autos01a a, pnt001 b";
			        query += " where a.pcodcia = b.codcia";
			        query += " and a.grupo = b.grupo";
			        query += " and pcodcia like '" + veccodcia[0] + "%'";
			        query += " and cast(a.cedula as text)||a.nombre||a.apellido like '%" + ((String) filterValue).toUpperCase() + "%'";
			        query += " and a.grupo = '" +  grupo + "'";
			        query += " order by " + sortField.replace("v", "");
			        query += " LIMIT " + pageSize;
			        query += " OFFSET " + first;

		             break;          		   
		  		}
		   		
		
		   	
		        
		        pstmt = con.prepareStatement(query);
		        //System.out.println(query);
		
		         r =  pstmt.executeQuery();
		        
		        
		        while (r.next()){
		        	Registros select = new Registros();
		        	select.setVcedula(r.getString(1));
		        	select.setVapellido(r.getString(2));
		        	select.setVnombre(r.getString(3));
		        	select.setVmail(r.getString(4));
		        	select.setVestatus(r.getString(5));
		        	select.setVgrupo(r.getString(8));
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
		  	public void counterAutos01a(Object filterValue ) throws SQLException, NamingException {
		     try {	
		    	Context initContext = new InitialContext();     
		   		DataSource ds = (DataSource) initContext.lookup(JNDI);
		 		
		 		String query = null;
		
		   		con = ds.getConnection();
		   	  //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
		 		DatabaseMetaData databaseMetaData = con.getMetaData();
		 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
		 		
		 		if(pcodcia==null){
		 			pcodcia = " - ";
		        }
		        if(pcodcia==""){
		        	pcodcia = " - ";
		        }
		        
		        String[] veccodcia = pcodcia.split("\\ - ", -1);
		 		
		 		switch ( productName ) {
		        case "Oracle":
		        	query = "SELECT count_autos01a('" + ((String) filterValue).toUpperCase() + "','" + veccodcia[0] + "','" + cedula + "','" + grupo + "') from dual";
		             break;
		        case "PostgreSQL":
		        	query = "SELECT count_autos01a('" + ((String) filterValue).toUpperCase() + "','" + veccodcia[0] + "','" + cedula + "','" + grupo + "')";
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
		  	
		  	public void reset(){
		    	pcodcia = "";
		    }
				
	}
