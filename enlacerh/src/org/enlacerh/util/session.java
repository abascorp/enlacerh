/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.enlacerh.util;


/**
 *
 * @author Andres
 */
public class session {


 public session(){

 }

/*****************************************************************************
 * Métodos y definición de variables para las JSP personalizadas del Birt
 * Andrés Dominguez 07/04/2009.
 ****************************************************************************/
//Definición de variables
private String param = "Parametros";
private String param1 = "parameterDialog";
private String expDatos = "Exportar Datos";
private String expDatos1 = "simpleExportDataDialog";
private String expRep = "Exportar Reporte";
private String expRep1 = "exportReportDialog";
private String impRep = "Imprimir Reporte";
private String impRep1 = "printReportDialog";

    public String getExpDatos() {
        return expDatos;
    }

    public String getExpRep() {
        return expRep;
    }

    public String getImpRep() {
        return impRep;
    }

    public  String getParam() {
        return param;
    }

    public String getExpDatos1() {
        return expDatos1;
    }

    public String getExpRep1() {
        return expRep1;
    }

    public String getImpRep1() {
        return impRep1;
    }

    public String getParam1() {
        return param1;
    }

}