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
import java.util.HashMap;
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
import org.enlacerh.getset.Segcia;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Andres Dominguez 08/02/2009
 */
/**
 * Clase para leer, insertar, modificar y eliminar registros de la tabla
 * SEG007(Paises)
 *  */
@ManagedBean
@ViewScoped
public class Seg007 extends Utils implements Serializable {
    
    private LazyDataModel<Segcia> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Segcia> getLazyModel() {
		return lazyModel;
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();


	// Constructor
    public Seg007() throws NamingException, SQLException, IOException  {
    	if (!rq.isRequestedSessionIdValid()) {
			//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
		}  else {
    	
    	lazyModel  = new LazyDataModel<Segcia>(){
    		/**
    		 * 
    		 */
    		private static final long serialVersionUID = 7217573531435419432L;
    		
            @Override
    		public List<Segcia> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
    	
    	selectSeg007Cias();//Lista compañias a añadir por usuarios
		}
    }
    
    private String pcoduser = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioInput");
    private Object filterValue = "";
    private List<Segcia> list = new ArrayList<Segcia>();
    private Map<String,String> listCias = new HashMap<String, String>();   //Lista de compañías disponibles para acceso a seguridad 
    private List<String> selectedCias;   //Listado de compañias seleccionadas
    private Map<String, String> sorted;
    private String exito = "exito";     

    /**
	 * @return the pcoduser
	 */
	public String getPcoduser() {
		return pcoduser;
	}

	/**
	 * @param pcoduser the pcoduser to set
	 */
	public void setPcoduser(String pcoduser) {
		this.pcoduser = pcoduser;
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
	 * @return the listCias
	 */
	public Map<String, String> getListCias() {
		return listCias;
	}
	/**
	 * @param listCias the listCias to set
	 */
	public void setListCias(Map<String, String> listCias) {
		this.listCias = listCias;
	}
	
     


	/**
	 * @return the selectedCias
	 */
	public List<String> getSelectedCias() {
		return selectedCias;
	}

	/**
	 * @param selectedCias the selectedCias to set
	 */
	public void setSelectedCias(List<String> selectedCias) {
		this.selectedCias = selectedCias;
	}
	
  	
  	
  	
  	/**
	 * @return the sorted
	 */
	public Map<String, String> getSorted() {
		return sorted;
	}

	/**
	 * @param sorted the sorted to set
	 */
	public void setSorted(Map<String, String> sorted) {
		this.sorted = sorted;
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null; 
	PntGenerica consulta = new PntGenerica();
    PntGenerica consulta1 = new PntGenerica();
	Accmnu acc = new Accmnu(); //Objeto tipo acc
	boolean vGacc; //Validador de opciones del menú
	private String orden = "1"; //Orden de la consulta
	private int rows; //Registros de tabla

	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	private String grupo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("grupo"); //Usuario logeado
	//private String tipusr = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tipusr"); //Usuario logeado
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//Coneccion a base de datos
	//Pool de conecciones JNDI
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;


	/**
     * Inserta Compañía por seguridad.
	 * @throws IOException 
     **/
    public void insert(String pcia) throws  NamingException, IOException {
    	if(licencia(grupo)) {
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    	} else {	
    	String[] veccouser = pcoduser.split("\\ - ", -1);
    	//Chequea que las variables no sean nulas 
        try {
        	Context initContext = new InitialContext();     
        	DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            
            String query = "INSERT INTO SEG007 VALUES (?,?" + "," + Integer.parseInt(grupo) + ")";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, veccouser[0].toUpperCase());
            pstmt.setString(2, pcia.toUpperCase());
            //System.out.println(query);
            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
            vGacc = acc.valAccmnu("seg03", "insert", login, JNDI);//LLama a la funcion que valida las opciones del rol
            if (vGacc) {
            	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnAccInsert"), "");
            	FacesContext.getCurrentInstance().addMessage(null, msj); 
            } else {
            try {
                pstmt.executeUpdate();              
            } catch (SQLException e)  {
            	exito = "error";
                 e.printStackTrace();
                 msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
                 FacesContext.getCurrentInstance().addMessage(null, msj); 
            }
                }//Fin validacion 
            pstmt.close();
            con.close();

        } catch (Exception e) {
        	exito = "error";
            //e.printStackTrace();
        }
    	}
    }
    
    /**
     * Genera las operaciones de guardar o modificar
     * @throws NamingException 
     * @throws SQLException 
     * @throws IOException 
     * @throws ClassNotFoundException 
     **/ 
    public void guardar() throws NamingException, SQLException, ClassNotFoundException, IOException{  
    	if(licencia(grupo)) {
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("licven"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    		exito = "error";
    	} else if (selectedCias.size()<=0){
    		msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("seg007IngCia"), "");
    		FacesContext.getCurrentInstance().addMessage(null, msj);
    		exito = "error";
    	} else {  	
    	   for (int i = 0; i< selectedCias.size(); i++){
    		insert(selectedCias.get(i));
    	   }
    	   if(exito=="exito"){
      		 msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
      		 FacesContext.getCurrentInstance().addMessage(null, msj);
      	}
    	}
    }

    /**
     * Borra Ciaseg
     * <p>
     * Parametros del metodo: String p_coduser. Pool de conecciones
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
	        	query = "DELETE FROM seg007 WHERE P_CODUSER||P_CODCIA||grupo in (" + param + ")";
	             break;
	        case "PostgreSQL":
	        	query = "DELETE FROM seg007 WHERE P_CODUSER||P_CODCIA||CAST(grupo AS text) in (" + param + ")";
	             break;
	  		}
            
       
            pstmt = con.prepareStatement(query);
            //System.out.println(query);
            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
            vGacc = acc.valAccmnu("seg03", "delete", login, JNDI);//LLama a la funcion que valida las opciones del rol
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
            } catch (SQLException e) {
                //e.printStackTrace();
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
     * Leer Datos de Ciaseg
     * @throws NamingException 
	 * @throws SQLException 
     * @throws IOException 
     **/ 	
  	public void select(int first, int pageSize, String sortField, Object filterValue) throws NamingException, SQLException, IOException  {

  		Context initContext = new InitialContext();     
    	DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();
        
      //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección

 		String query = "";
 		
 		 if(pcoduser==null){
         	pcoduser = " - ";
 	   	}
 	   	if(pcoduser==""){
 	   		pcoduser=" - ";
 	   	}
   		
   		String[] vecuser = pcoduser.split("\\ - ", -1);
 		
 		switch ( productName ) {
        case "Oracle":
    		//Consulta paginada
        	query = "  select * from ";
        	query += " ( select query.*, rownum as rn from";
      		query += " ( SELECT trim(a.p_coduser), trim(c.nbr), trim(a.p_codcia), trim(b.nomcia1), trim(b.nomcia2), a.grupo";
            query += " FROM SEG007 a , Pnt001 b, autos01 c";
            query += " where a.p_codcia=b.codcia";
            query += " and a.p_coduser=c.coduser";
            query += " and a.grupo=c.grupo";
            query += " and a.p_coduser like trim('" + vecuser[0] + "%')"; 
            query += " and a.p_coduser||a.p_codcia||b.nomcia2 like '%" + ((String) filterValue).toUpperCase() + "%'";
            query += " and a.grupo = '" +  grupo + "'";
            query += " order by " + sortField.replace("vp", "p_") + ", a.p_codcia" + ") query";
            query += " ) where rownum <= " + pageSize ;
		    query += " and rn > (" + first + ")";

             break;
        case "PostgreSQL":
        	//Consulta paginada
    		//Consulta paginada
      		query = "SELECT trim(a.p_coduser), trim(c.nbr), trim(a.p_codcia), trim(b.nomcia1), trim(b.nomcia2), a.grupo";
            query += " FROM SEG007 a , Pnt001 b, autos01 c";
            query += " where a.p_codcia=b.codcia";
            query += " and a.p_coduser=c.coduser";
            query += " and a.grupo=c.grupo";
            query += " and a.p_coduser like trim('" + vecuser[0] + "%')"; 
            query += " and a.p_coduser||a.p_codcia||b.nomcia2 like '%" + ((String) filterValue).toUpperCase() + "%'";
            query += " and CAST(a.grupo AS text) = '" +  grupo + "'";
            query += " order by " + sortField.replace("vp", "p_") + ", a.p_codcia";
            query += " LIMIT " + pageSize;
            query += " OFFSET " + first;
             break;          		   
  		}
  		   		
     
  		
  		
          
        pstmt = con.prepareStatement(query);
        System.out.println(query);
  		
        ResultSet r =  pstmt.executeQuery();
        
  		
        
        while (r.next()){
        	Segcia select = new Segcia();
        	select.setVpcodciadescia(r.getString(3) + " - " + r.getString(5));
        	select.setVpcoduser(r.getString(1));
        	select.setVdesuser(r.getString(2));
        	select.setVpcodcia(r.getString(3));
        	select.setVpdescia(r.getString(5));
        	select.setVpcoduserdesuser(r.getString(1) + " - " + r.getString(2));
        	select.setVgrupo(r.getString(6));
        	//Agrega la lista
        	list.add(select);

        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        
  	}
  	
  	


	/**
     * Leer Datos de compañías para asignar a menucheck
     * @throws NamingException 
	 * @throws SQLException 
     * @throws IOException 
     **/ 	
  	private void selectSeg007Cias() throws NamingException, SQLException, IOException  {
  		
  		Context initContext = new InitialContext();     
    	DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();
        
      //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
 		
 		String query = null;
 		
 		switch ( productName ) {
        case "Oracle":
        	//Consulta paginada
      		query = "SELECT codcia, codcia||' - '||nomcia2";
            query += " FROM pnt001";
            query += " where grupo = '" +  grupo + "'";
            query += " order by codcia";
             break;
        case "PostgreSQL":
        	//Consulta paginada
      		query = "SELECT codcia, codcia||' - '||nomcia2";
            query += " FROM pnt001";
            query += " where CAST(grupo AS text) = '" +  grupo + "'";
            query += " order by codcia";
             break;
  		}
  		   		
 		
  		
  		
        
        pstmt = con.prepareStatement(query);
        //System.out.println(query);
  		
        ResultSet r =  pstmt.executeQuery();
        
  		
        
        while (r.next()){
        	String cia = new String(r.getString(1));
        	String nombre = new String(r.getString(2));
        	
            listCias.put(nombre, cia);
            sorted = sortByValues(listCias);
            
        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        
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
 		String[] veccouser = pcoduser.split("\\ - ", -1);
 		
 		switch ( productName ) {
        case "Oracle":
        	query = "SELECT count_seg007(" + Integer.parseInt(grupo) + ",'" + ((String) filterValue).toUpperCase() + "','" + veccouser[0].toUpperCase() +  "') from dual";
             break;
        case "PostgreSQL":
        	query = "SELECT count_seg007(" + Integer.parseInt(grupo) + ",'" + ((String) filterValue).toUpperCase() + "','" + veccouser[0].toUpperCase() +  "')";
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
  		pcoduser = "";
    }
  	
    	

	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}
 


}

