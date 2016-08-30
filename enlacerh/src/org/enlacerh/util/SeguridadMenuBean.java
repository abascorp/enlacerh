package org.enlacerh.util;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.primefaces.context.RequestContext;

@ManagedBean(name = "smnubean")
@SessionScoped
public class SeguridadMenuBean extends Utils implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SeguridadMenuBean() throws NamingException, IOException {
		if (!rq.isRequestedSessionIdValid()) {
			//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");

		} else {
		//System.out.println("El login invalidado : "+login);
		if(login!=null){
		opcrol(login);
		opcAutosrv(login);
		grupos();
		}
		}
	}
	
	@PostConstruct
	public void ini(){
		if (selectIsAct().equals("1")) {
			//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			RequestContext.getCurrentInstance().execute("PF('idleDialog1').show()");

		} 
	}
	
	public void valmail(){
		if (selectMailValidado().equals("0")) {
			//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			RequestContext.getCurrentInstance().execute("PF('idleDialog2').show()");

		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////OPCIONES DE SEGURIDAD DEL MENU BASICO/////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private String bas = "true";
	private String bas01 = "true";
	private String bas02 = "true";
	private String bas03 = "true";
	private String bas04 = "true";
	private String bas05 = "true";
	private String seg = "true";
	private String seg01 = "false";
	private String seg02 = "true";
	private String seg03 = "true";
	private String seg04 = "true";
	private String seg05 = "true";
	private String seg06 = "true";
	//Autosericio
	private String autoconst = "true";
	private String mail = "true";
	private String autos02 = "true";
	private String autos03 = "true";
	private String autos04 = "true";
	private String autos05 = "true";
	private String autos06 = "true";
	private String autos07 = "true";
	private String autos10 = "true";
	private String login = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario"); //Usuario logeado
	private String pcodcia = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cia");
	private String admin = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("admin");
	private String grupo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("grupo"); //Usuario logeado
	private String cedula = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cedula"); //Cedula autoservicio

	PntGenerica consulta = new PntGenerica();
	HttpServletRequest rq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	
	//Para dassboard mostrar divs
	private String display1 = "";
	private String display2 = "";
	private String display3 = "";
	private String display4 = "";
	private String display5 = "";
	private String display6 = "";
	private String display7 = "";
	
	


	/**
	 * @return the display1
	 */
	public String getDisplay1() {
		return display1;
	}

	/**
	 * @param display1 the display1 to set
	 */
	public void setDisplay1(String display1) {
		this.display1 = display1;
	}

	/**
	 * @return the display2
	 */
	public String getDisplay2() {
		return display2;
	}

	/**
	 * @param display2 the display2 to set
	 */
	public void setDisplay2(String display2) {
		this.display2 = display2;
	}

	/**
	 * @return the display3
	 */
	public String getDisplay3() {
		return display3;
	}

	/**
	 * @param display3 the display3 to set
	 */
	public void setDisplay3(String display3) {
		this.display3 = display3;
	}

	/**
	 * @return the display4
	 */
	public String getDisplay4() {
		return display4;
	}

	/**
	 * @param display4 the display4 to set
	 */
	public void setDisplay4(String display4) {
		this.display4 = display4;
	}

	/**
	 * @return the display5
	 */
	public String getDisplay5() {
		return display5;
	}

	/**
	 * @param display5 the display5 to set
	 */
	public void setDisplay5(String display5) {
		this.display5 = display5;
	}

	/**
	 * @return the display6
	 */
	public String getDisplay6() {
		return display6;
	}

	/**
	 * @param display6 the display6 to set
	 */
	public void setDisplay6(String display6) {
		this.display6 = display6;
	}

	/**
	 * @return the display7
	 */
	public String getDisplay7() {
		return display7;
	}

	/**
	 * @param display7 the display7 to set
	 */
	public void setDisplay7(String display7) {
		this.display7 = display7;
	}
	

	/**
	 * @return the bas04
	 */
	public String getBas04() {
		return bas04;
	}

	/**
	 * @param bas04 the bas04 to set
	 */
	public void setBas04(String bas04) {
		this.bas04 = bas04;
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
	 * @return the autos02
	 */
	public String getAutos02() {
		return autos02;
	}

	/**
	 * @param autos02 the autos02 to set
	 */
	public void setAutos02(String autos02) {
		this.autos02 = autos02;
	}

	/**
	 * @return the autos03
	 */
	public String getAutos03() {
		return autos03;
	}

	/**
	 * @param autos03 the autos03 to set
	 */
	public void setAutos03(String autos03) {
		this.autos03 = autos03;
	}

	/**
	 * @return the autos04
	 */
	public String getAutos04() {
		return autos04;
	}

	/**
	 * @param autos04 the autos04 to set
	 */
	public void setAutos04(String autos04) {
		this.autos04 = autos04;
	}

	/**
	 * @return the autos05
	 */
	public String getAutos05() {
		return autos05;
	}

	/**
	 * @param autos05 the autos05 to set
	 */
	public void setAutos05(String autos05) {
		this.autos05 = autos05;
	}

	/**
	 * @return the autos06
	 */
	public String getAutos06() {
		return autos06;
	}

	/**
	 * @param autos06 the autos06 to set
	 */
	public void setAutos06(String autos06) {
		this.autos06 = autos06;
	}

	/**
	 * @return the autos07
	 */
	public String getAutos07() {
		return autos07;
	}

	/**
	 * @param autos07 the autos07 to set
	 */
	public void setAutos07(String autos07) {
		this.autos07 = autos07;
	}

	/**
	 * @return the autos10
	 */
	public String getAutos10() {
		return autos10;
	}

	/**
	 * @param autos10 the autos10 to set
	 */
	public void setAutos10(String autos10) {
		this.autos10 = autos10;
	}
	
	
	
	/**
	 * @return the bas
	 */
	public String getBas() {
		return bas;
	}

	/**
	 * @param bas
	 *            the bas to set
	 */
	public void setBas(String bas) {
		this.bas = bas;
	}

	
	
	/**
	 * @return the bas01
	 */
	public String getBas01() {
		return bas01;
	}

	/**
	 * @param bas01 the bas01 to set
	 */
	public void setBas01(String bas01) {
		this.bas01 = bas01;
	}

	/**
	 * @return the bas02
	 */
	public String getBas02() {
		return bas02;
	}

	/**
	 * @param bas02 the bas02 to set
	 */
	public void setBas02(String bas02) {
		this.bas02 = bas02;
	}

	/**
	 * @return the bas03
	 */
	public String getBas03() {
		return bas03;
	}

	/**
	 * @param bas03 the bas03 to set
	 */
	public void setBas03(String bas03) {
		this.bas03 = bas03;
	}

	

	/**
	 * @return the autoconst
	 */
	public String getAutoconst() {
		return autoconst;
	}

	/**
	 * @param autoconst the autoconst to set
	 */
	public void setAutoconst(String autoconst) {
		this.autoconst = autoconst;
	}

	/**
	 * @return the seg
	 */
	public String getSeg() {
		return seg;
	}

	/**
	 * @param seg the seg to set
	 */
	public void setSeg(String seg) {
		this.seg = seg;
	}

	/**
	 * @return the seg01
	 */
	public String getSeg01() {
		return seg01;
	}

	/**
	 * @param seg01 the seg01 to set
	 */
	public void setSeg01(String seg01) {
		this.seg01 = seg01;
	}

	/**
	 * @return the seg02
	 */
	public String getSeg02() {
		return seg02;
	}

	/**
	 * @param seg02 the seg02 to set
	 */
	public void setSeg02(String seg02) {
		this.seg02 = seg02;
	}

	/**
	 * @return the seg03
	 */
	public String getSeg03() {
		return seg03;
	}

	/**
	 * @param seg03 the seg03 to set
	 */
	public void setSeg03(String seg03) {
		this.seg03 = seg03;
	}

	

	/**
	 * @return the seg05
	 */
	public String getSeg05() {
		return seg05;
	}

	/**
	 * @param seg05 the seg05 to set
	 */
	public void setSeg05(String seg05) {
		this.seg05 = seg05;
	}

	/**
	 * @return the seg06
	 */
	public String getSeg06() {
		return seg06;
	}

	/**
	 * @param seg06 the seg06 to set
	 */
	public void setSeg06(String seg06) {
		this.seg06 = seg06;
	}

	
	
	/**
	 * @return the bas05
	 */
	public String getBas05() {
		return bas05;
	}

	/**
	 * @param bas05 the bas05 to set
	 */
	public void setBas05(String bas05) {
		this.bas05 = bas05;
	}

	/**
	 * @return the seg04
	 */
	public String getSeg04() {
		return seg04;
	}

	/**
	 * @param seg04 the seg04 to set
	 */
	public void setSeg04(String seg04) {
		this.seg04 = seg04;
	}

	/*Verifica las opciones de seguridad*/
	public void opcrol(String logged) throws NamingException, IOException{

		String[][] vlTabla = null;
		//Si existe usuario comienza validaciones de roles
			//Selecciona el numero de opcion y el acceso por roles
		String vlquery = "SELECT trim(numopc), trim(accopc) " +
					" FROM seg006 " +
					" WHERE p_codrol in (select codrol from autos01 where coduser = '" + logged.toUpperCase() + "') order by numopc";
			consulta.selectPntGenerica(vlquery, JNDI);
			//System.out.println(vlquery);
		int rows = consulta.getRows();
		//System.out.println("Tipo de usuario:" + tipusr);
		  if(rows>0){//Si la consulta es mayor a cero comineza la validación de seguridad
			  vlTabla = consulta.getArray();  
               for (int i=0; i<rows; i++){//Recorre la tabla de seguridad para asignar acceso o no
				  //Menú basico
				  if(vlTabla[i][0].toLowerCase().equals("bas") && vlTabla[i][1].equals("1")){					  
					  bas = "false";
				  }
				  //Países
				  if(vlTabla[i][0].toLowerCase().equals("bas01") && vlTabla[i][1].equals("1")){					  
					  bas01 = "false";
				  }
				  //Ciudades
				  if(vlTabla[i][0].toLowerCase().equals("bas02") && vlTabla[i][1].equals("1")){					  
					  bas02 = "false";
				  }
				//Cias
				  if(vlTabla[i][0].toLowerCase().equals("bas03") && vlTabla[i][1].equals("1")){					  
					  bas03 = "false";
				  }	
				  if(vlTabla[i][0].toLowerCase().equals("bas04") && vlTabla[i][1].equals("1")){					  
					  bas04 = "false";
				  }	
				  if(vlTabla[i][0].toLowerCase().equals("bas05") && vlTabla[i][1].equals("1")){					  
					  bas05 = "false";
				  }	

				  //Seguridad
				  if(vlTabla[i][0].toLowerCase().equals("seg") && vlTabla[i][1].equals("1")){					  
					  seg = "false";
				  }
				  if(vlTabla[i][0].toLowerCase().equals("seg02") && vlTabla[i][1].equals("1")){					  
					  seg02 = "false";
				  }
				  if(vlTabla[i][0].toLowerCase().equals("seg03") && vlTabla[i][1].equals("1")){					  
					  seg03 = "false";
				  }
				  if(vlTabla[i][0].toLowerCase().equals("seg04") && vlTabla[i][1].equals("1")){					  
					  seg04 = "false";
				  }
				  if(vlTabla[i][0].toLowerCase().equals("seg05") && vlTabla[i][1].equals("1")){					  
					  seg05 = "false";
				  }
				  if(vlTabla[i][0].toLowerCase().equals("seg06") && vlTabla[i][1].equals("1")){					  
					  seg06 = "false";
				  }

		  }		
	}
	}
	
	/*Verifica las opciones de seguridad y vista para autoservicio*/
	public void opcAutosrv(String logged) throws NamingException, IOException{	
		String[][] vlTabla = null;
		
		  consulta.selectPntGenerica("SELECT trim(numopc), trim(accopc) " +
					" FROM seg010 " +
					" WHERE p_codcia  = '" + pcodcia + "' and grupo = '" + grupo +"'", JNDI);	

		int rows;
		//System.out.println("Filas:" + rows);

		 vlTabla = consulta.getArray();  			  
		rows = consulta.getRows();	  
		//System.out.println("Filas:" + rows);
		  if(rows>0){//Si la consulta es mayor a cero comineza la validación de seguridad
			  vlTabla = consulta.getArray();  
               for (int i=0; i<rows; i++){//Recorre la tabla de seguridad para asignar acceso o no
				  //Menú basico
				  if(vlTabla[i][0].toLowerCase().equals("autos02") && vlTabla[i][1].equals("1")){					  
					  autos02 = "false";
					  display1 = "none";
				  }
				  //Compañías
				  if(vlTabla[i][0].toLowerCase().equals("autos03") && vlTabla[i][1].equals("1")){					  
					  autos03 = "false";
					  display2 = "none";
				  }
				  //Miscelaneas
				  if(vlTabla[i][0].toLowerCase().equals("autos04") && vlTabla[i][1].equals("1")){					  
					  autos04 = "false";
					  display3 = "none";
				  }
				  if(vlTabla[i][0].toLowerCase().equals("autos05") && vlTabla[i][1].equals("1")){					  
					  autos05 = "false";
					  display4 = "none";
				  }
				  if(vlTabla[i][0].toLowerCase().equals("autos06") && vlTabla[i][1].equals("1")){					  
					  autos06 = "false";
					  display5 = "none";
				  }
				  if(vlTabla[i][0].toLowerCase().equals("autos07") && vlTabla[i][1].equals("1")){					  
					  autos07 = "false";
					  display6 = "none";
				  }
				  if(vlTabla[i][0].toLowerCase().equals("autos10") && vlTabla[i][1].equals("1")){					  
					  autos10 = "false";
					  display7 = "none";
				  }if(vlTabla[i][0].toLowerCase().equals("autoconst") && vlTabla[i][1].equals("1")){					  
					  autoconst = "false";
				  }	  
				  if(vlTabla[i][0].toLowerCase().equals("mail") && vlTabla[i][1].equals("1")){					  
					  mail = "false";
				  }
               }      			
	         }	
	       }
	
	public String grupos(){
		if (!rq.isRequestedSessionIdValid()) {
			//rq.getCurrentInstance().execute("PF('yourdialogid').show()");
			RequestContext.getCurrentInstance().execute("PF('idleDialog').show()");
		}  else {
		if(admin.equals("1")){
			seg01 = "true";
		}
		//System.out.println(seg01);
		}
	 return seg01;		 
	}

	/**
	 * @return the admin
	 */
	public String getAdmin() {
		return admin;
	}
	
	//Coneccion a base de datos
		//Pool de conecciones JNDI
		Connection con;
		PreparedStatement pstmt = null;
		ResultSet r;

	/*
	 * Listar instancias al momento del login
	 * si el usuario no tiene alguna instancia predefinida
	 * muestra el modal para seleccionar la instancia,
	 * lee de las instancias asociadas al usuario
	 */
     public List<String> select() throws NamingException, SQLException   {
  		
  		Context initContext = new InitialContext();     
 		DataSource ds = (DataSource) initContext.lookup(JNDI);
 		con = ds.getConnection();
 		List<String> values = new ArrayList<String>();
 		
 		String query = "SELECT grupo||' - '||trim(empresa) ";
	       query += " FROM seg001 order by grupo";
  		  		
  		pstmt = con.prepareStatement(query);
        //System.out.println(query);
  		
        r =  pstmt.executeQuery();
        		
        while (r.next()){
 
        values.add(r.getString(1));

        }
        //Cierra las conecciones
        pstmt.close();
        con.close();
        return values;
    }
     
     /*
      * Define la instancia seleccionada
      */
      public void grupos(String grupo){
     	 FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("grupo", grupo.split(" - ")[0]);
      }
      
      
      /**
	     * Verifica si el trabajador está retirado
	     * @throws NamingException 
	     **/ 	
	  	private String selectIsAct()  {
	  		int rows;
	  		String isact = "";
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
	  		return isact;  		
	  	}
	  	
	  	
	  	/**
	     * Verifica si el trabajador validó cuenta
	     * @throws NamingException 
	     **/ 	
	  	private String selectMailValidado()  {
	  		int rows;
	  		String isact = "";
	  		if(grupo==null){
	  			grupo = "99999999";
	  		}
	  	    //Busca la consulta para encabezado básico
	  		try {
				consulta.selectPntGenerica("select mail_validado" +
						" from autos01 " +
						" where  coduser = trim('" + login.toUpperCase() + "')", JNDI);
			} catch (NamingException e) {
				e.printStackTrace();
			} 
	  		String[][] vltabla = consulta.getArray();
	  		rows = consulta.getRows();
	        if(rows>0){
	        	isact = vltabla[0][0];
	        }
	  		return isact;  		
	  	}
	

}
