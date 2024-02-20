package ar.gov.cba.Notificaciones.dao;

import ar.gov.cba.Notificaciones.exception.RecursoNoActualizadoException;
import ar.gov.cba.Notificaciones.logger.Log;
import ar.gov.cba.Notificaciones.model.Notificacion;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/*
 * Esta clase se encarga de gestionar las operaciones de acceso a la base de datos relacionadas con las notificaciones.
 * Utiliza Spring JDBC para interactuar con la base de datos y realiza consultas y actualizaciones en la tabla correspondiente.
 *
 * La clase está anotada con @Repository y @Primary, indicando que es un componente de acceso a datos primario y que
 * debe considerarse para la resolución de dependencias cuando hay múltiples implementaciones de la misma interfaz.
 *
 * La propiedad jdbcTemplate representa el objeto NamedParameterJdbcTemplate utilizado para ejecutar consultas parametrizadas
 * y realizar operaciones de actualización en la base de datos. El objeto bpRowMapper se utiliza para mapear las filas
 * de la tabla a objetos Notificacion, haciendo uso del patrón de diseño RowMapper personalizado ColumnRowMapper.
 *
 * La clase define constantes que contienen las consultas SQL utilizadas para obtener notificaciones pendientes y notificaciones
 * con errores. Además, contiene consultas SQL para actualizar la cantidad de envíos y el estado de error de una notificación.
 *
 * @Author: 20432313582
 */
@Primary
@Repository
public class NotificacionDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Notificacion> bpRowMapper;

    private static final String TABLE_H_EST = "ADM_NOTI";
    private static final String SQL_QUERY = String.format(
            "SELECT * FROM %s WHERE CANT_ENVIOS = 0 FETCH FIRST 5 ROW ONLY",
            TABLE_H_EST);
    private static final String SQL_QUERY_NOT = String.format(
            "SELECT * FROM %s WHERE CANT_ENVIOS > 0 AND CANT_ENVIOS < 6 AND ERROR=1 FETCH FIRST 5 ROW ONLY",
            TABLE_H_EST);
    private static final String SQL_QUERY_UPDATE = String.format(
            "UPDATE %s SET CANT_ENVIOS = :quantity where NUM_NOTIFICACION = :nroNotification",
            TABLE_H_EST);
    private static final String SQL_QUERY_UPDATE2 = String.format(
            "UPDATE %s SET ERROR = :quantity where NUM_NOTIFICACION = :nroNotification",
            TABLE_H_EST);

    @Autowired
    public NotificacionDAO(
            NamedParameterJdbcTemplate jdbcTemplate,
            DataSource dataSource,
            ConversionService conversionService
    ) {
        this.jdbcTemplate = jdbcTemplate;
         this.bpRowMapper = BeanPropertyRowMapper.newInstance(Notificacion.class);
    }

    /**
     * Recupera las notificaciones pendientes desde la base de datos.
     *
     * @return Optional que contiene la lista de notificaciones pendientes, o
     * vacío si no hay resultados.
     */
    public Optional<List<Notificacion>> getNotifications() {
        try {
            List<Notificacion> notificaciones = jdbcTemplate.query(SQL_QUERY, bpRowMapper);
            return Optional.of(notificaciones);
        } catch (EmptyResultDataAccessException e) {
            Log.logE("El Servicio no existe", e.toString(), "NotificacionDAO", LocalDateTime.now());
            return Optional.empty();
        }
    }

    /**
     * Recupera las notificaciones con errores desde la base de datos.
     *
     * @return Optional que contiene la lista de notificaciones con errores, o
     * vacío si no hay resultados.
     */
    public Optional<List<Notificacion>> getNotSendNotifications() {
        try {
            List<Notificacion> notificaciones = jdbcTemplate.query(SQL_QUERY_NOT, bpRowMapper);
            return Optional.of(notificaciones);
        } catch (EmptyResultDataAccessException e) {
            Log.logE("El Servicio no existe", e.getMessage(), "NotificacionDAO", LocalDateTime.now());
            return Optional.empty();
        }
    }

    /**
     * Actualiza la cantidad de envíos de una notificación.
     *
     * @param quantity Nueva cantidad de envíos.
     * @param nroNotification Número de la notificación a actualizar.
     */
    public void updateEnvios(int quantity, Long nroNotification) {
        MapSqlParameterSource paramMap = (new MapSqlParameterSource())
                .addValue("quantity", quantity)
                .addValue("nroNotification", nroNotification);

        try {
            jdbcTemplate.update(SQL_QUERY_UPDATE, paramMap);
        } catch (DataAccessException e) {
            Log.logE("Error al actualizar la notificacion", e.getMessage(), "NotificacionDAO", LocalDateTime.now());
            throw new RecursoNoActualizadoException("Notificacion", nroNotification.toString());
        }
    }

    /**
     * Actualiza el estado de error de una notificación.
     *
     * @param quantity Nuevo estado de error (0 o 1).
     * @param nroNotification Número de la notificación a actualizar.
     */
    public void updateError(int quantity, Long nroNotification) {
        MapSqlParameterSource paramMap = (new MapSqlParameterSource())
                .addValue("quantity", quantity)
                .addValue("nroNotification", nroNotification);

        try {
            jdbcTemplate.update(SQL_QUERY_UPDATE2, paramMap);
        } catch (DataAccessException e) {
            Log.logE("Error al actualizar la notificacion", e.getMessage(), "NotificacionDAO", LocalDateTime.now());
            throw new RecursoNoActualizadoException("Notificacion", nroNotification.toString());
        }
    }
}
