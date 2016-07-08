	/*
	 * Funciones estandart que pueden ser utilizadas en todas las páginas
	 * 
	 */
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
	
	//Crea el foco en el input usuario, al cargar la página
	function inicio(){
	    	var ie=(document.all)? true:false;
	    	if (ie){
	    		bar.show();
	    	}
	    	
	    	var cadena = document.URL;
	    	
	   	
	    	if(!/opennomina.com/.test(cadena)){
	    		//bar1.show();
	    	}
	    	 
	
	}
	
	
	function utf8_decode( string ) {
		return decodeURIComponent( escape( string ) );
		}
	
	
	
	/******************************************************************************
	 ******************************************************************************/
	
	function lTrim(sStr){
	    while (sStr.charAt(0) == " ")
	        sStr = sStr.substr(1, sStr.length - 1);
	    return sStr;
	}
	
	
	/******************************************************************************
	  *********************Funciones genéricas************
	 ******************************************************************************/
	function limpiarInput(vtId){
	    if(vtId != "")
	        document.getElementById(vtId).value = "";
	}
	
	function limpiarMensaje(vtId){
	    if(vtId != "")
	        document.getElementById(vtId).innerHTML = "";
	    document.getElementById(vtId).className = "";
	}
	
	
	
	function rTrim(sStr){
	    while (sStr.charAt(sStr.length - 1) == " ")
	        sStr = sStr.substr(0, sStr.length - 1);
	    return sStr;
	}
	
	
	
	function cursor(){ 
	    document.body.style.cursor = "pointer"; 
	} 
	
	function uncursor(){ 
	    document.body.style.cursor = ""; 
	} 
	
	function info(){
	    // Si el mensaje que retorna es acceso
	    window.open('../jsf/acercade.jsp','target_blank','height=190,width=550','top=100,resizable=false,scrollbars=no,toolbar=no,status=no');
	
	}
	
	function wiki(){
	    // Si el mensaje que retorna es acceso
	    window.open('wiki/wikinomina.php');
	
	}
	
	function fm_check(vTcheck){
	
		var chkBoxes = $('input[name='+vTcheck+']');
	    chkBoxes.prop("checked", !chkBoxes.prop("checked"));
	}
	
	
	function imprimir(rep, usuario, grupo, locale, cia, jndi){
		  // Si el mensaje que retorna es acceso
			window.open('../jsf/reportes.jsp?reporte='+rep+'.rptdesign'
					+ "&usuario=" + usuario 
					+ "&grupo=" + grupo
					+ "&locale=" + locale
					+ "&cia=" + cia
					+ "&jndi=" + jndi);
		}
	
	//MUestra menú
	function loadMnu(){
		jQuery(document).bind('keydown', 'down', function(e) {
			PF('mnu1').show();slide();
			});
	}
	
	//Esconde menú
	function unloadMnu(){
		jQuery(document).bind('keydown', 'up', function(e) {
			PF('mnu1').hide();slideOff();
			});
	}
	
	
	 
	function slide(){
		jQuery(document).ready(function(){
	    	jQuery(document).ready(function () {
	    		jQuery('#display').fadeIn(1200);
	        });
	    });
		jQuery('.targetDiv').hide();
	}
	
	function slideOff(){
		document.getElementById('display').style.display='none';
	}
	
	function mnuBasicos(){
		jQuery(document).ready(function(){
	    	jQuery(document).ready(function () {
	    		jQuery('#mnuBasicos').fadeIn(500);
	        });
	    });
	}
	
	function mnuNomina(){
		jQuery(document).ready(function(){
	    	jQuery(document).ready(function () {
	    		jQuery('#mnuNomina').fadeIn(500);
	        });
	    });
	}
	
	function mnuAutoservicio(){
		jQuery(document).ready(function(){
	    	jQuery(document).ready(function () {
	    		jQuery('#mnuAutosrv').fadeIn(500);
	        });
	    });
	}
	
	function mnuMiscelaneos(){
		jQuery(document).ready(function(){
	    	jQuery(document).ready(function () {
	    		jQuery('#mnuMisc').fadeIn(500);
	        });
	    });
	}
	
	function mnuOcupacional(){
		jQuery(document).ready(function(){
	    	jQuery(document).ready(function () {
	    		jQuery('#mnuOcupacional').fadeIn(500);
	        });
	    });
	}
	
	function mnuReportes(){
		jQuery(document).ready(function(){
	    	jQuery(document).ready(function () {
	    		jQuery('#mnuRep').fadeIn(500);
	        });
	    });
	}
	
	
	function mnuSeguridad(){
		jQuery(document).ready(function(){
	    	jQuery(document).ready(function () {
	    		jQuery('#mnuSeg').fadeIn(500);
	        });
	    });
	}
	
	function mnuPersonas(){
		jQuery(document).ready(function(){
	    	jQuery(document).ready(function () {
	    		jQuery('#mnuPersonas').fadeIn(500);
	        });
	    });
	}
	
	function doc1(){
		PF('mnu1').show();slide();	
		jQuery('.doc1').hide();
		jQuery('.doc2').show();
	}
	
	function doc2(){
		PF('mnu1').hide();slideOff();
		jQuery('.doc1').show();
		jQuery('.doc2').hide();
	}
	
	function doc3(){
		
		jQuery('.doc1').show();
		jQuery('.doc2').hide();
		//PF('L1').toggle('west');
	}
	
	function updateInput(vinputid, vcolor){
		document.getElementById(vinputid).style.backgroundColor = vcolor;
		document.getElementById(vinputid).readOnly = true;
	}
	
	function clearUpdateInput(vinputid, vcolor){
		document.getElementById(vinputid).style.backgroundColor = vcolor;
		document.getElementById(vinputid).readOnly = false;
	}
	
	function upload(){
	    jQuery(document).ready(function(){
	    	jQuery(document).ready(function () {
	    		jQuery('#upload').fadeIn(1200);
	        });
	    });
	}
	
	function uploadC(){
		document.getElementById("upload").style.display= "none";
	}
	
	function modal(vT0){
		if(vT0=="1"){
		$( document ).ready( function() {
		    $( '#myModal' ).modal( 'toggle' );
		});
	}
	}
	
	function dismissModal(){
		$( document ).ready( function() {
		    $( '#myModal' ).modal( 'hide' );
		});
	}
	
