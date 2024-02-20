/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.gov.cba.Notificaciones.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author 20432313582
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificacionResponse {

    @JsonProperty("Resultado")
    private String Resultado;

    @JsonProperty("CodigoError")
    private String CodigoError;

    @JsonProperty("SesionHash")
    private String SesionHash;

    @JsonProperty("Mensaje")
    private String Mensaje;

    @JsonProperty("Servidor")
    private String Servidor;

    @JsonProperty("TimeStamp")
    private String TimeStamp;

    public NotificacionResponse() {
    }

    @Override
    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);  
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return super.toString();
        }
    }
}
