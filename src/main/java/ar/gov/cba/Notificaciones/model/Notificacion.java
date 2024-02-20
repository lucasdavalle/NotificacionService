package ar.gov.cba.Notificaciones.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.sql.Date;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor()
@Getter
@Setter
public class Notificacion {

    private Integer LIMITE_REINTENTOS = 5;



    private String destinatario;

    private String asunto; 

    private String cuerpo;

    private String firma;

    private String remitente;

    private Integer error;

    private Long numNotificacion;

    private Integer cantidadEnvios;

    private Date fechaUltimoEnvio;


    @Override
    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);  // Habilitar la indentación
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            // Manejar la excepción, puedes imprimir el error o lanzar una RuntimeException, dependiendo de tus necesidades.
            e.printStackTrace();
            return super.toString();
        }
    }

    public int verificarEstado() {

        // Verificar la cantidad de reintentos
        if (this.cantidadEnvios >= LIMITE_REINTENTOS) {
            return 6; // Excedió la cantidad de reintentos
            // Puedes incluir un mensaje adicional para indicar que no se reintentará más
        }

        // Otro código relacionado con la notificación...
        return 0; // Estado normal, sin problemas
    }

}
