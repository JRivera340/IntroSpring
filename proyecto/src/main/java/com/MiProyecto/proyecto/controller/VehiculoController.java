package com.MiProyecto.proyecto.controller;


import com.MiProyecto.proyecto.model.Vehiculo;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;

import com.MiProyecto.proyecto.service.ConductorService; // Necesario para obtener conductores para la asignación
import com.MiProyecto.proyecto.service.VehiculoService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/vehiculos") // Todas las rutas de este controlador comenzarán con /vehiculos
public class VehiculoController {

    private final ConductorService conductorService;
    private final VehiculoService vehiculoService;
    
    public VehiculoController(VehiculoService vehiculoService, ConductorService conductorService) {
        this.vehiculoService = vehiculoService;
        this.conductorService = conductorService;
    }

    // --- Métodos para listar y ver detalles de vehículos ---

    // Maneja la petición GET a /vehiculos
    // Muestra una lista de todos los vehículos
    @GetMapping
    public String listarVehiculos(Model model) {
        List<Vehiculo> vehiculos = vehiculoService.obtenerTodosLosVehiculos();
        model.addAttribute("vehiculos", vehiculos);
       
        model.addAttribute("conductoresDisponibles", conductorService.obtenerTodosLosConductores()); 
        return "listaVehiculos";
    }


    // Maneja la petición GET a /vehiculos/{placa}
    // Muestra los detalles de un vehículo específico
    @GetMapping("/{placa}")
    public String verDetalleVehiculo(@PathVariable String placa, Model model, RedirectAttributes redirectAttributes) {
        Optional<Vehiculo> vehiculoOpt = vehiculoService.obtenerVehiculoPorPlaca(placa);
        
        if (vehiculoOpt.isPresent()) {
            model.addAttribute("vehiculo", vehiculoOpt.get());
            model.addAttribute("conductoresDisponibles", conductorService.obtenerTodosLosConductores());
            return "detalleVehiculo";
        }else {
            redirectAttributes.addFlashAttribute("Error", "Vehículo con placa " + placa + " no encontrado.");
            return "redirect:/vehiculos";
        }
    
    }

     // --- Métodos para agregar nuevos vehículos ---

    // Maneja la petición GET a /vehiculos/nuevo
    // Muestra el formulario para crear un nuevo vehículo
      @GetMapping("/nuevo")
    public String mostrarFormularioVehiculo(Model model) {
        // Objeto vacío para vincular el formulario. El ID interno se genera en el constructor de Vehiculo.
        model.addAttribute("vehiculo", new Vehiculo());
        return "formularioVehiculo"; // Nombre de tu archivo JSP
    }

    // Maneja la petición POST a /vehiculos/guardar
    // Procesa los datos enviados desde el formulario para agregar un vehículo
    @PostMapping("/guardar")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo, RedirectAttributes redirectAttributes) {
        boolean agregado = vehiculoService.registrarVehiculo(vehiculo);; // Usamos el método sin asignación inicial

        if (agregado) {
            redirectAttributes.addFlashAttribute("mensaje", "Vehículo agregado exitosamente!");
            return "redirect:/vehiculos/" + vehiculo.getPlaca(); // Redirige al detalle del nuevo vehículo
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al agregar el vehículo. Posiblemente la placa ya existe o los datos son inválidos.");
            return "redirect:/vehiculos/nuevo"; // Redirige de nuevo al formulario
        }
    }
    
    // --- Métodos para asignar/desasignar vehículos a conductores ---
    @PostMapping("/{placa}/asignar")
    public String asignarVehiculoAConductor(@PathVariable String placa,
                                            @RequestParam String numeroIdentificacionConductor,
                                            RedirectAttributes redirectAttributes) {
        // ¡Ahora la llamada coincide con la firma del nuevo método en VehiculoService!
        boolean asignado = vehiculoService.asignarVehiculoAConductor(placa, numeroIdentificacionConductor);
        if (asignado) {
            redirectAttributes.addFlashAttribute("mensaje", "Vehículo " + placa + " asignado a conductor " + numeroIdentificacionConductor + " exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo asignar el vehículo " + placa + " al conductor " + numeroIdentificacionConductor + ". Verificar IDs o si ya está asignado al mismo conductor.");
        }
        return "redirect:/vehiculos/" + placa; // Vuelve a la página de detalle del vehículo
    }

       // Maneja la petición POST para desasignar un vehículo de un conductor
    @PostMapping("/{placa}/desasignar")
    public String desasignarVehiculoDeConductor(@PathVariable String placa,
                                              RedirectAttributes redirectAttributes) {
        boolean desasignado = vehiculoService.desasignarVehiculoDeConductor(placa);
        if (desasignado) {
            redirectAttributes.addFlashAttribute("mensaje", "Vehículo " + placa + " desasignado exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo desasignar el vehículo " + placa + ".");
        }
        return "redirect:/vehiculos/" + placa; // Vuelve a la página de detalle del vehículo
    }

    // --- Métodos para eliminar vehículos (opcional) ---
    @PostMapping("/{placa}/eliminar")
    public String eliminarVehiculo(@PathVariable String placa, RedirectAttributes redirectAttributes) {
        boolean eliminado = vehiculoService.eliminarVehiculo(placa);
        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensaje", "Vehículo " + placa + " eliminado exitosamente.");
            return "redirect:/vehiculos"; // Redirige a la lista de vehículos
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el vehículo " + placa + ". Verifique si está asignado a un conductor.");
            return "redirect:/vehiculos/" + placa; // Vuelve al detalle si no se pudo eliminar
        }
    }
}
