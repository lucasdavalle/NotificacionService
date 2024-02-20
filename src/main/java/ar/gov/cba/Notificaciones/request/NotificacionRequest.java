/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.gov.cba.Notificaciones.request;

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
public class NotificacionRequest {

    private int Id_App = 210;
    private String Pass_App;
    private String TokenValue;
    private String TimeStamp;
    private String Cuil;
    private String Asunto;
    private String BodyHTML;
    private String Firma;
    private String Ente;

    public NotificacionRequest(String Pass_App, String TokenValue, String TimeStamp, String Cuil, String Asunto, String BodyHTML, String Firma, String Ente) {
        this.Pass_App = Pass_App;
        this.TokenValue = TokenValue;
        this.TimeStamp = TimeStamp;
        this.Cuil = Cuil;
        this.Asunto = Asunto;
        this.BodyHTML = BodyHTML;
        this.Firma = Firma;
        this.Ente = Ente;
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
