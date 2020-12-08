package bio.ferlab.fhir.console;

import ca.uhn.fhir.context.FhirVersionEnum;
import com.google.common.collect.ImmutableList;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@ConfigurationProperties(prefix = "hapi.fhir")
@Configuration
@EnableConfigurationProperties
public class AppProperties {

    private String authorized_roles;

    private List<Tester> tester = ImmutableList.of(new Tester());
    private Logger logger = new Logger();


    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }


    public List<Tester> getTester() {
        return tester;
    }

    public void setTester(List<Tester> tester) {
        this.tester = tester;
    }

    public String getAuthorized_roles() {
        return authorized_roles;
    }

    public void setAuthorized_roles(String authorized_roles) {
        this.authorized_roles = authorized_roles;
    }
//
//    public static class Cors {
//        private Boolean allow_Credentials = true;
//        private List<String> allowed_origin = ImmutableList.of("*");
//
//        public List<String> getAllowed_origin() {
//            return allowed_origin;
//        }
//
//        public void setAllowed_origin(List<String> allowed_origin) {
//            this.allowed_origin = allowed_origin;
//        }
//
//        public Boolean getAllow_Credentials() {
//            return allow_Credentials;
//        }
//
//        public void setAllow_Credentials(Boolean allow_Credentials) {
//            this.allow_Credentials = allow_Credentials;
//        }
//
//
//    }

    public static class Logger {

        private String name = "fhirtest.access";
        private String error_format = "ERROR - ${requestVerb} ${requestUrl}";
        private String format = "Path[${servletPath}] Source[${requestHeader.x-forwarded-for}] Operation[${operationType} ${operationName} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}] ResponseEncoding[${responseEncodingNoDefault}] Operation[${operationType} ${operationName} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}] ResponseEncoding[${responseEncodingNoDefault}]";
        private Boolean log_exceptions = true;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getError_format() {
            return error_format;
        }

        public void setError_format(String error_format) {
            this.error_format = error_format;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public Boolean getLog_exceptions() {
            return log_exceptions;
        }

        public void setLog_exceptions(Boolean log_exceptions) {
            this.log_exceptions = log_exceptions;
        }
    }

    public static class Tester {

        private String id = "home";
        private String name = "Local Tester";
        private String server_address = "http://localhost:8080/fhir";
        private Boolean refuse_to_fetch_third_party_urls = true;
        private FhirVersionEnum fhir_version = FhirVersionEnum.R4;

        public FhirVersionEnum getFhir_version() {
            return fhir_version;
        }

        public void setFhir_version(FhirVersionEnum fhir_version) {
            this.fhir_version = fhir_version;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getServer_address() {
            return server_address;
        }

        public void setServer_address(String server_address) {
            this.server_address = server_address;
        }

        public Boolean getRefuse_to_fetch_third_party_urls() {
            return refuse_to_fetch_third_party_urls;
        }

        public void setRefuse_to_fetch_third_party_urls(Boolean refuse_to_fetch_third_party_urls) {
            this.refuse_to_fetch_third_party_urls = refuse_to_fetch_third_party_urls;
        }
    }

}
