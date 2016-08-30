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
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;


/**
 * Crea Usuarios y clave de Base de Datos para todos los programas
 */
@ManagedBean
@RequestScoped
public class Utils  {



	// Constructor

    public Utils() {

    }
    //Declaracion de variables para manejo de mensajes multi idioma y pais
    private String lenguaje = "es";
    private String pais = "VEN";
    private Locale  localidad = new Locale(lenguaje, pais);
    ResourceBundle recursos =  ResourceBundle.getBundle("org.enlacerh.util.MessagesBundle",localidad);
    private String Message = "";
    @SuppressWarnings("unused")
	private Locale OsLang = Locale.getDefault();
    
    

    
      /**
     * Recursos de lenguaje. Archivos Properties
     **/
    public String getMessage(String mensaje) {
        Message = recursos.getString(mensaje);
        return Message;
    }

    
	/**
	 * @return the localidad
	 */
	public Locale getLocalidad() {
		return localidad;
	}
	
	//Para trabajar con quartz properties, por alguna razón no funciona con external context
    static FacesContext ctx = FacesContext.getCurrentInstance();
	protected static final String JNDI = ctx.getExternalContext().getInitParameter("JNDI_BD"); //"jdbc/orabiz"; //Nombre del JNDI
	static final String JNDINOMINA = ctx.getExternalContext().getInitParameter("JNDI_NOMINA"); //"jdbc/orabiz"; //Nombre del JNDI
	static final String JNDIMAIL = ctx.getExternalContext().getInitParameter("JNDI_MAIL"); //"jdbc/orabiz"; //Nombre del JNDI
    static final String THREADNUMBER = ctx.getExternalContext().getInitParameter("THREAD_NUMBER");
    static final String FECHAFORMAT = ctx.getExternalContext().getInitParameter("FORMAT_DATE");

    //Declaracion de variables y manejo de mensajes
    private int session = 900; //Reflejado en segundos. Son quince minutos 15*60=900
    private int tBlanq = 1000; //Segundos para eliminar mensajes(Segundos)
    String userLang = System.getProperty("user.language");//Identifica el lenguaje el OS
    String userCountry = System.getProperty("user.country");//Identifica el pais el OS
    Locale locale = new Locale(userLang, userCountry);//Identifica idioma y pais, por defecto le colocamos ven
    
    java.util.Date fecact = new java.util.Date();
    //Fecha para todas las pistas de auditoria
    ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext(); //Toma ruta real de la aplicación
 	File ruta = new File(extContext.getRealPath( File.separator + "WEB-INF"));//Ruta ubicada archivo xml
 	Xml xml = new Xml(); //Objeto xml   
 	//java.text.SimpleDateFormat sdfecha = new java.text.SimpleDateFormat("dd/MM/yyyy", locale );
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEE, d MMM yyyy", localidad);
    String fechaTop = sdf.format(fecact);//Fecha a mostrar en top
    String fecha = ""; //Fecha formateada para insertar en tablas
    String productName; //Manejador de base de datos
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////Variables para definir usuario y grupo para que cada usuario pueda administrar la aplicacion////
    ////de manera independiente.////////////////////////////////////////////////////////////////////////
    private String tipusr = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipusr"); //Usuario logeado
    private String growl = "growl"; //Manejo de getter y setter para mensajes de de validación
                                    //La idea es que no aparezca al momento de insertar, eliminar o modificar 
                                    //al momento de realizar cualquier operación se le cambia el nombre al growl
                                    //por growl1 así no actualiza el mensaje
    private String grupo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("grupo"); //Usuario logeado
    private int msg = 50000; //Duración de los mensajes
    
    
    
	/**
	 * @return the msg
	 */
	public int getMsg() {
		return msg;
	}


	/**
	 * @param msg the msg to set
	 */
	public void setMsg(int msg) {
		this.msg = msg;
	}


	/**
	 * @return the grupo
	 */
	public String getGrupo() {
		return grupo;
	}


	/**
	 * @return the growl
	 */
	public String getGrowl() {
		return growl;
	}


	/**
	 * @param growl the growl to set
	 */
	public void setGrowl(String growl) {
		this.growl = growl;
	}

	

	public int getSession() {
        return session;
    }


    public int gettBlanq() {
        return tBlanq;
    }
 

    public String getFechaTop() {
        return fechaTop;
    }

   /**
     * Obtiene la fecha del dia, ya que se va a utilizar en todas la tablas
     * se crea el metodo.
 * @throws IOException 
     */
    public String getFecha() throws IOException {
    	java.text.SimpleDateFormat sdfecha = new java.text.SimpleDateFormat(FECHAFORMAT, locale );
    	fecha = sdfecha.format(fecact);
        return fecha;
    }
    
 
    /**
     * Formatea la fecha leyendo el formato desde xml de configuración
     * @param La fecha a cargar 
     * @throws IOException 
     */
    public String getFechaFormat(Date pfecha) throws IOException {
    	java.text.SimpleDateFormat sdfecha = new java.text.SimpleDateFormat(FECHAFORMAT, locale );    	 
        return sdfecha.format(pfecha);
    }
   
    
    /**
     * Valida que el valor en el parámetro sea entero.
     * Retorna cero(0) si es numérico, retorna (1) si no lo és
     * @return Integer
     */
    public int IsInt (String Valor){
     try {
      Integer.parseInt(Valor);
    	return 0;
    	} catch (Exception e) {
    	return 1;
    	}
    }
    
