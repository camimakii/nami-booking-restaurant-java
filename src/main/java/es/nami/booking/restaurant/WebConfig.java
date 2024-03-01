package es.nami.booking.restaurant;

import es.nami.booking.restaurant.monitor.PerformanceLogHttpRequestInterceptor;
import es.nami.booking.restaurant.util.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "PUT", "POST", "DELETE");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(new PerformanceLogHttpRequestInterceptor())
                .addPathPatterns(Constants.API_URL + "**"); // Apply interceptor to paths that match this pattern
//                .excludePathPatterns("/api/auth/**", "/monitor/**"); // Exclude paths that match this pattern
    }

}