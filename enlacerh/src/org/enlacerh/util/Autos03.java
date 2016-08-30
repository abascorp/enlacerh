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
	
	import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import org.enlacerh.getset.Autovac;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
	
	 
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
	public class Autos03 extends Utils implements Serializable {
		
       private LazyDataModel<Autovac> lazyModel;  
		
		
		/**
		 * @return the lazyModel
		 */
		public LazyDataModel<Autovac> getLazyModel() {
			return lazyModel;
		}
		
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	

		@PostConstruct	
	    public void init(){
			if (!rq.isRequestedSessionIdValid()) {
				//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
				RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
			}  else {
			//Carga imagen
			new Autos02().imagen();
			//Ejecuta el encabezado
	  		selectEncab(cedula, pcodcia, "I");
	  		//
	  		lazyModel  = new LazyDataModel<Autovac>(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 7217573531435419432L;

				
	            @Override
				public List<Autovac> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) { 
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
	private String anio = "";
	private String fechaDesde = "";
	private String fechaHasta = "";
	public static boolean existe; //Valida si la imagen existe
	private static final String RUTA_IMAGEN = File.separator + "imagenes" + File.separator  + "colab"; //Ruta donde sube las imágenes para autoservicio
	//Campos de tabla
	private String pcodcia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cia");
	private String codcia = ""; //Compañía que pasa como parámetro el reporte
	private String ptipnom = "";
	private String pfictra = "";
	private String cedula = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cedula"); //Cedula autoservicio
	private String upload = "1";
	private String codcto = "";
	private String imprimir = "1";
	int registros;
	private String fecing = "";
	private String apell1 = "";
	private String nombre1 = "";
	private String pcodcar = ""; 
	private String pcodloc = "";
	private String pcodsuc = "";
	private String pcoduni = "";
	private String pcoddep = "";
	private String sueld1 = "";
	private Object filterValue = "";
	private List<Autovac> list = new ArrayList<Autovac>();//LIstar paises
	
	
	
	
		
	/**
	 * @return the filterValue
	 */
	public Object getFilterValue() {
		return filterValue;
	}

	/**
	 * @param filterValue the filterValue to set
	 */
	public void setFilterValue(Object filterValue) {
		this.filterValue = filterValue;
	}

	/**
	 * @return the list
	 */
	public List<Autovac> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<Autovac> list) {
		this.list = list;
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
	 * @return the fechaDesde
	 */
	public String getFechaDesde() {
		return fechaDesde;
	}
	
	/**
	 * @param fechaDesde the fechaDesde to set
	 */
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	
	/**
	 * @return the fechaHasta
	 */
	public String getFechaHasta() {
		return fechaHasta;
	}
	
	/**
	 * @param fechaHasta the fechaHasta to set
	 */
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	
	/**
	 * @return the codcto
	 */
	public String getCodcto() {
		return codcto;
	}
	
	/**
	 * @param codcto the codcto to set
	 */
	public void setCodcto(String codcto) {
		this.codcto = codcto;
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
	 * @return the imprimir
	 */
	public String getImprimir() {
		return imprimir;
	}
	
	/**
	 * @param imprimir the imprimir to set
	 */
	public void setImprimir(String imprimir) {
		this.imprimir = imprimir;
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
	String[][] vltabla = null; // Tabla para el select
	private String grupo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("grupo"); //Usuario logeado
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Coneccion a base de datos
	//Pool de conecciones JNDI
	Connection con;
	PreparedStatement pstmt = null;
	ResultSet r;
	   
	
	
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
	     * Leer Datos de autos03 autoservicio de vacaciones
	     * @throws NamingException 
		 * @throws SQLException 
		 * @throws ClassNotFoundException 
	     * @throws IOException 
	     **/ 	
	  	public void select(int first, int pageSize, String sortField, Object filterValue)   {
	
	  		Context initContext;
			try {
				initContext = new InitialContext();
			 
	    	DataSource ds = (DataSource) initContext.lookup(JNDI);
	        con = ds.getConnection();
	        //Reconoce la base de datos de conección para ejecutar el query correspondiente a cada uno
	 		DatabaseMetaData databaseMetaData = con.getMetaData();
	 		productName    = databaseMetaData.getDatabaseProductName();//Identifica la base de datos de conección
	
	  		String query = "";
	  	    //Busca cedula de autoservcio
	
	  			
	  		if(registros>0){
	  			imprimir = "0"; 
	  		}
	
	  		 if(fechaDesde.equals("")){
	  			 fechaDesde = "01/01/1900";
	  		 }
	  		 if(fechaHasta.equals("")){
	  			 fechaHasta = "01/01/2900";
	  		 }
	  		 
	  		 
	  		switch ( productName ) {
	        case "Oracle":
	        	query = "  select * from ";
	        	query += " ( select query.*, rownum as rn from";
	        	query += " ( SELECT anio, to_char(fecha,'dd/mm/yyyy'), codcto, descto, canven";
		        query += " FROM autos03";
		        query += " where anio  like '" + anio + "%'";
		        query += " and codcto  like '" + codcto.toUpperCase() + "%'";
		        //query += " and fecha  between '" + fechaDesde + "' and '" + fechaHasta + "'";
		        query += " and trim(cedula) like '" + cedula + "%'";
		        query += " and trim(pcodcia) = '" + pcodcia + "'";
		        query += " and anio||codcto like '%" + ((String) filterValue).toUpperCase() + "%'";
		        query += " and grupo = '" + grupo + "'";
		        query += " order by " + sortField.replace("v", "") + ") query";
		        query += " ) where rownum <= " + pageSize ;
		        query += " and rn > (" + first + ")";
	             break;
	        case "PostgreSQL":
	        	query = " SELECT anio, to_char(fecha,'dd/mm/yyyy'), codcto, descto, canven";
		        query += " FROM autos03";
		        query += " where CAST(anio AS text)  like '" + anio + "%'";
		        query += " and codcto  like '" + codcto.toUpperCase() + "%'";
		        //query += " and fecha  between '" + fechaDesde + "' and '" + fechaHasta + "'";
		        query += " and trim(cedula) like '" + cedula + "%'";
		        query += " and trim(pcodcia) = '" + pcodcia + "'";
		        query += " and cast(anio as text)||codcto like '%" + ((String) filterValue).toUpperCase() + "%'";
		        query += " and grupo = '" + grupo + "'";
		        query += " order by " + sortField.replace("v", "") ;
		        query += " LIMIT " + pageSize;
		        query += " OFFSET " + first;
	             break;          		   
	  		}

	  		
	  		
	  		
	        pstmt = con.prepareStatement(query);
	        //System.out.println(query);
	        
	        r =  pstmt.executeQuery();      
	  		
	        while (r.next()){
	        	Autovac select = new Autovac();
	            select.setVanio(r.getString(1));
	            select.setVfecha(r.getString(2));
	            select.setVcodcto(r.getString(3));
	            select.setVdescto(r.getString(4));
	            select.setVcanven(r.getString(5));
	        	//Agrega la lista
	        	list.add(select);
	        	registros = list.size();
	        }
	        //Cierra las conecciones
	        pstmt.close();
	        con.close();
	        r.close();
	        
			} catch (NamingException | SQLException e) {
				e.printStackTrace();
			}    
	  		
	  	}
	  	
	  		
	  	
	 /**
		 * @return the rows
		 */
		public int getRows() {
			return rows;
		}
		
	  	
	  	/**
		 * @return the registros
		 */
		public int getRegistros() {
			return registros;
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
	 		
	 		String query = "";
	 		
	 		switch ( productName ) {
	        case "Oracle":
	        	query = "SELECT count_autos03(" + Integer.parseInt(grupo) + ",'" + anio + "','"  + cedula + "','" + pcodcia + "','" + ((String) filterValue).toUpperCase() + "') from dual";
	             break;
	        case "PostgreSQL":
	        	query = "SELECT count_autos03(" + Integer.parseInt(grupo) + ",'" + anio + "','"  + cedula + "','" + pcodcia + "','" + ((String) filterValue).toUpperCase() + "')";
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
		 /////////////////////////////////////////////////ENCABEZADOS///////////////////////////////////////////////////////////////////////////////////////
		
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
	  		       query += " from autos03";
	  		       query += " where CAST(anio AS text) like (select cast(max(anio) as text) from autos05 where cedula like'" + cedula +"%')";
	  		       //query += " and CAST(mes AS text) like (select cast(max(mes) as text) from autos05 where cedula like'" + cedula +"%' and CAST(anio AS text) like (select cast(max(anio) as text) from autos05 where cedula like'" + cedula +"%'))";
	  		       query += " and  cedula like'" + cedula +"%'";
	  		       query += " and grupo = '" + grupo + "'";
	  	    }  else {
	  	    	   query = "select distinct trim(pcodcia), trim(nomcia2), trim(ptipnom), trim(desnom), trim(pfictra),";	
	  		       query += " trim(cedula), to_char(fecing,'DD/MM/YYYY'), trim(apell1), trim(apell2), trim(nombre1), trim(nombre2),";
	  		       query += " trim(pcodcar), trim(descar), trim(pcodloc), trim(desloc), trim(pcodsuc), trim(desuc), trim(pcoduni), desuni, pcoddep, desdep, to_char(sueld1,'999,999,999.99')";
	  		       query += " from autos03";
	  		       //query += " where CAST(anio AS text) like '" + anio + "'";
	  		       //query += " and CAST(mes AS text) like '" + mes + "'";
	  	    	
	  	    }
	  	    
	  	  String queryora = "";
	  	    if (opc.equals("I")){		
	  	    //Busca la consulta para encabezado básico
	  	    	queryora = "select distinct trim(pcodcia), trim(nomcia2), trim(ptipnom), trim(desnom), trim(pfictra),";	
	  	    	queryora += " trim(cedula), to_char(fecing,'DD/MM/YYYY'), trim(apell1), trim(apell2), trim(nombre1), trim(nombre2),";
	  	    	queryora += " trim(pcodcar), trim(descar), trim(pcodloc), trim(desloc), trim(pcodsuc), trim(desuc), trim(pcoduni), desuni, pcoddep, desdep, to_char(sueld1,'999,999,999.99')";
	  	    	queryora += " from autos03";
	  	    	queryora += " where anio like (select max(anio) from autos05 where cedula like'" + cedula +"%')";
	  		       //query += " and CAST(mes AS text) like (select cast(max(mes) as text) from autos05 where cedula like'" + cedula +"%' and CAST(anio AS text) like (select cast(max(anio) as text) from autos05 where cedula like'" + cedula +"%'))";
	  	    	queryora += " and  cedula like'" + cedula +"%'";
	  	    	queryora += " and grupo = '" + grupo + "'";
	  	    }  else {
	  	    	queryora = "select distinct trim(pcodcia), trim(nomcia2), trim(ptipnom), trim(desnom), trim(pfictra),";	
	  	    	queryora += " trim(cedula), to_char(fecing,'DD/MM/YYYY'), trim(apell1), trim(apell2), trim(nombre1), trim(nombre2),";
	  	    	queryora += " trim(pcodcar), trim(descar), trim(pcodloc), trim(desloc), trim(pcodsuc), trim(desuc), trim(pcoduni), desuni, pcoddep, desdep, to_char(sueld1,'999,999,999.99')";
	  	    	queryora += " from autos03";
	  		       //query += " where CAST(anio AS text) like '" + anio + "'";
	  		       //query += " and CAST(mes AS text) like '" + mes + "'";
	  	    	
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
	
	
	}
	
