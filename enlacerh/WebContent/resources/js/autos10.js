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






function limpiar()
{  //Llamado por el boton limpiar
	document.getElementById("formAutoSImp:anoimp").value="";
	document.getElementById("formAutoSImp:mesimp").value="";
}

function todos()
{  //Llamado por el boton limpiar
	document.getElementById("formAutoSImp:anio").value="%";
}

function imprimirPlanilla(rep, ficha){
	 window.open('http://opennomina.ecorrespondencia.com/enlacerh/frameset?__report=' + rep + '.rptdesign&__format=pdf' 
			 + '&ANIO='  
			 
			 + '&FICHA=' +
			 ficha
			 + "&__overwrite=true&__locale=es_ES&__svg=true&__designer=false");
	  
	}

