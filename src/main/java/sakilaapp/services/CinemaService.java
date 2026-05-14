package sakilaapp.services;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import sakilaapp.model.dao.Actor;
import sakilaapp.model.dao.Pais;
import sakilaapp.model.dao.Pelicula;
import sakilaapp.repository.JdbcRepository;

@Service
public class CinemaService {

    private static final Logger log = LogManager.getLogger(CinemaService.class);

    private final JdbcRepository jdbcRepo;

    public CinemaService(JdbcRepository jdbcRepository) {
        this.jdbcRepo = jdbcRepository;
    }

    // Excepción personalizada para el servicio
    public static class ServiceException extends RuntimeException {
        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public List<Actor> llistaActors() {
        log.info("Petició recuperació Actors rebuda");
        List<Actor> actors = jdbcRepo.llistaActors();
        log.info("Actors recuperats: {}", actors);
        return actors;
    }

    public List<Pais> llistaPaisos() {
        log.info("Petició recuperació Pais rebuda.");
        List<Pais> paisos = jdbcRepo.llistaPaisos();
        log.info("Països recuperats: {}", paisos);
        return paisos;
    }

    public List<Pelicula> llistaPelis() {
        log.info("Petició recuperació Pelicules rebuda.");
        List<Pelicula> pelis = jdbcRepo.llistaPelis();
        log.info("Pelicules recuperats: {}", pelis);
        return pelis;
    }

    public void registrarActor(String nom, String cognom) {
        if (nom == null || nom.isBlank() || cognom == null || cognom.isBlank()) {
            throw new ServiceException("El nom i el cognom són obligatoris.", null);
        }

        String nomUpper = nom.toUpperCase().trim();
        String cognomUpper = cognom.toUpperCase().trim();

        try {
            if (jdbcRepo.existeixActor(nomUpper, cognomUpper)) {
                throw new ServiceException(
                        String.format("L'actor %s %s ja existeix.", nomUpper, cognomUpper), null);
            }
            jdbcRepo.insertarActor(nomUpper, cognomUpper);
        } catch (SQLException e) {
            log.error("Error JDBC: {}", e.getMessage(), e);
            throw new ServiceException("Error en l'operació de base de dades", e);
        }
    }
}