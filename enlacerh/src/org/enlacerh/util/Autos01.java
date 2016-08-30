	package org.enlacerh.util;
	
	import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
import org.enlacerh.getset.Registros;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
	
	@ManagedBean
	@ViewScoped
	public class Autos01 extends Utils implements Serializable {
		
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
		
		public Autos01() {
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
						select(first, pageSize,sortField, filterValue);

						//Counter
						counter(filterValue);

						//Contador lazy
						lazyModel.setRowCount(rows);  //Necesario para crear la paginación
					} catch (SQLException | NamingException | IOException  e) {	
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
		private String pcodcia = "";
		private String cedula = "";
		private String coduser = "";
		private String cluser = "";
		private String mail = "";
		private String audit = "";
		private String nbr = "";
		private String param;
		private String rol = "";
		StringMD md = new StringMD();//Objeto encriptador
		private Object filterValue = "";
		private List<Registros> list = new ArrayList<Registros>();
		
		//Validación
	    private String vcodcia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ciacopia");
	    private String vgrupo = ""; 
	    	    
	    

		/**
		 * @return the vgrupo
		 */
		public String getVgrupo() {
			return vgrupo;
		}

		/**
		 * @param vgrupo the vgrupo to set
		 */
		public void setVgrupo(String vgrupo) {
			this.vgrupo = vgrupo;
		}

		/**
		 * @return the vcodcia
		 */
		public String getVcodcia() {
			return vcodcia;
		}

		/**
		 * @param vcodcia the vcodcia to set
		 */
		public void setVcodcia(String vcodcia) {
			this.vcodcia = vcodcia;
		}

		

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
		 * @return the coduser
		 */
		public String getCoduser() {
			return coduser;
		}
	
		/**
		 * @param coduser the coduser to set
		 */
		public void setCoduser(String coduser) {
			this.coduser = coduser;
		}
	
		/**
		 * @return the cluser
		 */
		public String getCluser() {
			return cluser;
		}
	
		/**
		 * @param cluser the cluser to set
		 */
		public void setCluser(String cluser) {
			this.cluser = cluser;
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
	
	
	
		/**
		 * @return the audit
		 */
		public String getAudit() {
			return audit;
		}
	
		/**
		 * @param audit the audit to set
		 */
		public void setAudit(String audit) {
			this.audit = audit;
		}
	
	
	
		/**
		 * @return the nbr
		 */
		public String getNbr() {
			return nbr;
		}
	
		/**
		 * @param nbr the nbr to set
		 */
		public void setNbr(String nbr) {
			this.nbr = nbr;
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
	
		 /**
	     * Borra Usuario
	     * @throws IOException 
	     **/
	    public void delete() throws  NamingException, IOException {  
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
	        	
	        	String param = "'" + StringUtils.join(chkbox, "','") + "'";
	
	            String query = "DELETE FROM autos01 WHERE P_CODCIA||CODUSER in (" + param + ")";
	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);
	            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
	            vGacc = acc.valAccmnu("seg05", "delete", login, JNDI);//LLama a la funcion que valida las opciones del rol
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
	     * Actualiza Usuario autoservicio
	     * @throws IOException 
	     **/
	    public void update() throws  NamingException, IOException {
	    	String[] veccodcia = pcodcia.split("\\ - ", -1);
	    	String[] vecrol = rol.split("\\ - ", -1);
	    	
	    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	String[] chkbox = request.getParameterValues("toTransfer");
	    	
	    	 //Busca cedula por compañía para verificar que ya existe
	        //consulta.selectPntGenerica("select cedula from autos01 where cedula = '" + cedula + "' and p_codcia = '" + veccodcia[0] + "'", JNDI);
	       // String[][] vltabla1 = consulta.getArray();
	       // int rows1 = consulta.getRows();
	        
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    		//if(rows1>0 && cedula.toUpperCase().equals(vltabla1[0][0])){
	           // 	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("regvalced"), "");
	            //}else {
	    	
	         
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	        	con = ds.getConnection();
	
	            String query = "UPDATE AUTOS01 SET CEDULA = " + Integer.parseInt(cedula) + ", NBR = ?"
	                    + ", FECACT = '" + getFecha() + "', P_CODCIA = '" + veccodcia[0] 
	                    + "' , codrol = '" + vecrol[0] + "'"
	                    + " WHERE CODUSER = trim('" + coduser.toUpperCase() + "')";
	            //System.out.println(query);
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, nbr.toUpperCase());
	            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
	            vGacc = acc.valAccmnu("seg05", "update", login, JNDI);//LLama a la funcion que valida las opciones del rol
	            if (vGacc) {
	            	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnAccUpdate"), "");
	            } else {
	            try {
	            	 pstmt.executeUpdate();
	                 if(pstmt.getUpdateCount()==0){
	                 	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
	                 } else {
	                 msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
	                 }
	                 if(chkbox!=null){
	                	 //System.out.println("Actualizando cias");
	                	 actTransf(veccodcia[0].toUpperCase(), veccodcia[1].toUpperCase(), cedula);
	                 }
	            	cedula = "";
	            	cluser = "";
	            	mail = "";
	            	audit = "";
	            	nbr = "";
	            	rol = "";
	            } catch (SQLException e) {
	                e.printStackTrace();
	                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	
	            pstmt.close();
	            con.close();
	               
	                }//Fin validacion de licencia
	        
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	            //} //Fin valida cedula repetida
	        FacesContext.getCurrentInstance().addMessage(null, msj);
	    	}
	    }
	    
	    
	    /**
	     * Actualiza Usuario autoservicio
	     * @throws IOException 
	     **/
	    public void activar() throws  NamingException, IOException {
	        
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    		//if(rows1>0 && cedula.toUpperCase().equals(vltabla1[0][0])){
	           // 	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("regvalced"), "");
	            //}else {
	    	
	         
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	        	con = ds.getConnection();
	
	            String query = "UPDATE AUTOS02 set isact = '0'"
	                    + " WHERE cedula = trim('" + cedula + "')";
	            //System.out.println(query);
	            pstmt = con.prepareStatement(query);
	            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
	            vGacc = acc.valAccmnu("seg05", "update", login, JNDI);//LLama a la funcion que valida las opciones del rol
	            if (vGacc) {
	            	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnAccUpdate"), "");
	            } else {
	            try {
	            	 pstmt.executeUpdate();
	                 if(pstmt.getUpdateCount()==0){
	                 	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
	                 } else {
	                 msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  getMessage("msnUpdate"), "");
	                 }
	            	cedula = "";
	            	cluser = "";
	            	mail = "";
	            	audit = "";
	            	nbr = "";
	            	rol = "";
	            } catch (SQLException e) {
	                e.printStackTrace();
	                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	
	            pstmt.close();
	            con.close();
	               
	                }//Fin validacion de licencia
	        
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	            //} //Fin valida cedula repetida
	        FacesContext.getCurrentInstance().addMessage(null, msj);
	    	}
	    }
	    
	        
	    /**
	     * Leer Datos de paises
	     * @throws NamingException 
	     * @throws IOException 
	     **/ 	
	  	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, NamingException, IOException {
	
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
	        
	        
	  		//String[] veccodcia = pcodcia.split("\\ - ", -1);
	        
	        switch ( productName ) {
	        case "Oracle":
	        	//Consulta paginada
	        	query = "  select * from ";
	        	query += " ( select query.*, rownum as rn from";
		  		query += " ( SELECT b.nomcia2, trim(a.p_codcia), trim(a.coduser), a.cedula, trim(a.cluser), trim(a.mail), trim(a.nbr), trim(a.codrol)||' - '||trim(c.desrol), a.grupo";
		        query += " FROM autos01 a, pnt001 b, seg002 c";
		        query += " where a.p_codcia=b.codcia";
		        query += " and a.grupo=b.grupo";
		        query += " and a.codrol=c.codrol";
		        query += " and a.grupo=c.grupo";
		        query += " and b.nomcia2||a.coduser||a.cedula like '%" + ((String) filterValue).toUpperCase() + "%'";
		        //query += " and a.p_codcia like '" + veccodcia[0] + "%'"; 
		        query += " and a.grupo = '" +  grupo + "'";
		  		query += " order by " + sortField.replace("v", "") + ") query";
		        query += " ) where rownum <= " + pageSize ;
		        query += " and rn > (" + first + ")";

	             break;
	        case "PostgreSQL":
	        	//Consulta paginada
		  		query = "SELECT b.nomcia2, trim(a.p_codcia), trim(a.coduser), CAST(a.cedula AS text), trim(a.cluser), trim(a.mail), trim(a.nbr), trim(a.codrol)||' - '||trim(c.desrol), a.grupo";
		        query += " FROM autos01 a, pnt001 b, seg002 c";
		        query += " where a.p_codcia=b.codcia";
		        query += " and a.grupo=b.grupo";
		        query += " and a.codrol=c.codrol";
		        query += " and a.grupo=c.grupo";
		        query += " and b.nomcia2||a.coduser||a.cedula like '%" + ((String) filterValue).toUpperCase() + "%'";
		        //query += " and a.p_codcia like '" + veccodcia[0] + "%'"; 
		        query += " and CAST(a.grupo AS text) = '" +  grupo + "'";
		        query += " order by " + sortField.replace("v", "") ;
		        query += " LIMIT " + pageSize;
		        query += " OFFSET " + first;
	             break;          		   
	  		}
	
	  		
	  		
	  		
	
	
	        
	        pstmt = con.prepareStatement(query);
	        //System.out.println(query);
	  		
	        ResultSet r =  pstmt.executeQuery();
	        
	        
	        while (r.next()){
	        	Registros select = new Registros();
	        	select.setVpcodcia(r.getString(1));
	            select.setVpcodciadescia(r.getString(2) + " - " + r.getString(1));
	            select.setVcedula(r.getString(4));
	            select.setVcoduser(r.getString(3));
	            select.setVcluser(r.getString(5));
	            select.setVmail(r.getString(6));
	            select.setPcodcia(r.getString(2));
	            select.setVnbr(r.getString(7));
	            select.setVcodrol(r.getString(8));
	            select.setVgrupo(r.getString(9));
	        	//Agrega la lista
	        	list.add(select);
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
	  	public void counter(Object filterValue ) throws SQLException, NamingException, IOException {
	     try {	
	    	Context initContext = new InitialContext();     
	   		DataSource ds = (DataSource) initContext.lookup(JNDI);
	
	   		con = ds.getConnection();
	   		
	 		//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	 		DatabaseMetaData databaseMetaData = con.getMetaData();
	 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
	   		
	   		String[] veccodcia = pcodcia.split("\\ - ", -1);
	
	   		String query = null;
	   		
	   		switch ( productName ) {
	        case "Oracle":
	        	query = "SELECT count_autos01('" + ((String) filterValue).toUpperCase() + "','" +  veccodcia[0] + "','" + grupo + "') from dual";
	             break;
	        case "PostgreSQL":
	        	query = "SELECT count_autos01('" + ((String) filterValue).toUpperCase() + "','" +  veccodcia[0] + "','" + grupo + "')";
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
	
	
	
	   public void auditar() throws SQLException, NamingException, IOException {
		   select(1,15,"coduser", "");
	   }
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////OPERACIONES BASICAS//////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Limpia los valores
	 **/
	private void limpiarValores(){
		pcodcia = "";
		cedula = "";
		coduser = "";
		cluser = "";
		mail = "";
		audit = "";
		nbr = "";
		rol = "";
	}
	
	
			/**
			 * Rutina que transfiere compañías
			 * @throws IOException 
			 **/
			private void actTransf(String pcodcia, String pdescia, String id) throws  NamingException, IOException {
			    //Pool de conecciones JNDI. Cambio de metodología de conexión a bd. Julio 2010
			    Context initContext = new InitialContext();
			    DataSource ds = (DataSource) initContext.lookup(JNDI);
			    try {
			        Connection con = ds.getConnection();
			        CallableStatement proc = null;
			        //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
			 		DatabaseMetaData databaseMetaData = con.getMetaData();
			 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
			
			 		switch ( productName ) {
			        case "Oracle":
			        	proc = con.prepareCall("{act_transferencia(?,?,?)}");
				        proc.setString(1, pcodcia);
				        proc.setString(2, pdescia);
				        proc.setString(3, id);				       
			             break;
			        case "PostgreSQL":
			        	proc = con.prepareCall("{? = CALL act_transferencia(?,?,?)}");
				        proc.registerOutParameter(1, Types.OTHER);
				        proc.setString(2, pcodcia);
				        proc.setString(3, pdescia);
				        proc.setString(4, id);
			             break;
			  		}
			        //System.out.println("Compañía: " + cia);
			        //System.out.println("Año: " + anio);
			        //System.out.println("Mes: " + mes);
			        //System.out.println("Usuario: " + user);
			        //System.out.println("Opción " + opcauto);
			        //
			 		proc.execute();
			 		 
			        proc.close();
			        con.close();
			        
			        //System.out.println("Actualizando");
			
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
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
		            
		            String[] veccodcia = vcodcia.split("\\ - ", -1);
		            
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
				int rows = 0;
				String[][] vltabla;
				Sendmail mail = new Sendmail();
				String query = "select trim(mail), trim(codigo_validacion) from autos01a where estatus = '0'";
				try {
					consulta.selectPntGenerica(query, JNDI);
				} catch (NamingException e) {
					e.printStackTrace();
				}
				if (rows > 0){
					vltabla = consulta.getArray();
					for (int i = 0; i < rows; i++){
						mail.enviarRandomkey(vltabla[0][0], vltabla[0][1]);
					}
				}
			}
			
		
		  	
						
	}