    /**
     * Valida que el valor en el parámetro sea numérico.
     * Retorna cero(0) si es numérico, retorna (1) si no lo és
     * @return Integer
     */
    public int IsFloat (String Valor){
     try {
      Float.parseFloat(Valor);
    	return 0;
    	} catch (Exception e) {
    	return 1;
    	}
    }
    
    
    public int isEmail(String correo) {
        Pattern pat = null;
        Matcher mat = null;        
        pat = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        mat = pat.matcher(correo);
        if (mat.find()) {
            //System.out.println("[" + mat.group() + "]");
            return 0;
        }else{
            return 1;
        }        
    }

	/**
	 * @return the ruta
	 */
	public File getRuta() {
		return ruta;
	}

	/**
	 * @param ruta the ruta to set
	 */
	public void setRuta(File ruta) {
		this.ruta = ruta;
	}
    
    
////FUNCION QUE INDICA SI ES ROOT PARA MOSTRAR VALORES EN PAGINA
	
	public String isRoot() throws NamingException{
		String retorna = "false";
		if(tipusr.equals("root")){
			retorna = "true";
		} 
		return retorna;	
	}
   
	/**
	 * Verifica si licencia esta activa o vencida por grupo de usuario
	 * @param boolean
	 * @throws IOException 
	 */
	public boolean licencia(String grupo) throws NamingException, IOException{
		PntGenerica consulta = new PntGenerica();
		consulta.selectPntGenerica("select estatus from seg001 where grupo = " + grupo, JNDI);
		String [][] vltabla = consulta.getArray();
		int rows = consulta.getRows();
		if (rows>0 && vltabla[0][0].equals("0")){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Retorna la fecha de finalización al año 2999
	 * utilizado para variables de fechas que no son obligatorias.
	 * Retorna 31/12/2999
	 * @return  Date
	 */
	protected Date getLastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2999);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        Date date = calendar.getTime();
        return date;
    }
	
  	/*
     * Java method to sort Map in Java by value e.g. HashMap or Hashtable
     * throw NullPointerException if Map contains null values
     * It also sort values even if they are duplicates
     */
    @SuppressWarnings("rawtypes")
	public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
     
        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

            @SuppressWarnings("unchecked")
			@Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
     
        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
     
        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
     
        return sortedMap;
    }

 
    
	//////////////////////////////////////////VARIABLES AUTOCOMPLETE//////////////////////////////////
	/**
	 * Setea pais seleccionado
	 * @param next
	 */
	public void setPais(String pais){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pais", pais);
    }
	
	/**
	 * Setea ciudad seleccionada
	 * @param next
	 */
	public void setCiudad(String ciudad){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ciudad", ciudad);
    }
	
	/**
	 * Setea municipio seleccionada
	 * @param next
	 */
	public void setMunicipio(String municipio){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("municipio", municipio);
    }
	
	/**
	 * Setea compañia seleccionada para copias
	 * @param next
	 */
	public void setCiaCopia(String ciacopia){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ciacopia", ciacopia);
    }
	
	/**
	 * Setea nomina seleccionada para copias
	 * @param next
	 */
	public void setNomCopia(String nomcopia){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("nomcopia", nomcopia);
    }
	
	/**
	 * Setea Unidad de negocio seleccionada
	 * @param next
	 */
	public void setUnidadNegocio(String un){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("un", un);
    }
	
	
	/**
	 * Setea promedio seleccionada para copias
	 * @param next
	 */
	public void setPromCopia(String promcopia){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("promcopia", promcopia);
    }
	
	/**
	 * Setea promedio seleccionada para copias
	 * @param next
	 */
	public void setUsuarioInput(String usuario){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioInput", usuario);
    }
	
	/**
	 * Setea promedio seleccionada para copias
	 * @param next
	 */
	public void setciaInput(String pcia){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ciaInput", pcia);
    }
	
	/**
	 * Setea tipo de unidad
	 * @param next
	 */
	public void setTipuni(String tipuni){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipuni", tipuni);
    }
	
	
	/**
	 * Setea si unidad estáactiva o no
	 * @param next
	 */
	public void setIsAct(String isact){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isact", isact);
    }


	/**
	 * Setea si unidad estáactiva o no
	 * @param next
	 */
	public void setUnip(String unip){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("unip", unip);
    }
	
	
	/**
	 * Setea el tipo de concepto para tabla pnt028
	 * @param next
	 */
	public void setTipcto(String tipcto){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipcto", tipcto);
    }
	
	/**
	 * Setea el promedio pnt030
	 * @param next
	 */
	public void setProm(String prom){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("prom", prom);
    }
	
	/**
	 * Setea el rol
	 * @param next
	 */
	public void setRol(String rol){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("rol", rol);
    }

}
