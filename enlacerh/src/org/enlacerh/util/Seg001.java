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
import java.util.Date;
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
import org.enlacerh.getset.GruposSeg;
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
public class Seg001 extends Utils implements Serializable {
	

    private LazyDataModel<GruposSeg> lazyModel;  
	
	
	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<GruposSeg> getLazyModel() {
		return lazyModel;
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();


	// Constructor
    public Seg001()  {
    	if (!rq.isRequestedSessionIdValid()) {
			//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
		}  else {
    	lazyModel  = new LazyDataModel<GruposSeg>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<GruposSeg> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
		}
    }
    
    private String grupo = "";
    private String cantrab = "100";
    private Date fecven = null;
    private String estatus = "1";
    private String empresa = "";
    private Object filterValue = "";
    private String jndi = "";
    private List<GruposSeg> list = new ArrayList<GruposSeg>();

/**
	 * @return the grupo
	 */
	public String getGrupo() {
		return grupo;
	}
  
	

	/**
 * @return the jndi
 */
public String getJndi() {
	return jndi;
}





/**
 * @param jndi the jndi to set
 */
public void setJndi(String jndi) {
	this.jndi = jndi;
}





	/**
	 * @param grupo the grupo to set
	 */
	public void setGrupo(String grupo) {
		this.grupo = grupo;
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
	 * @return the fecven
	 */
	public Date getFecven() {
		return fecven;
	}




	/**
	 * @param fecven the fecven to set
	 */
	public void setFecven(Date fecven) {
		this.fecven = fecven;
	}




	/**
	 * @return the estatus
	 */
	public String getEstatus() {
		return estatus;
	}



	/**
	 * @param estatus the estatus to set
	 */
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
	
	
/**
	 * @return the empresa
	 */
	public String getEmpresa() {
		return empresa;
	}




	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null; 
	PntGenerica consulta = new PntGenerica();
	Accmnu acc = new Accmnu(); //Objeto tipo acc
	boolean vGacc; //Validador de opciones del menú
	private int rows; //Registros de tabla
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;

	
	/**
     * Inserta JNDI.
     * <p>
     * Parametros del Metodo: String codpai, String despai. Pool de conecciones y login
	 * @throws NamingException 
     **/
    public void insert() throws NamingException {
        try {
        	Context initContext = new InitialContext();     
        	DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();

            String query = "INSERT INTO seg001 VALUES (?,?,'" + getFechaFormat(fecven) + "',?,?, now(), ?)";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(grupo));
            pstmt.setInt(2, Integer.parseInt(cantrab));
            pstmt.setString(3, estatus);
            pstmt.setString(4, empresa);
            pstmt.setString(5, jndi);
            //System.out.println(query);
            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
              try {
                pstmt.executeUpdate();
                msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), "");
                limpiarValores();
            } catch (SQLException e)  {
                 //e.printStackTrace();
                 msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
            }         
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
     **/  
    public void delete()  {  
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
	        	query = "DELETE FROM SEG001 WHERE grupo in (" + param + ")";
	             break;
	        case "PostgreSQL":
	        	query = "DELETE FROM SEG001 WHERE CAST(grupo AS text) in (" + param + ")";
	             break;
	  		}

            pstmt = con.prepareStatement(query);
            //System.out.println(query);

            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
            vGacc = acc.valAccmnu("bas1", "delete", login, JNDI);//LLama a la funcion que valida las opciones del rol
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
    
    


    /**
     * Actualiza Paises
     **/
    public void update()  {
        try {
        	Context initContext = new InitialContext();     
        	DataSource ds = (DataSource) initContext.lookup(JNDI);
        	con = ds.getConnection();
        	

            String query = "UPDATE SEG001";
            	   query += " SET CANTTRAB =" + Integer.parseInt(cantrab);
            	   query += " ,FECVEN = '" + getFechaFormat(fecven) + "'";
            	   query += " ,ESTATUS = ?";
            	   query += " ,EMPRESA = ?";
            	   query += " ,JNDI = ?";
                   query += " WHERE grupo = " + Integer.parseInt(grupo);
            //System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, estatus.toUpperCase());
            pstmt.setString(2, empresa.toUpperCase());
            pstmt.setString(3, jndi.toLowerCase());
            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
            vGacc = acc.valAccmnu("bas1", "update", login, JNDI);//LLama a la funcion que valida las opciones del rol
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
                fecven = null;
                cantrab = "25";
                estatus = "0";
                empresa="";
                jndi = "";
            } catch (SQLException e) {
                e.printStackTrace();
                msj = new FacesMessage(FacesMessage.SEVERITY_FATAL,  e.getMessage(), "");
            }

            pstmt.close();
            con.close();
               }

        } catch (Exception e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().addMessage(null, msj); 
    }
    
    
   
    

	/**
     * Leer Datos de paises
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException, NamingException, IOException {
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
        	query = "  select * from ";
        	query += " ( select query.*, rownum as rn from";
       		query += " ( SELECT grupo, canttrab, to_char(fecven, 'dd/mm/yyyy'), case when estatus ='0' then '" + getMessage("pnt009Opc0") + "' else '" + getMessage("pnt009Opc1") + "' end, estatus, empresa, JNDI";
            query += " FROM seg001";
            query += " where grupo||empresa like '%" + ((String) filterValue).toUpperCase() + "%'";
            //query += " order by " + sortField.replace("v", "") + ") query";
		    query += ") query";
            query += ") where rn <= " + pageSize ;
		    query += " and rn > (" + first + ")";

             break;
        case "PostgreSQL":
        	//Consulta paginada
       		query = "SELECT grupo, canttrab, to_char(fecven, 'dd/mm/yyyy'), case when estatus ='0' then '" + getMessage("pnt009Opc0") + "' else '" + getMessage("pnt009Opc1") + "' end, estatus, empresa, JNDI";
            query += " FROM seg001";
            query += " where CAST(grupo AS text)||empresa like '%" + ((String) filterValue).toUpperCase() + "%'";
            query += " order by 1";
            query += " LIMIT " + pageSize;
            query += " OFFSET " + first;
             break;          		   
  		}

  		
        
        pstmt = con.prepareStatement(query);
        System.out.println(query);
  		
         r =  pstmt.executeQuery();
        
        
        while (r.next()){
        	GruposSeg select = new GruposSeg();
        	select.setVgrupo(r.getString(1));
        	select.setVcantrab(r.getString(2));
        	select.setVfecven(r.getString(3));
        	select.setVestatus(r.getString(4));
        	select.setEstatuscode(r.getString(5));
        	select.setVempresa(r.getString(6));
        	select.setVjndi(r.getString(7));
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
	 * @return the rows
	 */
	public int getRows() {
		return rows;
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
 		
 		switch ( productName ) {
        case "Oracle":
        	query = "SELECT count_seg001('" + ((String) filterValue).toUpperCase() +  "') from dual";
             break;
        case "PostgreSQL":
        	query = "SELECT count_seg001('" + ((String) filterValue).toUpperCase() +  "')";
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
     * Limpia los valores
     **/
	private void limpiarValores(){
		grupo = "";
	    cantrab = "";
	    fecven = null;
	    estatus = "";
	    empresa="";
	}




}

