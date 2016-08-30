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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

/**
 *
 * @author andres
 */
public class FileIO extends Utils {

	FacesMessage msj = null; 


    /**
    Lee un archivo de texto. Utilizado para query de nomina externa. Completa el query internamente con los parametros,
    de compañia, nomina y periodo.
    @param ruta Directorio de lectura del archivo
    @param archivo Nombre del archivo que desea leer
    @param cianom Compañia y tipo de nomina que va a pasar como parametro del query. Ej (011101)
    @param numper Número de periodo que va a pasar como parametro del query. Ej (201101)
    @return Retorna string completo query generado en el archivo .sql
     **/
    public static String readFile(String ruta, String archivo, String cianom, String numper) throws IOException {
        String lineSep = System.getProperty("line.separator");
        String queryResult = "";
        try {
            FileReader fr = new FileReader(ruta + File.separator + archivo + ".sql");
            @SuppressWarnings("resource")
			BufferedReader bf = new BufferedReader(fr);
            String nextLine = "";
            String paramQuery = "where codpai in (" + cianom + ")";
            StringBuilder sb = new StringBuilder();
            while ((nextLine = bf.readLine()) != null) {
                sb.append(nextLine);
                sb.append(lineSep);
            }
            queryResult = sb.toString() + paramQuery;
            //System.out.println(queryResult);
        } catch (Exception e) {
            //System.out.println("No existe el fichero " + archivo + ".sql. Súbalo al servidor");
        }
        return queryResult;
    }

