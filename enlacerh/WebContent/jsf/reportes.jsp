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
response.sendRedirect("/enlacerh/faces/login.xhtml");

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
        String usuario = request.getParameter("usuario");
        String grupo = request.getParameter("grupo");
        String locale = request.getParameter("locale");
        String cia = request.getParameter("cia");
        String jndi = request.getParameter("jndi");
        if (captura!=null){
         rep =  captura;
        }

        %>
        <birt:viewer id='birtViewer' reportDesign= "<%=rep%>" isHostPage="true" format="pdf">
           <birt:param  name="USER" value="<%=usuario%>"  ></birt:param>
           <birt:param  name="GRUPO" value="<%=grupo%>"  ></birt:param>
           <birt:param  name="LOCALE" value="<%=locale%>"  ></birt:param>
           <birt:param  name="CIA" value="<%=cia%>"  ></birt:param>
           <birt:param  name="jndi" value="<%=jndi%>"  ></birt:param>
        </birt:viewer>
    </body>
</html>
