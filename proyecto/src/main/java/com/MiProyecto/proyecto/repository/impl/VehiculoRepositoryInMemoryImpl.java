package com.MiProyecto.proyecto.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.MiProyecto.proyecto.model.Vehiculo;
import com.MiProyecto.proyecto.repository.VehiculoRepository;

@Repository
public class VehiculoRepositoryInMemoryImpl implements VehiculoRepository {
   // Este mapa es ahora propiedad del repositorio
    private final Map<String, Vehiculo> vehiculosDB = new HashMap<>();

    @Override
    public List<Vehiculo> findAll() {
        return new ArrayList<>(vehiculosDB.values());
    }

    @Override
    public Optional<Vehiculo> findByPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(vehiculosDB.get(placa.trim().toUpperCase()));
    }

    @Override
    public void save(Vehiculo vehiculo) {
        // La lógica de save es idempotente: añade si no existe, actualiza si existe
        if (vehiculo != null && vehiculo.getPlaca() != null && !vehiculo.getPlaca().trim().isEmpty()) {
            vehiculosDB.put(vehiculo.getPlaca().trim().toUpperCase(), vehiculo);
        }
    }

    @Override
    public boolean deleteByPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return false;
        }
        // remove() retorna el valor eliminado o null si no existe
        return vehiculosDB.remove(placa.trim().toUpperCase()) != null;
    }

    @Override
    public boolean existsByPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return false;
        }
        return vehiculosDB.containsKey(placa.trim().toUpperCase());
    }
}