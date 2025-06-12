package com.MiProyecto.proyecto.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.MiProyecto.proyecto.model.Conductor;

@Service
public class ConductorService {

     private final Map<String, Conductor> conductoresRegistrados = new HashMap<>();

    public ConductorService() {
        // La inicialización del mapa ya se hace en la declaración del campo.
    }



    public Optional<Conductor> obtenerConductorPorIdentificacion(String numeroIdentificacionConductor) {
        
        if(numeroIdentificacionConductor == null || numeroIdentificacionConductor.trim().isEmpty()){
            return Optional.empty();
        }
        return Optional.ofNullable(conductoresRegistrados.get(numeroIdentificacionConductor.trim().toUpperCase()));
        
    }

    // ¡¡¡ASEGÚRATE DE QUE ESTE MÉTODO ESTÉ CORRECTO!!!
    public List<Conductor> obtenerTodosLosConductores() {
        return new ArrayList<>(conductoresRegistrados.values());
    }


    public boolean agregarConductor(Conductor conductor){
        if (conductor == null || conductor.getNumeroIdentificacion() == null) {
            return false;
        }
        String idConductor = conductor.getNumeroIdentificacion().trim().toUpperCase();

        if (conductoresRegistrados.containsKey(idConductor)) {
            System.out.println("Advertencia: Ya existe un conductor con la identificación " + conductor.getNumeroIdentificacion());
            return false; // El conductor ya existe.
            
        }

        conductoresRegistrados.put(idConductor, conductor);
        System.out.println("Conductor '" + conductor.getNombre() + "' agregado con ID: " + conductor.getNumeroIdentificacion());
        return true;
    }

    public boolean actualizarConductor(Conductor conductorActualizado){
        if (conductorActualizado == null || conductorActualizado.getNumeroIdentificacion().trim().isEmpty() || conductorActualizado.getNumeroIdentificacion() == null ) {
            return false;
        }
        String idConductor = conductorActualizado.getNumeroIdentificacion().trim().toUpperCase();
        if (conductoresRegistrados.containsKey(idConductor)) {

            conductoresRegistrados.put(idConductor, conductorActualizado);
            System.out.println("Conductor con ID " + conductorActualizado.getNumeroIdentificacion() + " actualizado.");
            return true;
        }
        System.out.println("No se encontró conductor con ID " + conductorActualizado.getNumeroIdentificacion() + " para actualizar.");
        return false;
    }

    // Eliminar un conductor por número de identificación
    public boolean eliminarConductor(String numeroIdentificacion){

        if (numeroIdentificacion == null || numeroIdentificacion.trim().isEmpty()) {
            return false;
        }

        Conductor conductorEliminado = conductoresRegistrados.get(numeroIdentificacion);
        if (conductorEliminado != null) {

            conductorEliminado.getVehiculos().clear();
            System.out.println("Conductor con ID " + numeroIdentificacion + " y nombre '" + conductorEliminado.getNombre() + "' eliminado.");
            return true;
        }
        
        System.out.println("No se encontró conductor con ID " + numeroIdentificacion + " para eliminar.");
        return false;
    }
    
}
