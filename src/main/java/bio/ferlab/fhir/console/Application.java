package bio.ferlab.fhir.console;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


@SpringBootApplication(exclude = {ElasticsearchRestClientAutoConfiguration.class})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        //UI is now accessible at http://localhost:8080/hapi-fhir-jpaserver/
    }

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    @Autowired
    AutowireCapableBeanFactory beanFactory;

    @Bean
    public FilterRegistrationBean<RedirectHomeFilter> loggingFilter(){
        FilterRegistrationBean<RedirectHomeFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new RedirectHomeFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
    @Bean
    public ServletRegistrationBean overlayRegistrationBean() {

        AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext = new AnnotationConfigWebApplicationContext();
        annotationConfigWebApplicationContext.register(FhirTesterConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(
                annotationConfigWebApplicationContext);
        dispatcherServlet.setContextClass(AnnotationConfigWebApplicationContext.class);
        dispatcherServlet.setContextConfigLocation(FhirTesterConfig.class.getName());

        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(dispatcherServlet);
        registrationBean.addUrlMappings("/*");

        registrationBean.setLoadOnStartup(1);
        return registrationBean;

    }
}
