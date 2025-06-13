package com.MiProyecto.proyecto.service;

import com.MiProyecto.proyecto.model.Conductor;
import com.MiProyecto.proyecto.repository.ConductorRepository; // Importa la interfaz del repositorio
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConductorService {

    // Ahora inyectamos la interfaz del repositorio
    private final ConductorRepository conductorRepository;

    // Constructor actualizado para inyectar el repositorio
    public ConductorService(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    public Optional<Conductor> obtenerConductorPorIdentificacion(String numeroIdentificacionConductor) {
        if(numeroIdentificacionConductor == null || numeroIdentificacionConductor.trim().isEmpty()){
            return Optional.empty();
        }
        return conductorRepository.findByIdentificacion(numeroIdentificacionConductor); // Usar el repositorio
    }

    public List<Conductor> obtenerTodosLosConductores() {
        return conductorRepository.findAll(); // Usar el repositorio
    }

    public boolean agregarConductor(Conductor conductor){
        if (conductor == null || conductor.getNumeroIdentificacion() == null || conductor.getNumeroIdentificacion().trim().isEmpty()) {
            return false;
        }
        // Usar el repositorio para verificar si existe
        if (conductorRepository.existsByIdentificacion(conductor.getNumeroIdentificacion())) {
            System.out.println("Advertencia: Ya existe un conductor con la identificación " + conductor.getNumeroIdentificacion());
            return false;
        }
        conductorRepository.save(conductor); // Usar el repositorio para guardar
        System.out.println("Conductor '" + conductor.getNombre() + "' agregado con ID: " + conductor.getNumeroIdentificacion());
        return true;
    }

    public boolean actualizarConductor(Conductor conductorActualizado){
        if (conductorActualizado == null || conductorActualizado.getNumeroIdentificacion().trim().isEmpty()) {
            return false;
        }
        // Usar el repositorio para verificar si existe
        if (conductorRepository.findByIdentificacion(conductorActualizado.getNumeroIdentificacion()).isPresent()) {
            conductorRepository.save(conductorActualizado); // Usar el repositorio para guardar (actualizar)
            System.out.println("Conductor con ID " + conductorActualizado.getNumeroIdentificacion() + " actualizado.");
            return true;
        }
        System.out.println("No se encontró conductor con ID " + conductorActualizado.getNumeroIdentificacion() + " para actualizar.");
        return false;
    }

    public boolean eliminarConductor(String numeroIdentificacion){
        if (numeroIdentificacion == null || numeroIdentificacion.trim().isEmpty()) {
            return false;
        }

        Optional<Conductor> conductorOpt = conductorRepository.findByIdentificacion(numeroIdentificacion);
        if (conductorOpt.isPresent()) {
            Conductor conductorEliminado = conductorOpt.get();
            // Asegurarte de que al eliminar el conductor, sus vehículos también se "liberen"
            // Esto es lógica de negocio, no del repositorio.
            if (conductorEliminado.getVehiculos() != null) {
                conductorEliminado.getVehiculos().clear(); // Desvincula los vehículos del conductor
                // Podrías necesitar guardar el conductor después de limpiar sus vehículos,
                // pero si el repositorio lo elimina completamente, esto es redundante.
            }
            boolean deleted = conductorRepository.deleteByIdentificacion(numeroIdentificacion);
            if (deleted) {
                System.out.println("Conductor con ID " + numeroIdentificacion + " y nombre '" + conductorEliminado.getNombre() + "' eliminado.");
                return true;
            }
        }
        System.out.println("No se encontró conductor con ID " + numeroIdentificacion + " para eliminar.");
        return false;
    }
}