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
import org.enlacerh.getset.Ciudades;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Andres Dominguez 08/02/2009
 */
/**
 * Clase para leer, insertar, modificar y eliminar registros de la tabla
 * Pnt004(Ciudades)
 *  */

@ManagedBean
@ViewScoped
public class Pnt004 extends Utils implements Serializable {
	
    private LazyDataModel<Ciudades> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Ciudades> getLazyModel() {
		return lazyModel;
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

	// Constructor
    public Pnt004() {
    	if (!rq.isRequestedSessionIdValid()) {
			//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
		}  else {
		lazyModel  = new LazyDataModel<Ciudades>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Ciudades> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
    private String codciu = "";
    private String desciu = "";
    private String estado = "";
    private String p_codpai = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pais");
    private String localgrupo = "";
    private int validarOperacion = 0;//Param guardar para validar si guarda o actualiza
    private Object filterValue = "";
    List<Ciudades> list = new ArrayList<Ciudades>();
    
	/**
	 * @return the codciu
	 */
	public String getCodciu() {
		return codciu;
	}

	/**
	 * @param codciu the codciu to set
	 */
	public void setCodciu(String codciu) {
		this.codciu = codciu;
	}

	/**
	 * @return the desciu
	 */
	public String getDesciu() {
		return desciu;
	}

	/**
	 * @param desciu the desciu to set
	 */
	public void setDesciu(String desciu) {
		this.desciu = desciu;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the p_pcodpai
	 */
	public String getP_codpai() {
		return p_codpai;
	}

	/**
	 * @param p_pcodpai the p_pcodpai to set
	 */
	public void setP_codpai(String p_codpai) {
		this.p_codpai = p_codpai;
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
//O genera un error
private String orden = "1"; //Orden de la consulta
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
     * Inserta Ciudades.
     * <p>
     * Parametros del Metodo: String codciu, String desciu, String estado, String p_codpai. Pool de conecciones y login
     * @throws SQLException 
     **/
    private void insert()  {
    	String[] vecValores = p_codpai.split("\\ - ", -1);

        try {
        	Context initContext = new InitialContext();     
        	DataSource ds = (DataSource) initContext.lookup(JNDI);
        	con = ds.getConnection();

            String query = "INSERT INTO PNT004 VALUES (" + Integer.parseInt(codciu) + ",?,?,"+ Integer.parseInt(vecValores[0]) + ",?,'" + getFecha() + "',?,'" + getFecha() + "'," + Integer.parseInt(grupo) + ")";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, desciu.toUpperCase());
            pstmt.setString(2, estado.toUpperCase());
            pstmt.setString(3, login);
            pstmt.setString(4, login);
            //System.out.println(query);
            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
            vGacc = acc.valAccmnu("bas02", "insert", login, JNDI);//LLama a la funcion que valida las opciones del rol
            if (vGacc) {
            	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnAccInsert"), "");
            } else {
            try {
                pstmt.executeUpdate();
               msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), ""); 
               limpiarValores();
            } catch (SQLException e)  {
                 e.printStackTrace();
                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");   
            }
                }//Fin validacion 
            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    	
        FacesContext.getCurrentInstance().addMessage(null, msj);
    }

    /**
     * Borra Ciudades
     * <p>
     * Parametros del metodo: String param. String cuenta
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
	        	query = "DELETE FROM  PNT004 WHERE codciu||grupo in (" + param + ")";
	             break;
	        case "PostgreSQL":
	        	query = "DELETE FROM  PNT004 WHERE CAST(codciu AS text)||CAST(grupo AS text) in (" + param + ")";
	             break;
	  		}

            pstmt = con.prepareStatement(query);

            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
            vGacc = acc.valAccmnu("bas02", "delete", login, JNDI);//LLama a la funcion que valida las opciones del rol
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
     * Actualiza ciudades
     **/
    private void update()  {
    		
    	String[] vecValores = p_codpai.split("\\ - ", -1);

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
	        	query = "UPDATE PNT004 SET desciu = ?, estado = ?,";
	            query += "p_codpai = " + Integer.parseInt(vecValores[0]);
	            query += ",FECACT = '" + getFecha() + "' , USRACT = '" + login + "'";
	            query += " WHERE codciu = '" + codciu + "'";
	            query += " and grupo = " + Integer.parseInt(localgrupo);
	             break;
	        case "PostgreSQL":
	        	query = "UPDATE PNT004 SET desciu = ?, estado = ?,";
	            query += "p_codpai = " + Integer.parseInt(vecValores[0]);
	            query += ",FECACT = '" + getFecha() + "' , USRACT = '" + login + "'";
	            query += " WHERE CAST(codciu AS text) = '" + codciu + "'";
	            query += " and grupo = " + Integer.parseInt(localgrupo);
	             break;
	  		}


        	
            //System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, desciu.toUpperCase());
            pstmt.setString(2, estado.toUpperCase());
            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
            vGacc = acc.valAccmnu("bas02", "update", login, JNDI);//LLama a la funcion que valida las opciones del rol
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
        		desciu = "";
        		p_codpai = "";
        		estado = "";
        		validarOperacion = 0;
        		localgrupo = "";
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
     * Leer Datos de ciudades
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, NamingException {
  		try {
        Context initContext = new InitialContext();     
    	DataSource ds = (DataSource) initContext.lookup(JNDI);
        con = ds.getConnection();
      //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección

 		String query = "";
 		
 		 if(p_codpai==null){
         	p_codpai = " - ";
         }
         if(p_codpai==""){
         	p_codpai = " - ";
         }
         
         String[] vecValores = p_codpai.split("\\ - ", -1);
 		
 		switch ( productName ) {
        case "Oracle":
        	//Consulta paginada
        	query = "  select * from ";
        	query += " ( select query.*, rownum as rn from";
      		query += " ( SELECT a.codciu, trim(a.desciu), trim(a.estado), b.codpai, trim(b.despai), a.grupo";
            query += " FROM Pnt004 a, Pnt003 b";
            query += " where a.p_codpai=b.codpai";
            query += " and a.grupo=b.grupo";
            query += " and a.codciu like '" + codciu.toUpperCase() + "%'";
            query += " and a.p_codpai like '%" + vecValores[0] + "%'";
            query += " and a.codciu||a.desciu like '%" + ((String) filterValue).toUpperCase() + "%'";
            query += " and a.grupo = '" +  grupo + "'";
            query += " order by " + sortField.replace("v", "") + ") query";
		    query += " ) where rownum <= " + pageSize ;
		    query += " and rn > (" + first + ")";

             break;
        case "PostgreSQL":
        	//Consulta paginada
      		query = "SELECT a.codciu, trim(a.desciu), trim(a.estado), b.codpai, trim(b.despai), a.grupo";
            query += " FROM Pnt004 a, Pnt003 b";
            query += " where a.p_codpai=b.codpai";
            query += " and a.grupo=b.grupo";
            query += " and CAST(a.codciu AS text) like '" + codciu.toUpperCase() + "%'";
            query += " and cast(a.p_codpai AS text) like '%" + vecValores[0] + "%'";
            query += " and cast(a.codciu AS text)||a.desciu like '%" + ((String) filterValue).toUpperCase() + "%'";
            query += " and CAST(a.grupo AS text) = '" +  grupo + "'";
            query += " order by " + sortField.replace("v", "") ;
            query += " LIMIT " + pageSize;
            query += " OFFSET " + first;

             break;          		   
  		}
		
       
  		
              
        pstmt = con.prepareStatement(query);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();
        
        
        while (r.next()){
        	Ciudades ciudad = new Ciudades();
        	ciudad.setVcodciu(r.getString(1));
            ciudad.setVdesciu(r.getString(2));
            ciudad.setVestado(r.getString(3));
            ciudad.setVp_pcodpai(r.getString(4));
            ciudad.setVcodpaidespai(r.getString(4) + " - " + r.getString(5));
            ciudad.setVgrupo(r.getString(6));
        	//Agrega la lista
        	list.add(ciudad);        
  	}
      //Cierra las conecciones
        pstmt.close();
        con.close(); 
        r.close();
        
  		} catch (Exception e) {
            e.printStackTrace();
        }
  	
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
   		
   	//Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
 		DatabaseMetaData databaseMetaData = con.getMetaData();
 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
 		
 		String query = null;
 		
 		if(p_codpai==null){
        	p_codpai = " - ";
        }
        if(p_codpai==""){
        	p_codpai = " - ";
        }
   		
   		String[] vecValores = p_codpai.split("\\ - ", -1);
 		
 		switch ( productName ) {
        case "Oracle":
        	 query = "SELECT count_pnt004(" + Integer.parseInt(grupo) + ",'" + ((String) filterValue).toUpperCase() + "','" + vecValores[0] + "') from dual";
             break;
        case "PostgreSQL":
        	 query = "SELECT count_pnt004(" + Integer.parseInt(grupo) + ",'" + ((String) filterValue).toUpperCase() + "','" + vecValores[0] + "')";
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
	private void limpiarValores(){
		codciu = "";
		desciu = "";
		p_codpai = "";
		estado = "";
		validarOperacion = 0;
		localgrupo = "";
	}

	
	public void reset(){
    	p_codpai = "";
    }

//public static void main (String [] args) throws NamingException, SQLException, ClassNotFoundException{
//Pnt004 a = new Pnt004();
//a.select("15");
//a.insertPnt004("2|ccs|ccs|1", "", "ADMIN");
// a.deletePnt003("2", "", "admin");
//a.updatePnt004("2|caracas|ccs|1", "", "admin");
//System.out.println(a.getMsj());
//}

}

