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



function enviar(vT0,vT1,vT2,vT3,vT4,vT5,vT6,vT7,vT8,vT9,vT10,vT11){
	  document.getElementById("formPnt001:tabView:codcia").value= rTrim(vT0);
	  document.getElementById("formPnt001:tabView:nomci1").value= rTrim(vT1);
	  document.getElementById("formPnt001:tabView:nomci2").value= rTrim(vT2);
	  document.getElementById("formPnt001:tabView:id1").value= rTrim(vT3);
	  document.getElementById("formPnt001:tabView:id2").value= rTrim(vT4);
	  document.getElementById("formPnt001:tabView:dir").value= rTrim(vT5);
	  document.getElementById("formPnt001:tabView:pcodpai_input").value= rTrim(vT6);
	  document.getElementById("formPnt001:tabView:pcodciu_input").value= rTrim(vT7);
	  document.getElementById("formPnt001:tabView:tlf1").value= rTrim(vT8);
	  document.getElementById("formPnt001:tabView:tlf2").value= rTrim(vT9);
	  document.getElementById("formPnt001:vop").value= rTrim(vT10);
	  document.getElementById("formPnt001:grupo").value= rTrim(vT11);
	  updateInput("formPnt001:tabView:codcia", "#F2F2F2");
}

