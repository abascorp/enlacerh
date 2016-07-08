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



function limpiar()
{  //Llamado por el boton limpiar
	limpiarInput('formAutoext:anio');
	document.getElementById("formAutoext:mes").value="";
    document.getElementById("formAutoext:lista").value="";
    jQuery('#toMail').find(':checked').each(function() {
    	   $(this).removeAttr('checked');
    	});
}

function borrar(){
	document.getElementById("formAutoext:anio").value= "1";
	document.getElementById("formAutoext:mes").value= "1";
	document.getElementById("formAutoext:lista").value= "N";

	
    jQuery.fn.getCheckboxValues = function(){
        var values = [];
        var i = 0;
        this.each(function(){
            // guarda los valores en un array
            values[i++] = "'"+$(this).val()+"'";
        });
        // devuelve un array con los checkboxes seleccionados
        return values;
    }
    var arr = $("input:checked").getCheckboxValues();
   
       if(arr==""){
    	   
       }  else {   
       	// Limpiamos valores   
       	document.getElementById("formAutoext:param").value= arr;   
       	
  }
        setTimeout("document.getElementById('formAutoext:anio').value=''",150);
      	setTimeout("document.getElementById('formAutoext:mes').value=''",150);  
      	setTimeout("document.getElementById('formAutoext:lista').value=''",150);

}

function back(){
	document.getElementById("formAutoext:anio").value= "1";
	document.getElementById("formAutoext:mes").value= "1";
	document.getElementById("formAutoext:lista").value= "N";
	
	setTimeout("document.getElementById('formAutoext:anio').value= ''",100);
	setTimeout("document.getElementById('formAutoext:mes').value= ''",100);
	setTimeout("document.getElementById('formAutoext:lista').value= ''",100);
	
}

function showdt(){
	document.getElementById("datatable").style.display= "";
	document.getElementById("formAutoext:btnlinkShow").style.display= "none";
	document.getElementById("formAutoext:btnlinkHide").style.display= "";
	
}

function hidedt(){
	document.getElementById("datatable").style.display= "none";
	document.getElementById("formAutoext:btnlinkShow").style.display= "";
	document.getElementById("formAutoext:btnlinkHide").style.display= "none";
}

function showdt1(){
	document.getElementById("upload").style.display= "";
	document.getElementById("formAutoext:btnlinkShow1").style.display= "none";
	document.getElementById("formAutoext:btnlinkHide1").style.display= "";
	
}

function hidedt1(){
	document.getElementById("upload").style.display= "none";
	document.getElementById("formAutoext:btnlinkShow1").style.display= "";
	document.getElementById("formAutoext:btnlinkHide1").style.display= "none";
}

