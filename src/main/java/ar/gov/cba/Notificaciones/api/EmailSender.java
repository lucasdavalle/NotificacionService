/*
 * Para cambiar este encabezado de licencia, elige Encabezados de Licencia en Propiedades del Proyecto.
 * Para cambiar este archivo de plantilla, elige Herramientas | Plantillas
 * y abre la plantilla en el editor.
 */
package ar.gov.cba.Notificaciones.api;

import ar.gov.cba.Notificaciones.model.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa el envío de correos electrónicos a través de un servidor
 * REST. Utiliza la implementación de NotificacionClient (NotificacionImplement)
 * para realizar la interacción con el servidor. También se encarga de la
 * generación del token de autenticación y la preparación de la solicitud de
 * notificación.
 *
 * @author 20432313582
 */
@Slf4j
@Service
public class EmailSender {

    @Autowired
    private JavaMailSender emailSender;

    /**
     * Método para enviar un correo electrónico.
     *
     * @param newNotificacion Objeto Notificacion que contiene la información
     * del correo.
     * @return true si el correo se envía correctamente, false en caso
     * contrario.
     */
    public boolean sendMail(Notificacion newNotificacion) throws MessagingException  {
        javax.mail.internet.MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(newNotificacion.getDestinatario());
            helper.setSubject(newNotificacion.getAsunto());
            helper.setText(newNotificacion.getCuerpo() + "<br/><br/>" + newNotificacion.getFirma(), true);
            emailSender.send(message);
            return true; 
        } catch (MessagingException e) {
            e.printStackTrace(); // o maneja el error de una mejor manera
            return false; // Indica que hubo un error al enviar el correo
        }
    }

}
