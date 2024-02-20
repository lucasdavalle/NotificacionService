package ar.gov.cba.Notificaciones.service;

import ar.gov.cba.Notificaciones.logger.Log;
import ar.gov.cba.Notificaciones.api.EmailSender;
import ar.gov.cba.Notificaciones.dao.NotificacionDAO;
import ar.gov.cba.Notificaciones.exception.NotificationException;
import ar.gov.cba.Notificaciones.exception.RecursoNoEncontradoException;
import ar.gov.cba.Notificaciones.model.Notificacion;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/*
 * Este servicio, representado por la clase NotificacionService, gestiona el envío de notificaciones programadas
 * mediante el uso de la anotación @Scheduled de Spring. La clase se encarga de coordinar la interacción entre el
 * componente de acceso a datos (NotificacionDAO) y el sistema de envío de correos electrónicos (EmailSender).
 *
 * La clase incluye un método programado (@Scheduled) para enviar notificaciones pendientes a intervalos regulares,
 * así como un método para reintentar el envío de notificaciones que han fallado en intentos anteriores.
 *
 * @Author: 20432313582
 */
@Slf4j
@Service
public class NotificacionService {

    private final NotificacionDAO notificacionDAO;
    private final EmailSender emailSender;
    private final long intervaloEjecucionSegundos;

    @Autowired
    public NotificacionService(
            NotificacionDAO notificacionDAO,
            EmailSender emailSender,
            Environment environment
    ) {
        // Se inicializan las instancias y se obtiene el intervalo de ejecución desde las propiedades.
        this.notificacionDAO = notificacionDAO;
        this.emailSender = emailSender;
        this.intervaloEjecucionSegundos = Long.parseLong(environment.getProperty("notificacion.intervaloEjecucionMiliSegundos", "120"));
        log.info("Intervalo de ejecucion : {} ms", intervaloEjecucionSegundos);
    }

    /**
     * Método programado (@Scheduled) para enviar notificaciones pendientes a
     * intervalos regulares. Recupera las notificaciones pendientes desde la
     * base de datos, intenta enviarlas y actualiza su estado en consecuencia.
     */
    @Scheduled(fixedDelayString = "${notificacion.intervaloEjecucionMiliSegundos}000")
    public void send() {

        List<Notificacion> notificaciones = notificacionDAO.getNotifications().orElseThrow(()
                -> new RecursoNoEncontradoException("Notificaciones", "estado", "PENDIENTE"));

        if (!notificaciones.isEmpty()) {
            log.info("Enviando {} notificaciones nuevas: ", notificaciones.size());

            notificaciones.forEach(Not -> {
                int Estado = Not.verificarEstado();
                log.info("Estado de la notificacion" + Estado);
                if (Estado != 0) {
                    notificacionDAO.updateError(Estado, Not.getNumNotificacion());
                } else {
                    notificacionDAO.updateEnvios(Not.getCantidadEnvios() + 1, Not.getNumNotificacion());
                    try {
                        if (emailSender.sendMail(Not)) {
                            log.info("Notificacion enviada Nro {} enviada con exito", Not.getNumNotificacion());
                        } else {
                            notificacionDAO.updateError(1, Not.getNumNotificacion());
                            log.info("Error al enviar Notificacion Nro {}", Not.getNumNotificacion());
                        }
                    } catch (NullPointerException | NotificationException | MessagingException ex) {
                        Log.logE("Error en el envío de correo", ex.getMessage(), "NotificacionService", LocalDateTime.now());
                    }
                }

            });
        }

        sendError(); // Intenta reenviar notificaciones que fallaron en envíos anteriores.
    }

    /**
     * Método para reintentar el envío de notificaciones fallidas. Recupera las
     * notificaciones con errores desde la base de datos, intenta reenviarlas y
     * actualiza su estado en consecuencia.
     */
    public void sendError() {
        List<Notificacion> notificaciones = notificacionDAO.getNotSendNotifications().orElseThrow(()
                -> new RecursoNoEncontradoException("Notificaciones", "estado", "PENDIENTE"));

        if (!notificaciones.isEmpty()) {
            log.info("Reintentando envío de {} notificaciones fallidas: ", notificaciones.size());

            notificaciones.forEach(Not -> {
                int Estado = Not.verificarEstado();
                log.info("Estado de la notificacion" + Estado);
                if (Estado != 0) {
                    notificacionDAO.updateError(Estado, Not.getNumNotificacion());
                } else {
                    notificacionDAO.updateEnvios(Not.getCantidadEnvios() + 1, Not.getNumNotificacion());
                    try {
                        if (emailSender.sendMail(Not)) {
                            notificacionDAO.updateError(0, Not.getNumNotificacion());
                            log.info("Notificacion enviada Nro {} enviada con exito", Not.getNumNotificacion());
                        } else {
                            log.info("Error al reenviar Notificacion Nro {}", Not.getNumNotificacion());
                        }
                    } catch (NullPointerException | NotificationException | MessagingException ex) {
                        Log.logE("Error en el reenvío de correo", ex.getMessage(), "NotificacionService", LocalDateTime.now());
                    }
                }
            });
        }
    }
}
