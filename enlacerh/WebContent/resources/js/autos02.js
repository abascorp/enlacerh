/*
 *  Copyright (C) 2011  ANDRES DOMINGUEZ

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
var vganio;
var vgmes;
var vgper;

function limpiar()
{  //Llamado por el boton limpiar
	document.getElementById("formAutoSNomina:anio").value="";
	document.getElementById("formAutoSNomina:mes").value="";
    document.getElementById("tablaresult").style.display= "";
	document.getElementById("upload").style.display= 'none';
	document.getElementById("tablaresultDet").style.display= 'none';

}


function fmper(periodo, id){
	document.getElementById("formAutoSNomina:per").value= periodo;
	document.getElementById("formAutoSNomina:id").value= id;
}

function mdet(anio, mes, periodo){
	document.getElementById("tablaresult").style.display= "none";
	jQuery(document).ready(function(){
    	jQuery(document).ready(function () {
    		jQuery('#tablaresultDet').fadeIn(1200);
        });
    });
	//setTimeout("document.getElementById('tablaresultDet').style.display= ''", 100);
	vganio = rTrim(anio);
	vgmes = rTrim(mes);
	vgper = rTrim(periodo);
}

function odet(){
	document.getElementById("tablaresult").style.display= "";
	document.getElementById("tablaresultDet").style.display= 'none';
}

function imprimirRecibo(rep, ficha, id){
 window.open('../jsf/reportesauto.jsp?reporte='+rep+'.rptdesign&anio=' + vganio + "&mes=" + vgmes + "&periodo=" + vgper + "&ficha=" + ficha + "&id=" + id);
}

function imprimirPermiso(rep, ficha){
	 window.open('../jsf/reportesauto.jsp?reporte='+rep+'.rptdesign&ficha=' + ficha);
	}


function imprimirConst(rep){
	
	var codcia = document.getElementById("formAutoSNomina:codcia").value;
	var ficha = document.getElementById("formAutoSNomina:ficha").value;
	var anio = document.getElementById("formAutoSNomina:anio").value;
	var mes = document.getElementById("formAutoSNomina:mes").value;
	var isact = document.getElementById("formAutoSNomina:isact").value;
	if(anio==""){
		$('#myModal1').modal('show');
	} else if (mes==""){
		$('#myModal1').modal('show');
	} else if (isact=="1"){
		$('#myModal2').modal('show');	
	} else {	
  // Si el mensaje que retorna es acceso
	window.open('../jsf/reportesauto.jsp?reporte='+codcia+rep+'.rptdesign&anio=' + anio + '&mes=' + mes + '&ficha=' + ficha);
	}
}
