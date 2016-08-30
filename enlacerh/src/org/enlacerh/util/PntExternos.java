/*
 *  Copyright (C) 2011  ANDRES DOMINGUEZ

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

/*
 * Clase para leer archivos externos e insertar registros en las tablas
 * permitirá disminuir el trabajo de carga manual en las tablas, ya que
 * el sistema está diseñado para alimentarse de sistemas de nóminas ya
 * existentes.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


/**
 *
 * @author Andres
 */
public class PntExternos extends Utils implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Declaración de variables
	FacesMessage msj = null; 
    int cuenta = 0;
    int i;

//Constructor

    public PntExternos() {
    }

    /**Método genérico para lectura de archivos externos, te arma la sentencia
     * para cargar registros externos. Genérico para todas las tablas.
     * <p>
     * Parámetros:
     * <p>
     * El path donde está guardado el txt y el nombre de la tabla.
     * @param externos: ruta donde está ubicado el archivo
     * @param archivo: nombre del archivo, debe ser txt
     * @param tabla: tabla a procesar
     * @param pool: pool de conexiones JNDI()
     */
     public void pntExt(String externos, String archivo, String tabla, String pool) throws InterruptedException {    	 
       try {
    	int hilos = Integer.parseInt(THREADNUMBER);
    	ExecutorService ex = Executors.newFixedThreadPool(hilos);   //Número de hilos a usar para el insert
    	
        FileReader fr = new FileReader(externos + File.separator + archivo + ".txt");
        BufferedReader bf = new BufferedReader(fr);
        //System.out.println("ARCHIVO: " + externos + File.separator + archivo + ".txt");
        String entrada;

        String[] st = new String[3];
        st[0] = "INSERT INTO " + tabla + " VALUES (";
        st[1] = ")";
            //Valida si el archivo tiene una próxima linea
            while ((entrada = bf.readLine()) != null) {
                cuenta++;
                //Corre a la siguiente lí­nea
               // System.out.println(entrada);
                st[2] = st[0] + entrada +  st[1];
                
                
                QueryGenericThread th;
                th = new QueryGenericThread(pool, st[2]); //Insert Generic                
                ex.execute(th);
                Thread.sleep(10);
                         
                
                if (cuenta <= 1){
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("pntexReg"), "");
                }else{
                	msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("pntexRegs"), "");
                }
            }
            cuenta = 0;
            ex.shutdown();
            bf.close();
       } catch (IOException e){ // Dispara una exepción si no consigue el archivo y entrega mensaje
           //e.printStackTrace();
           msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("pntextFail") + " " + archivo + getMessage("pntextFail1"), "");
       }
       FacesContext.getCurrentInstance().addMessage(null, msj);
	    
    }
     
     
     
     public void pntExtThread(String externos, String archivo, String tabla, String pool)  {
         try {
          int hilos = Integer.parseInt(THREADNUMBER);	 
          ExecutorService ex = Executors.newFixedThreadPool(hilos);   //Número de hilos a usar para el insert
          
          FileReader fr = new FileReader(externos + File.separator + archivo + ".txt");
          BufferedReader bf = new BufferedReader(fr);
          //System.out.println("ARCHIVO: " + externos + File.separator + archivo + ".txt");
          String entrada;

          String[] st = new String[3];
          st[0] = "INSERT INTO " + tabla + " VALUES (";
          st[1] = ")";
              //Valida si el archivo tiene una próxima linea
          while ((entrada = bf.readLine()) != null) {
              cuenta++;
              //Corre a la siguiente lí­nea
             // System.out.println(entrada);
              st[2] = st[0] + entrada +  st[1];
              
              
              QueryGenericThread th;
              th = new QueryGenericThread(pool, st[2]); //Insert Generic
              ex.execute(th);
              
                           
          }
              ex.shutdown();
              cuenta = 0;
              bf.close();
         } catch (IOException e){ // Dispara una exepción si no consigue el archivo y entrega mensaje
             //e.printStackTrace();
             //msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, getMessage("pntextFail") + " " + archivo + getMessage("pntextFail1"), "");
         }
         //FacesContext.getCurrentInstance().addMessage(null, msj);
  	    
      }



//public static void main (String []arg) throws IOException, NamingException{
//PntExternos a = new PntExternos();
//a.pntExt("/home/andres/Descargas", "I201204", "AUTOS05", "");
//System.out.println(a.getMsj());
//}

}