    /**
    Genera un archivo de texto. Clase generica para crear archivos txt a partir de una consulta .sql.
    la lectura del txt es completada por los parametros en la clase readFile, los cuales son compañia y nomina (cianom)
    y periodo (numper).
    Una vez que lee del fichero, ejecuta una consulta utilizando la clase PntGenerica y devuelve el resultado en un arreglo.
    Para ello internamente se define una variable String[][] tabla. Se recorre la busqueda y se toma el valor de la tabla[i][0],
    posteriormente se genera un fichero txt con esos valores.
    @param ruta Directorio de lectura del archivo
    @param archivo Nombre del archivo que desea leer
    @param cianom Compañia y tipo de nomina que va a pasar como parametro del query. Ej (011101)
    @param numper Número de periodo que va a pasar como parametro del query. Ej (201101)
    @param nombrTxt Nombre de salida del fichero txt
    @param pool Pool de conexiones para la consulta generica
    @param opc Número de opcion para saber si archivo lleva encabezado o solo detalle. 0-detalle,1-encabezado y detalle, 
    2-encabezado-detalle-pie, 3-detalle y pie
     **/
    public void writeFile(String ruta, String archivo, String cianom, String numper, String nombreTxt, String pool, String opc) throws IOException, NamingException {
        //Creamos nuestro String que vamos a escribir en el fichero, para ello leemos el archivo .sql con el metodo (readFile)
        String myQuery = FileIO.readFile(ruta, archivo, cianom, numper);
        //Hacemos la consulta en la tabla
        PntGenerica pntGen = new PntGenerica();
        pntGen.selectPntGenerica(myQuery, pool);
        String[][] tabla = pntGen.getArray();
        int rows = pntGen.getRows();
        try {
            //Creamos un Nuevo objeto FileWriter dandole
            //como parametros la ruta y nombre del fichero            
            FileWriter fichero = new FileWriter(ruta + File.separator + nombreTxt + ".txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(ruta + File.separator + nombreTxt + ".txt", true));
            //Si existe encabezado lo leemos. Opcion 1. El txt lleva encabezado y detalle

            if (opc.equals("1")) {
                String myQueryEncab = FileIO.readFile(ruta, archivo + "_encab", cianom, numper);
                //Hacemos la consulta en la tabla
                PntGenerica pntGenEncab = new PntGenerica();
                pntGenEncab.selectPntGenerica(myQueryEncab, pool);
                String[][] tablaEncab = pntGenEncab.getArray();
                //Insertamos el texto creado y si trabajamos
                //en Windows terminaremos cada linea con "\r\n", recorriendo la consulta sql
                out.write(tablaEncab[0][0] + "\r\n");
                for (int i = 0; i < rows; i++) {
                    out.write(tabla[i][0] + "\r\n");

                } // fin del loop for

                //Opcion 2. El txt lleva encabezado, detalle y pie de pagina    
            } else if (opc.equals("2")) {
                String myQueryEncab = FileIO.readFile(ruta, archivo + "_encab", cianom, numper);
                //Hacemos la consulta en la tabla
                PntGenerica pntGenEncab = new PntGenerica();
                pntGenEncab.selectPntGenerica(myQueryEncab, pool);
                String[][] tablaEncab = pntGenEncab.getArray();
                //Pie
                String myQueryPie = FileIO.readFile(ruta, archivo + "_pie", cianom, numper);
                //Hacemos la consulta en la tabla
                PntGenerica pntGenPie = new PntGenerica();
                pntGenPie.selectPntGenerica(myQueryPie, pool);
                String[][] tablaPie = pntGenPie.getArray();
                //Insertamos el texto creado y si trabajamos
                //en Windows terminaremos cada linea con "\r\n", recorriendo la consulta sql
                out.write(tablaEncab[0][0] + "\r\n");
                for (int i = 0; i < rows; i++) {
                    out.write(tabla[i][0] + "\r\n");

                } // fin del loop for
                out.write(tablaPie[0][0] + "\r\n");
                //Opcion 3. El txt lleva detalle y pie de pagina    

            } else if (opc.equals("3")) {
                //Pie
                String myQueryPie = FileIO.readFile(ruta, archivo + "_pie", cianom, numper);
                //Hacemos la consulta en la tabla
                PntGenerica pntGenPie = new PntGenerica();
                pntGenPie.selectPntGenerica(myQueryPie, pool);
                String[][] tablaPie = pntGenPie.getArray();
                //Insertamos el texto creado y si trabajamos
                //en Windows terminaremos cada linea con "\r\n", recorriendo la consulta sql
                for (int i = 0; i < rows; i++) {
                    out.write(tabla[i][0] + "\r\n");

                } // fin del loop for
                out.write(tablaPie[0][0] + "\r\n");

            } else { //De lo contrario solo detalle...............
                //Insertamos el texto creado y si trabajamos
                //en Windows terminaremos cada linea con "\r\n", recorriendo la consulta sql
                for (int i = 0; i < rows; i++) {
                    fichero.write(tabla[i][0] + "\r\n");
                } // Fin del loop for
            }
            //cerramos el fichero
            out.close();
            fichero.close();
            //System.out.println("Archivo generado con exito en: " + ruta + ". " + rows + " registros");
            //msj = getMessage("FileIOExt") + ruta + ". " + rows + " " + getMessage("FileIOExt1");

        } catch (Exception e) {
           // System.out.println("No se pudo generar el archivo de texto, no existe la ruta");
            //msj = getMessage("FileIOErr");
        }
    }



    /**
     * Elimina fichero de un directorio
     **/
    public void borraFile(String ruta, String arhivo) {
        File vLarchivo = new File(ruta + File.separator + arhivo);
        try {
            // A partir del objeto File creamos el fichero físicamente
            if (vLarchivo.delete()) {
                //System.out.println("El archivo se ha eliminado correctamente");
            } else {
                //System.out.println("No ha podido eliminar archivo");
            }
        } catch (Exception e) {
        	msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
			FacesContext.getCurrentInstance().addMessage(null, msj);
        }

    }
    
    /**
     * Funcion que lee el contenido de la carpeta y retorna
     * cuantos megas tiene el contenido del directorio
     * @param Ruta dedirectorio
     * @return int
     **/
    public int sizeDir(File dir){
    	int sizefolder = 0;
    	File[] ficheros = dir.listFiles();
        if (ficheros == null){
             // System.out.println("No hay ficheros en el directorio especificado");
        } else {
              for (int i=0;i<ficheros.length;i++){
            	  //System.out.println(ficheros[i].length());
            	  //System.out.println(ficheros[i].getName());
            	  sizefolder = (int) (sizefolder + ficheros[i].length());
            }
             // System.out.println(sizefolder);
            }
        return sizefolder;
    }
    
    
}
