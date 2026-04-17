package sakilaApp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sakilaApp.services.CinemaService;

@Controller
public class CinemaController {

	private static final Logger log = LogManager.getLogger(CinemaController.class);
	
	private final CinemaService cinemaService;
	
	public CinemaController(CinemaService cinemaService) {
		this.cinemaService = cinemaService;
	}

	@GetMapping("/")
	public String index() {
		log.info("Petició d'inici i càrrega d'index");
		return "index";
	}

	@GetMapping("/actors")
	public String actors(Model model) throws Exception {
			model.addAttribute("actors", cinemaService.llistaActors());
		return "llistatActors";
	}
	
	@PostMapping("/actors/add")
	public String addActor(@RequestParam("first_name") String firstName, 
	                       @RequestParam("last_name") String lastName, 
	                       RedirectAttributes redirectAttributes) {
	    try {
	    	cinemaService.registrarActor(firstName, lastName);
	        
	        redirectAttributes.addFlashAttribute("missatge", "Actor afegit amb èxit!");
	    } catch (Exception e) {
	    	log.error("Error intentant afegir l'actor {} {}: {}",firstName,lastName,e.getMessage());
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	    }
	    
	    // Redirigim a la llista d'actors per veure el nou element
	    return "redirect:/actors"; 
	}
	
	@GetMapping("/paisos")
	public String paisos(Model model) throws Exception {
			model.addAttribute("paisos", cinemaService.llistaPaisos());
		return "llistatPaisos";
	}
	
	@GetMapping("/pelis")
	public String pelis(Model model) throws Exception {

			model.addAttribute("pelis", cinemaService.llistaPelis());

		return "llistatPelis";
	}
	
}
