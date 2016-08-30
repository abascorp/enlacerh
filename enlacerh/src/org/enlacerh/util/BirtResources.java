
package org.enlacerh.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Andres
 */
public class BirtResources  {
//Declaración de variables para manejo de mensajes multi idioma y país
    private String lenguaje = "es";
    private String pais = "VEN";
    private Locale  localidad = new Locale(lenguaje, pais);
    ResourceBundle BirtResources =  ResourceBundle.getBundle("org.enlacerh.util.MessagesBirt",localidad);

    private String Message = "";


      /**
     * Recursos de lenguaje. Archivos Properties. Integración con birt
     **/
    public  String getMessage(String mensaje) {
        Message = BirtResources.getString(mensaje);
        return Message;
    }

    public  String getHtmlMessage(String mensaje) {
        Message = BirtResources.getString(mensaje);
        return Message;
    }

     public  String getJavaScriptMessage(String mensaje) {
        Message = BirtResources.getString(mensaje);
        return Message;
    }

}
