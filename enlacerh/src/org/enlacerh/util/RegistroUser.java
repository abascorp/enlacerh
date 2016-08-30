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
	
	import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import javax.sql.DataSource;
	
	
	 
	/**
	 *
	 * @author Andres Dominguez 08/05/2012
	 */
	/**
	 * Clase para leer, insertar, modificar y eliminar registros de la tabla
	 * autos01 registro de usuarios de autoservicio
	 *  */
	@ManagedBean(name="regBean")
	@ViewScoped
	public class RegistroUser extends Utils implements Serializable {
		
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	
		// Constructor
	    public RegistroUser() {
	    }
	
	    
	//Campos de la tabla para getter y setter
	private String cedula = "";
	private String coduser = "";
	private String cluser = "";
	private String mail = "";
	private String nbr = "";
	private int nextGrupo = 0;
	StringMD md = new StringMD(); //Objeto encripta
	private String emp = "";
	private long tlf1;
	private long tlf2;
	private String pais = "";
	private String ciudad = "";
	private String cantrab = "";
	private String captcha="1";
	
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
	 * @return the emp
	 */
	public String getEmp() {
		return emp;
	}
	
	/**
	 * @param emp the emp to set
	 */
	public void setEmp(String emp) {
		this.emp = emp;
	}
	
	
	
	/**
	 * @return the tlf1
	 */
	public long getTlf1() {
		return tlf1;
	}
	
	/**
	 * @param tlf1 the tlf1 to set
	 */
	public void setTlf1(long tlf1) {
		this.tlf1 = tlf1;
	}
	
	/**
	 * @return the tlf2
	 */
	public long getTlf2() {
		return tlf2;
	}
	
	/**
	 * @param tlf2 the tlf2 to set
	 */
	public void setTlf2(long tlf2) {
		this.tlf2 = tlf2;
	}
	
	/**
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}
	
	/**
	 * @param pais the pais to set
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}
	
	/**
	 * @return the ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}
	
	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	
	/**
	 * @return the cantrab
	 */
	public String getCantrab() {
		return cantrab;
	}
	
	/**
	 * @param cantrab the cantrab to set
	 */
	public void setCantrab(String cantrab) {
		this.cantrab = cantrab;
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
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;
	
	   
	    
	    /**
		 * Retorna el proximo grupo de usuarios
		 * @param boolean
	     * @throws IOException 
		 */
		public int nextGrp() throws NamingException, IOException{
			int nextGrp = 0;
			PntGenerica consulta = new PntGenerica();
			consulta.selectPntGenerica("select max(grupo)+1 from seg001", JNDI);
			String [][] vltabla = consulta.getArray();
			int rows = consulta.getRows();
			if(rows>0){
				nextGrp = Integer.parseInt(vltabla[0][0]);
				nextGrupo = nextGrp;
			}
			return nextGrp;
		}
	    
	    /**
	     * Inserta Seg001 (Grupos).
		 * @throws NamingException 
	     **/
	    private void insertSeg001() throws NamingException {
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
	            	query = "INSERT INTO seg001 VALUES (" + nextGrp() + ",?, '31/12/2999', '1',?, sysdate, ?)";
	 	            pstmt = con.prepareStatement(query);
	 	            pstmt.setInt(1, Integer.parseInt(cantrab));
	 	            pstmt.setString(2, emp.toUpperCase());
	 	            pstmt.setString(3, JNDI);

	                 break;
	            case "PostgreSQL":
	            	query = "INSERT INTO seg001 VALUES (" + nextGrp() + ",?, '31/12/2999', '1',?, current_timestamp, ?)";
	 	            pstmt = con.prepareStatement(query);
	 	            pstmt.setInt(1, Integer.parseInt(cantrab));
	 	            pstmt.setString(2, emp.toUpperCase());
	 	            pstmt.setString(3, JNDI);

	                 break;          		   
	      		}
	
	           
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
	
	    
	    /**
	     * Inserta usuario de aplicación
	     * @throws IOException 
	     **/
	    public void insertApp() throws  NamingException, IOException {
	    	if(captcha.equals("1")){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnCaptchaInvalido"), "");
	    	} else {
	    	//
	    	int rows = 0;
	    	int rows1 = 0;
	    	
	    	//Busca usuario para verificar que ya existe
	        consulta.selectPntGenerica("select trim(coduser) from autos01 where coduser = '" + coduser.toUpperCase() + "'", JNDI);
	        String[][] vltabla = consulta.getArray();
	        rows = consulta.getRows();
	        
	        
	      //Busca usuario en usuarios de aplicacion para verificar que ya existe
	        consulta.selectPntGenerica("select trim(coduser) from seg003 where coduser = '" + coduser.toUpperCase() + "'", JNDI);
	        String[][] vltabla2 = consulta.getArray();
	        rows1 = consulta.getRows();
	                      
	        
	        //
	    	//Valida que los campos no sean nulos
	        if (rows>0 && coduser.toUpperCase().equals(vltabla[0][0])){
	        	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("regvaluser"), "");
	        } else 
	        if (rows1>0 && coduser.toUpperCase().equals(vltabla2[0][0])){
	        	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("regvaluser"), "");
	        } else 	
	        {	
	        	insertSeg001(); //Inserta el proximo grupo
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
	            	 query = "INSERT INTO SEG003 VALUES (?,?,?,?,?,?," + nextGrupo + ",?,sysdate ," +  BigInteger.valueOf(tlf1) + "," + BigInteger.valueOf(tlf2) + ", ?, ?)";
	 	            pstmt = con.prepareStatement(query);
	 	            pstmt.setString(1, coduser.toUpperCase());
	 	            pstmt.setString(2, nbr.toUpperCase());
	 	            pstmt.setString(3, md.getStringMessageDigest(cluser.toUpperCase(), StringMD.SHA256));
	 	            pstmt.setString(4, null);
	 	            pstmt.setString(5, "opennomina");
	 	            pstmt.setString(6, mail.toLowerCase());
	 	            pstmt.setString(7, "administrador");
	 	            pstmt.setString(8, pais.toUpperCase());
	 	            pstmt.setString(9, ciudad.toUpperCase());

	                 break;
	            case "PostgreSQL":
	            	 query = "INSERT INTO SEG003 VALUES (?,?,?,?,?,?," + nextGrupo + ",?,current_timestamp," +  BigInteger.valueOf(tlf1) + "," + BigInteger.valueOf(tlf2) + ", ?, ?)";
	 	            pstmt = con.prepareStatement(query);
	 	            pstmt.setString(1, coduser.toUpperCase());
	 	            pstmt.setString(2, nbr.toUpperCase());
	 	            pstmt.setString(3, md.getStringMessageDigest(cluser.toUpperCase(), StringMD.SHA256));
	 	            pstmt.setString(4, null);
	 	            pstmt.setString(5, "opennomina");
	 	            pstmt.setString(6, mail.toLowerCase());
	 	            pstmt.setString(7, "administrador");
	 	            pstmt.setString(8, pais.toUpperCase());
	 	            pstmt.setString(9, ciudad.toUpperCase());

	                 break;          		   
	      		}
	
	           
	            //System.out.println(query);
	            //Antes de ejecutar verifica que no exista un usuario igual en la bd
	            
	           //Sigue la operación de insertar 	
	            try {
	                pstmt.executeUpdate();
	                msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("reginserted1"), "");              
	                NewUserNotificationRequest(nbr.toUpperCase(), coduser.toUpperCase());
	                NewUserNotification(mail.toLowerCase());
	                limpiarValores();
	
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
	     * Envía notificación de registro de un nuevo usuario
	    **/
	    private void NewUserNotificationRequest(String nbr, String usuario) {
			try {
				File ruta = new File(extContext.getRealPath( File.separator + "WEB-INF"));
				Context initContext = new InitialContext();     
		    	Session session = (Session) initContext.lookup(new Xml().XmlJndiMail(ruta));
					// Crear el mensaje a enviar
					MimeMessage mm = new MimeMessage(session);
	
					// Establecer las direcciones a las que será enviado
					// el mensaje (test2@gmail.com y test3@gmail.com en copia
					// oculta)
					// mm.setFrom(new
					// InternetAddress("opennomina@dvconsultores.com"));
					mm.addRecipient(Message.RecipientType.TO,
							new InternetAddress("asanchez@abascorp.com"));
	
					// Establecer el contenido del mensaje
					mm.setSubject(getMessage("mailUser"));
					mm.setText(getMessage("mailNewUser") + ". Datos: Usuario: " + usuario + ". Nombre: " + nbr);
					
					// use this is if you want to send html based message
		            mm.setContent(getMessage("mailNewUser") + ". Datos: Usuario: " + usuario + ". Nombre: " + nbr, "text/html; charset=utf-8");
	
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
		    	Session session = (Session) initContext.lookup(new Xml().XmlJndiMail(ruta));
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
					mm.setText(getMessage("mailNewUserMsj"));
					
					// use this is if you want to send html based message
		            mm.setContent(getMessage("mailNewUserMsj"), "text/html; charset=utf-8");
	
					// Enviar el correo electrónico
					Transport.send(mm);
					//System.out.println("Correo enviado exitosamente");
			} catch (Exception e) {
				e.printStackTrace();
			}
	     }
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////OPERACIONES BASICAS//////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Limpia los valores
	 **/
	private void limpiarValores(){
		cedula = "";
		coduser = "";
		cluser = "";
		mail = "";
		nbr = "";
	}
	
	
	}
	
