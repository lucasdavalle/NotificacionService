package ar.gov.cba.Notificaciones.exception;

import lombok.Getter;

/**
 *
 * @author 23331711489
 */
@Getter
public class NotificationException extends RuntimeException {

    public NotificationException(String error) {
        super("Error al intentar enviar la notificacion: " + error);
    }

}
