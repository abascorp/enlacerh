package org.enlacerh.util;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;


@ManagedBean(name = "listBean")
@RequestScoped
public class ListBean extends Utils  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Utils bd = new Utils();
	
	//Selecciona las opciones de idioma en el login. Para manejo de idiomas
	 private SelectItem[] lang = new SelectItem[]{
			    new SelectItem("es", getMessage("idioma1")),
		        new SelectItem("en", getMessage("idioma2")),
		        new SelectItem("por", getMessage("idioma3"))};
	 

	 private String langSelected;

	    public ListBean() {
	    }

	    public SelectItem[] getlang() {
	        return lang;
	    }
        //Toma el valor del idioma
		public String getLangSelected() {
			if (langSelected == null) {  
				langSelected = getMessage("idioma1"); // This will be the default selected item.  
		    }  
			return langSelected;
		}

		public void setLangSelected(String langSelected) {
			this.langSelected = langSelected;
		}
       
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////LISTA DE OPCIONES DE PAGINACION///////////////////////////////////////////		
////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		
		//Selecciona las opciones de paginación de todas las tablas
		private SelectItem[] rpp = new SelectItem[]{
			    new SelectItem("5",  "5"),
		        new SelectItem("10", "10"),
		        new SelectItem("15", "15"),
		        new SelectItem("20", "20"),
		        new SelectItem("50", "50"),
		        new SelectItem("100","100"),
		        new SelectItem("500","500")};
		
		private String rppSelected = "15";

		/**
		 * @return the rpp
		 */
		public SelectItem[] getRpp() {
			return rpp;
		}

		/**
		 * @return the rppSelected
		 */
		public String getRppSelected() {
			return rppSelected;
		}

		/**
		 * @param rppSelected the rppSelected to set
		 */
		public void setRppSelected(String rppSelected) {
			this.rppSelected = rppSelected;
		}
		

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////LISTA DE OPCIONES DE PNT009 UNIDADES DE NEGOCIO////////////////////////////		
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Selecciona las opciones de tipo de unidad en PNT009
	private SelectItem[] tipoUni = new SelectItem[]{		
	new SelectItem("1",  getMessage("pnt009Hijo")),
	new SelectItem("0", getMessage("pnt009Pad"))};
	
	/**
	 * @return the tipoUni
	 */
	public SelectItem[] getTipoUni() {
		return tipoUni;
	}
	
	
	private String tipoUniSelected = "s";
	
	/**
	 * @return the tipoUniSelected
	 */
	public String getTipoUniSelected() {
		return tipoUniSelected;
	}
	
	/**
	 * @param tipoUniSelected the tipoUniSelected to set
	 */
	public void setTipoUniSelected(String tipoUniSelected) {
		this.tipoUniSelected = tipoUniSelected;
	}
	
	private SelectItem[] estado = new SelectItem[]{
	new SelectItem("0",  getMessage("pnt009Opc0")),
	new SelectItem("1", getMessage("pnt009Opc1"))};
	
	/**
	 * @return the estado
	 */
	public SelectItem[] getEstado() {
		return estado;
	}
	
	private String estadoSelected = "s";
	
	/**
	 * @return the estadoSelected
	 */
	public String getEstadoSelected() {
		return estadoSelected;
	}
	
	/**
	 * @param estadoSelected the estadoSelected to set
	 */
	public void setEstadoSelected(String estadoSelected) {
		this.estadoSelected = estadoSelected;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////LISTA DE OPCIONES DE CARGA EN AUTOSERVICIO///////////////////////////////////////////		
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Selecciona las opciones de paginación de todas las tablas
	private SelectItem[] autoext = new SelectItem[]{
	new SelectItem("N", getMessage("autos02Opc1")),
	new SelectItem("V", getMessage("autos02Opc2")),
	new SelectItem("P", getMessage("autos02Opc3")),
	new SelectItem("I", getMessage("autos02Opc4")),
	new SelectItem("PR", getMessage("autos02Opc5")),
	new SelectItem("U", getMessage("autos02Opc6")),
	new SelectItem("T", getMessage("autos02Opc7"))};
	
	private String autoextselectded = "S";
	
	/**
	 * @return the autoextselectded
	 */
	public String getAutoextselectded() {
		return autoextselectded;
	}
	
	/**
	 * @param autoextselectded the autoextselectded to set
	 */
	public void setAutoextselectded(String autoextselectded) {
		this.autoextselectded = autoextselectded;
	}
	
	/**
	 * @return the autoext
	 */
	public SelectItem[] getAutoext() {
		return autoext;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////LISTA DE OPCIONES DE CARGA EN AUTOSERVICIO///////////////////////////////////////////		
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Selecciona las opciones de paginación de todas las tablas
	private SelectItem[] meses = new SelectItem[]{
	new SelectItem("1", getMessage("enero")),
	new SelectItem("2", getMessage("febrero")),
	new SelectItem("3", getMessage("marzo")),
	new SelectItem("4", getMessage("abril")),
	new SelectItem("5", getMessage("mayo")),
	new SelectItem("6", getMessage("junio")),
	new SelectItem("7", getMessage("julio")),
	new SelectItem("8", getMessage("agosto")),
	new SelectItem("9", getMessage("septiembre")),
	new SelectItem("10", getMessage("octubre")),
	new SelectItem("11", getMessage("noviembre")),
	new SelectItem("12", getMessage("diciembre"))};
	
	private String messelectded = "S";
	
	/**
	 * @return the messelectded
	 */
	public String getMesselectded() {
		return messelectded;
	}
	
	/**
	 * @return the meses
	 */
	public SelectItem[] getMeses() {
		return meses;
	}
	
	/**
	 * @param meses the meses to set
	 */
	public void setMeses(SelectItem[] meses) {
		this.meses = meses;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////LISTA DE OPCIONES DE ROLES DE SEGURIDAD///////////////////////////////////////////		
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Selecciona las opciones de paginación de todas las tablas
	private SelectItem[] opcrol = new SelectItem[]{
	new SelectItem("1", getMessage("seg006Opc1")),
	new SelectItem("2", getMessage("seg006Opc2")),
	new SelectItem("3", getMessage("seg006Opc3")),
	new SelectItem("4", getMessage("seg006Opc4"))};
	
	private String opcroselected = "1";
	
	/**
	 * @return the opcrol
	 */
	public SelectItem[] getOpcrol() {
		return opcrol;
	}
	
	/**
	 * @param opcrol the opcrol to set
	 */
	public void setOpcrol(SelectItem[] opcrol) {
		this.opcrol = opcrol;
	}
	
	/**
	 * @return the opcroselected
	 */
	public String getOpcroselected() {
		return opcroselected;
	}
	
	/**
	 * @param opcroselected the opcroselected to set
	 */
	public void setOpcroselected(String opcroselected) {
		this.opcroselected = opcroselected;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////LISTA TIPO DE USUARIO///////////////////////////////////////////		
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Selecciona las opciones de paginación de todas las tablas
	private SelectItem[] tipusr = new SelectItem[]{
	new SelectItem("usuario", "usuario"),	
	new SelectItem("administrador", "administrador"),
	new SelectItem("root", "root")};
	
	private String tipusrselected = "administrador";
	
	
	/**
	 * @return the tipusr
	 */
	public SelectItem[] getTipusr() {
		return tipusr;
	}
	
	public void setTipusr(SelectItem[] tipusr) {
		this.tipusr = tipusr;
	}
	
	public String getTipusrselected() {
		return tipusrselected;
	}
	
	public void setTipusrselected(String tipusrselected) {
		this.tipusrselected = tipusrselected;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////LISTA CANTIDAD DE TRABAJADORES///////////////////////////////////////////		
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Selecciona las opciones de paginación de todas las tablas
	private SelectItem[] canttrab = new SelectItem[]{
	new SelectItem("25", "25"),
	new SelectItem("50", "50"),
	new SelectItem("100", "100"),
	new SelectItem("400", "400"),
	new SelectItem("800", "800"),
	new SelectItem("1000", "1000"),
	new SelectItem("2000", "2000"),
	new SelectItem("5000", "5000"),
	new SelectItem("10000", "10000"),
	new SelectItem("100000", "100000")};
	
	private String canttrabselected = "25";
	
	/**
	 * @return the canttrab
	 */
	public SelectItem[] getCanttrab() {
		return canttrab;
	}
	
	/**
	 * @param canttrab the canttrab to set
	 */
	public void setCanttrab(SelectItem[] canttrab) {
		this.canttrab = canttrab;
	}
	
	/**
	 * @return the canttrabselected
	 */
	public String getCanttrabselected() {
		return canttrabselected;
	}
	
	/**
	 * @param canttrabselected the canttrabselected to set
	 */
	public void setCanttrabselected(String canttrabselected) {
		this.canttrabselected = canttrabselected;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////LISTA DE OPCIONES DE REPORTES CONSTANCIAS//////////////////////////////////		
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Selecciona las opciones de paginación de todas las tablas
	private SelectItem[] opcconst = new SelectItem[]{
	new SelectItem("AUTOCONST", getMessage("autos02Const")),
	new SelectItem("AUTOCONST1", getMessage("autos02Const1")),
	new SelectItem("AUTOCONST2", getMessage("autos02Const2"))};
	
	/**
	 * @return the opcconst
	 */
	public SelectItem[] getOpcconst() {
		return opcconst;
	}
	
	/**
	 * @param opcconst the opcconst to set
	 */
	public void setOpcconst(SelectItem[] opcconst) {
		this.opcconst = opcconst;
	}
	

	
    
}
