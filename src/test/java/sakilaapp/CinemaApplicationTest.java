package sakilaapp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

class CinemaApplicationTest {

    @Test
    void testConfigure() {
        // Testegem el mètode que fa servir el Tomcat extern
        CinemaApplication application = new CinemaApplication();
        SpringApplicationBuilder builder = mock(SpringApplicationBuilder.class);
        
        when(builder.sources(CinemaApplication.class)).thenReturn(builder);

        SpringApplicationBuilder result = application.configure(builder);

        verify(builder).sources(CinemaApplication.class);
        assertNotNull(result);
    }
}