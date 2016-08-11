var milisec = 120000;

function limpiar()
{  //Llamado por el boton limpiar
	document.getElementById("formAutoSUtil:anio").value="";
	document.getElementById("formAutoSUtil:mes").value="";
}

function todos()
{  //Llamado por el boton limpiar
	document.getElementById("formAutoSUtil:anio").value="";
}

function imprimirSolicitud(rep, ficha){
	  // Si el mensaje que retorna es acceso
		window.open('../jsf/reportesauto.jsp?reporte='+rep+'.rptdesign&ficha='+ficha);

	}


