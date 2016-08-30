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

package org.enlacerh.getset;

//Clase que se utiliza para pasar parámetros desde CiaNom(Seleccionador de compañías y nóminas.
//Estos parámetros son impresos en jsp para informar al usuario donde está ubicado, también se usa
//para los accesos directos. Se crean los Getter y Setter
public class DatosCianom {
	private String compania;//Código de la compañía
	private String tipnom;//Código de la nómina
	private String descodcia;//Descripción de la compañía
	private String destipnom;//Descripción de la nómina
	/**
	 * @return the compania
	 */
	public String getCompania() {
		return compania;
	}
	/**
	 * @param compania the compania to set
	 */
	public void setCompania(String compania) {
		this.compania = compania;
	}
	/**
	 * @return the tipnom
	 */
	public String getTipnom() {
		return tipnom;
	}
	/**
	 * @param tipnom the tipnom to set
	 */
	public void setTipnom(String tipnom) {
		this.tipnom = tipnom;
	}
	/**
	 * @return the descodcia
	 */
	public String getDescodcia() {
		return descodcia;
	}
	/**
	 * @param descodcia the descodcia to set
	 */
	public void setDescodcia(String descodcia) {
		this.descodcia = descodcia;
	}
	/**
	 * @return the destipnom
	 */
	public String getDestipnom() {
		return destipnom;
	}
	/**
	 * @param destipnom the destipnom to set
	 */
	public void setDestipnom(String destipnom) {
		this.destipnom = destipnom;
	}

}
