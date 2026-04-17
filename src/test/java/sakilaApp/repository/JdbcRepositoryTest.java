package sakilaApp.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sakilaApp.model.dao.Actor;
import sakilaApp.model.dao.Pais;
import sakilaApp.model.dao.Pelicula;

@ExtendWith(MockitoExtension.class)
class JdbcRepositoryTest {

    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    private JdbcRepository jdbcRepository;

    @BeforeEach
    void setUp() throws SQLException {
        jdbcRepository = new JdbcRepository(dataSource);
    }

    // --- TESTS DE LLISTA ACTORS ---

    @Test
    void testLlistaActors_Success() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("actor_id")).thenReturn(1);
        when(resultSet.getString("first_name")).thenReturn("PENELOPE");
        when(resultSet.getString("last_name")).thenReturn("CRUZ");
        when(resultSet.getTimestamp("last_update")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

        List<Actor> resultat = jdbcRepository.llistaActors();

        assertNotNull(resultat);
        assertEquals(1, resultat.size());
        verify(connection).close();
    }

    @Test
    void testLlistaActors_Exception() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("DB Error"));

        assertThrows(Exception.class, () -> jdbcRepository.llistaActors());
    }

    // --- TESTS DE LLISTA PAISOS ---

    @Test
    void testLlistaPaisos_Success() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("country_id")).thenReturn(1);
        when(resultSet.getString("country")).thenReturn("Spain");
        when(resultSet.getTimestamp("last_update")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

        List<Pais> resultat = jdbcRepository.llistaPaisos();

        assertNotNull(resultat);
        assertEquals(1, resultat.size());
        assertEquals("Spain", resultat.get(0).nom());
    }

    @Test
    void testLlistaPaisos_Exception() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("DB Error"));

        assertThrows(Exception.class, () -> jdbcRepository.llistaPaisos());
    }

    // --- TESTS DE LLISTA PELIS ---

    @Test
    void testLlistaPelis_Success() throws Exception {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("film_id")).thenReturn(1);
        when(resultSet.getString("title")).thenReturn("ACADEMY DINOSAUR");
        when(resultSet.getString("description")).thenReturn("A Epic Drama");
        when(resultSet.getInt("release_year")).thenReturn(2006);

        List<Pelicula> resultat = jdbcRepository.llistaPelis();

        assertNotNull(resultat);
        assertEquals(1, resultat.size());
        assertEquals("ACADEMY DINOSAUR", resultat.get(0).titol());
    }

    @Test
    void testLlistaPelis_Exception() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("DB Error"));

        assertThrows(Exception.class, () -> jdbcRepository.llistaPelis());
    }

    // --- TESTS EXISTEIX ACTOR ---

    @Test
    void testExisteixActor_True() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        assertTrue(jdbcRepository.existeixActor("BRAD", "PITT"));
    }

    @Test
    void testExisteixActor_False() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0); // COUNT és 0

        assertFalse(jdbcRepository.existeixActor("INEXISTENT", "ACTOR"));
    }

    @Test
    void testExisteixActor_EmptyResultSet() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        
        when(resultSet.next()).thenReturn(false); // Cas on el ResultSet és buit

        assertFalse(jdbcRepository.existeixActor("NOM", "COGNOM"));
    }

    // --- TESTS INSERTAR ACTOR ---

    @Test
    void testInsertarActor_Success() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        jdbcRepository.insertarActor("TOM", "CRUISE");

        verify(preparedStatement).setString(1, "TOM");
        verify(preparedStatement).setString(2, "CRUISE");
        verify(preparedStatement).executeUpdate();
    }
}