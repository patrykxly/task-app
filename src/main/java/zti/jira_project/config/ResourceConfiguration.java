package zti.jira_project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for handling static and template resources.
 */
@Configuration
@EnableAspectJAutoProxy
public class ResourceConfiguration implements WebMvcConfigurer {

    private static final String[] RESOURCES = {
            "classpath:/static/", "classpath:/templates/", "classpath:/templates/fragments/"};

    /**
     * Configures the locations from which static resources will be served.
     *
     * @param registry the ResourceHandlerRegistry to configure
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(RESOURCES);
    }
}
