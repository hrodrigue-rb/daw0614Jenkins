package sakilaApp.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sakilaApp.model.dao.Actor;
import sakilaApp.model.dao.Pais;
import sakilaApp.model.dao.Pelicula;
import sakilaApp.repository.JdbcRepository;

@Service
public class CinemaService {
	
	private static final Logger log = LogManager.getLogger(CinemaService.class);
	
	
	private final JdbcRepository jdbcRepo;
	
	public CinemaService(JdbcRepository jdbcRepository) {
		this.jdbcRepo=jdbcRepository;
	}
	
    public List<Actor> llistaActors() throws Exception {
    	log.info("Petició recuperació Actors rebuda" );
    	List<Actor> actors = new ArrayList<Actor>();
    	
    		actors = jdbcRepo.llistaActors();
    	
    	log.info("Actors recuperats: "+actors);
    	return actors;
    	
        
    }
    
    public List<Pais> llistaPaisos() throws Exception {
    	log.info("Petició recuperació Pais rebuda.");
    	List<Pais> paisos = new ArrayList<Pais>();
    	paisos = jdbcRepo.llistaPaisos();
    	log.info("Països recuperats: "+paisos);
    	return paisos;
    	
        
    }
    public List<Pelicula> llistaPelis() throws Exception {
    	log.info("Petició recuperació Pelicules rebuda.");
    	List<Pelicula> pelis = new ArrayList<Pelicula>();
    	pelis = jdbcRepo.llistaPelis();
    	log.info("Pelicules recuperats: "+pelis);
    	return pelis;
    	
        
    }
    
    public void registrarActor(String nom, String cognom) throws Exception {
        if (nom == null || nom.isBlank() || cognom == null || cognom.isBlank()) {
            throw new RuntimeException("El nom i el cognom són obligatoris.");
        }

        String nomUpper = nom.toUpperCase().trim();
        String cognomUpper = cognom.toUpperCase().trim();

        try {
            if (jdbcRepo.existeixActor(nomUpper, cognomUpper)) {
                throw new RuntimeException("L'actor " + nomUpper + " " + cognomUpper + " ja existeix.");
            }
            jdbcRepo.insertarActor(nomUpper, cognomUpper);
        } catch (SQLException e) {
            log.error("Error JDBC: {}", e.getMessage());
            throw new Exception("Error en l'operació de base de dades", e);
        }
    }
    
}