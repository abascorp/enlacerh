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

var milisec = 120000;


function limpiar(){
    limpiarInput('formSeg001:grupo');
    limpiarInput('formSeg001:empresa');
    limpiarInput('formSeg001:jndi');
    limpiarInput('formSeg001:fecven_input');

    document.getElementById("formSeg001:canttrab").value= "25";
    document.getElementById("formSeg001:estatus").value= "0";
}



function borrar(){
	document.getElementById("formSeg001:grupo").value= " ";
	document.getElementById("formSeg001:fecven_input").value= "09/09/9999";
	document.getElementById("formSeg001:jndi").value= " ";

    setTimeout("document.getElementById('formSeg001:grupo').value=''",100);
   	setTimeout("document.getElementById('formSeg001:fecven_input').value=''",100);  
   	setTimeout("document.getElementById('formSeg001:jndi').value=''",100);
}


function enviar(vT0,vT1,vT2,vT3,vT4,vT5){
	  document.getElementById("formSeg001:grupo").value= rTrim(vT0);
	  document.getElementById("formSeg001:canttrab").value= rTrim(vT1);
	  document.getElementById("formSeg001:fecven_input").value= rTrim(vT2);
	  document.getElementById("formSeg001:estatus").value= rTrim(vT3);
	  document.getElementById("formSeg001:empresa").value= rTrim(vT4);
	  document.getElementById("formSeg001:jndi").value= rTrim(vT5);
	  updateInput("formSeg001:grupo", "#F2F2F2");
	}

