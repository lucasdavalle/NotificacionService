/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.gov.cba.Notificaciones.exception;

import lombok.Getter;


/**
 *
 * @author 23331711489
 */
@Getter

public class RecursoNoEncontradoException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public RecursoNoEncontradoException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s no encontrados con %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
