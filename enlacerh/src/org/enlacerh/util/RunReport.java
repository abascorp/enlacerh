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

import java.io.Serializable;
import java.util.HashMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


import org.eclipse.birt.report.engine.api.*;

public class RunReport implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Constructor
	public RunReport(){
		
	}

	FacesMessage msj = null; 
	
	/**
     * Este método utiliza el BIRT engine para generar reportes
	 * desde el API utilizando POJO.
     * @param reporte: Nombre del reporte que esá leyendo, debe ser .rptdesing
	 * @parama format: Formato de salida: HTML, PDF, DOC
	 * @param ubicacionrep: Ubicación del reporte
	 * @param rutasalida: Salida del reporte ubicación en disco
	 * @param nbrreporte: nombre del reporte al momento de la salida
	 * @param nbrreporte: nombre del reporte al momento de la salida
	 * @param anio
	 * @param mes
	 * @param periodo
	 * @param ficha
     **/ 
	public void outReporteRecibo(String reporte, String format, String ubicacionrep
			, String rutasalida, String nbrreporte, String anio, String mes, String periodo, String ficha, String JNDI, String id){
		
	  //Variables used to control BIRT Engine instance
  	  //Now, setup the BIRT engine configuration. The Engine Home is hardcoded
  	  EngineConfig config = new EngineConfig( );
  	  //Create new Report engine based off of the configuration
      ReportEngine engine = new ReportEngine( config );
      IReportRunnable report = null;
      IRunAndRenderTask task;
      IRenderOption options = new RenderOption();     
      final HashMap<String, String> PARAMANIO = new HashMap<String, String>();
      final HashMap<String, String> PARAMMES = new HashMap<String, String>();
      final HashMap<String, String> PARAMPER = new HashMap<String, String>();
      final HashMap<String, String> PARAMFICHA = new HashMap<String, String>();
      final HashMap<String, String> PARAMJNDI = new HashMap<String, String>();
      final HashMap<String, String> PARAMID = new HashMap<String, String>();
      
      
      PARAMANIO.put("ANIO", anio);
      PARAMMES.put("MES", mes);
      PARAMPER.put("PERIODO", periodo);
      PARAMFICHA.put("FICHA", ficha);
      PARAMJNDI.put("JNDI()", JNDI);
      PARAMID.put("ID", id);
 
        
        //With our new engine, lets try to open the report design
        try
        {
      	  report = engine.openReportDesign( ubicacionrep + "/" + reporte + ".rptdesign"); 

        }
        catch (Exception e)
        {
        	 msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
			 FacesContext.getCurrentInstance().addMessage(null, msj);
             System.exit(-1);
        }
        
        //With the file open, create the Run and Render task to run the report
        task = engine.createRunAndRenderTask(report);
          

        //This will set the output file location, the format to render to, and
        //apply to the task
        options.setOutputFormat(format);
        options.setOutputFileName( rutasalida + "/" + nbrreporte + "." + format);
        
       
        task.setRenderOption(options);
        
        try
        {
        	 
        	 task.setParameterValues(PARAMANIO);
        	 task.setParameterValues(PARAMMES);
        	 task.setParameterValues(PARAMPER);
        	 task.setParameterValues(PARAMFICHA);
        	 task.setParameterValues(PARAMID);
        	 task.validateParameters();
             task.run();
        }
        catch (Exception e)
        {
        	 msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "");
			 FacesContext.getCurrentInstance().addMessage(null, msj);
             e.printStackTrace();
             //System.exit(-1);
        }
        
        //Yeah, we finished. Now destroy the engine and let the garbage collector
        //do its thing
        //System.out.println("Reporte ejecutado");
        engine.destroy();
        task.close();
   }
	   
}
