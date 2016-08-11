var milisec = 120000;

function limpiar()
{  //Llamado por el boton limpiar
	document.getElementById("formAutoSImp:anoimp").value="";
	document.getElementById("formAutoSImp:mesimp").value="";
}

function todos()
{  //Llamado por el boton limpiar
	document.getElementById("formAutoSImp:anoimp").value="";
}


function imprimirPlanillaARC(rep, ficha){
	  // Si el mensaje que retorna es acceso
		window.open('../jsf/reportesauto.jsp?reporte='+rep+'.rptdesign&ficha='+ficha);

	}

