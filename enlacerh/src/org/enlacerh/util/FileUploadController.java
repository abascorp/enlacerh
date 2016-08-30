/*
 *  Copyright (C) 2016 ABAS CORP, C.A.

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
 *  Esta clase controla todos los archivos de subida y procesamiento de txt al servidos
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.enlacerh.getset.Autonom;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;


@ManagedBean
@ViewScoped
public class FileUploadController extends Utils implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<Autonom> lazyModel;  
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Autonom> getLazyModel() {
		return lazyModel;
	}
	

	// Constructor
	public FileUploadController() {
		/*
		lazyModel  = new LazyDataModel<Autonom>(){
	
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Autonom> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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

		
            public void setRowIndex(int rowIndex) {

                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                }
                else
                    super.setRowIndex(rowIndex % getPageSize());
            }
            
		};*/
	}

	// Primitives
	private static final int BUFFER_SIZE = 6124;
	private static final String RUTA_EXTERNO = File.separator + "externos" + File.separator + "autoext";
	private static final String RUTA_REPORTE = File.separator + "reportes";
	//private static final String RUTA_LOGS = File.separator + "logs";
	private static final String RUTA_IMAGEN = File.separator + "imagenes" + File.separator  + "colab"; //Ruta donde sube las imágenes para autoservicio
	private Pattern pattern; //Validador de formatos de imagenes o texto
	private Matcher matcher; //Match parra validar imágenes o texto
	private static final String FORMATO_FILE = "([^\\s]+(\\.(?i)(txt))$)"; //Solo admite extensiones .txt
	private static final String FORMATO_REPORTE = "([^\\s]+(\\.(?i)(rptdesign|properties))$)"; //Solo admite extensiones .rptdesign
	private static final String FORMATO_IMAGE = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)"; //Solo admite extensiones .jpg, jpeg, gif, png
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	PntExternos externos = new PntExternos(); //Objeto para procesar archivos externos
	private String anio = ""; //Año a subir autoservicio
	private String mes = ""; //Mes subir autoservicio
	private String opc = ""; //Opciones de autoservicio a subir
	int count = 0; //Contador
	private String cedula = ""; //Cédula de usuario autoservicio logeado
	ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); //Toma ruta real de la aplicación
	private String grupo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("grupo"); //Usuario logeado
	String vlcodcia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ciaselected");
    
	//Variables para escaneo de documentos
	private double nroscan = 0; //Número de documento escaneado
    private String nbrdoc = "";
    private String comentdoc = ""; //Breve descripción del documento escaneado
    
    private List<Autonom> list = new ArrayList<Autonom>();
    private int rows; //Registros de tabla
    private String param = "";
    int sleep = 100; //100 milisegundos de retraso para ejecutar actautosrv
    
	/**
	 * @return the anio
	 */
	public String getAnio() {
		return anio;
	}

	/**
	 * @param anio
	 *            the anio to set
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
	 * @param mes
	 *            the mes to set
	 */
	public void setMes(String mes) {
		this.mes = mes;
	}
	
	

	/**
	 * @return the opc
	 */
	public String getOpc() {
		return opc;
	}

	/**
	 * @param opc the opc to set
	 */
	public void setOpc(String opc) {
		this.opc = opc;
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
	 * @return the nroscan
	 */
	public double getNroscan() {
		return nroscan;
	}

	/**
	 * @param nroscan the nroscan to set
	 */
	public void setNroscan(double nroscan) {
		this.nroscan = nroscan;
	}

	/**
	 * @return the nbrdoc
	 */
	public String getNbrdoc() {
		return nbrdoc;
	}

	/**
	 * @param nbrdoc the nbrdoc to set
	 */
	public void setNbrdoc(String nbrdoc) {
		this.nbrdoc = nbrdoc;
	}

	/**
	 * @return the comentdoc
	 */
	public String getComentdoc() {
		return comentdoc;
	}

	/**
	 * @param comentdoc the comentdoc to set
	 */
	public void setComentdoc(String comentdoc) {
		this.comentdoc = comentdoc;
	}

	
	
	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}


	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
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

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Variables seran utilizadas para capturar mensajes de errores de Oracle y
	// parametros de metodos
	FacesMessage msj = null; 
	PntGenerica consulta = new PntGenerica();
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Coneccion a base de datos
	//Pool de conecciones JNDI()
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////VARIABLES  PARA INDICAR COMPAÑ�?A SELECCIONADA/////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @return the codcia
	 * @throws NamingException 
	 */
	public String getCodcia() throws NamingException {
		String cia = null;
		String ciaselected = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ciaselected"); //Usuario logeado
		String desciaselected = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("desciaselected"); //Usuario logeado
        cia = ciaselected + " - " + desciaselected;
		return cia;
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////UPLOAD DE ARCHIVOS PARA AUTOSERVICIO//////////////////////////////////////////////////

	public void handleFileUpload(FileUploadEvent event) throws NamingException, IOException {
		if(licencia(grupo)) {
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    	} else {

		
		String vlcodcia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ciaselected");


		File ruta = new File(extContext.getRealPath(RUTA_EXTERNO) + File.separator + vlcodcia 
				+ File.separator + vlcodcia + "_" +  event.getFile().getFileName());
		FileValidator(FORMATO_FILE);//Compila el archivo
		if(validate(event.getFile().getFileName())){
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(ruta);
			
			byte[] buffer = new byte[BUFFER_SIZE];

			int bulk;
			InputStream inputStream = event.getFile().getInputstream();
			while (true) {
				bulk = inputStream.read(buffer);
				if (bulk < 0) {
					break;
				}
				fileOutputStream.write(buffer, 0, bulk);
				fileOutputStream.flush();
			}

			fileOutputStream.close();
			inputStream.close();

			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("autos02Uploaded"), "");

		} catch (IOException e) {
			e.printStackTrace();
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autos02UploadedFail"), "");
		}
		}else {
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autos02Txt"), "");
		}
		FacesContext.getCurrentInstance().addMessage(null, msj);
    	}
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////UPLOAD DE IMAGENES PARA AUTOSERVICIO//////////////////////////////////////////////////
	private String pcodcia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cia");
	private String vcedula = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cedula"); //Cedula autoservicio
	
	public void handleImageUpload(FileUploadEvent event) throws NamingException {
		if(event.getFile().getSize()>2097152){
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autonomSize"), "");
			
		} else {

        String foto = event.getFile().getFileName().replace(event.getFile().getFileName(), pcodcia+vcedula + ".png");
 
		File ruta = new File(extContext.getRealPath(RUTA_IMAGEN)
				+ File.separator +   foto);
		FileValidator(FORMATO_IMAGE);//Compila el archivo
		if(validate(event.getFile().getFileName())){
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(ruta);
			
			byte[] buffer = new byte[BUFFER_SIZE];

			int bulk;
			InputStream inputStream = event.getFile().getInputstream();
			while (true) {
				bulk = inputStream.read(buffer);
				if (bulk < 0) {
					break;
				}
				fileOutputStream.write(buffer, 0, bulk);
				fileOutputStream.flush();
			}

			fileOutputStream.close();
			inputStream.close();

			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("autos02Uploaded"), "");

		} catch (IOException e) {
			e.printStackTrace();
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autos02UploadedFail"), "");
		}
		}else {
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autonomPng"), "");
		}
		}//Valida el tamaño del archivo
		FacesContext.getCurrentInstance().addMessage(null, msj);
	}
	
	   	 
	   private void FileValidator(String format){
		  pattern = Pattern.compile(format);
	   }
	 
	   /**
	   * Validate extension with regular expression
	   * @param ifile for validation
	   * @return true valid file, false invalid file
	   */
	   private boolean validate(final String file){	 
		  matcher = pattern.matcher(file);
		  return matcher.matches();
	   }
	   
	   
    
	    /**
	     * Borra autos02
	     **/
	    public void deleteAutos (String tabla, String codcia, String opc) throws  NamingException {  
	    	try {
	    		Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	        	con = ds.getConnection();

	        	String query = "";
	        	if(opc.equals("0")){
	        	query = "DELETE FROM " + tabla +" WHERE PCODCIA = '" + codcia + "' AND ANIO = " + Integer.parseInt(anio) + " and MES = " + Integer.parseInt(mes) ;
	        	} else {
	        	query = "DELETE FROM " + tabla +" WHERE PCODCIA = '" + codcia + "'" ;
	        	}
	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);		            
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                //System.out.println("Registros:" + pstmt.getUpdateCount());	
	                count = pstmt.getUpdateCount();
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
	     * Procesa el autoservicio.
	     * Primero borra el mes y año a la compañía corresponiente
	     * Busca el txt en la carpeta de archivos
	     * Procesa el txt en la tabla autos02
	     * @throws NamingException 
	     * @throws IOException 
	     * @throws InterruptedException 
	     **/
	    @SuppressWarnings("static-access")
		public void procAutos02() throws NamingException, IOException, InterruptedException{
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {

			
		  	
	    	//Selecciona la compañia por defecto para agregarla al nombre de txt. El autoservicio se debe procesar por compañía
			//, el mismo debe garantizar que cada archivo txt tenga asociado la compañía para poder separlas.
	    	
			
			//HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	    //String[] chkbox = request.getParameterValues("toMail");
			
            //Genera uno por cada opción
            //Genera uno por cada opción
    	    /*
			if (chkbox!=null  && opc.equals("P")){
        		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autos02MailMsj"), "");
        		FacesContext.getCurrentInstance().addMessage(null, msj);
        	} else if (chkbox!=null  &&  opc.equals("V")){
        		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autos02MailMsj"), "");
        		FacesContext.getCurrentInstance().addMessage(null, msj);
        	} else if (chkbox!=null  &&  opc.equals("U")){
        		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autos02MailMsj"), "");
        		FacesContext.getCurrentInstance().addMessage(null, msj);
        	} else if (chkbox!=null  &&  opc.equals("I")){
        		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autos02MailMsj"), "");
        		FacesContext.getCurrentInstance().addMessage(null, msj);
        	} else if (chkbox!=null  &&  opc.equals("PR")){
        		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autos02MailMsj"), "");
        		FacesContext.getCurrentInstance().addMessage(null, msj);	
        	} else*/
    	    if (opc.equals("N")){ 	    	
    		deleteAutos("AUTOS02", vlcodcia, "0"); //Elimina registros anteriores
    		//Procesa el archivo de texto
    		externos.pntExt(extContext.getRealPath(RUTA_EXTERNO)+ File.separator + vlcodcia, vlcodcia+"_"+opc+anio+mes, "AUTOS02", JNDI); //Procesa nóminas

    			  try {
					Thread.currentThread().sleep(sleep);
					//sleep for 100 ms
	    			  //do what you want to do after sleeptig
	    			  actAutoSrv(vlcodcia, Integer.parseInt(anio), Integer.parseInt(mes), login.toUpperCase(), "autos02");
	
					} catch (InterruptedException e) {
						e.printStackTrace();
						msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
					}
    		
    		
    		//Envía recibos de pago
    			  /*
    		if (chkbox!=null  && opc.equals("N")){
    			
    			//Procesando reportes en pdf para envios de correo
        		
        		//Selecciona ficha segun usuarios de autoservicio
        		consulta.selectPntGenerica("select distinct trim(CAST(periodo as text)), trim(pfictra), trim(b.mail) " +
        				" from autos02 a, autos01 b" +
        				" where trim(a.cedula)=trim(CAST(b.cedula AS text))" +
        				" and a.pcodcia='" + vlcodcia + 
        				"' and anio=" + anio + " and mes =" + mes +
        				" and periodo in (select periodo from recibos_enviados where pcodcia = '" + vlcodcia + 
        				"' and anio = " + anio + " and mes =" + mes + " and isenv = '0') and mail_validado = '1'", JNDI);
        		int filas = consulta.getRows();
        		String[][] vlconsulta = consulta.getArray();
        		RunReport reportout = new RunReport();
        		FileIO borrarFile = new FileIO();
               
        		//Si hay al go enviar
        		if(filas>0){
        		
        		for (int i = 0; i < filas; i ++) {
        			 //Genera los recibos en pdf
        			reportout.outReporteRecibo(vlcodcia+"AUTONOM", "pdf"
        					, extContext.getRealPath(RUTA_REPORTE), extContext.getRealPath(RUTA_LOGS)
        					  , "RP_"+vlconsulta[i][1] + "_" + vlconsulta[i][0], anio, mes, vlconsulta[i][0].toString(), vlconsulta[i][1], JNDI);  

        		}
        		for (int i = 0; i < filas; i ++) {
       			//Enviar correo por recibo de pago
       			enviarRP(vlconsulta[i][2], extContext.getRealPath(RUTA_LOGS), "RP_"+vlconsulta[i][1] + "_" + vlconsulta[i][0]);
       			//Actualiza histórico de recibos enviados
       			update(vlcodcia, anio, mes, vlconsulta[i][0], '1');
       		    }
        	    for (int i = 0; i < filas; i ++) {       			
       			//Para finalizar borrar los archivos generados de la carpeta logs
       			borrarFile.borraFile(extContext.getRealPath(RUTA_LOGS), "RP_"+vlconsulta[i][1] + "_" + vlconsulta[i][0] + ".pdf");
       	        }
        		}
    			
    		}*/
    	
    		
    		anio="";
    		opc="S";
    		mes="S";  
    		}else if(opc.equals("V")){
    		deleteAutos("AUTOS03", vlcodcia, "1"); //Elimina registros anteriores
	    	//Procesa el archivo de texto
    		externos.pntExt(extContext.getRealPath(RUTA_EXTERNO)+ File.separator + vlcodcia, vlcodcia+"_"+opc+anio+mes, "AUTOS03", JNDI); //Procesa vacaciones	
    		try {
				Thread.currentThread().sleep(sleep);
				//sleep for 100 ms
    			  //do what you want to do after sleeptig
    			  actAutoSrv(vlcodcia, Integer.parseInt(anio), Integer.parseInt(mes), login.toUpperCase(), "autos03");

				} catch (InterruptedException e) {
					e.printStackTrace();
					msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
				}
    		anio="";
    		opc="S";
    		mes="S";   		
    		}else if(opc.equals("P")){
	    	deleteAutos("AUTOS04", vlcodcia, "0"); //Elimina registros anteriores
		    //Procesa el archivo de texto
	    	externos.pntExt(extContext.getRealPath(RUTA_EXTERNO)+ File.separator + vlcodcia, vlcodcia+"_"+opc+anio+mes, "AUTOS04", JNDI); //Procesa prestaciones	
	    	try {
				Thread.currentThread().sleep(sleep);
				//sleep for 100 ms
    			  //do what you want to do after sleeptig
    			  actAutoSrv(vlcodcia, Integer.parseInt(anio), Integer.parseInt(mes), login.toUpperCase(), "autos04");

				} catch (InterruptedException e) {
					e.printStackTrace();
					msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
				}
	    	anio="";
    		opc="S";
    		mes="S";    		
    		}else if(opc.equals("I")){
	    	deleteAutos("AUTOS05", vlcodcia, "1"); //Elimina registros anteriores
		    //Procesa el archivo de texto
	    	externos.pntExt(extContext.getRealPath(RUTA_EXTERNO)+ File.separator + vlcodcia, vlcodcia+"_"+opc+anio+mes, "AUTOS05", JNDI); //Procesa intereses	
	    	try {
				Thread.currentThread().sleep(sleep);
				//sleep for 100 ms
    			  //do what you want to do after sleeptig
    			  actAutoSrv(vlcodcia, Integer.parseInt(anio), Integer.parseInt(mes), login.toUpperCase(), "autos05");

				} catch (InterruptedException e) {
					e.printStackTrace();
					msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
				}
	    	anio="";
    		opc="S";
    		mes="S";    		
    		}else if(opc.equals("PR")){
	    	deleteAutos("AUTOS06", vlcodcia, "0"); //Elimina registros anteriores
		    //Procesa el archivo de texto
	    	externos.pntExt(extContext.getRealPath(RUTA_EXTERNO)+ File.separator + vlcodcia, vlcodcia+"_"+opc+anio+mes, "AUTOS06", JNDI); //Procesa préstamos	
	    	actAutoSrv(vlcodcia, Integer.parseInt(anio), Integer.parseInt(mes), login.toUpperCase(), "autos06");
	    	anio="";
    		opc="S";
    		mes="S";   		
	    	}else if(opc.equals("U")){
	    	deleteAutos("AUTOS07", vlcodcia, "0"); //Elimina registros anteriores
		    //Procesa el archivo de texto
	    	externos.pntExt(extContext.getRealPath(RUTA_EXTERNO)+ File.separator + vlcodcia, vlcodcia+"_"+opc+anio+mes, "AUTOS07", JNDI); //Procesa utilidades	
	    	try {
				Thread.currentThread().sleep(sleep);
				//sleep for 100 ms
    			  //do what you want to do after sleeptig
    			  actAutoSrv(vlcodcia, Integer.parseInt(anio), Integer.parseInt(mes), login.toUpperCase(), "autos07");

				} catch (InterruptedException e) {
					e.printStackTrace();
					msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
				}
	    	anio="";
    		opc="S";
    		mes="S";	
	    	}else if(opc.equals("T")){//Procesa todas las opciones utilida multiThreads para el caso
	    		
	    		deleteAutos("AUTOS02", vlcodcia, "0"); //Elimina registros anteriores
	    		//Procesa el archivo de texto
	    		externos.pntExtThread(extContext.getRealPath(RUTA_EXTERNO)+ File.separator + vlcodcia, vlcodcia+"_N"+anio+mes, "AUTOS02", JNDI); //Procesa nóminas
	    		try {
					Thread.currentThread().sleep(sleep);
					//sleep for 100 ms
	    			  //do what you want to do after sleeptig
	    			  actAutoSrv(vlcodcia, Integer.parseInt(anio), Integer.parseInt(mes), login.toUpperCase(), "autos02");
	
					} catch (InterruptedException e) {
						e.printStackTrace();
						msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
					}
	    		
	    		//Envía recibos de pago
	    		/*
	    		if (chkbox!=null  && opc.equals("T")){
	    			
	    			//Procesando reportes en pdf para envios de correo
	        		
	        		//Selecciona ficha segun usuarios de autoservicio
	    			consulta.selectPntGenerica("select distinct trim(CAST(periodo as text)), trim(pfictra), trim(b.mail) " +
	        				" from autos02 a, autos01 b" +
	        				" where trim(a.cedula)=trim(CAST(b.cedula AS text))" +
	        				" and a.pcodcia='" + vlcodcia + 
	        				"' and anio=" + anio + " and mes =" + mes +
	        				" and periodo in (select periodo from recibos_enviados where pcodcia = '" + vlcodcia + 
	        				"' and anio = " + anio + " and mes =" + mes + " and isenv = '0')", JNDI);
	        		int filas = consulta.getRows();
	        		String[][] vlconsulta = consulta.getArray();
	        		RunReport reportout = new RunReport();
	        		FileIO borrarFile = new FileIO();

	        		
	        		//Si hay al go enviar
	        		if(filas>0){
	        		
	        		for (int i = 0; i < filas; i ++) {
	        			 //Genera los recibos en pdf
	        			reportout.outReporteRecibo(vlcodcia+"AUTONOM", "pdf"
	        					, extContext.getRealPath(RUTA_REPORTE), extContext.getRealPath(RUTA_LOGS)
	        					  , "RP_"+vlconsulta[i][1] + "_" + vlconsulta[i][0], anio, mes, vlconsulta[i][0].toString(), vlconsulta[i][1], JNDI);  

	        		}
	        		for (int i = 0; i < filas; i ++) {
	       			
	       			//Enviar correo por recibo de pago
	       			enviarRP(vlconsulta[i][2], extContext.getRealPath(RUTA_LOGS), "RP_"+vlconsulta[i][1] + "_" + vlconsulta[i][0]);
	       			//Actualiza histórico de recibos enviados
	       			update(vlcodcia, anio, mes, vlconsulta[i][0], '1');
	       		    }
	        	    for (int i = 0; i < filas; i ++) {       			
	       			//Para finalizar borrar los archivos generados de la carpeta logs
	       			borrarFile.borraFile(extContext.getRealPath(RUTA_LOGS), "RP_"+vlconsulta[i][1] + "_" + vlconsulta[i][0] + ".pdf");
	       	        }
	        		}
	    			
	    		}*/
	    	//Utiliza la técica de multithread para optimizar el proceso usando todos los cores del equipo
	    	//Crea Objetos tipo AutosrvThread	
	    	AutosrvThread pr =  new AutosrvThread("AUTOS02", vlcodcia, "N", anio, mes, "0");
	    	AutosrvThread pr1 = new AutosrvThread("AUTOS03", vlcodcia, "V", anio, mes, "1");
	    	AutosrvThread pr2 = new AutosrvThread("AUTOS04", vlcodcia, "P", anio, mes, "0");
	    	AutosrvThread pr3 = new AutosrvThread("AUTOS05", vlcodcia, "I", anio, mes, "1");
	    	AutosrvThread pr4 = new AutosrvThread("AUTOS06", vlcodcia, "PR", anio, mes, "0");
	    	AutosrvThread pr5 = new AutosrvThread("AUTOS07", vlcodcia, "U", anio, mes, "0");
	    	
    	
	    	//Llamada de ejecución de los Threads
	    	pr.start();
	    	pr1.start();
	    	pr2.start();	
	    	pr3.start();
	    	pr4.start();
	    	pr5.start();
	    	    	   		
    		
    		try {
				Thread.currentThread().sleep(sleep);
				//sleep for 100 ms
    			  //do what you want to do after sleeptig
				actAutoSrv(vlcodcia, Integer.parseInt(anio), Integer.parseInt(mes), login.toUpperCase(), "autos02");
				actAutoSrv(vlcodcia, Integer.parseInt(anio), Integer.parseInt(mes), login.toUpperCase(), "autos03");
				actAutoSrv(vlcodcia, Integer.parseInt(anio), Integer.parseInt(mes), login.toUpperCase(), "autos04");
				actAutoSrv(vlcodcia, Integer.parseInt(anio), Integer.parseInt(mes), login.toUpperCase(), "autos05");
				actAutoSrv(vlcodcia, Integer.parseInt(anio), Integer.parseInt(mes), login.toUpperCase(), "autos06");
    			actAutoSrv(vlcodcia, Integer.parseInt(anio), Integer.parseInt(mes), login.toUpperCase(), "autos07");

				} catch (InterruptedException e) {
					e.printStackTrace();
					msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
				}
    		anio="";
    		opc="S";
    		mes="S";
    		
    		msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnProcAutoSrvThread"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
		    }	
	    	}
	    	actCaracteres();
	    }
	    
	    /**
	     * Rutina que ejecuta el control de proceso de licencias de grupos de trabajadores.
	     * Por ejemplo si la licencia es hasta 25 y el usuario carga mas de 25 el procedimiento
	     * almacenado en la base de datos elimina los trabajadres de 25 en adelante
	     **/
	    private void actAutoSrv(String cia, Integer anio, Integer mes, String user, String opcauto) throws  NamingException {
	        //Pool de conecciones JNDI(). Cambio de metodología de conexión a bd. Julio 2010
	        Context initContext = new InitialContext();
	        DataSource ds = (DataSource) initContext.lookup(JNDI);
	        try {
	            Connection con = ds.getConnection();
	            CallableStatement proc = null;


	            proc = con.prepareCall("{? = CALL act_autosrv(?,?,?,?,?)}");
	            proc.registerOutParameter(1, Types.OTHER);
	            proc.setString(2, cia);
	            proc.setInt(3, anio);
	            proc.setInt(4, mes);
	            proc.setString(5, user);
	            proc.setString(6, opcauto);
	            proc.execute();
	            //System.out.println("Compañía: " + cia);
	            //System.out.println("Año: " + anio);
	            //System.out.println("Mes: " + mes);
	            //System.out.println("Usuario: " + user);
	            //System.out.println("Opción " + opcauto);
	            //
	            proc.close();
	            con.close();
	            
	            //System.out.println("Actualizando");

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    
	    
	    /**
	     * Rutina que ejecuta el control de proceso de licencias de grupos de trabajadores.
	     * Por ejemplo si la licencia es hasta 25 y el usuario carga mas de 25 el procedimiento
	     * almacenado en la base de datos elimina los trabajadres de 25 en adelante
	     **/
	    private  void actCaracteres() throws  NamingException {
	        //Pool de conecciones JNDI(). Cambio de metodología de conexión a bd. Julio 2010
	        Context initContext = new InitialContext();
	        DataSource ds = (DataSource) initContext.lookup(JNDI);
	        try {
	            Connection con = ds.getConnection();
	            CallableStatement proc = null;


	            proc = con.prepareCall("{? = CALL act_caracteres()}");
	            proc.registerOutParameter(1, Types.OTHER);
	            proc.execute();
	            //System.out.println("Compañía: " + cia);
	            //System.out.println("Año: " + anio);
	            //System.out.println("Mes: " + mes);
	            //System.out.println("Usuario: " + user);
	            //System.out.println("Opción " + opcauto);
	            //
	            proc.close();
	            con.close();

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * Actualiza es estatus de los recibos de pago enviados
	     
	    private void update(String codcia, String anio, String mes, String periodo, char c)  {
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	        	con = ds.getConnection();

	            String query = "UPDATE recibos_enviados";
	            	   query += " SET isenv = '" + c + "'";
	                   query += " WHERE pcodcia = trim('" + codcia + "')";
	                   query += " and anio = " + Integer.parseInt(anio);
	                   query += " and mes = " + Integer.parseInt(mes);
	                   query += " and periodo = " + Integer.parseInt(periodo);
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

	    }**/
	    
	  
	    
	    /**
	     * Leer Datos de TipoConsulta
	     * @throws NamingException 
	     * @throws IOException 
	     **/ 	
	  	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, NamingException {
	     try {	
	    	Context initContext = new InitialContext();     
	   		DataSource ds = (DataSource) initContext.lookup(JNDI);

	   		con = ds.getConnection();
	   		
	   		String ciaselected = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ciaselected");
	   		

	  		//Consulta paginada
	  		String query = "select distinct  id,  anio, mes, periodo, case when isenv = '1' then '" + getMessage("autos02EnviadoS") + "' else '" + getMessage("autos02EnviadoN") + "' end";
	  		query += " from recibos_enviados";
	        query += " where id||cast(anio as text)||cast(mes as text)||cast(periodo as text)  like '%" + ((String) filterValue).toUpperCase() + "%'";
	        query += " and pcodcia = '" + ciaselected.toUpperCase() + "'";
	        query += " order by anio, periodo desc ";
	        query += " LIMIT " + pageSize;
	        query += " OFFSET " + first;
	        
	        pstmt = con.prepareStatement(query);
	        //System.out.println(query);
	  		
	         r =  pstmt.executeQuery();
	        
	        
	        while (r.next()){
	        	Autonom select = new Autonom();
                select.setVid(r.getString(1));
                select.setVanio(r.getString(2));
                select.setVmes(r.getString(3));
                select.setVperiodo(r.getString(4));
                select.setVcodcto(r.getString(5));
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

	   		con = ds.getConnection();
	   		String ciaselected = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ciaselected");

	  		//Consulta paginada
	  		String query = "SELECT count_recibos_enviados('" + ((String) filterValue).toUpperCase() + "','" + ciaselected.toUpperCase() +  "')";

	        
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
	     * Borra TipoConsulta
	     * <p>
	     * Parametros del metodo: String codcentro. Pool de conecciones
	     * @throws NamingException 
	  	 * @throws IOException 
	     **/  
	    public void delete_recibos_enviados() throws NamingException, IOException  {  
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	String[] chkbox = request.getParameterValues("toDelete");
	    	if (chkbox==null){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("del"), "");
	    	} else {
	        try {
	        	Context initContext = new InitialContext();     
	        	DataSource ds = (DataSource) initContext.lookup(JNDI);
	        	con = ds.getConnection();
	        	
	        	String param = "'" + StringUtils.join(chkbox, "','") + "'";
	        	
	        	String ciaselected = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ciaselected");

	        	String query = "DELETE FROM recibos_enviados WHERE cast(anio as text)||cast(mes as text)||cast(periodo as text) in (" + param + ") and pcodcia = '" + ciaselected + "'";
	            pstmt = con.prepareStatement(query);
	            //System.out.println(query);

	 
	            try {
	                //Avisando
	                pstmt.executeUpdate();
	                if (pstmt.getUpdateCount() <= 1) {
	                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDelete"), "");
	                } else {
	                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO,  pstmt.getUpdateCount() + " " +getMessage("msnDeletes"), "");
	                }

	            } catch (SQLException e) {
	                e.printStackTrace();
	                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
	            }

	            pstmt.close();
	            con.close();
	               

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    	}
	    	FacesContext.getCurrentInstance().addMessage(null, msj); 
	    	}
	    }
	    
	    
	    
	    
	    /**
	     * Elimina datos en autoservicio.
	     **/
	    public void delete() throws NamingException, IOException{
	    	if(licencia(grupo)) {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {

			if(anio==null){
				anio = "";
			}

			if (anio.equals("")){
				msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autos02ProcAnio"), "");
				FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else 	if (mes.equals("S")){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autos02ProcMes"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else if (opc.equals("S")){
	    		msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autos02ProcOpc"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    	//Valida que los campos no sean nulos y procesa autoservicio
	        //No hace nada si son nulos
	    	//Selecciona la compañia por defecto para agregarla al nombre de txt. El autoservicio se debe procesar pro compañía
			//, el mismo debe garantizar que cada archivo txt tenga asociado la compañía para poder separlas.
	    	String vlcodcia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ciaselected");
            //Genera uno por cada opción

    		if(opc.equals("N")){
    		deleteAutos("AUTOS02", vlcodcia, "0"); //Elimina registros anteriores
    		if(count>0){
    			anio="";
        		opc="S";
        		mes="S";
    			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDeletes"), "");
    			FacesContext.getCurrentInstance().addMessage(null, msj);
    		} else {
    			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnnoDelete"), "");
    			FacesContext.getCurrentInstance().addMessage(null, msj);
    		}
    		}else if(opc.equals("V")){
    		deleteAutos("AUTOS03", vlcodcia, "1"); //Elimina registros anteriores
    		if(count>0){
    			anio="";
        		opc="S";
        		mes="S";
    			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDeletes"), "");
    			FacesContext.getCurrentInstance().addMessage(null, msj);
    		} else {
    			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnnoDelete"), "");
    			FacesContext.getCurrentInstance().addMessage(null, msj);
    		}
    		}else if(opc.equals("P")){
	    	deleteAutos("AUTOS04", vlcodcia, "0"); //Elimina registros anteriores	
	    	if(count>0){
	    		anio="";
	    		opc="S";
	    		mes="S";
    			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDeletes"), "");
    			FacesContext.getCurrentInstance().addMessage(null, msj);
    		} else {
    			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnnoDelete"), "");
    			FacesContext.getCurrentInstance().addMessage(null, msj);
    		}
	    	}else if(opc.equals("I")){
	    	deleteAutos("AUTOS05", vlcodcia, "0"); //Elimina registros anteriores
	    	if(count>0){
	    		anio="";
	    		opc="S";
	    		mes="S";
    			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDeletes"), "");
    			FacesContext.getCurrentInstance().addMessage(null, msj);
    		} else {
    			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnnoDelete"), "");
    			FacesContext.getCurrentInstance().addMessage(null, msj);
    		}
	    	}else if(opc.equals("PR")){
	    	deleteAutos("AUTOS06", vlcodcia, "0"); //Elimina registros anteriores
	    	if(count>0){
	    		anio="";
	    		opc="S";
	    		mes="S";
    			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDeletes"), "");
    			FacesContext.getCurrentInstance().addMessage(null, msj);
    		} else {
    			msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnnoDelete"), "");
    			FacesContext.getCurrentInstance().addMessage(null, msj);
    		}
	    	}else if(opc.equals("U")){
	    	deleteAutos("AUTOS07", vlcodcia, "0"); //Elimina registros anteriores	
	    	if(count>0){
	    		anio="";
	    		opc="S";
	    		mes="S";
	    		msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnDeletes"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    	} else {
	    		msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnnoDelete"), "");
	    		FacesContext.getCurrentInstance().addMessage(null, msj);
	    		}
	    	}	    		
	    	}
	    	}
	    }
	    
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////UPLOAD DE ARCHIVOS PARA AUTOSERVICIO//////////////////////////////////////////////////

	public void handleReportUpload(FileUploadEvent event) throws NamingException, IOException {
		if(licencia(grupo)) {
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    	} else {
		File ruta = new File(extContext.getRealPath(RUTA_REPORTE)+ File.separator +  event.getFile().getFileName());
		FileValidator(FORMATO_REPORTE);// Compila el archivo
		if (validate(event.getFile().getFileName())) {
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(ruta);

				byte[] buffer = new byte[BUFFER_SIZE];

				int bulk;
				InputStream inputStream = event.getFile().getInputstream();
				while (true) {
					bulk = inputStream.read(buffer);
					if (bulk < 0) {
						break;
					}
					fileOutputStream.write(buffer, 0, bulk);
					fileOutputStream.flush();
				}

				fileOutputStream.close();
				inputStream.close();

				msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("autos02Uploaded"), "");

			} catch (IOException e) {
				e.printStackTrace();
				msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autos02UploadedFail"), "");
			}
		} else {
			msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("autos02Rpt"), "");
		}
		FacesContext.getCurrentInstance().addMessage(null, msj);
    	}
	}
	
	

	/**
	 * Método que envía los recibos de pago a cada usuario
	 * @throws IOException 
	 * @throws NumberFormatException 
	 * **/
	public void enviarRP(String to, String laruta, String file) throws NumberFormatException, IOException {
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
			} catch (Exception e) {
				msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage() + ": " + to, "");
				FacesContext.getCurrentInstance().addMessage(null, msj);
				e.printStackTrace();
			}
   }
	
	/**
	 * Método para recibir los mail jndi
	 * **/
	private String jndimail(){
		String query = "select trim(jndimail) from seg001 where grupo = '" + grupo + "'";
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
	
	
  //////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	 ///////////////////////////////////////////////////////////////////////////////////////////////////
	  ////////////////////////////INTEGRACIONES LOCALES EN CADA CLIENTE///////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	private int columns;
	private String[][] arr;
	private String[][] tabla;
	
	
	
	/**
	 * @return the columns
	 */
	public int getColumns() {
		return columns;
	}


	/**
	 * @param columns the columns to set
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}


	/**
	 * @return the arr
	 */
	public String[][] getArr() {
		return arr;
	}


	/**
	 * @param arr the arr to set
	 */
	public void setArr(String[][] arr) {
		this.arr = arr;
	}


	/**
	 * @return the tabla
	 */
	public String[][] getTabla() {
		return tabla;
	}


	/**
	 * @param tabla the tabla to set
	 */
	public void setTabla(String[][] tabla) {
		this.tabla = tabla;
	}
	
	
	
}
