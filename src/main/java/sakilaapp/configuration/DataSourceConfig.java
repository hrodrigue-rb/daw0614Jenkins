package sakilaapp.configuration;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

@Configuration
@Lazy
public class DataSourceConfig {

    @Bean
    @Profile("prod")
    @Lazy
    @ConditionalOnProperty(name = "app.datasource.jndi-name")
    public DataSource jndiDataSource(@Value("${app.datasource.jndi-name}") String jndiName)
            throws NamingException {

        try (InitialContext ctx = new InitialContext()) {
            // Evita el cast inseguro usando lookup con clase
            return ctx.lookup(jndiName, DataSource.class);
        }
    }
}