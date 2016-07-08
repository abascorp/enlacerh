<%-- 
    Document   : 2
    Created on : 08/05/2009, 08:21:43 AM
    Author     : Andres
--%>

<%@ page session="true" language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/birt.tld" prefix="birt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-----------------------------------------------------------------------------
    Expected java beans
-----------------------------------------------------------------------------%>

<%-------------------------------------------------------------------------------------------
  Define variables de session.
--------------------------------------------------------------------------------------------%>
<%
boolean sesion = false;
HttpSession sesionOk = request.getSession();
if (sesionOk.getAttribute("usuario") == null) {
	sesion = false;
response.sendRedirect("/enlacerh/login.xhtml");

}//Fin valida que usuario no sea nulo
%>
<html>
    <head>
        <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
        
        <title></title>
    </head>
    <body>
        <%
        // Recoge parámetros para pasar a reportes BIRT
        String rep= null;
        String captura = request.getParameter("reporte");
        String anio = request.getParameter("anio");
        String mes = request.getParameter("mes");
        String periodo = request.getParameter("periodo");
        String ficha = request.getParameter("ficha");
        String fechadesde = request.getParameter("fechadesde");
        String fechahasta = request.getParameter("fechahasta");
        String codcto = request.getParameter("codcto");
        String id = request.getParameter("id");
        if (captura!=null){
         rep =  captura;
        }
        if (anio==null || anio==""){
            anio =  "%";
           }
        if (mes==null || mes=="" || mes=="S"){
            mes =  "%";
           }
        if (codcto==null || codcto==""){
            codcto =  "%";
           }
        %>
        <birt:viewer id='birtViewer' reportDesign= "<%=rep%>" isHostPage="true" format="pdf" >
           <birt:param  name="ANIO" value="<%=anio%>"  ></birt:param>
           <birt:param  name="MES" value="<%=mes%>"  ></birt:param>
           <birt:param  name="PERIODO" value="<%=periodo%>"  ></birt:param>
           <birt:param  name="FICHA" value="<%=ficha%>"  ></birt:param>
           <birt:param  name="FECHADESDE" value="<%=fechadesde%>"  ></birt:param>
           <birt:param  name="FECHAHASTA" value="<%=fechahasta%>"  ></birt:param>
           <birt:param  name="CONCEPTO" value="<%=codcto%>"  ></birt:param>
           <birt:param  name="ID" value="<%=id%>"  ></birt:param>
        </birt:viewer>
    </body>
</html>
