package com.MiProyecto.proyecto.controller;

import com.MiProyecto.proyecto.model.Conductor;
import com.MiProyecto.proyecto.service.ConductorService;


import org.springframework.stereotype.Controller; 
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping; // Para mapear peticiones HTTP GET
import org.springframework.web.bind.annotation.ModelAttribute; // Para vincular datos de formulario a un objeto
import org.springframework.web.bind.annotation.PathVariable; // Para extraer variables de la URL
import org.springframework.web.bind.annotation.PostMapping; // Para mapear peticiones HTTP POST
import org.springframework.web.bind.annotation.RequestMapping; // Para mapear una URL base para el controlador
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Para mensajes flash en redirecciones

import java.util.List;
import java.util.Optional;



@Controller
@RequestMapping("/conductores") // Todas las peticiones a /conductores/... serán manejadas por este controlador
public class ConductorController {
    
    private final ConductorService conductorService; // Inyectamos el servicio de conductores

    // Constructor para la inyección de dependencias (Spring inyectará ConductorService aquí)
    public ConductorController(ConductorService conductorService) {
        this.conductorService = conductorService;
    }

    // --- Métodos para listar y ver detalles de conductores ---

    // Maneja la petición GET a /conductores (URL base)
    // Muestra una lista de todos los conductores
    @GetMapping // Mapea GET /conductores
    public String listarConductores(Model model) {
        List<Conductor> conductores = conductorService.obtenerTodosLosConductores();
        model.addAttribute("conductores", conductores); // Añade la lista de conductores al modelo
        return "listaConductores"; // Devuelve el nombre de la vista (listaConductores.jsp)
    }

    // Maneja la petición GET a /conductores/{numeroIdentificacion}
    // Muestra los detalles de un conductor específico
    @GetMapping("/{numeroIdentificacion}")
    public String verDetalleConductor(@PathVariable String numeroIdentificacion, Model model, RedirectAttributes redirectAttributes) {
        
        Optional<Conductor> conductorOpt = conductorService.obtenerConductorPorIdentificacion(numeroIdentificacion);

        if (conductorOpt.isPresent()) {
            model.addAttribute("conductor", conductorOpt.get());
            return "detalleConductor"; // Devuelve la vista de detalle
        } else {
            redirectAttributes.addFlashAttribute("Error", "Conductor con identificación " + numeroIdentificacion + " no encontrado.");
            return "redirect:/conductores"; //Redirige a la lista conductores
        }

    }

    // --- Métodos para agregar nuevos conductores ---

    // Maneja la petición GET a /conductores/nuevo
    // Muestra el formulario para crear un nuevo conductor
    @GetMapping("/nuevo")
    public String mostrarFormularioConductor(Model model) {
        model.addAttribute("conductor", new Conductor()); // Ahora con el constructor vacío
        return "formularioConductor";
    }

    @GetMapping("/buscar")
    public String buscarVehiculoPorPlaca(@RequestParam("idConductor") String idConductor, Model model, RedirectAttributes redirectAttributes) {


        Optional<Conductor> conductorOpt = conductorService.obtenerConductorPorIdentificacion(idConductor);
        
        if (conductorOpt.isPresent()) {
            return "redirect:/conductores/" + conductorOpt.get().getNumeroIdentificacion();
        } else {
            redirectAttributes.addFlashAttribute("error", "Conductor con ID: " + idConductor + " no encontrado.");
            return "redirect:/conductores"; // Si no se encuentra, redirige de vuelta a la lista de vehículos con un mensaje de error
        }
        
    }
    

    // Maneja la petición POST a /conductores/guardar
    // Procesa los datos enviados desde el formulario para agregar un conductor
    @PostMapping("/guardar") // Mapea POST /conductores/guardar
    public String guardarConductor(@ModelAttribute Conductor conductor, RedirectAttributes redirectAttributes){
        // @ModelAttribute vincula automáticamente los campos del formulario al objeto Conductor
        boolean agregado = conductorService.agregarConductor(conductor);

        if (agregado) {
            redirectAttributes.addFlashAttribute("mensaje", "Conductor agregado exitosamente!");
            // Redirigimos al detalle del nuevo conductor o a la lista
             return "redirect:/conductores/" + conductor.getNumeroIdentificacion();
        } else {
            redirectAttributes.addFlashAttribute("Error","Error al agregar el conductor" );
            // Si hay un error, redirigimos de nuevo al formulario para que el usuario pueda corregir
            return "redirect:/conductores/nuevo";
        }
    }
    
}
