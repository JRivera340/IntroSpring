package com.MiProyecto.proyecto.repository;

import com.MiProyecto.proyecto.model.Conductor;
import java.util.List;
import java.util.Optional;

public interface ConductorRepository {
    List<Conductor> findAll();
    Optional<Conductor> findByIdentificacion(String identificacion);
    void save(Conductor conductor); // Para añadir o actualizar un conductor
    boolean deleteByIdentificacion(String identificacion); // Para eliminar un conductor
    boolean existsByIdentificacion(String identificacion); // Para verificar si una identificación ya existe
}