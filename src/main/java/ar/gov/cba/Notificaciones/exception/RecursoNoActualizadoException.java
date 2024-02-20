package ar.gov.cba.Notificaciones.exception;

import lombok.Getter;

/**
 *
 * @author 23331711489
 */
@Getter
public class RecursoNoActualizadoException extends RuntimeException {

    private String resourceName;

    public RecursoNoActualizadoException(String resourceName, String nroNoti) {
        super(String.format("%s no actualizado id: %s", resourceName, nroNoti));
        this.resourceName = resourceName;
    }

}
