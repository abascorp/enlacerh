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
	
	package org.opennomina.util;
	
	import java.io.File;
	import java.io.IOException;
	import java.io.Serializable;
	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.ArrayList;
	import java.util.List;
	
	import javax.faces.application.FacesMessage;
	import javax.faces.bean.ManagedBean;
	import javax.faces.bean.ViewScoped;
	import javax.faces.context.FacesContext;
	import javax.mail.Message;
	import javax.mail.Session;
	import javax.mail.Transport;
	import javax.mail.internet.InternetAddress;
	import javax.mail.internet.MimeMessage;
	import javax.naming.Context;
	import javax.naming.InitialContext;
	import javax.naming.NamingException;
	import javax.servlet.http.HttpServletRequest;
	import javax.sql.DataSource;
	
	import org.opennomina.getset.Registros;
	
	 
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
	public class Registro extends Utils implements Serializable {
		
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	
		// Constructor
	    public Registro() throws SQLException, NamingException, IOException {
	
	    }
	
	    
		//Campos de la tabla para getter y setter
		private String pcodcia = "";
		private String cedula = "";
		private String coduser = "";
		private String cluser = "";
		private String confirmCluser = "";
		private String mail = "";
		private String confirmMail = "";
		private String audit = "";
		private String nbr = "";
		private String param;
		StringMD md = new StringMD(); //Objeto encripta
		private String randomKey;
		private String captcha = "";
	
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
		
		
		
		
		/**
		 * @return the randomKey
		 */
		public String getRandomKey() {
			return randomKey;
		}
		
		/**
		 * @param randomKey the randomKey to set
		 */
		public void setRandomKey(String randomKey) {
			this.randomKey = randomKey;
		}
		
		
		
		
		/**
		 * @return the confirmCluser
		 */
		public String getConfirmCluser() {
			return confirmCluser;
		}
		
		/**
		 * @param confirmCluser the confirmCluser to set
		 */
		public void setConfirmCluser(String confirmCluser) {
			this.confirmCluser = confirmCluser;
		}
		
		
		
		
		/**
		 * @return the confirmMail
		 */
		public String getConfirmMail() {
			return confirmMail;
		}
		
		/**
		 * @param confirmMail the confirmMail to set
		 */
		public void setConfirmMail(String confirmMail) {
			this.confirmMail = confirmMail;
		}
	
	
	
		/**
		 * @return the captcha
		 */
		public String getCaptcha() {
			return captcha;
		}
		
		/**
		 * @param captcha the captcha to set
		 */
		public void setCaptcha(String captcha) {
			this.captcha = captcha;
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
	//private String tipusr = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipusr"); //Usuario logeado
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;
	
	
	   
	    /**
	     * Inserta usuario autoservicio
	     * @throws IOException 
	     **/
	    public void insert() throws  NamingException, IOException {
	    	if(!captcha.equals(codVal())){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnCaptchaInvalido"), "");
	    	} else {
	    	//
	    	int rows = 0;
	    	int rows1 = 0;
	    	int rows2 = 0;
	    	
	    	//Busca usuario para verificar que ya existe
	        consulta.selectPntGenerica("select trim(coduser) from autos01 where coduser = '" + coduser.toUpperCase() + "'", JNDI);
	        String[][] vltabla = consulta.getArray();
	        rows = consulta.getRows();
	        
	        
	        //Busca cedula por compañía para verificar que ya existe
	        consulta.selectPntGenerica("select cedula from autos01 where cedula = '" + cedulaCodVal() + "' and p_codcia = '" + ciaCodVal() + "'", JNDI);
	        String[][] vltabla1 = consulta.getArray();
	        rows1 = consulta.getRows();
	        
	      //Busca compañia para verificar que existe
	        consulta.selectPntGenerica("select trim(codcia) from pnt001 where codcia = '" + ciaCodVal() + "'", JNDI);
	        rows2 = consulta.getRows();
	        
	        
	        //
	    	//Valida que los campos no sean nulos
	        if (rows>0 && coduser.toUpperCase().equals(vltabla[0][0])){
	        	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("regvaluser"), "");
	        } else 
	        if (rows2==0){
	        	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("tooltip1"), "");
	        } else 	
	        if(rows1>0 && cedula.toUpperCase().equals(vltabla1[0][0])){
	        	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("regvalced"), "");
	        }else {	

	        
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con = ds.getConnection();
	            
	            String query = "INSERT INTO AUTOS01 VALUES (?,'" + cedulaCodVal() + "',?,?,?,'" + getFecha() + "','" + getFecha() + "', 'opennomina', ?, current_timestamp,'0', select_grupo('" + ciaCodVal() + "') ,?,'0','0')";
	            //System.out.println(query);
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, ciaCodVal());
	            pstmt.setString(2, coduser.toUpperCase());
	            pstmt.setString(3, md.getStringMessageDigest(cluser.toUpperCase(), StringMD.SHA256));
	            pstmt.setString(4, mail.toLowerCase());
	            pstmt.setString(5, nombresCodVal());
	            pstmt.setString(6, "01");
	            //System.out.println(query);
	            //Antes de ejecutar verifica que no exista un usuario igual en la bd
	            
	           //Sigue la operación de insertar 	
	            try {
	                pstmt.executeUpdate();               
	                delete_temp_registro(coduser);
	                temp_registro(); 
	                NewUserNotificationRequest(nbr.toUpperCase(), coduser.toUpperCase(), ciaCodVal());
	                NewUserNotification(mail.toLowerCase());
	                limpiarValores();
	               msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("reginserted"), "");
	            } catch (SQLException e)  {
	                 e.printStackTrace();
	                 msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
	            }
	            
	            pstmt.close();
	            con.close();           
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    	}//Fin de validación de campos nulos
	    	}
	    	FacesContext.getCurrentInstance().addMessage(null, msj);
	    }
	    
	    
	    /**
	     * Inserta Paises.
	     * <p>
	     * Parametros del Metodo: String codpai, String despai. Pool de conecciones y login
		 * @throws NamingException 
	     **/
	    private void temp_registro() throws NamingException {
	        try {
	        	Context initContext = new InitialContext();    
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	            con = ds.getConnection();
	            
	            randomKey = "key"+md.randomKey()+(int) (Math.random()*9873333+1);      
	            
	            String query = "INSERT INTO temp_registro VALUES ("+ cedulaCodVal() +",?,?,?)";
	            pstmt = con.prepareStatement(query);
	            /** pstmt.setInt(1, Integer.parseInt(cedula)); **/
	            pstmt.setString(1, coduser.toUpperCase());
	            pstmt.setString(2, mail.toLowerCase());
	            pstmt.setString(3, randomKey);
	            System.out.println(query);
	            try {
	                pstmt.executeUpdate();
	            } catch (SQLException e)  {
	                 e.printStackTrace();
	            }
	
	            pstmt.close();
	            con.close();           
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	      
	      private int cedulaCodVal(){
	    	  String query = "select cedula from autos01a where trim(codigo_validacion)='" + captcha + "'";
	    	  int vlrows;
	    	  String[][] vltabla;
	    	  int cedula = 0;
	    	  System.out.println(query);
	    	  try {
				consulta.selectPntGenerica(query, JNDI);
				vlrows = consulta.getRows();
				if(vlrows>0){
		    		  vltabla = consulta.getArray();
		    		  cedula = Integer.parseInt(vltabla[0][0]);
		    	  }
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 return cedula; 
	      }
	      
	      private String codVal(){
	    	  String query = "select trim(codigo_validacion) from autos01a where trim(codigo_validacion)='" + captcha + "'";
	    	  //System.out.println(query);
	    	  int vlrows;
	    	  String[][] vltabla;
	    	  String codval = "";
	    	  try {
				consulta.selectPntGenerica(query, JNDI);
				vlrows = consulta.getRows();
				if(vlrows>0){
		    		  vltabla = consulta.getArray();
		    		  codval = vltabla[0][0];
		    	  }
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 return codval; 
	      }
	      
	      private String ciaCodVal(){
	    	  String query = "select trim(pcodcia) from autos01a where trim(codigo_validacion)='" + captcha + "'";
	    	  int vlrows;
	    	  String[][] vltabla;
	    	  String codval = "";
	    	  try {
				consulta.selectPntGenerica(query, JNDI);
				vlrows = consulta.getRows();
				if(vlrows>0){
		    		  vltabla = consulta.getArray();
		    		  codval = vltabla[0][0];
		    	  }
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 return codval; 
	      }
	      
	      private String nombresCodVal(){
	    	  String query = "select trim(trim(nombre)||' '||trim(apellido)) from autos01a where trim(codigo_validacion)='" + captcha + "'";
	    	  int vlrows;
	    	  String[][] vltabla;
	    	  String codval = "";
	    	  try {
				consulta.selectPntGenerica(query, JNDI);
				vlrows = consulta.getRows();
				if(vlrows>0){
		    		  vltabla = consulta.getArray();
		    		  codval = vltabla[0][0];
		    	  }
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 return codval; 
	      }
	
	    
	       private void delete_temp_registro(String usuario){
			
			//System.out.println("Validando usuario: " + idTemp);
			
			try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	        	con = ds.getConnection();
	
	            String query = "delete from temp_registro where coduser='" + usuario.toUpperCase() + "'";
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
	
	            String query = "DELETE FROM autos01 WHERE P_CODCIA||CODUSER in (" + param + ")";
	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);
	            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
	            vGacc = acc.valAccmnu("seg06", "delete", login, JNDI);//LLama a la funcion que valida las opciones del rol
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
	                list.clear();
	                select();
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
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    	//
	    	String[] veccodcia = pcodcia.split("\\ - ", -1);
	
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	        	con = ds.getConnection();
	
	            String query = "UPDATE AUTOS01 SET CLUSER = ?, CEDULA = " + Integer.parseInt(cedula) + ", MAIL = ?, NBR = ?"
	                    + ",FECACT = '" + getFecha() + "'"
	                    + " WHERE CODUSER = trim('" + coduser.toUpperCase() + "') and p_codcia = '" + veccodcia[0] + "'";
	            //System.out.println(query);
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, md.getStringMessageDigest(cluser.toUpperCase(), StringMD.SHA256));
	            pstmt.setString(2, mail.toLowerCase());
	            pstmt.setString(3, nbr.toUpperCase());
	            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
	            vGacc = acc.valAccmnu("seg06", "update", login, JNDI);//LLama a la funcion que valida las opciones del rol
	            if (vGacc) {
	            	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnAccUpdate"), "");
	            } else {
	            // Antes de ejecutar valida si existe el registro en la base de Datos.
	            consulta.selectPntGenerica("select coduser from autos01 where coduser =trim('" + coduser.toUpperCase() + "') and p_codcia = '" + veccodcia[0] + "'", JNDI);
	               if (consulta.getRows()==0){
	            	   msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnNoUpdate"), "");
	               } else {
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnUpdate"), "");
	            	cedula = "";
	            	cluser = "";
	            	mail = "";
	            	audit = "";
	            	nbr = "";
	            	list.clear();
	            	select();
	            } catch (SQLException e) {
	                e.printStackTrace();
	                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }
	
	            pstmt.close();
	            con.close();
	               }
	                }//Fin validacion de licencia
	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        FacesContext.getCurrentInstance().addMessage(null, msj);
	    	}
	    }
	    
	    /**
	     * Actualiza clave de usuario de autoservicio
	     * @throws IOException 
	     **/
	    public void updateCl() throws  NamingException, IOException {
	    	if(!cluser.toUpperCase().equals(confirmCluser.toUpperCase())){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("msnNoClave"), "");
	    	} else {
	    	//Valida que los campos no sean nulos
	    	String query = "";	

	    	String[][] ltabla;
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	        	con = ds.getConnection();

	        	query = "UPDATE AUTOS01 SET CLUSER = ?"
	                	+ " , FECACT = '" + getFecha() + "'"
	                    + " WHERE CODUSER = trim('" + login.toUpperCase() + "')";	
	        	
	        	consulta.selectPntGenerica("select coduser, mail from autos01 where coduser ='" + login.toUpperCase() + "'", JNDI);
	        	ltabla = consulta.getArray();
	
	            pstmt = con.prepareStatement(query);
	            
	            ChgPass(ltabla[0][1].toString());
	            pstmt.setString(1, md.getStringMessageDigest(cluser.toUpperCase(), StringMD.SHA256));
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("chgpwd"), "");
	                FacesContext.getCurrentInstance().addMessage(null, msj);
	            } catch (SQLException e) {
	            	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	                e.printStackTrace();
	                FacesContext.getCurrentInstance().addMessage(null, msj);
	            }
	
	            pstmt.close();
	            con.close();
	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    	}
	        
	    }
	    
	    /**
	     * Actualiza clave de usuario de autoservicio o aplicación
	     * al momento de olvidar claves
	     * @throws IOException 
	     **/
	    public void actCl(String login, int registros) throws  NamingException, IOException {
	     if(registros>0){
	    	//Valida que los campos no sean nulos
	    	String query = "";	

	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	        	con = ds.getConnection();
	        	
	        	randomKey = "OPEN"+md.randomKey();

	        	query = "UPDATE AUTOS01 SET CLUSER = '" + md.getStringMessageDigest(randomKey, StringMD.SHA256) + "'"
	                	+ " , FECACT = '" + getFecha() + "'"
	                    + " WHERE CODUSER = trim('" + login.toUpperCase() + "')";	

	            //System.out.println(query);
	            //System.out.println("REGISTROS= " + rows);
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
	    
	    /**
	     * Actualiza mail de usuario de autoservicio
	     * @throws IOException 
	     **/
	    public void updateMail() throws  NamingException, IOException {
	    	if(!mail.toLowerCase().equals(confirmMail.toLowerCase())){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("msnNoMail"), "");
	    	} else {
	    	//Valida que los campos no sean nulos
	    	String query = "";	
	    	//Verifica que tipo de usuario está cambiando
	    	consulta.selectPntGenerica("Select coduser, cedula  from autos01 where coduser ='" + login.toUpperCase() + "'", JNDI);
	    	int rows = consulta.getRows();
	    	String[][] tabla = consulta.getArray();
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	        	con = ds.getConnection();

	        	query = "UPDATE AUTOS01 SET MAIL = ?, mail_validado='0'"
	                	+ " , FECACT = '" + getFecha() + "'"
	                    + " WHERE CODUSER = trim('" + login.toUpperCase() + "')";	

	            //System.out.println(query);
	            //System.out.println("REGISTROS= " + rows);
	            pstmt = con.prepareStatement(query);
	            pstmt.setString(1, mail.toLowerCase());
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                if (rows>0){
	                	cedula = tabla[0][1];
	                	coduser = tabla[0][0];
	                	delete_temp_registro(coduser);
	                	temp_registro();
	                	ChgMailAuto(mail.toLowerCase());
	                } else {
	                ChgMail(mail.toLowerCase());
	                }
	                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("chgmail"), "");
	            } catch (SQLException e) {
	            	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	                e.printStackTrace();
	            }
	
	            pstmt.close();
	            con.close();
	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    	}
	        FacesContext.getCurrentInstance().addMessage(null, msj);
	    }
	    
	    private List<Registros> list = new ArrayList<Registros>();
	    
	       
	
	     /**
		 * @return the list
		 */
		public List<Registros> getList() {
			return list;
		}
	
		/**
	     * Leer Datos de paises
	     * @throws NamingException 
	     * @throws IOException 
	     **/ 	
	  	public void select() throws SQLException, NamingException, IOException {
	
	  		Context initContext = new InitialContext();     
	    	DataSource ds = (DataSource) initContext.lookup(JNDI);
	  		con = ds.getConnection();
	  		
	  		
	  		
	  		//Consulta paginada
	  		String query = "SELECT b.nomcia2, trim(a.p_codcia), trim(a.coduser), CAST(a.cedula AS text), trim(a.cluser), trim(a.mail), trim(a.nbr)";
	        query += " FROM autos01 a, pnt001 b";
	        query += " where a.p_codcia=b.codcia";
	        //query += " and p_codcia like '" + veccodcia[0] + "%'";
	        query += " and p_codcia in (select p_codcia from seg007 where p_coduser = '" + login.toUpperCase() + "')";
	        //query += " and coduser like '" + coduser.toUpperCase() + "%'";
	        if(audit.equals("1")){
	        query += " and p_codcia||cedula not in (select distinct pcodcia||cedula from autos02)";	
	        }
	        query += " order by 1";
	
	
	        
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
	        	//Agrega la lista
	        	list.add(select);
	        	rows = list.size();
	        }
	        //Cierra las conecciones
	        pstmt.close();
	        con.close();
	        r.close();
	        
	  	}
	  	
	  	private List<Registros> filtro;
	  	
	  	
	 
	 /**
		 * @return the filtro
		 */
		public List<Registros> getFiltro() {
			return filtro;
		}
	
		/**
		 * @param filtro the filtro to set
		 */
		public void setFiltro(List<Registros> filtro) {
			this.filtro = filtro;
		}
	
	/**
		 * @return the rows
		 */
		public int getRows() {
			return rows;
		}
	
	
	
	   public void auditar() throws SQLException, NamingException, IOException {
		   list.clear();
		   select();
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
	}
	
	////////////////////////////////////////OPCIONES DE CORREO//////////////////////////////////////////////////////////////////////
	/**
	 * Envía notificación de registro de un nuevo usuario
	**/
	private void NewUserNotificationRequest(String nbr, String usuario, String cia) {
		try {
			Context initContext = new InitialContext();     
	    	Session session = (Session) initContext.lookup(JNDIMAIL);
				// Crear el mensaje a enviar
				MimeMessage mm = new MimeMessage(session);
	
				// Establecer las direcciones a las que será enviado
				// el mensaje (test2@gmail.com y test3@gmail.com en copia
				// oculta)
				// mm.setFrom(new
				// InternetAddress("opennomina@dvconsultores.com"));
				mm.addRecipient(Message.RecipientType.TO,
						new InternetAddress("asanchez@abascorp.com"));
				
				//mm.addRecipient(RecipientType.CC, new InternetAddress("jaguilar@dvconsultores.com"));
	
				// Establecer el contenido del mensaje
				mm.setSubject(getMessage("mailUser"));
				mm.setText("Nuevo usuario creado para autoservicio" + ". Datos: Usuario: " + usuario + ". Nombre: " + nbr + "Compañía: " + cia);
				
				// use this is if you want to send html based message
	            mm.setContent("Nuevo usuario creado para autoservicio" + ". Datos: Usuario: " + usuario + ". Nombre: " + nbr + " Compañía: " + cia, "text/html; charset=utf-8");
	
				// Enviar el correo electrónico
				Transport.send(mm);
				//System.out.println("Correo enviado exitosamente");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Envía notificación a usuario que se registra
	**/
	private void NewUserNotification(String mail) {
		try {
			File ruta = new File(extContext.getRealPath( File.separator + "WEB-INF"));
			Context initContext = new InitialContext();     
	    	Session session = (Session) initContext.lookup(JNDIMAIL);
				// Crear el mensaje a enviar
				MimeMessage mm = new MimeMessage(session);
				// Establecer las direcciones a las que será enviado
				// el mensaje (test2@gmail.com y test3@gmail.com en copia
				// oculta)
				// mm.setFrom(new
				// InternetAddress("opennomina@dvconsultores.com"));
				mm.addRecipient(Message.RecipientType.TO,
						new InternetAddress(mail));
	
				// Establecer el contenido del mensaje
				mm.setSubject(getMessage("mailUser"));
				mm.setText(getMessage("mailNewUserMsj1"));
				
				// use this is if you want to send html based message
	            mm.setContent(getMessage("mailNewUserMsj1") + new Xml().XmlUrl(ruta) +"?id="+randomKey + " <br/><br/>" + getMessage("mailNewUserMsj2"), "text/html; charset=utf-8");
	
				// Enviar el correo electrónico
				Transport.send(mm);
				//System.out.println("Correo enviado exitosamente");
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	
	/**
	 * Envía notificación a usuario que se registra
	**/
	private void ChgMail(String mail) {
		try {
			Context initContext = new InitialContext();     
	    	Session session = (Session) initContext.lookup(JNDIMAIL);
				// Crear el mensaje a enviar
				MimeMessage mm = new MimeMessage(session);
	
				// Establecer las direcciones a las que será enviado
				// el mensaje (test2@gmail.com y test3@gmail.com en copia
				// oculta)
				// mm.setFrom(new
				// InternetAddress("opennomina@dvconsultores.com"));
				mm.addRecipient(Message.RecipientType.TO,
						new InternetAddress(mail));
	
				// Establecer el contenido del mensaje
				mm.setSubject(getMessage("mailUser2"));
				mm.setText(getMessage("mailChgMail"));
				
				// use this is if you want to send html based message
	            mm.setContent(getMessage("mailChgMail"), "text/html; charset=utf-8");
	
				// Enviar el correo electrónico
				Transport.send(mm);
				//System.out.println("Correo enviado exitosamente");
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	
	private void ChgMailAuto(String mail) {
		try {
			Context initContext = new InitialContext();     
	    	Session session = (Session) initContext.lookup(JNDIMAIL);
				// Crear el mensaje a enviar
				MimeMessage mm = new MimeMessage(session);
	
				// Establecer las direcciones a las que será enviado
				// el mensaje (test2@gmail.com y test3@gmail.com en copia
				// oculta)
				// mm.setFrom(new
				// InternetAddress("opennomina@dvconsultores.com"));
				mm.addRecipient(Message.RecipientType.TO,
						new InternetAddress(mail));
	
				// Establecer el contenido del mensaje
				mm.setSubject(getMessage("mailUser2"));
				mm.setText(getMessage("mailChgMail1"));
				
				// use this is if you want to send html based message
	            mm.setContent(getMessage("mailChgMail1") + new Xml().XmlUrl(ruta) +"?id="+randomKey + " <br/><br/>" + getMessage("mailNewUserMsj2"), "text/html; charset=utf-8");
	
				// Enviar el correo electrónico
				Transport.send(mm);
				//System.out.println("Correo enviado exitosamente");
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	
		/**
		 * Envía notificación a usuario que se registra
		**/
		private void ChgPass(String mail) {
			try {
				Context initContext = new InitialContext();     
		    	Session session = (Session) initContext.lookup(JNDIMAIL);
					// Crear el mensaje a enviar
					MimeMessage mm = new MimeMessage(session);
		
					// Establecer las direcciones a las que será enviado
					// el mensaje (test2@gmail.com y test3@gmail.com en copia
					// oculta)
					// mm.setFrom(new
					// InternetAddress("opennomina@dvconsultores.com"));
					mm.addRecipient(Message.RecipientType.TO,
							new InternetAddress(mail));
		
					// Establecer el contenido del mensaje
					mm.setSubject(getMessage("mailUser1"));
					mm.setText(getMessage("mailChgPass"));
					
					// use this is if you want to send html based message
		            mm.setContent(getMessage("mailChgPass"), "text/html; charset=utf-8");
		
					// Enviar el correo electrónico
					Transport.send(mm);
					//System.out.println("Correo enviado exitosamente");
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
	
	  	
	
	
	}
	
