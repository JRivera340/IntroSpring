package com.MiProyecto.proyecto.repository;

import com.MiProyecto.proyecto.model.Vehiculo;
import java.util.List;
import java.util.Optional;

public interface VehiculoRepository {
    List<Vehiculo> findAll();
    Optional<Vehiculo> findByPlaca(String placa);
    void save(Vehiculo vehiculo); // Para añadir o actualizar un vehículo
    boolean deleteByPlaca(String placa); // Para eliminar por placa
    boolean existsByPlaca(String placa); // Para verificar si una placa ya existe
}