package sakilaapp.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import sakilaapp.model.dao.Actor;
import sakilaapp.model.dao.Pais;
import sakilaapp.model.dao.Pelicula;
import sakilaapp.services.CinemaService;

@WebMvcTest(CinemaController.class) // Només carrega el context de la capa web
class CinemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CinemaService cinemaService; // Mock de la dependència del controlador

    @Test
    void testIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testActors() throws Exception {
        // GIVEN
        List<Actor> actors = new ArrayList<>();
        when(cinemaService.llistaActors()).thenReturn(actors);

        // WHEN & THEN
        mockMvc.perform(get("/actors"))
                .andExpect(status().isOk())
                .andExpect(view().name("llistatActors"))
                .andExpect(model().attributeExists("actors"));
    }

    @Test
    void testAddActorSuccess() throws Exception {
        // GIVEN: El service no llança excepció
        doNothing().when(cinemaService).registrarActor("BRAD", "PITT");

        // WHEN & THEN
        mockMvc.perform(post("/actors/add")
                .param("first_name", "BRAD")
                .param("last_name", "PITT"))
                .andExpect(status().is3xxRedirection()) // Comprova que redirigeix
                .andExpect(redirectedUrl("/actors"))
                .andExpect(flash().attribute("missatge", "Actor afegit amb èxit!"));
    }

    @Test
    void testAddActorError() throws Exception {
        // GIVEN: El service llança una excepció (ex: actor ja existeix)
        doThrow(new RuntimeException("Ja existeix")).when(cinemaService).registrarActor("BRAD", "PITT");

        // WHEN & THEN
        mockMvc.perform(post("/actors/add")
                .param("first_name", "BRAD")
                .param("last_name", "PITT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/actors"))
                .andExpect(flash().attribute("error", "Ja existeix"));
    }

    @Test
    void testPaisos() throws Exception {
        // GIVEN
        List<Pais> paisos = new ArrayList<>();
        when(cinemaService.llistaPaisos()).thenReturn(paisos);

        // WHEN & THEN
        mockMvc.perform(get("/paisos"))
                .andExpect(status().isOk())
                .andExpect(view().name("llistatPaisos"))
                .andExpect(model().attributeExists("paisos"));
    }

    @Test
    void testPelis() throws Exception {
        // GIVEN
        List<Pelicula> pelis = new ArrayList<>();
        when(cinemaService.llistaPelis()).thenReturn(pelis);

        // WHEN & THEN
        mockMvc.perform(get("/pelis"))
                .andExpect(status().isOk())
                .andExpect(view().name("llistatPelis"))
                .andExpect(model().attributeExists("pelis"));
    }
}