/*
 *  Copyright (C) 2016 ABAS CORP, C.A.

    Este programa es software libre: usted puede redistribuirlo y/o modificarlo 
    bajo los terminos de la Licencia Pública General GNU publicada 
    por la Fundacion para el Software Libre, ya sea la version 3 
    de la Licencia, o (a su eleccion) cualquier version posterior.

    Este programa se distribuye con la esperanza de que sea útil, pero 
    SIN GARANTiA ALGUNA; ni siquiera la garantia implicita 
    MERCANTIL o de APTITUD PARA UN PROPoSITO DETERMINADO. 
    Consulte los detalles de la Licencia Pública General GNU para obtener 
    una informacion mas detallada. 

    Deberia haber recibido una copia de la Licencia Pública General GNU 
    junto a este programa. 
    En caso contrario, consulte <http://www.gnu.org/licenses/>.
 */
package org.enlacerh.util;

import java.io.Serializable;

import javax.naming.NamingException;

/**
 *
 * @author Andres
 */
public class Accmnu extends Utils implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PntGenerica consulta = new PntGenerica();

    public Accmnu() {
    }

    /**
     * Retorna si el usuario tiene acceso o no a insertar modificar o eliminar en una tabla.
     * @return
     * False o true.
     * @param 
     *<p> Parametros del Metodo:<br> <b>String numopc:</b>
     * Esta opcion permite seleccionar el número de opcion del menú de la aplicacion.
     * el orden del menú es el que se observa en la pagina de la aplicacion, iniciando con paises como opcion 1.
     * <p><b>String acc:</b> Se coloca la opcion insert, update o delete, para que retorne si tiene acceso o no
     * <p><b>String user:</b> Se coloca el usuario para seleccionar en la consulta cual es el rol del usuario a consultar
     * <p><b>String pool:</b> Coneccion JNDI(). <br><br>
     * <b>Ejemplo:</b> valAccmnu("bas1","insert","admin","jdbc/opennomina").<br><br>
     * Si el usuario no tiene ninguna limitacion 0, de lo contrario 1.
     **/
    public boolean valAccmnu(String numopc, String acc, String user, String pool) throws NamingException {
        boolean vLresult = false; //Valor de retorno de la funcion
        String opc = "acc"; // Accion a concatenar

        //Seleccionamos el rol al que pertenece el usuario
        consulta.selectPntGenerica("Select trim(codrol) from autos01 where coduser = '" + user.toUpperCase() + "'", pool);
        String[][] rol = consulta.getArray();
        //
        //Hacemos la consulta del número de opcion del menú para que retorne 0, 1 aunque el usuario no tenga
        //definido ninguna opcion en su rol.
        consulta.selectPntGenerica("Select trim(" + opc + acc + ") from seg006 where p_codrol = '" + rol[0][0] + "' and numopc = '" + numopc + "'", pool);
        String[][] result = consulta.getArray();
        int rows = consulta.getRows();
        if (rows > 0 && result[0][0].equals("1")) {
            vLresult = true;
        }
        return vLresult;
    }

}



