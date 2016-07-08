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


function limpiar(){
	document.getElementById("formSeg003:tabViewR:coduser").value= "";
	document.getElementById("formSeg003:tabViewR:desuser").value= "";
	document.getElementById("formSeg003:tabViewR:cluser").value= "";
	document.getElementById("formSeg003:tabViewR:mail").value= "";
	document.getElementById("formSeg003:tabViewR:pcodrol_input").value= "";
	document.getElementById("formSeg003:vop").value="0";
	document.getElementById("formSeg003:tabViewR:tlf1").value= "";
	document.getElementById("formSeg003:tabViewR:tlf2").value= "";
	document.getElementById("formSeg003:tabViewR:pais").value= "";
	document.getElementById("formSeg003:tabViewR:cdad").value= "";
	
}


function borrar(){
	document.getElementById("formSeg003:tabViewR:coduser").value= " ";
	document.getElementById("formSeg003:tabViewR:desuser").value= " ";
	document.getElementById("formSeg003:tabViewR:cluser").value= "12345";
	document.getElementById("formSeg003:tabViewR:mail").value= "a@a.com";
	document.getElementById("formSeg003:tabViewR:pais").value= " ";
	document.getElementById("formSeg003:tabViewR:cdad").value= " ";
	
    setTimeout("document.getElementById('formSeg003:tabViewR:coduser').value=''",100);
   	setTimeout("document.getElementById('formSeg003:tabViewR:desuser').value=''",100);  
   	setTimeout("document.getElementById('formSeg003:tabViewR:cluser').value=''",100);
   	setTimeout("document.getElementById('formSeg003:tabViewR:mail').value=''",100);
   	setTimeout("document.getElementById('formSeg003:tabViewR:pais').value=''",100);
   	setTimeout("document.getElementById('formSeg003:tabViewR:cdad').value=''",100);
}


function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6,vT7, root,vT8,vT9,vT10,vT11){
	  document.getElementById("formSeg003:tabViewR:coduser").value= rTrim(vT0);
	  document.getElementById("formSeg003:tabViewR:desuser").value= rTrim(vT1);
	  document.getElementById("formSeg003:tabViewR:cluser").value= rTrim(vT2);
	  document.getElementById("formSeg003:tabViewR:pcodrol_input").value= rTrim(vT3);
	  document.getElementById("formSeg003:tabViewR:mail").value= rTrim(vT4);
	  document.getElementById("formSeg003:vop").value= rTrim(vT5);
	  if(root=="true"){
	  document.getElementById("formSeg003:tabViewR:tipousr").value= rTrim(vT6);
	  }
	  document.getElementById("formSeg003:grupo").value= rTrim(vT7);
	  document.getElementById("formSeg003:tabViewR:tlf1").value= rTrim(vT8);
	  document.getElementById("formSeg003:tabViewR:tlf2").value= rTrim(vT9);
	  document.getElementById("formSeg003:tabViewR:pais").value= rTrim(vT10);
	  document.getElementById("formSeg003:tabViewR:cdad").value= rTrim(vT11);
	}

