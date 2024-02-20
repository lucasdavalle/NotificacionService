package ar.gov.cba.Notificaciones.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author 20432313582
 */
@AllArgsConstructor
@Getter
@Setter
public class NotificacionStatus {

    private Long numeroNotificacion;
    private Integer numeroEnvio;
    private String id;
    private String status;
    private Long tiempoTotal;

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
