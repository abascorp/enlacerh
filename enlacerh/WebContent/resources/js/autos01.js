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
var retorno = false;


function limpiarInputsReg(){
	document.getElementById("pcodcia_input").value="";	
	limpiarInput('cedula');
	limpiarInput('coduser');
    limpiarInput('nbr');
    }




function borrar(){
	document.getElementById("pcodcia_input").value= ' ';
	document.getElementById("coduser").value= ' ';
	document.getElementById("cedula").value= '0';
	document.getElementById("nbr").value= ' ';
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
    	document.getElementById("param").value= arr;  
  }
   	setTimeout("document.getElementById('pcodcia_input').value=''",100);
	setTimeout("document.getElementById('coduser').value=''",100);
	setTimeout("document.getElementById('cedula').value=''",100);
	setTimeout("document.getElementById('nbr').value=''",100);
}


function limpiarReg()
{  //Llamado por el boton limpiar
	document.getElementById("pcodcia_input").value="";
	document.getElementById("cedula").value="";
	document.getElementById("coduser").value="";
	document.getElementById("clave").value="";
	document.getElementById("mail").value="";
	document.getElementById("nbr").value="";
	document.getElementById("total").value="";
}

function limpiarReg1()
{  //Llamado por el boton limpiar
	document.getElementById("coduser").value="";
	document.getElementById("clave").value="";
	document.getElementById("mail").value="";
	document.getElementById("nbr").value="";
	document.getElementById("tlf1").value="";
	document.getElementById("tlf2").value="";
	document.getElementById("pais").value="";
	document.getElementById("cdad").value="";
	document.getElementById("org").value="";
}


function enviar(vT0,vT1,vT2,vT3,vT4,vT5){
	  document.getElementById("pcodcia_input").value= rTrim(vT0);
	  document.getElementById("coduser").value= rTrim(vT1);
	  document.getElementById("cedula").value= rTrim(vT2);
	  document.getElementById("nbr").value= rTrim(vT3);	  
	  document.getElementById("rol_input").value= rTrim(vT4);
	  updateInput("coduser", "#F2F2F2");
	}

function enviar1(vT1,vT2){
	  document.getElementById("formAutos01a:pcodcia_input").value= " ";
	  document.getElementById("formAutos01a:cedula").value= vT2;
	  document.getElementById("formAutos01a:nombre").value= " ";
	  document.getElementById("formAutos01a:apellido").value= " ";	  
	  document.getElementById("formAutos01a:mail").value= rTrim(vT1);
	}

/******************************************************************************
 *********************Función randomnum para no usar captcha************
******************************************************************************/

	function randomnum(){
	var number1 = 5;
	var number2 = 15;
	var randomnum = (parseInt(number2) - parseInt(number1)) + 1;
	var rand1 = Math.floor(Math.random()*randomnum)+parseInt(number1);
	var rand2 = Math.floor(Math.random()*randomnum)+parseInt(number1);
	$(".rand1").html(rand1);
	$(".rand2").html(rand2);
	}
	
	$(document).ready(function(){
	$(".re").click(function(){
	randomnum();
	});
	

	randomnum();
	});
	
    function valida(vT1){
    	var total=parseInt($('.rand1').html())+parseInt($('.rand2').html());
    	var total1=$('#total').val();
    	if(total!=total1)
    	{
    	//alert("Wrong Calculation Entered");
    	randomnum();
    	retorno = false;
    	document.getElementById("captcha").value= '1';
    	}
    	else
    	{
    	//alert("Calculation Entered Correctly");
    	retorno = true;
    	document.getElementById("captcha").value= vT1;
    	}
    }
