package ar.gov.cba.Notificaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NotificacionesApplication   {
    @Autowired

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NotificacionesApplication.class, args);
    }

}