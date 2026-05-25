package sakilaapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sakilaapp.model.dao.Actor;
import sakilaapp.model.dao.Pais;
import sakilaapp.model.dao.Pelicula;
import sakilaapp.repository.JdbcRepository;

@ExtendWith(MockitoExtension.class)
class CinemaServiceTest {

    @Mock
    private JdbcRepository jdbcRepo;

    @InjectMocks
    private CinemaService cinemaService;

    // --- TESTS DE LLISTATS (Consultes) ---

    @Test
    void testLlistaActors() throws Exception {
        List<Actor> actorsMock = new ArrayList<>();
        when(jdbcRepo.llistaActors()).thenReturn(actorsMock);

        List<Actor> resultat = cinemaService.llistaActors();

        assertNotNull(resultat);
        verify(jdbcRepo).llistaActors();
    }

    @Test
    void testLlistaPaisos() throws Exception {
        // GIVEN
        List<Pais> paisosMock = new ArrayList<>();
        when(jdbcRepo.llistaPaisos()).thenReturn(paisosMock);

        // WHEN
        List<Pais> resultat = cinemaService.llistaPaisos();

        // THEN
        assertNotNull(resultat);
        verify(jdbcRepo).llistaPaisos();
    }

    @Test
    void testLlistaPelis() throws Exception {
        // GIVEN
        List<Pelicula> pelisMock = new ArrayList<>();
        when(jdbcRepo.llistaPelis()).thenReturn(pelisMock);

        // WHEN
        List<Pelicula> resultat = cinemaService.llistaPelis();

        // THEN
        assertNotNull(resultat);
        verify(jdbcRepo).llistaPelis();
    }

    // --- TESTS DE REGISTRAR ACTOR (Lògica i Validacions) ---

    @Test
    void testRegistrarActor_Success() throws Exception {
        when(jdbcRepo.existeixActor("JOHN", "DOE")).thenReturn(false);

        cinemaService.registrarActor("  john  ", "doe"); // Provem també el trim i upper

        verify(jdbcRepo).insertarActor("JOHN", "DOE");
    }

    @Test
    void testRegistrarActor_NomNul_ThrowsException() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cinemaService.registrarActor(null, "Doe");
        });
        assertEquals("El nom i el cognom són obligatoris.", ex.getMessage());
    }

    @Test
    void testRegistrarActor_CognomNul_ThrowsException() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cinemaService.registrarActor("John", null);
        });
        assertEquals("El nom i el cognom són obligatoris.", ex.getMessage());
    }

    @Test
    void testRegistrarActor_CognomBuit_ThrowsException() {
        // Cobrim la branca de isBlank() per al cognom
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cinemaService.registrarActor("John", "   ");
        });
        assertEquals("El nom i el cognom són obligatoris.", ex.getMessage());
    }

    @Test
    void testRegistrarActor_AlreadyExists_ThrowsException() throws SQLException {
        when(jdbcRepo.existeixActor("JOHN", "DOE")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cinemaService.registrarActor("John", "Doe");
        });
        
        assertTrue(ex.getMessage().contains("ja existeix"));
        verify(jdbcRepo, never()).insertarActor(anyString(), anyString());
    }

    @Test
    void testRegistrarActor_DatabaseError_ThrowsException() throws SQLException {
        when(jdbcRepo.existeixActor(anyString(), anyString())).thenReturn(false);
        doThrow(new SQLException("Error de DB")).when(jdbcRepo).insertarActor(anyString(), anyString());

        Exception ex = assertThrows(Exception.class, () -> {
            cinemaService.registrarActor("John", "Doe");
        });

        assertEquals("Error en l'operació de base de dades", ex.getMessage());
        assertInstanceOf(SQLException.class, ex.getCause());
    }
}