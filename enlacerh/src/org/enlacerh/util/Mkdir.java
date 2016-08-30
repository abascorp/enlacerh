/**
 * 
 */
package org.enlacerh.util;

import java.io.File;

/**
 * @author andres
 *Crea y/o elimina directorios
 */
public class Mkdir {
	
	public Mkdir(){
		
	}
	
    /**
     * Crea carpeta para directorio
     **/
    public void crtCarpeta(String ruta, String carpeta) {
        File vLcarpeta = new File(ruta + File.separator + carpeta);
        try {
            // A partir del objeto File creamos el fichero físicamente
            if (vLcarpeta.mkdir()) {
               // System.out.println("La carpeta se ha creado correctamente");
            } else {
               // System.out.println("No se ha creado la carpeta");
            }
        } catch (Exception e) {
        }

    }

     /**
     * Elimina carpeta para directorio
     **/
    public void borraCarpeta(String ruta, String carpeta) {
        File vLcarpeta = new File(ruta + File.separator + carpeta);
        try {
            // A partir del objeto File creamos el fichero físicamente
            if (vLcarpeta.delete()) {
               // System.out.println("La carpeta se ha eliminado correctamente");
            } else {
              //  System.out.println("No ha podido eliminar la carpeta");
            }
        } catch (Exception e) {
        }

    }

}
