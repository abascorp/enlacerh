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
import org.enlacerh.getset.Cia;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Andres Dominguez 08/02/2009
 */
/**
 * Clase para leer, insertar, modificar y eliminar registros de la tabla
 * Pnt001(Compañias)
 *  */
@ManagedBean
@ViewScoped
public class Pnt001 extends Utils implements Serializable {
	
	private LazyDataModel<Cia> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Cia> getLazyModel() {
			return lazyModel;
		}
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

// Constructor
    public Pnt001() {
    	if (!rq.isRequestedSessionIdValid()) {
			//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
		}  else {
    	lazyModel  = new LazyDataModel<Cia>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 7217573531435419432L;
			
            @Override
			public List<Cia> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
    
	private String codcia = "";
	private String nomcia1 = "";
    private String nomcia2 = "";
    private String id1 = "";
    private String id2 = "";
    private String dir = "";
    private String pcodpai = "";
    private String pcodciu = "";
    private long tlf1 = 0;
    private long tlf2 = 0;
    private String pcodciudesciu = "";
    private int validarOperacion = 0;//Param guardar para validar si guarda o actualiza
    private String localgrupo = "";
    private Object filterValue = "";
    List<Cia> list = new ArrayList<Cia>();
    
    
  //Creando carpetas para separar archivos externos por ccompañías
    //ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
    //Rutas externos
    private static final String RUTA_AUTOEXT = File.separator + "externos" + File.separator + "autoext" ;
    private static final String RUTA_QUERYTEXT = File.separator + "externos" + File.separator + "querytext" ;
    private static final String RUTA_TABLASEXT = File.separator + "externos" + File.separator + "sueldoext" ;
    private static final String RUTA_VAREXT = File.separator + "externos" + File.separator + "tablasext" ;
    private static final String RUTA_SUELDOEXT = File.separator + "externos" + File.separator + "varext" ;
    File ruta = new File(extContext.getRealPath(RUTA_AUTOEXT));
    File ruta1 = new File(extContext.getRealPath(RUTA_QUERYTEXT));
    File ruta2 = new File(extContext.getRealPath(RUTA_TABLASEXT));
    File ruta3 = new File(extContext.getRealPath(RUTA_VAREXT));
    File ruta4 = new File(extContext.getRealPath(RUTA_SUELDOEXT));

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
	 * @return the nomcia1
	 */
	public String getNomcia1() {
		return nomcia1;
	}

	/**
	 * @param nomcia1 the nomcia1 to set
	 */
	public void setNomcia1(String nomcia1) {
		this.nomcia1 = nomcia1;
	}

	/**
	 * @return the nomcia2
	 */
	public String getNomcia2() {
		return nomcia2;
	}

	/**
	 * @param nomcia2 the nomcia2 to set
	 */
	public void setNomcia2(String nomcia2) {
		this.nomcia2 = nomcia2;
	}

	/**
	 * @return the id
	 */
	public String getId1() {
		return id1;
	}

	/**
	 * @param id the id to set
	 */
	public void setId1(String id1) {
		this.id1 = id1;
	}

	/**
	 * @return the id2
	 */
	public String getId2() {
		return id2;
	}

	/**
	 * @param id2 the id2 to set
	 */
	public void setId2(String id2) {
		this.id2 = id2;
	}

	/**
	 * @return the pcodpai
	 */
	public String getPcodpai() {
		return pcodpai;
	}

	/**
	 * @param pcodpai the pcodpai to set
	 */
	public void setPcodpai(String pcodpai) {
		this.pcodpai = pcodpai;
	}

	/**
	 * @return the pcodciu
	 */
	public String getPcodciu() {
		return pcodciu;
	}

	/**
	 * @param pcodciu the pcodciu to set
	 */
	public void setPcodciu(String pcodciu) {
		this.pcodciu = pcodciu;
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
	 * @return the pcodciudesciu
	 */
	public String getPcodciudesciu() {
		return pcodciudesciu;
	}

	/**
	 * @param pcodciudesciu the pcodciudesciu to set
	 */
	public void setPcodciudesciu(String pcodciudesciu) {
		this.pcodciudesciu = pcodciudesciu;
	}
	
	
	
	/**
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}

	/**
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
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

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Variables seran utilizadas para capturar mensajes de errores de Oracle y parametros de metodos
	FacesMessage msj = null; 
	PntGenerica consulta = new PntGenerica();
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
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;


	/**
     * Inserta Compañias.
     **/
    private void insert() throws  NamingException {
    	//Valida que los campos no sean nulos
    	String[] veccodpai = pcodpai.split("\\ - ", -1);
        String[] veccodciu = pcodciu.split("\\ - ", -1);
        
        try {
        	Context initContext = new InitialContext();     
        	DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();

            String query = "INSERT INTO Pnt001 VALUES (?,?,?,?,?,?," + Integer.parseInt(veccodpai[0]) + ","+ Integer.parseInt(veccodciu[0]) + "," + BigInteger.valueOf(tlf1) + "," + BigInteger.valueOf(tlf2)
                     + ",?,'" + getFecha() + "',?,'" + getFecha() + "'," + Integer.parseInt(grupo) + ")";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, codcia.toUpperCase());
            pstmt.setString(2, nomcia1.toUpperCase());
            pstmt.setString(3, nomcia2.toUpperCase());
            pstmt.setString(4, id1.toUpperCase());
            pstmt.setString(5, id2.toUpperCase());
            pstmt.setString(6, dir.toUpperCase());
            pstmt.setString(7, login);
            pstmt.setString(8, login);
            //System.out.println(query);
            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
            vGacc = acc.valAccmnu("bas03", "insert", login, JNDI);//LLama a la funcion que valida las opciones del rol
            if (vGacc) {
            	msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("msnAccInsert"), "");
            } else {
                // Antes de ejecutar valida si existe el registro en la base de Datos.
                consulta.selectPntGenerica("select codcia from Pnt001 where codcia ='" + codcia.toUpperCase() + "'", JNDI);
                   if (consulta.getRows()>0){
                	   msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("pnt001valCia"), "");  
                   } else {        	
            try {
                pstmt.executeUpdate();
            
                //
                new Mkdir().crtCarpeta(ruta.toString(), codcia.toUpperCase());//Crea autoservicio
                new Mkdir().crtCarpeta(ruta1.toString(), codcia.toUpperCase());//Crea querys externos
                new Mkdir().crtCarpeta(ruta2.toString(), codcia.toUpperCase());//Crea tablas externas
                new Mkdir().crtCarpeta(ruta3.toString(), codcia.toUpperCase());//Crea variables externas
                new Mkdir().crtCarpeta(ruta4.toString(), codcia.toUpperCase());//Crea aumentos de sueldo externos
                //
               msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("msnInsert"), ""); 
               limpiarValores();
            } catch (SQLException e)  {
                 e.printStackTrace();
                 msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
                
                //Elimina las carpeta si hay algun error
                new Mkdir().borraCarpeta(ruta.toString(), codcia.toUpperCase());//Borra autoservicio
                new Mkdir().borraCarpeta(ruta1.toString(), codcia.toUpperCase());//Borra querys externos
                new Mkdir().borraCarpeta(ruta2.toString(), codcia.toUpperCase());//Borra tablas externas
                new Mkdir().borraCarpeta(ruta3.toString(), codcia.toUpperCase());//Borra variables externas
                new Mkdir().borraCarpeta(ruta4.toString(), codcia.toUpperCase());//Borra aumentos de sueldo externos
            }
                }//Fin validacion 
            pstmt.close();
            con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
        FacesContext.getCurrentInstance().addMessage(null, msj);
    }

    /**
     * Borra Compañías
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
	        	query = "DELETE FROM PNT001 WHERE CODCIA||grupo in (" + param + ")";
	             break;
	        case "PostgreSQL":
	        	query = "DELETE FROM PNT001 WHERE CODCIA||CAST(grupo AS text) in (" + param + ")";
	             break;
	  		}

            pstmt = con.prepareStatement(query);
            //System.out.println(query);
            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
            vGacc = acc.valAccmnu("bas03", "delete", login, JNDI);//LLama a la funcion que valida las opciones del rol
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
                deleteFolders();
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
     * Borra los directorios creados el insertar una nueva compañia
     * <p>
     **/
    private void deleteFolders() throws  NamingException {  
    	HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	String[] param = request.getParameterValues("toDelete");
    	if(param!=null){
    	for(int i=0;i<param.length;i++){
    		new Mkdir().borraCarpeta(ruta.toString(), param[i].toUpperCase());//Borra autoservicio
            new Mkdir().borraCarpeta(ruta1.toString(), param[i].toUpperCase());//Borra querys externos
            new Mkdir().borraCarpeta(ruta2.toString(), param[i].toUpperCase());//Borra tablas externas
            new Mkdir().borraCarpeta(ruta3.toString(), param[i].toUpperCase());//Borra variables externas
            new Mkdir().borraCarpeta(ruta4.toString(), param[i].toUpperCase());//Borra aumentos de sueldo externos
    		  }
    	}
    }
   

    /**
     * Actualiza Compañias
     **/
    private void update()  {
    	//Valida que los campos no sean nulos
    	String[] veccodpai = pcodpai.split("\\ - ", -1);
        String[] veccodciu = pcodciu.split("\\ - ", -1);
        
        try {
        	Context initContext = new InitialContext();     
        	DataSource ds = (DataSource) initContext.lookup(JNDI);
            con = ds.getConnection();
            String query = "UPDATE Pnt001 SET nomcia1 = ?";
                   query += " ,nomcia2 = ?";
                   query += " ,id1 = ?";
                   query += " ,id2 = ?";
                   query += " ,dir = ?";
                   query += " ,p_codpai = " + Integer.parseInt(veccodpai[0]);
                   query += " ,p_codciu = " + Integer.parseInt(veccodciu[0]);
                   query += " ,tlf1 = " + BigInteger.valueOf(tlf1);
                   query += " ,tlf2 = " + BigInteger.valueOf(tlf2);
                   query += " ,FECACT = '" + getFecha() + "' , USRACT = '" + login + "'";
                   query += " WHERE codcia = ?";
                   query += " and grupo = " + Integer.parseInt(localgrupo);
            //System.out.println(query);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, nomcia1.toUpperCase());
            pstmt.setString(2, nomcia2.toUpperCase());
            pstmt.setString(3, id1.toUpperCase());
            pstmt.setString(4, id2.toUpperCase());
            pstmt.setString(5, dir.toUpperCase());
            pstmt.setString(6, codcia.toUpperCase());
            //Antes de insertar verifica si el rol del usuario tiene permisos para insertar
            vGacc = acc.valAccmnu("bas03", "update", login, JNDI);//LLama a la funcion que valida las opciones del rol
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
                id1 = "";
                id2 = "";
                dir = "";
                pcodpai = "";
                pcodciu = "";
                tlf1 = 0;
                tlf2 = 0;
                pcodciudesciu = "";
                validarOperacion = 0;
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
     * @throws IOException 
     * @throws SQLException 
     * @throws ClassNotFoundException 
     **/ 
    public void guardar() throws NamingException, ClassNotFoundException, SQLException, IOException{
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
     * Leer Datos de paises
	 * @throws SQLException 
     * @throws NamingException 
     * @throws IOException 
     **/ 	
  	public void select(int first, int pageSize, String sortField, Object filterValue) throws SQLException  {	
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
      		query += " ( select trim(a.codcia), trim(a.nomcia1), trim(a.nomcia2), trim(a.id1), trim(a.id2), trim(a.dir),";
            query += " a.p_codpai, a.p_codciu, a.tlf1, a.tlf2, trim(b.despai), trim(c.desciu), a.grupo";
            query += " from Pnt001 a, Pnt003 b, Pnt004 c";
            query += " where a.p_codpai=b.codpai";
            query += " and a.grupo=b.grupo";
            query += " and a.p_codciu=c.codciu";
            query += " and a.grupo=c.grupo";
            query += " and a.codcia like  '" + codcia.toUpperCase() + "%'";   
            query += " and a.codcia||a.nomcia2||a.id1 like '%" + ((String) filterValue).toUpperCase() + "%'";
            query += " and a.grupo = '" +  grupo + "'";
            query += " order by " + sortField.replace("v", "") + ") query";
		     query += " ) where rownum <= " + pageSize ;
		     query += " and rn > (" + first + ")";

             break;
        case "PostgreSQL":
        	//Consulta paginada
        	//Consulta paginada
      		query ="  select trim(a.codcia), trim(a.nomcia1), trim(a.nomcia2), trim(a.id1), trim(a.id2), trim(a.dir),";
            query += " a.p_codpai, a.p_codciu, a.tlf1, a.tlf2, trim(b.despai), trim(c.desciu), a.grupo";
            query += " from Pnt001 a, Pnt003 b, Pnt004 c";
            query += " where a.p_codpai=b.codpai";
            query += " and a.grupo=b.grupo";
            query += " and a.p_codciu=c.codciu";
            query += " and a.grupo=c.grupo";
            query += " and a.codcia like  '" + codcia.toUpperCase() + "%'";   
            query += " and a.codcia||a.nomcia2||a.id1 like '%" + ((String) filterValue).toUpperCase() + "%'";
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
        	Cia select = new Cia();
        	select.setVcodcia(r.getString(1));
        	select.setVnomcia1(r.getString(2));
        	select.setVnomcia2(r.getString(3));
        	select.setVid(r.getString(4));
        	select.setVid2(r.getString(5));
        	select.setVdir(r.getString(6));
        	select.setVpcodpaidespai(r.getString(7) + " - " + r.getString(11));
        	select.setVpcodciudesciu(r.getString(8) + " - " + r.getString(12));
        	select.setTlf1(r.getString(9));
        	select.setTlf2(r.getString(10));
        	select.setVpcodpai(r.getString(11));
        	select.setVgrupo(r.getString(13));
        	//Agrega la lista
        	list.add(select);
     
        }
      } catch (Exception e){
    	  
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
 		
 		String query = null;
 		
 		switch ( productName ) {
        case "Oracle":
        	query = "SELECT count_pnt001(" + Integer.parseInt(grupo) + ",'" + ((String) filterValue).toUpperCase() +  "') from dual";
             break;
        case "PostgreSQL":
        	query = "SELECT count_pnt001(" + Integer.parseInt(grupo) + ",'" + ((String) filterValue).toUpperCase() +  "') ";
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



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////OPERACIONES BASICAS//////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Limpia los valores
     **/
	private void limpiarValores(){
		codcia = "";
    	nomcia1 = "";
        nomcia2 = "";
        id1 = "";
        id2 = "";
        dir = "";
        pcodpai = "";
        pcodciu = "";
        tlf1 = 0;
        tlf2 = 0;
        pcodciudesciu = "";	
		validarOperacion = 0;
	}    



}

