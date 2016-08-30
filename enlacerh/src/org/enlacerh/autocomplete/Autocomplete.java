	/*
	 *  Copyright (C) 2011  ABASCORP, C.A.
	
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
	
	package org.enlacerh.autocomplete;
	
	import java.io.IOException;
	import java.io.Serializable;
	import java.util.ArrayList;
	import java.util.List;
	
	import javax.faces.bean.ManagedBean;
	import javax.faces.bean.RequestScoped;
	import javax.faces.context.FacesContext;
	import javax.naming.NamingException;

import org.enlacerh.util.PntGenerica;
import org.enlacerh.util.Utils;

	/*
	 * Esta clase permite mostrar las listas de valores en todas las páginas
	 * utilizando Primefaces y sustituyendo a JQuery
	 */
	
	@ManagedBean
	@RequestScoped
	public class Autocomplete extends Utils implements Serializable {
	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	PntGenerica consulta = new PntGenerica();
	int rows;
	String [][] tabla;
	Utils ruta = new Utils();
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	private String grupo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("grupo"); //Usuario logeado
	
	/**
	 * Lista Compañías.
	 * @throws NamingException 
	 * @return List String
	 * @throws IOException 
	 **/
	 public List<String> completeCia(String query) throws NamingException, IOException {  
	  List<String> results = new ArrayList<String>();  
	 consulta.selectPntGenerica("Select codcia||' - '||nomcia2 " +
	   " from pnt001 " +
		" where codcia||nomcia2 like '%" + query.toUpperCase() + "%' and grupo =" + grupo 
		+ " and codcia in (select p_codcia from seg007 where p_coduser = '" + login.toUpperCase() + "' and grupo = " + grupo + ")"   
		+ " order by codcia", JNDI);
	 rows = consulta.getRows();
	 tabla = consulta.getArray();
	   for (int i = 0; i < rows; i++) {  
		   results.add(tabla[i][0]);  
	    }  
	    return results;  
	}  
	 
	 /**
	  * Lista Compañías.
	  * @throws NamingException 
	  * @return List String
	 * @throws IOException 
	  **/
	  public List<String> completeCia1(String query) throws NamingException, IOException {  
	   List<String> results = new ArrayList<String>();  
	  consulta.selectPntGenerica("Select codcia||' - '||nomcia2 " +
	    " from pnt001 " +
	 	" where codcia||nomcia2 like '%" + query.toUpperCase() + "%'"
	 	+ " order by codcia", JNDI);
	  rows = consulta.getRows();
	  tabla = consulta.getArray();
	    for (int i = 0; i < rows; i++) {  
	 	   results.add(tabla[i][0]);  
	     }  
	     return results;  
	 }  
	 
	 public List<String> completeCiaGrupo(String query) throws NamingException, IOException {  
		  List<String> results = new ArrayList<String>();  

	     consulta.selectPntGenerica("Select codcia||' - '||nomcia2 " +
	       " from pnt001 " +
	    	" where  codcia||nomcia2 like '%" + query.toUpperCase() + "%'" +
	    	" and grupo like '" +  grupo + "'" + 
	    	" order by codcia", JNDI);
	 		    
		 rows = consulta.getRows();
		 tabla = consulta.getArray();
		   for (int i = 0; i < rows; i++) {  
			   results.add(tabla[i][0]);  
		    }  
		    return results;  
		}  
	 
	 
		/**
		 * Lista Paises.
		 * @throws NamingException 
		 * @return List String
		 * @throws IOException 
		 **/
		 public List<String> completePais(String query) throws NamingException, IOException { 
		
		  List<String> results = new ArrayList<String>();  
		
		 consulta.selectPntGenerica("Select codpai||' - '||despai " +
		   " from pnt003 " +
			" where codpai||despai like '%" + query.toUpperCase() + "%' and grupo =" + grupo + " order by codpai", JNDI);
		
		 rows = consulta.getRows();
		 tabla = consulta.getArray();
		   for (int i = 0; i < rows; i++) {  
			   results.add(tabla[i][0]);  
		    }  
		    return results;  
		}  
		

	          
	          /**
	           * Lista Roles.
	           * @throws NamingException 
	           * @return List String
	         * @throws IOException 
	           **/
	           public List<String> completeRol(String query) throws NamingException, IOException {  
	            List<String> results = new ArrayList<String>(); 
	            String vlquery;
	
	    		  	vlquery = "Select codrol||' - '||desrol " +
	             " from seg002 " +
	          	 " where  codrol||desrol like '%" + query.toUpperCase() + "%' and cast(grupo as text) like '" +  grupo + "'" +
	          	 				" order by codrol, grupo";
	    		  	
	    		String 	vlqueryora = "Select codrol||' - '||desrol " +
	    		             " from seg002 " +
	    		          	 " where  codrol||desrol like '%" + query.toUpperCase() + "%' and grupo like '" +  grupo + "'" +
	    		          	 				" order by codrol, grupo";
	           consulta.selectPntGenericaMDB(vlqueryora, vlquery, JNDI);
	
	            //System.out.println(vlquery);
	           rows = consulta.getRows();
	           tabla = consulta.getArray();
	             for (int i = 0; i < rows; i++) {  
	          	   results.add(tabla[i][0]);  
	              }  
	              return results;  
	          }  
	           
	           /**
	            * Lista Usuarios.
	            * @throws NamingException 
	            * @return List String
	            **/
	            public List<String> completeUser(String query) throws NamingException {  
	             List<String> results = new ArrayList<String>(); 
	             
	             String vlquery = "Select coduser||' - '||nbr " +
	              " from autos01 " +
	           	" where coduser||nbr like '%" + query.toUpperCase() + "%' and grupo = '" +  grupo + "'" +
	           	" order by coduser";

	            consulta.selectPntGenerica(vlquery, JNDI);
     		    //System.out.println(vlquery);
	            rows = consulta.getRows();
	            tabla = consulta.getArray();
	              for (int i = 0; i < rows; i++) {  
	           	   results.add(tabla[i][0]);  
	               }  
	               return results;  
	           }   
	
	            /**
	             * Lista Compañías.
	             * @throws NamingException 
	             * @return List String
	             * @throws IOException 
	             **/
	             public List<String> completeCiaSeg(String query) throws NamingException, IOException {  
	              List<String> results = new ArrayList<String>(); 
	              String usuarioInput = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	              String[] vecusuarioInput = usuarioInput.split("\\ - ", -1);
	             consulta.selectPntGenerica("Select codcia||' - '||nomcia2 " +
	               " from pnt001 " +
	            	" where  codcia||nomcia2 like '%" + query.toUpperCase() + "%'" +
	            	" and cast(grupo as text) like '" +  grupo + "'" + 
	            	" and codcia in (select p_codcia from seg007 where p_coduser = '" + vecusuarioInput[0].toUpperCase() + "')" + 
	            	" order by codcia", JNDI);
	         		    
	             rows = consulta.getRows();
	             tabla = consulta.getArray();
	               for (int i = 0; i < rows; i++) {  
	            	   results.add(tabla[i][0]);  
	                }  
	                return results;  
	            } 

	             
	             /**
	              * Lista Compañías.
	              * @throws NamingException 
	              * @return List String
	              * @throws IOException 
	              **/
	              public List<String> completeCiaSegAcc(String query) throws NamingException, IOException {  
	               List<String> results = new ArrayList<String>(); 
	               String usuarioInput = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioInput");
	               String[] vecusuarioInput = usuarioInput.split("\\ - ", -1);
	               String vquery = "Select codcia||' - '||nomcia2 " +
	                " from pnt001 " +
	             	" where  codcia||nomcia2 like '%" + query.toUpperCase() + "%'" +
	             	" and cast(grupo as text) like '" +  grupo + "'" + 
	             	" and codcia in (select p_codcia from seg007 where p_coduser = '" + vecusuarioInput[0].toUpperCase() + "')" + 
	             	" order by codcia";
	               String vqueryora = "Select codcia||' - '||nomcia2 " +
	   	                " from pnt001 " +
	   	             	" where  codcia||nomcia2 like '%" + query.toUpperCase() + "%'" +
	   	             	" and grupo like '" +  grupo + "'" + 
	   	             	" and codcia in (select p_codcia from seg007 where p_coduser = '" + vecusuarioInput[0].toUpperCase() + "')" + 
	   	             	" order by codcia";
	              consulta.selectPntGenericaMDB(vqueryora, vquery, JNDI);
	              //System.out.println(vquery);
	          		    
	              rows = consulta.getRows();
	              tabla = consulta.getArray();
	                for (int i = 0; i < rows; i++) {  
	             	   results.add(tabla[i][0]);  
	                 }  
	                 return results;  
	             } 
	              
	              /**
	               * Lista Ciudades.
	               * @throws NamingException 
	               * @return List String
	              * @throws IOException 
	               **/
	               public List<String> completeCiudad(String query) throws NamingException, IOException {  
	             	  String pais = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pais");
	             	  String[] veccodpai = pais.split("\\ - ", -1);

	                List<String> results = new ArrayList<String>(); 

	                consulta.selectPntGenerica("Select codciu||' - '||desciu " +
	             		   " from pnt004 " +
	             			" where  codciu||desciu like '%" + query.toUpperCase() + "%' " +
	             			" and p_codpai = '" + veccodpai[0] + "'" +
	             					" and grupo =" + grupo +
	             							" order by codciu", JNDI);
	               rows = consulta.getRows();
	               tabla = consulta.getArray();
	                 for (int i = 0; i < rows; i++) {  
	              	   results.add(tabla[i][0]);  
	                  }  
	                  return results;  
	              }  
	             
	             /**
	              * Lista Compañías.
	              * @throws NamingException 
	              * @return List String
	              * @throws IOException 
	              **/
	              public List<String> completeCiaSegAutosrv(String query) throws NamingException, IOException {  
	               List<String> results = new ArrayList<String>(); 
	               String usuarioInput = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	               String[] vecusuarioInput = usuarioInput.split("\\ - ", -1);
	              consulta.selectPntGenerica("Select codcia||' - '||nomcia2 " +
	                " from pnt001 " +
	             	" where  codcia||nomcia2 like '%" + query.toUpperCase() + "%'" +
	             	" and grupo = '" +  grupo + "'" + 
	             	" and codcia in (select p_codcia from seg007 where p_coduser = '" + vecusuarioInput[0].toUpperCase() + "')" + 
	             	" order by codcia", JNDI);
	          		    
	              rows = consulta.getRows();
	              tabla = consulta.getArray();
	                for (int i = 0; i < rows; i++) {  
	             	   results.add(tabla[i][0]);  
	                 }  
	                 return results;  
	             } 

		

			 
	}
