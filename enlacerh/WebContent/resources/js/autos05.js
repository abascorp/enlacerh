/*
 *  Copyright (C) 2011  ANDRES DOMINGUEZ, CHRISTIAN DÍAZ

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los términos de la Licencia Pública General GNU publicada 
    por la Fundación para el Software Libre, ya sea la versión 3 
    de la Licencia, o (a su elección) cualquier versión posterior.

    Este programa se distribuye con la esperanza de que sea útil, pero 
    SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
    MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. 
    Consulte los detalles de la Licencia Pública General GNU para obtener 
    una información más detallada. 

    Debería haber recibido una copia de la Licencia Pública General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.
 */

var milisec = 120000;
var vganio = '';
var vgfechadesde = '01/01/2012';
var vgfechahasta = '31/12/2999';
var vgcodcto = '';
var vgmes = '';



function limpiar()
{  //Llamado por el boton limpiar
	document.getElementById("formAutoSInteres:anio").value="";
	document.getElementById("formAutoSInteres:codcto").value="";
	document.getElementById("formAutoSInteres:fd").value="01/01/1900";
	document.getElementById("formAutoSInteres:fh").value="31/12/2999";
	document.getElementById("formAutoSInteres:mes").value="";

}



function imprimirRecibo(rep, ficha, anio, mes){
	  // Si el mensaje que retorna es acceso
		window.open('../jsf/reportesauto.jsp?reporte='+rep+'.rptdesign&anio=' + anio 
				+ "&ficha=" + rTrim(ficha) 
				+ "&mes=" + mes);
	}

function imprimirDetalleInt(rep, ficha, anio, mes){
	  // Si el mensaje que retorna es acceso
		window.open('../jsf/reportesauto.jsp?reporte='+rep+'.rptdesign&ficha=' + rTrim(ficha));
	}

