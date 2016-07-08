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




function enviar(vT0,vT1,vT2,vT3){
	  document.getElementById("formSeg002:codrol").value= rTrim(vT0);
	  document.getElementById("formSeg002:desrol").value= rTrim(vT1);
	  document.getElementById("formSeg002:vop").value= rTrim(vT2);
	  document.getElementById("formSeg002:grupo").value= rTrim(vT3);
	  updateInput("formSeg002:codrol", "#F2F2F2");
	}

