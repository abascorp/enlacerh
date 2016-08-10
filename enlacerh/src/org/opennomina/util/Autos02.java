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
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.opennomina.getset.Autonom;
import org.primefaces.context.RequestContext;

	
	 
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
	public class Autos02 extends Utils implements Serializable {
			
		HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	
		@PostConstruct	
	    public void init() {
			if (!rq.isRequestedSessionIdValid()) {
				//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
				RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
			}  else {
			//Carga imagen
			imagen();
			//Ejecuta el encabezado
	  		selectEncab(cedula, pcodcia, "I");
	  		
	  		//Verifica si el trabajador está activo para emitir la constancia
	  		selectIsAct(cedula, pcodcia);
	  		
	  		//Detalle
	  		selectDetalle();
			}
	    }
	    
	
	    
	//Campos de la tabla para getter y setter
	private String anio = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
	private String mes = "";
	private String periodo = "";
	private String id = "";
	private String grupo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("grupo"); //Usuario logeado
	public static boolean existe; //Valida si la imagen existe
	private static final String RUTA_REPORTE = File.separator + "reportes";
	private static final String RUTA_LOGS = File.separator + "logs";
	private static final String RUTA_IMAGEN = File.separator + "imagenes" + File.separator  + "colab"; //Ruta donde sube las imágenes para autoservicio
	//Campos de tabla
	private String pcodcia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cia");
	private String codcia = ""; //Compañía que pasa como parámetro el reporte
	private String ptipnom = "";
	private String pfictra = "";
	private String cedula = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cedula"); //Cedula autoservicio
	private String fecing = "";
	private String apell1 = "";
	private String nombre1 = "";
	private String pcodcar = ""; 
	private String pcodloc = "";
	private String pcodsuc = "";
	private String pcoduni = "";
	private String pcoddep = "";
	private String sueld1 = "";
	private String frecuencia = "";
	private String totalasig = "";
	private String totaldedu = "";
	private String totalneto = "";
	private String upload = "1";
	private String isact = "0";
	private String constancia="AUTOCONST";
	private Object filterValue = "";
    private List<Autonom> list = new ArrayList<Autonom>();
    private List<Autonom> listanios = new ArrayList<Autonom>();
    
  	
  	
  	
	/**
	 * @return the listanios
	 */
	public List<Autonom> getListanios() {
		return listanios;
	}

	/**
	 * @param listanios the listanios to set
	 */
	public void setListanios(List<Autonom> listanios) {
		this.listanios = listanios;
	}

	/**
	 * @return the grupo
	 */
	public String getGrupo() {
		return grupo;
	}

	/**
	 * @param grupo the grupo to set
	 */
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	/**
    * @return the list
    */
    public List<Autonom> getList() {
	return list;
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
	 * @return the pfictra
	 */
	public String getPfictra() {
		return pfictra;
	}
	
	/**
	 * @param pfictra the pfictra to set
	 */
	public void setPfictra(String pfictra) {
		this.pfictra = pfictra;
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
	 * @return the fecing
	 */
	public String getFecing() {
		return fecing;
	}
	
	/**
	 * @param fecing the fecing to set
	 */
	public void setFecing(String fecing) {
		this.fecing = fecing;
	}
	
	/**
	 * @return the apell1
	 */
	public String getApell1() {
		return apell1;
	}
	
	/**
	 * @param apell1 the apell1 to set
	 */
	public void setApell1(String apell1) {
		this.apell1 = apell1;
	}
	
	
	
	/**
	 * @return the nombre1
	 */
	public String getNombre1() {
		return nombre1;
	}
	
	/**
	 * @param nombre1 the nombre1 to set
	 */
	public void setNombre1(String nombre1) {
		this.nombre1 = nombre1;
	}
	
	
	
	/**
	 * @return the pcodcar
	 */
	public String getPcodcar() {
		return pcodcar;
	}
	
	/**
	 * @param pcodcar the pcodcar to set
	 */
	public void setPcodcar(String pcodcar) {
		this.pcodcar = pcodcar;
	}
	
	
	/**
	 * @return the pcodloc
	 */
	public String getPcodloc() {
		return pcodloc;
	}
	
	/**
	 * @param pcodloc the pcodloc to set
	 */
	public void setPcodloc(String pcodloc) {
		this.pcodloc = pcodloc;
	}
	
	
	/**
	 * @return the pcodsuc
	 */
	public String getPcodsuc() {
		return pcodsuc;
	}
	
	/**
	 * @param pcodsuc the pcodsuc to set
	 */
	public void setPcodsuc(String pcodsuc) {
		this.pcodsuc = pcodsuc;
	}
	
	
	/**
	 * @return the pcoduni
	 */
	public String getPcoduni() {
		return pcoduni;
	}
	
	/**
	 * @param pcoduni the pcoduni to set
	 */
	public void setPcoduni(String pcoduni) {
		this.pcoduni = pcoduni;
	}
	
	
	/**
	 * @return the pcoddep
	 */
	public String getPcoddep() {
		return pcoddep;
	}
	
	/**
	 * @param pcoddep the pcoddep to set
	 */
	public void setPcoddep(String pcoddep) {
		this.pcoddep = pcoddep;
	}
	
	/**
	 * @return the sueld1
	 */
	public String getSueld1() {
		return sueld1;
	}
	
	/**
	 * @param sueld1 the sueld1 to set
	 */
	public void setSueld1(String sueld1) {
		this.sueld1 = sueld1;
	}
	
	
	/**
	 * @return the anio
	 */
	public String getAnio() {
		return anio;
	}
	
	/**
	 * @param anio the anio to set
	 */
	public void setAnio(String anio) {
		this.anio = anio;
	}
	
	/**
	 * @return the mes
	 */
	public String getMes() {
		return mes;
	}
	
	/**
	 * @param mes the mes to set
	 */
	public void setMes(String mes) {
		this.mes = mes;
	}
	
	/**
	 * @return the periodo
	 */
	public String getPeriodo() {
		return periodo;
	}
	
	/**
	 * @param periodo the periodo to set
	 */
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	/**
	 * @return the frecuencia
	 */
	public String getFrecuencia() {
		return frecuencia;
	}
	
	/**
	 * @param frecuencia the frecuencia to set
	 */
	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}
	
	/**
	 * @return the totalasig
	 */
	public String getTotalasig() {
		return totalasig;
	}
	
	/**
	 * @param totalasig the totalasig to set
	 */
	public void setTotalasig(String totalasig) {
		this.totalasig = totalasig;
	}
	
	/**
	 * @return the totaldedu
	 */
	public String getTotaldedu() {
		return totaldedu;
	}
	
	/**
	 * @param totaldedu the totaldedu to set
	 */
	public void setTotaldedu(String totaldedu) {
		this.totaldedu = totaldedu;
	}
	
	/**
	 * @return the totalneto
	 */
	public String getTotalneto() {
		return totalneto;
	}
	
	/**
	 * @param totalneto the totalneto to set
	 */
	public void setTotalneto(String totalneto) {
		this.totalneto = totalneto;
	}
	
	
	
	
	/**
	 * @return the upload
	 */
	public String getUpload() {
		return upload;
	}
	
	/**
	 * @param upload the upload to set
	 */
	public void setUpload(String upload) {
		this.upload = upload;
	}
	
	
	
	/**
	 * @return the codcia
	 */
	public String getCodcia() {
		return codcia;
	}
	
	/**
	 * @param codcia the codcia to set
	 */
	public void setCodcia(String codcia) {
		this.codcia = codcia;
	}
	
	
	
	/**
	 * @return the isact
	 */
	public String getIsact() {
		return isact;
	}
	
	/**
	 * @param isact the isact to set
	 */
	public void setIsact(String isact) {
		this.isact = isact;
	}
	
	
	/**
	 * @return the constancia
	 */
	public String getConstancia() {
		return constancia;
	}
	
	/**
	 * @param constancia the constancia to set
	 */
	public void setConstancia(String constancia) {
		this.constancia = constancia;
	}
	
	
	
	/**
	 * @return the JNDI
	 */
	public String getJNDI() {
		return JNDI;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null; 
	PntGenerica consulta = new PntGenerica();
	
	private String orden = "1"; //Orden de la consulta
	private int rows; //Registros de tabla
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;
	
	   
	/**
	 * Muestra la imagen, retorna la imagen del usuario sino existe, muestra la estandart
	 * @throws NamingException 
	 * @throws IOException 
	 **/
	public String imagen() {
	    //Valida la imagen del usuario sino retorna la estandart
		String imagen = "../imagenes/colab/imagen.png";
		if(login==null){
			login = "scott";
		}
		String query = "select p_codcia||cedula from autos01 where coduser = '" + login.toUpperCase() + "'";
			upload = "0";
		//Busca la imagen del usuario logeado		
			try {
				consulta.selectPntGenerica(query, JNDI);
			} catch (NamingException  e) {
				e.printStackTrace();
			}
		
		String[][] imagenResult = consulta.getArray();
		int rows = consulta.getRows();
		//System.out.println("rows:" + rows);
		if(rows>0){
			findfiles(pcodcia+cedula);
			if(existe){
				imagen = "../imagenes/colab/"  + imagenResult[0][0].toString() + ".png";
			} else {
			imagen = "../imagenes/colab/imagen.png";	
			}
		}
		//System.out.println("ruta:" + imagen);
	    return imagen;
	}
	
	
	/**
	 * Busca una imagen el el directorio para validar si existe
	 **/
	public static void findfiles(String imagen){
	   ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
	   File file=new File(extContext.getRealPath(RUTA_IMAGEN)+ File.separator +  imagen + ".png");
	  boolean exists = file.exists();
	 if (!exists) {
	  // It returns false if File or directory does not exist
		 existe = exists;
	 }else{
	  // It returns true if File or directory exists
		 existe = exists;
	 }
	}
	
		
	
	
	     /**
	     * Leer Datos de paises
	     * @throws NamingException 
	     * @throws IOException 
	     **/ 	
	  	public void select()  { 	
	  	 try {		
	  		Context initContext = new InitialContext();     
	    	DataSource ds = (DataSource) initContext.lookup(JNDI);
	        con = ds.getConnection();
	        //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	 		DatabaseMetaData databaseMetaData = con.getMetaData();
	 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
	        
	        	  		
	  		String query = "";
	  		  
	  		
	  		//Ejecuta el encabezado
	  		selectEncab(cedula, pcodcia, "I");
	  		
	  		
	  		switch ( productName ) {
	        case "Oracle":
	        	query = " SELECT distinct to_char(totalasig,'999,999,999.99')";
		        query += " , to_char(totaldedu,'999,999,999.99'), to_char(totalneto,'999,999,999.99'), anio, mes, periodo, id";
		        query += " FROM autos02";
		        query += " where anio  like '" + anio + "'";
		        query += " and mes  = '" + mes + "'";
		        query += " and trim(cedula) like '" + cedula + "%'";
		        query += " and trim(pcodcia) like '" + pcodcia + "%'";
		        query += " and grupo = '" + grupo + "'";
		        query += " order by periodo, anio, mes asc";
	             break;
	        case "PostgreSQL":
	        	query = " SELECT distinct to_char(totalasig,'999,999,999.99')";
		        query += " , to_char(totaldedu,'999,999,999.99'), to_char(totalneto,'999,999,999.99'), anio, mes, periodo, id";
		        query += " FROM autos02";
		        query += " where CAST(anio AS text)  like '" + anio + "'";
		        query += " and CAST(mes AS text)  = '" + mes + "'";
		        query += " and trim(cedula) like '" + cedula + "%'";
		        query += " and trim(pcodcia) like '" + pcodcia + "%'";
		        query += " and grupo = '" + grupo + "'";
		        query += " order by periodo, anio, mes asc";
	             break;          		   
	  		}

	  		
	        pstmt = con.prepareStatement(query);
	        //System.out.println(query);
	
	        r =  pstmt.executeQuery();
	        
	  		
	
	        while (r.next()){
	        	Autonom select = new Autonom();
	            select.setVtotalasig(r.getString(1));
	            select.setVtotaldedu(r.getString(2));
	            select.setVtotalneto(r.getString(3));
	            select.setVanio(r.getString(4));
	            select.setVmes(r.getString(5));
	            select.setVperiodo(r.getString(6));
	            select.setVid(r.getString(7));
	        	//Agrega la lista
	        	list.add(select);
	        	rows = list.size();
	        }
	        
	        //Cierra las conecciones
	        pstmt.close();
	        con.close();
	        r.close();
	  	 } catch (SQLException | NamingException e){
	         e.printStackTrace();    
	     }
	  	}
	  	
	  	private List<Autonom> listdet = new ArrayList<Autonom>();
	  	
		/**
	     * Leer Datos de paises
	     * @throws NamingException 
		 * @throws SQLException 
		 * @throws ClassNotFoundException 
	  	 * @throws IOException 
	     **/ 	
	  	public List<Autonom>  selectDetalle()  {
	  		listdet.clear();
	  		
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
	        	//Consulta paginada
		  		query = " SELECT codcto, descto, cantid, case when tipcto='1' then to_char(monto,'999,999,999.99') else '' end  ";
		        query += " , case when tipcto='2' then  to_char(monto,'999,999,999.99') else '' end, case when tipcto='3' then  to_char(monto,'999,999,999.99') else '0' end , to_char(saldo,'999,999,999.99')";
		        query += " FROM autos02";
		        query += " where anio  like '" + anio + "'";
		        query += " and mes  = '" + mes + "'";
		        query += " and trim(periodo)  = trim('" + periodo + "')";
		        query += " and trim(id)  = trim('" + id + "')";
		        query += " and trim(cedula) = '" + cedula + "'";
		        query += " and trim(pcodcia) = '" + pcodcia + "'";
		        query += " and codcto||descto like '%" + ((String) filterValue).toUpperCase() + "%'";
		        query += " and grupo = '" + grupo + "'";
		        //Customizado cliente local, caso proagro en la autos02 tenemos todo, ya que se utiliza para otras cosas
	  		    //query += " and  tipcto in ('1','2')";
	  		    //query += " and  proceso in (1,2,3,4,7,60)";
	  		    //query += " and  codcto < 800";
		        query += " order by to_number(tipcto), to_number(codcto)" ;
	             break;
	        case "PostgreSQL":
	        	//Consulta paginada
		  		query = " SELECT codcto, descto, cantid, case when tipcto='1' then to_char(monto,'999,999,999.99') else '' end  ";
		        query += " , case when tipcto='2' then  to_char(monto,'999,999,999.99') else '' end, case when tipcto='3' then  to_char(monto,'999,999,999.99') else '0' end , to_char(saldo,'999,999,999.99')";
		        query += " FROM autos02";
		        query += " where CAST(anio AS text)  like '" + anio + "'";
		        query += " and CAST(mes AS text)  = '" + mes + "'";
		        query += " and trim(CAST(periodo AS text))  = trim('" + periodo + "')";
		        query += " and trim(id)  = trim('" + id + "')";
		        query += " and trim(cedula) = '" + cedula + "'";
		        query += " and trim(pcodcia) = '" + pcodcia + "'";
		        query += " and codcto||descto like '%" + ((String) filterValue).toUpperCase() + "%'";
		        query += " and grupo = '" + grupo + "'";
		        query += " order by 1" ;
	             break;          		   
	  		}
	  		  
		
 		
	  		
	  	
	  		//System.out.println(query);
	        pstmt = con.prepareStatement(query);
	        
	
	        r =  pstmt.executeQuery();
	        
	                	
	        
	        while (r.next()){
	        	Autonom select = new Autonom();
	            select.setVcodcto(r.getString(1));
	            select.setVdescto(r.getString(2));
	            select.setVcantid(r.getString(3));
	            select.setVmontoasig(r.getString(4));
	            select.setVmontodedu(r.getString(5));
	            select.setVmontoreserva(r.getString(6));
	            select.setVsaldo(r.getString(7));
	        	//Agrega la lista
	        	listdet.add(select);
	        	rows = listdet.size();
	        }
	        //Cierra las conecciones
	        pstmt.close();
	        con.close();
	        r.close();
	  	 } catch (SQLException | NamingException  e){
	         e.printStackTrace();    
	     }
	  	 return listdet;
	
	  	}
	
	  	
	 /**
		 * @return the rows
		 */
		public int getRows() {
			return rows;
		}
		
		/**
	     * Leer Datos básicos del trabajador para consulta
	     * @throws NamingException 
		 * @throws IOException 
	     **/ 	
	  	private void selectEncab(String cedula, String paramcia, String opc)  {
	  		try {	
	  		String query = "";
	  	    if (opc.equals("I")){		
	  	    //Busca la consulta para encabezado básico
	  		       query = "select distinct trim(pcodcia), trim(nomcia2), trim(ptipnom), trim(desnom), trim(pfictra),";	
	  		       query += " trim(cedula), to_char(fecing,'DD/MM/YYYY'), trim(apell1), trim(apell2), trim(nombre1), trim(nombre2),";
	  		       query += " trim(pcodcar), trim(descar), trim(pcodloc), trim(desloc), trim(pcodsuc), trim(desuc), trim(pcoduni), desuni, pcoddep, desdep, to_char(sueld1,'999,999,999.99')";
	  		       query += " from autos02";
	  		       query += " where CAST(anio AS text) like (select cast(max(anio) as text) from autos02 where cedula like'" + cedula +"%')";
	  		       query += " and CAST(mes AS text) like (select cast(max(mes) as text) from autos02 where cedula like'" + cedula +"%' and CAST(anio AS text) like (select cast(max(anio) as text) from autos02 where cedula like'" + cedula +"%'))";
	  		       query += " and  cedula like'" + cedula +"%'";
	  		       query += " and  grupo ='" + grupo + "'";
	  		       
	  	    }  else {
	  	    	   query = "select distinct trim(pcodcia), trim(nomcia2), trim(ptipnom), trim(desnom), trim(pfictra),";	
	  		       query += " trim(cedula), to_char(fecing,'DD/MM/YYYY'), trim(apell1), trim(apell2), trim(nombre1), trim(nombre2),";
	  		       query += " trim(pcodcar), trim(descar), trim(pcodloc), trim(desloc), trim(pcodsuc), trim(desuc), trim(pcoduni), desuni, pcoddep, desdep, to_char(sueld1,'999,999,999.99')";
	  		       query += " from autos02";
	  		       query += " where CAST(anio AS text) like '" + anio + "'";
	  		       query += " and CAST(mes AS text) like '" + mes + "'";
	  	    	
	  	    }
	  	    
	  	  String queryora = "";
	  	    if (opc.equals("I")){		
	  	    //Busca la consulta para encabezado básico
	  	    	queryora = "select distinct trim(pcodcia), trim(nomcia2), trim(ptipnom), trim(desnom), trim(pfictra),";	
	  	    	queryora += " trim(cedula), to_char(fecing,'DD/MM/YYYY'), trim(apell1), trim(apell2), trim(nombre1), trim(nombre2),";
	  	    	queryora += " trim(pcodcar), trim(descar), trim(pcodloc), trim(desloc), trim(pcodsuc), trim(desuc), trim(pcoduni), desuni, pcoddep, desdep, to_char(sueld1,'999,999,999.99')";
	  	    	queryora += " from autos02";
	  	    	queryora += " where anio like (select max(anio) from autos02 where cedula like'" + cedula +"%')";
	  	    	queryora += " and mes like (select max(mes) from autos02 where cedula like'" + cedula +"%' and anio like (select max(anio) from autos02 where cedula like'" + cedula +"%'))";
	  	    	queryora += " and  cedula like'" + cedula +"%'";
	  	    	queryora += " and  grupo ='" + grupo + "'";
	  		     //Customizado cliente local, caso proagro en la autos02 tenemos todo, ya que se utiliza para otras cosas
	  	    	//queryora += " and  tipcto in ('1','2')";
	  	    	//queryora += " and  proceso in (1,4,51,7)";
	  	    }  else {
	  	    	queryora = "select distinct trim(pcodcia), trim(nomcia2), trim(ptipnom), trim(desnom), trim(pfictra),";	
	  	    	queryora += " trim(cedula), to_char(fecing,'DD/MM/YYYY'), trim(apell1), trim(apell2), trim(nombre1), trim(nombre2),";
	  	    	queryora += " trim(pcodcar), trim(descar), trim(pcodloc), trim(desloc), trim(pcodsuc), trim(desuc), trim(pcoduni), desuni, pcoddep, desdep, to_char(sueld1,'999,999,999.99')";
	  	    	queryora += " from autos02";
	  	    	queryora += " where anio like '" + anio + "'";
	  	    	queryora += " and mes like '" + mes + "'";
	  	    	
	  	    }
	  		//System.out.println(query);
	  		consulta.selectPntGenericaMDB(queryora, query, JNDI); 
	
	  		rows = consulta.getRows();
	  		String[][] vltabla = consulta.getArray();
	        if(rows>0){      	
	        	codcia = vltabla[0][0] + " - " + vltabla[0][1];
	        	ptipnom = vltabla[0][2] + " - " + vltabla[0][3];
	            pfictra = vltabla[0][4];
	        	cedula = vltabla[0][5];
	        	fecing = vltabla[0][6];
	        	apell1 = vltabla[0][7] + "   " + vltabla[0][8];
	        	nombre1 = vltabla[0][9] + "  " + vltabla[0][10];
	        	pcodcar = vltabla[0][11] + "  " + vltabla[0][12]; 
	        	pcodloc = vltabla[0][13] + " - " + vltabla[0][14];
	        	pcodsuc = vltabla[0][15] +  " - " + vltabla[0][16];
	        	pcoduni = vltabla[0][17] + " - " + vltabla[0][18];
	        	pcoddep = vltabla[0][19] +  " - " + vltabla[0][20];
	        	sueld1 = vltabla[0][21];
	        }
	  		 } catch (NamingException  e){
		         e.printStackTrace();    
		     }
	  		  		
	  	}
	  	
		/**
	     * Verifica si el trabajador está retirado
	     * @throws NamingException 
	     **/ 	
	  	private void selectIsAct(String cedula, String paramcia)  {
	  		if(grupo==null){
	  			grupo = "99999999";
	  		}
	  	    //Busca la consulta para encabezado básico
	  		try {
				consulta.selectPntGenerica("select distinct isact" +
						" from autos02 " +
						" where  cedula like '" + cedula + "%' and isact = '1' and pcodcia = '" + pcodcia.toUpperCase() + "' and grupo = '" + grupo + "'", JNDI);
			} catch (NamingException e) {
				e.printStackTrace();
			} 
	  		String[][] vltabla = consulta.getArray();
	  		rows = consulta.getRows();
	        if(rows>0){
	        	isact = vltabla[0][0];
	        }
	  		  		
	  	}
	  	
	  	
	  	/**
	     * Envio de recibo de pago a solicitud de usuario
	  	 * @throws NamingException 
	     **/ 
	  	public void enviarRecibo() throws NamingException{
	  		//Procesando reportes en pdf para envios de correo
			
			//Selecciona ficha segun usuarios de autoservicio
	  		String query = "select distinct trim(CAST(periodo as text)), trim(pfictra), trim(b.mail), trim(a.id) " +
						" from autos02 a, autos01 b" +
						" where trim(a.cedula)=trim(CAST(b.cedula AS text))" +
						" and a.grupo=b.grupo" +
						" and a.pcodcia='" + pcodcia + 
						"' and a.anio=" + anio + " and a.mes =" + mes +
						"  and a.periodo =" + periodo + " and b.mail_validado = '1' and a.cedula = '" + cedula + "' and a.grupo = '" + grupo + "' and a.id = '" + id + "'";
	  		
	  	    //Selecciona ficha segun usuarios de autoservicio
	  		String queryora = "select distinct trim(periodo), trim(pfictra), trim(b.mail), trim(a.id) " +
						" from autos02 a, autos01 b" +
						" where trim(a.cedula)=trim(b.cedula)" +
						" and a.grupo=b.grupo" +
						" and a.pcodcia='" + pcodcia + 
						"' and a.anio=" + anio + " and a.mes =" + mes +
						"  and a.periodo =" + periodo + " and b.mail_validado = '1' and a.cedula = '" + cedula + "' and a.grupo = '" + grupo + "' and a.id = '" + id + "'";
	  		
			try {
				//System.out.println(query);
			consulta.selectPntGenericaMDB(queryora, query, JNDI);
			
			int filas = consulta.getRows();
			String[][] vlconsulta = consulta.getArray();
			RunReport reportout = new RunReport();
			FileIO borrarFile = new FileIO();
	       
			//Si hay al go enviar
			if(filas>0){			
				 //Genera los recibos en pdf
				reportout.outReporteRecibo(pcodcia+"AUTONOM", "pdf"
						, extContext.getRealPath(RUTA_REPORTE), extContext.getRealPath(RUTA_LOGS)
						  , "RP_"+vlconsulta[0][1] + "_" + vlconsulta[0][0], anio, mes, vlconsulta[0][0].toString(), vlconsulta[0][1], JNDI, vlconsulta[0][3]); 
				//Envía por correo
				enviarRP(vlconsulta[0][2], extContext.getRealPath(RUTA_LOGS), "RP_"+vlconsulta[0][1] + "_" + vlconsulta[0][0]);
				//Los borra de la carpeta temporal
				borrarFile.borraFile(extContext.getRealPath(RUTA_LOGS), "RP_"+vlconsulta[0][1] + "_" + vlconsulta[0][0] + ".pdf");					
			}
			
			} catch (NamingException | NumberFormatException | IOException | InterruptedException e) {
				msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
				FacesContext.getCurrentInstance().addMessage(null, msj);
				e.printStackTrace();
			}
	  	}
	  	
	  	
	  	/**
	     * Envio constancias a solicitud de usuario
	  	 * @throws NamingException 
	     **/ 
	  	public void enviarConstancia() throws NamingException{
	  		//Procesando reportes en pdf para envios de correo
			
			//Selecciona ficha segun usuarios de autoservicio
	  		String query = "select distinct trim(CAST(periodo as text)), trim(pfictra), trim(b.mail) " +
						" from autos02 a, autos01 b" +
						" where trim(a.cedula)=trim(CAST(b.cedula AS text))" +
						" and a.grupo=b.grupo" +
						" and a.pcodcia='" + pcodcia + 
						"' and a.anio=" + anio + " and a.mes =" + mes +
						"  and a.periodo =" + periodo + " and b.mail_validado = '1' and a.cedula = '" + cedula + "' and a.grupo = '" + grupo + "'";
	  		
	  	    //Selecciona ficha segun usuarios de autoservicio
	  		String queryora = "select distinct trim(periodo), trim(pfictra), trim(b.mail) " +
						" from autos02 a, autos01 b" +
						" where trim(a.cedula)=trim(b.cedula)" +
						" and a.grupo=b.grupo" +
						" and a.pcodcia='" + pcodcia + 
						"' and a.anio=" + anio + " and a.mes =" + mes +
						"  and a.periodo =" + periodo + " and b.mail_validado = '1' and a.cedula = '" + cedula + "' and a.grupo = '" + grupo + "'";
	  		
			try {
				//System.out.println(query);
			consulta.selectPntGenericaMDB(queryora, query, JNDI);
			
			int filas = consulta.getRows();
			String[][] vlconsulta = consulta.getArray();
			RunReport reportout = new RunReport();
			FileIO borrarFile = new FileIO();
	       
			//Si hay algo enviar
			if(filas>0){			
				 //Genera los recibos en pdf
				reportout.outReporteRecibo(pcodcia +constancia, "pdf"
						, extContext.getRealPath(RUTA_REPORTE), extContext.getRealPath(RUTA_LOGS)
						  , pcodcia + constancia, anio, mes, vlconsulta[0][0].toString(), vlconsulta[0][1], JNDI, ""); 
				//Envía por correo
				enviarConst(vlconsulta[0][2], extContext.getRealPath(RUTA_LOGS), pcodcia + constancia);
				//Los borra de la carpeta temporal
				borrarFile.borraFile(extContext.getRealPath(RUTA_LOGS), pcodcia + constancia + ".pdf");					
			}
			
			} catch (NamingException | NumberFormatException | IOException | InterruptedException e) {
				msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
				FacesContext.getCurrentInstance().addMessage(null, msj);
				e.printStackTrace();
			}
	  	}
	  	
	  	/**
		 * Método que envía los recibos de pago a cada usuario
		 * @throws IOException 
		 * @throws NumberFormatException 
	  	 * @throws InterruptedException 
		 * **/
		public void enviarRP(String to, String laruta, String file) throws NumberFormatException, IOException, InterruptedException {
			 try {
					//System.out.println("Enviando recibo");
					//System.out.println(jndimail());
					Context initContext = new InitialContext();     
			    	Session session = (Session) initContext.lookup(jndimail());
						// Crear el mensaje a enviar
						MimeMessage mm = new MimeMessage(session);

						// Establecer las direcciones a las que será enviado
						// el mensaje (test2@gmail.com y test3@gmail.com en copia
						// oculta)
						// mm.setFrom(new
						// InternetAddress("opennomina@dvconsultores.com"));
						mm.addRecipient(Message.RecipientType.TO,
								new InternetAddress(to));

						// Establecer el contenido del mensaje
						mm.setSubject(getMessage("mailRP"));
						//mm.setText(getMessage("mailContent"));
						
						// Create the message part 
				         BodyPart messageBodyPart = new MimeBodyPart();
				         
				      // Fill the message
				         messageBodyPart.setContent(getMessage("mailRPcontent"), "text/html; charset=utf-8");
				         
						// Create a multipar messageo
				         Multipart multipart = new MimeMultipart();
				         
				        // Set text message part
				         multipart.addBodyPart(messageBodyPart);
				         
				        // Part two is attachment
				         messageBodyPart = new MimeBodyPart();
				         String filename = laruta + File.separator + file + ".pdf";
				         javax.activation.DataSource source = new FileDataSource(filename);
				         messageBodyPart.setDataHandler(new DataHandler(source));
				         messageBodyPart.setFileName(file + ".pdf");
				         multipart.addBodyPart(messageBodyPart);
				         
				         // Send the complete message parts
				         mm.setContent(multipart );


						// Enviar el correo electrónico
						Transport.send(mm);
						//System.out.println("Correo enviado exitosamente a :" + to);
						//System.out.println("Fin recibo");
						msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("autos02EnviadoS"), "");
			    		FacesContext.getCurrentInstance().addMessage(null, msj);
						
												
				} catch (Exception e) {
					msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage() + ": " + to, "");
					FacesContext.getCurrentInstance().addMessage(null, msj);
					e.printStackTrace();
				}
	   }
		
		
		/**
		 * Método que envía los recibos de pago a cada usuario
		 * @throws IOException 
		 * @throws NumberFormatException 
	  	 * @throws InterruptedException 
		 * **/
		public void enviarConst(String to, String laruta, String file) throws NumberFormatException, IOException, InterruptedException {
			 try {
					//System.out.println("Enviando recibo");
					//System.out.println(jndimail());
					Context initContext = new InitialContext();     
			    	Session session = (Session) initContext.lookup(jndimail());
						// Crear el mensaje a enviar
						MimeMessage mm = new MimeMessage(session);

						// Establecer las direcciones a las que será enviado
						// el mensaje (test2@gmail.com y test3@gmail.com en copia
						// oculta)
						// mm.setFrom(new
						// InternetAddress("opennomina@dvconsultores.com"));
						mm.addRecipient(Message.RecipientType.TO,
								new InternetAddress(to));

						// Establecer el contenido del mensaje
						mm.setSubject(getMessage("mailConst"));
						//mm.setText(getMessage("mailContent"));
						
						// Create the message part 
				         BodyPart messageBodyPart = new MimeBodyPart();
				         
				      // Fill the message
				         messageBodyPart.setContent(getMessage("mailConstcontent"), "text/html; charset=utf-8");
				         
						// Create a multipar message
				         Multipart multipart = new MimeMultipart();
				         
				        // Set text message part
				         multipart.addBodyPart(messageBodyPart);
				         
				        // Part two is attachment
				         messageBodyPart = new MimeBodyPart();
				         String filename = laruta + File.separator + file + ".pdf";
				         javax.activation.DataSource source = new FileDataSource(filename);
				         messageBodyPart.setDataHandler(new DataHandler(source));
				         messageBodyPart.setFileName(file + ".pdf");
				         multipart.addBodyPart(messageBodyPart);
				         
				         // Send the complete message parts
				         mm.setContent(multipart );


						// Enviar el correo electrónico
						Transport.send(mm);
						//System.out.println("Correo enviado exitosamente a :" + to);
						//System.out.println("Fin recibo");
						msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("autos02EnviadoConst"), "");
			    		FacesContext.getCurrentInstance().addMessage(null, msj);
						
												
				} catch (Exception e) {
					msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage() + ": " + to, "");
					FacesContext.getCurrentInstance().addMessage(null, msj);
					e.printStackTrace();
				}
	   }
	  	
	  	/**
		 * Método para recibir los mail jndi
		 * **/
		protected String jndimail(){
			String query = "select trim(jndi) from seg001 where grupo = '" + grupo + "'";
			int rows = 0;
			String[][] vltabla;
			String jndi = "";
			try {
				consulta.selectPntGenerica(query, JNDI);
				rows = consulta.getRows();
				vltabla = consulta.getArray();
				if(rows>0){
					jndi = vltabla[0][0];
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
			return jndi;
		}

	  	
	  	/**
	     * Leer Datos básicos del detalle
	     * @throws NamingException 
	  	 * @throws IOException 
	     	
	  	private void selectBasicos(String cedula, String paramcia)  {
	
	  	    //Busca la consulta para encabezado básico
	  		try {
				consulta.selectPntGenerica("select id, to_char(totalasig,'999,999,999.99'), to_char(totaldedu,'999,999,999.99'), to_char(totalneto,'999,999,999.99')" +
						" from autos02 " +
						" where CAST(anio AS text) = '" + anio + "' " +
								" and CAST(mes AS text) = '" + mes + "' and  trim(cedula) like '" + cedula + "%' " +
										"and CAST(periodo AS text) like trim('" + periodo + "%') and id like trim('" + id + "%') and trim(pcodcia) in (select max(pcodcia) from autos02 where cast(anio as text) like '" + anio +  "' and cast(mes as text) like '" + mes + "' and cedula like '" + cedula + "%')" , JNDI);
			} catch (NamingException e) {
				e.printStackTrace();
			} 
	
	  		String[][] vltabla = consulta.getArray();
	  		rows = consulta.getRows();
	        if(rows>0){
	            frecuencia = vltabla[0][0];
	        	totalasig = vltabla[0][1];
	        	totaldedu = vltabla[0][2];
	        	totalneto = vltabla[0][3];
	        }
	  		  		
	  	}**/ 
	

	
	}
	
