package edu.eci.cvds.proyect.booking;

import edu.eci.cvds.proyect.booking.persistency.service.BookingService;
import edu.eci.cvds.proyect.booking.persistency.service.LaboratoryService;
import edu.eci.cvds.proyect.booking.persistency.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import edu.eci.cvds.proyect.booking.persistency.service.AuthorizationService;

/**

 * Clase de configuración que gestiona la creación de beans para el servicio de tareas

 * y su repositorio.

 */

@Configuration
public class Config implements WebMvcConfigurer {
    @Override

    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(false);

    }

    /**

     * Crea y configura el bean de TaskService, que gestiona la lógica de las tareas.

     *

     * @return Una nueva instancia de TaskService.

     */

    @Bean
    public BookingService bookingService() {
        return new BookingService();

    }

    @Bean
    public UserService userService() {
        return new UserService();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Bean
    public LaboratoryService laboratoryService() {
        return new LaboratoryService();

    }
    @Bean
    public AuthorizationService authorizationService() {
        return new AuthorizationService();
    }

}
