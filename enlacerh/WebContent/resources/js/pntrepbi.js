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


function limpiar(){
    limpiarInput('formPntrepbi:codrep');
    limpiarInput('formPntrepbi:desrep');
    limpiarInput('formPntrepbi:comrep');
}



function borrar(){
	document.getElementById("formPntrepbi:codrep").value= "1";
	document.getElementById("formPntrepbi:desrep").value= "1";

       setTimeout("document.getElementById('formPntrepbi:codrep').value=''",100);
   	setTimeout("document.getElementById('formPntrepbi:desrep').value=''",100);  
}

function log(){
	displayTime();
	
	document.getElementById("formPntrepbi:codrep").value= "1";
	document.getElementById("formPntrepbi:desrep").value= "1";

       setTimeout("document.getElementById('formPntrepbi:codrep').value=''",100);
   	setTimeout("document.getElementById('formPntrepbi:desrep').value=''",100);  
}


function enviar(vT0,vT1,vT2,vT3,vT4){
	  document.getElementById("formPntrepbi:codrep").value= rTrim(vT0);
	  document.getElementById("formPntrepbi:desrep").value= rTrim(vT1);
	  document.getElementById("formPntrepbi:comrep").value= rTrim(vT2);
	  document.getElementById("formPntrepbi:vop").value= rTrim(vT3);
	  document.getElementById("formPntrepbi:grupo").value= rTrim(vT4);
	}

//Muestra la hora del lado del cliente
function displayTime()
   {
       var localTime = new Date();
       var month= localTime.getMonth() +1; 
       var cadena = month.toString();
       var hora = localTime.getHours();
       var minutos = localTime.getMinutes();
       var segundos = localTime.getSeconds();
       
       if(cadena.length==1){
       	month = '0'+month;
       }
       
       var fechaHora = localTime.getDate()+"/"+month+"/"+localTime.getFullYear()+" "+hora+":"+minutos+":"+segundos;
       document.getElementById("formPntrepbi:fechaU").value= fechaHora;
   } 

function formatAMPM(date) {
	  var hours = date.getHours();
	  var minutes = date.getMinutes();
	  var ampm = hours >= 12 ? 'PM' : 'AM';
	  hours = hours % 12;
	  hours = hours ? hours : 12; // the hour '0' should be '12'
	  minutes = minutes < 10 ? '0'+minutes : minutes;
	  var strTime = hours + ':' + minutes + ' ' + ampm;
	  return strTime;
	}