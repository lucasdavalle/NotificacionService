package ar.gov.cba.Notificaciones.logger;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author 20432313582
 */
@Slf4j
public class Log {

    public static void logE(String mensaje, String error, String clase, LocalDateTime fecha) {
        log.error("\n"+"{" + "\n"
                + "Mensaje: " + mensaje + "\n"
                + "Error: " + error + "\n"
                + "Clase: " + clase + "\n"
                + "Fecha: " + fecha.toString() + "\n" + "}");
    }


}
