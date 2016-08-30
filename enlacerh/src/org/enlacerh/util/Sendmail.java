package org.enlacerh.util;


import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

@ManagedBean
@ViewScoped
public class Sendmail extends Utils  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sendmail() {

	}


	FacesMessage msj = null; 
	PntGenerica consulta = new PntGenerica();
	private String usuario = "";

	/**
	 * @return the usuario
	 */
	public String getusuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 *            the usuario to set
	 */
	public void setusuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Método que envía clave a los usuario que la olvidaron
	 * **/
	public void enviarClave() {
			try {
				Context initContext = new InitialContext();     
		    	Session session = (Session) initContext.lookup(JNDIMAIL);
		    	
		        
				// Validamos usuario
				Seg003forLogin seg = new Seg003forLogin(); // Crea objeto para el login
				Registro reg = new Registro();
				String[][] tabla = null;
				int rows = 0;
				// LLama al método que retorna el usuario y la contraseña
				seg.selectLogin(usuario, JNDI, "autos01");
				tabla = seg.getArray();
				rows = seg.getRows();
				reg.actCl(usuario, rows);

				if (rows == 0) {
					msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, getMessage("noreg"), "");
					usuario = "";
				}
				

				if (rows > 0) {
										
					String vLmail = tabla[0][2].toString();
					// Crear el mensaje a enviar
					MimeMessage mm = new MimeMessage(session);

					// Establecer las direcciones a las que será enviado
					// el mensaje (test2@gmail.com y test3@gmail.com en copia
					// oculta)
					// mm.setFrom(new
					// InternetAddress("opennomina@dvconsultores.com"));
					mm.addRecipient(Message.RecipientType.TO,
							new InternetAddress(vLmail));

					// Establecer el contenido del mensaje
					mm.setSubject(getMessage("mailSubjetc"));
					mm.setText(getMessage("mailContent") + " " 
							+ reg.getRandomKey());

					// Enviar el correo electrónico
					Transport.send(mm);
					msj = new FacesMessage(FacesMessage.SEVERITY_WARN, getMessage("mailExito"), "");
					//System.out.println("Correo enviado exitosamente a:" + vLmail);
				}
			} catch (Exception e) {
				msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
				e.printStackTrace();
			}
		FacesContext.getCurrentInstance().addMessage(null, msj);
	}
	
	
	/**
	 * Método que envía clave a los usuario que la olvidaron
	 * **/
	public void enviarRandomkey(String pmail, String prandomkey) {
		Autos02 jndigroup = new Autos02();
			try {
				Context initContext = new InitialContext();     
		    	Session session = (Session) initContext.lookup(jndigroup.jndimail());

					// Crear el mensaje a enviar
					MimeMessage mm = new MimeMessage(session);

					// Establecer las direcciones a las que será enviado
					// el mensaje (test2@gmail.com y test3@gmail.com en copia
					// oculta)
					// mm.setFrom(new
					// InternetAddress("opennomina@dvconsultores.com"));
					mm.addRecipient(Message.RecipientType.TO,
							new InternetAddress(pmail));
					//System.out.println("Correo enviado exitosamente a:" + pmail);

					// Establecer el contenido del mensaje
					mm.setSubject(getMessage("autos01mailT"));
					//mm.setText(getMessage("mailContentRandomkey") + " " 
						//	+ prandomkey, "text/html; charset=utf-8");
					mm.setContent(getMessage("mailContentRandomkey") + " " 
							+ prandomkey, "text/html; charset=utf-8");

					// Enviar el correo electrónico
					Transport.send(mm);
					msj = new FacesMessage(FacesMessage.SEVERITY_INFO, getMessage("autos01mail"), "");
					//System.out.println("Correo enviado exitosamente a:" + vLmail);
				
			} catch (Exception e) {
				msj = new FacesMessage(FacesMessage.SEVERITY_FATAL, e.getMessage(), "");
				e.printStackTrace();
			}
		FacesContext.getCurrentInstance().addMessage(null, msj);
	}
	
	

}
