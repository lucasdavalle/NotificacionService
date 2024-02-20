package ar.gov.cba.Notificaciones.utils;

import ar.gov.cba.Notificaciones.exception.NotificationException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.stereotype.Component;

/*
 * Clase Utils:
 * Esta clase proporciona métodos de utilidad para operaciones comunes en el contexto de notificaciones.
 *
 * - replaceHtmlSpecialCharacters(String input): Método privado que reemplaza ciertos caracteres especiales de HTML
 *   en una cadena de entrada.
 * 
 * - decodeBase64UrlToHtml(String base64UrlEncodedString): Método público que decodifica una cadena codificada en Base64 URL
 *   y la convierte en una cadena UTF-8. Luego, escapa los caracteres especiales para su uso en HTML utilizando
 *   replaceHtmlSpecialCharacters.
 *
 * La anotación @Slf4j proporciona soporte para el registro de eventos mediante SLF4J.
 * La anotación @Component indica que la clase es un componente de Spring y puede ser administrada por el contenedor de Spring.
 *
 * @Author: 20432313582
 */
@Component
public class Utils {

    /**
     * Reemplaza ciertos caracteres especiales de HTML en una cadena de entrada.
     *
     * @param input La cadena de entrada.
     * @return La cadena con los caracteres especiales de HTML reemplazados.
     */
    private static String replaceHtmlSpecialCharacters(String input) {
        String output = input;
        output = output.replaceAll("&", "&amp;");
        output = output.replaceAll("Â°", "&deg;");
        output = output.replaceAll("Ã¡", "&aacute;");
        output = output.replaceAll("Ã©", "&eacute;");
        output = output.replaceAll("Ã­", "&iacute;");
        output = output.replaceAll("Ã³", "&oacute;");
        output = output.replaceAll("Ãº", "&uacute;");
        output = output.replaceAll("Ã", "&Aacute;");
        output = output.replaceAll("Ã‰", "&Eacute;");
        output = output.replaceAll("Ã", "&Iacute;");
        output = output.replaceAll("Ã“", "&Oacute;");
        output = output.replaceAll("Ãš", "&Uacute;");
        output = output.replaceAll("ï¿½", "&Ntilde;");

        return output;
    }

    /**
     * Decodifica una cadena codificada en Base64 URL y la convierte en una
     * cadena UTF-8. Luego, escapa los caracteres especiales para su uso en HTML
     * utilizando replaceHtmlSpecialCharacters.
     *
     * @param base64UrlEncodedString La cadena codificada en Base64 URL.
     * @return La cadena decodificada y escapada para su uso en HTML.
     * @throws NotificationException Si ocurre un error durante la decodificación o la conversión de caracteres.
     */
    public String decodeBase64UrlToHtml(String base64UrlEncodedString) throws NotificationException {
        try {

            String base64String = URLDecoder.decode(base64UrlEncodedString, StandardCharsets.UTF_8.toString());

            byte[] decodedBytes = Base64.getUrlDecoder().decode(base64String);

            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

            decodedString = replaceHtmlSpecialCharacters(decodedString);

            return decodedString;
        } catch (UnsupportedEncodingException e) {
            throw new NotificationException("Error durante la decodificación de Base64 URL: " + e.getMessage());
        }
    }
}
