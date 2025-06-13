package com.MiProyecto.proyecto.repository.impl;

import com.MiProyecto.proyecto.model.Conductor;
import com.MiProyecto.proyecto.repository.ConductorRepository;
import org.springframework.stereotype.Repository; // Importa la anotación @Repository

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository // Indica que es un componente de repositorio de Spring
public class ConductorRepositoryInMemoryImpl implements ConductorRepository {

    // Este mapa es ahora propiedad del repositorio
    private final Map<String, Conductor> conductoresDB = new HashMap<>();

    @Override
    public List<Conductor> findAll() {
        return new ArrayList<>(conductoresDB.values());
    }

    @Override
    public Optional<Conductor> findByIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            return Optional.empty();
        }
        // Usamos trim() para limpiar espacios y mantenemos la sensibilidad a mayúsculas/minúsculas para IDs
        // o puedes convertir a toUpperCase() si tus IDs son case-insensitive.
        return Optional.ofNullable(conductoresDB.get(identificacion.trim()));
    }

    @Override
    public void save(Conductor conductor) {
        if (conductor != null && conductor.getNumeroIdentificacion() != null && !conductor.getNumeroIdentificacion().trim().isEmpty()) {
            conductoresDB.put(conductor.getNumeroIdentificacion().trim(), conductor);
        }
    }

    @Override
    public boolean deleteByIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            return false;
        }
        return conductoresDB.remove(identificacion.trim()) != null;
    }

    @Override
    public boolean existsByIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            return false;
        }
        return conductoresDB.containsKey(identificacion.trim());
    }
}