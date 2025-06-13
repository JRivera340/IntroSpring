package com.MiProyecto.proyecto.service;


//Para el tema de las anotaciones
import com.MiProyecto.proyecto.model.Vehiculo;
import com.MiProyecto.proyecto.repository.ConductorRepository;
import com.MiProyecto.proyecto.repository.VehiculoRepository;

import jakarta.annotation.PostConstruct;

import com.MiProyecto.proyecto.model.Conductor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;  // Para manejar casos donde un vehículo no se encuentra

@Service
public class VehiculoService {
    

    private ConductorService conductorService; // Esta dependencia será inyectada

   // Ahora inyectamos las interfaces de los repositorios
    private final VehiculoRepository vehiculoRepository;
    private final ConductorRepository conductorRepository;
    

    // Constructor actualizado para inyectar ambos repositorios y el ConductorService (si es necesario)
    public VehiculoService(VehiculoRepository vehiculoRepository, ConductorRepository conductorRepository, ConductorService conductorService) {
        this.vehiculoRepository = vehiculoRepository;
        this.conductorRepository = conductorRepository;
        this.conductorService = conductorService; // Inyecta ConductorService
    }

     @PostConstruct
    public void inicializarDatos() {
        System.out.println("Iniciando la inicialización de datos de Vehículos y Conductores...");

        // --- 1. Crear 9 vehículos diferentes ---
        Vehiculo v1 = new Vehiculo("v-001", "ABC123", 1600, "Gasolina", "MOTOR001", "Toyota", 2020);
        Vehiculo v2 = new Vehiculo("v-002", "DEF456", 1800, "Diesel", "MOTOR002", "Nissan", 2018);
        Vehiculo v3 = new Vehiculo("v-003", "GHI789", 2000, "Gasolina", "MOTOR003", "Ford", 2022);
        Vehiculo v4 = new Vehiculo("v-004", "JKL012", 1500, "Gasolina", "MOTOR004", "Honda", 2019);
        Vehiculo v5 = new Vehiculo("v-005", "MNO345", 2500, "Gasolina", "MOTOR005", "Mazda", 2021);
        Vehiculo v6 = new Vehiculo("v-006", "PQR678", 1600, "Eléctrico", "MOTOR006", "Hyundai", 2023);
        Vehiculo v7 = new Vehiculo("v-007", "STU901", 2000, "Gasolina", "MOTOR007", "Kia", 2017);
        Vehiculo v8 = new Vehiculo("v-008", "VWX234", 3000, "Diesel", "MOTOR008", "BMW", 2024);
        Vehiculo v9 = new Vehiculo("v-009", "YZA567", 4000, "Gasolina", "MOTOR009", "Mercedes", 2020);

        // Agregamos todos los vehículos al registro global de VehiculoService
        // El método registrarVehiculo ya se encarga de ponerlos en el mapa
        registrarVehiculo(v1);
        registrarVehiculo(v2);
        registrarVehiculo(v3);
        registrarVehiculo(v4);
        registrarVehiculo(v5);
        registrarVehiculo(v6);
        registrarVehiculo(v7);
        registrarVehiculo(v8);
        registrarVehiculo(v9);
        System.out.println("9 vehículos registrados en el sistema.");


        // --- 2. Crear 3 conductores y asignarles al menos 3 vehículos a cada uno ---
        // Conductor 1
        Conductor c1 = new Conductor("Juan Perez", "Gerente", "CC", "100000001");
        conductorService.agregarConductor(c1); // Primero registra el conductor en ConductorService
        // Ahora asigna los vehículos. Esto debería ir a través de los métodos de la clase Conductor
        c1.agregarVehiculo(v1);
        c1.agregarVehiculo(v2);
        c1.agregarVehiculo(v3);
        System.out.println("Conductor Juan Perez inicializado con 3 vehículos.");

        // Conductor 2
        Conductor c2 = new Conductor("Maria Lopez", "Analista", "CC", "100000002");
        conductorService.agregarConductor(c2);
        c2.agregarVehiculo(v4);
        c2.agregarVehiculo(v5);
        c2.agregarVehiculo(v6);
        System.out.println("Conductor Maria Lopez inicializada con 3 vehículos.");

        // Conductor 3
        Conductor c3 = new Conductor("Carlos Gomez", "Supervisor", "CC", "100000003");
        conductorService.agregarConductor(c3);
        c3.agregarVehiculo(v7);
        c3.agregarVehiculo(v8);
        c3.agregarVehiculo(v9);
        System.out.println("Conductor Carlos Gomez inicializado con 3 vehículos.");

        System.out.println("¡Inicialización de datos completada!");
    }

    public List<Vehiculo> obtenerTodosLosVehiculos(){
            return vehiculoRepository.findAll();
    }

    // Código CORREGIDO y BUENAS PRÁCTICAS
    public Optional<Vehiculo> obtenerVehiculoPorPlaca(String placa){
        // PRIMERO validamos la entrada en la capa de servicio
        if (placa == null || placa.trim().isEmpty()) {
            return Optional.empty(); // Si es inválido, el servicio decide no buscar y retorna vacío.
        }
        // SI la placa es válida, entonces delegamos al repositorio.
        return vehiculoRepository.findByPlaca(placa.trim().toUpperCase()); // Opcional: limpiar y normalizar la placa
    }

     // --- El método problemático: ASIGNAR un VEHÍCULO EXISTENTE a un CONDUCTOR ---
    // Este método es el que el controlador espera cuando llama:
    // vehiculoService.asignarVehiculoAConductor(placa, numeroIdentificacionConductor);
    // Asignar un vehículo existente a un conductor
    public boolean asignarVehiculoAConductor(String placaVehiculo, String numeroIdentificacionConductor) {
        if (placaVehiculo == null || placaVehiculo.trim().isEmpty() ||
            numeroIdentificacionConductor == null || numeroIdentificacionConductor.trim().isEmpty()) {
            return false;
        }

        Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findByPlaca(placaVehiculo);
        Optional<Conductor> conductorOpt = conductorRepository.findByIdentificacion(numeroIdentificacionConductor);

        if (vehiculoOpt.isPresent() && conductorOpt.isPresent()) {
            Vehiculo vehiculoParaAsignar = vehiculoOpt.get();
            Conductor conductorObjetivo = conductorOpt.get();

            // Validar que el vehículo no esté ya asignado a otro conductor
            for (Conductor c : conductorRepository.findAll()) { // Usar el repositorio para obtener todos los conductores
                if (c.getVehiculos().stream().anyMatch(v -> v.getPlaca().equalsIgnoreCase(placaVehiculo))) {
                    if (!c.getNumeroIdentificacion().equalsIgnoreCase(conductorObjetivo.getNumeroIdentificacion())) {
                        c.eliminarVehiculo(placaVehiculo);
                        conductorRepository.save(c); // Guardar el conductor actualizado
                        System.out.println("Vehículo " + placaVehiculo + " desasignado de su conductor anterior: " + c.getNombre());
                    } else {
                        System.out.println("El vehículo " + placaVehiculo + " ya está asignado al conductor " + conductorObjetivo.getNombre());
                        return true;
                    }
                }
            }

            conductorObjetivo.agregarVehiculo(vehiculoParaAsignar);
            conductorRepository.save(conductorObjetivo); // Guardar el conductor actualizado
            System.out.println("Vehículo " + placaVehiculo + " asignado a " + conductorObjetivo.getNombre());
            return true;
        } else {
            System.out.println("Error al asignar vehículo: Vehículo (" + placaVehiculo + ") o Conductor (" + numeroIdentificacionConductor + ") no encontrado.");
            return false;
        }
    }

         // Método para desasignar un vehículo de CUALQUIER conductor
   // Método para desasignar un vehículo de CUALQUIER conductor
    public boolean desasignarVehiculoDeConductor(String placaVehiculo) {
        if (placaVehiculo == null || placaVehiculo.trim().isEmpty()) {
            return false;
        }

        for (Conductor conductor : conductorRepository.findAll()) { // Usar el repositorio para obtener todos
            if (conductor.eliminarVehiculo(placaVehiculo)) {
                conductorRepository.save(conductor); // Guardar el conductor actualizado
                System.out.println("Vehículo " + placaVehiculo + " desasignado del conductor " + conductor.getNombre());
                return true;
            }
        }
        System.out.println("Vehículo " + placaVehiculo + " no estaba asignado a ningún conductor o no se encontró.");
        return false;
    }

    // Método para agregar un vehículo al registro general (persistirlo)
    public Boolean registrarVehiculo(Vehiculo vehiculo){
        if (vehiculo == null || vehiculo.getPlaca().trim().isEmpty()) {
            return false;
        }
        // Usar el repositorio para verificar si existe
        if (vehiculoRepository.existsByPlaca(vehiculo.getPlaca())) {
            System.out.println("Este vehículo ya ha sido registrado: " + vehiculo.getPlaca());
            return false;
        }
        vehiculoRepository.save(vehiculo); // Usar el repositorio para guardar
        System.out.println("Vehiculo "+ vehiculo.getPlaca()+" Agregado");
        return true;
    }

     // Eliminar un vehículo específico usando la placa
    public boolean eliminarVehiculo(String placa){
        if (placa == null || placa.trim().isEmpty()) {
            return false;
        }

        boolean removedFromGlobal = vehiculoRepository.deleteByPlaca(placa); // Eliminar del repositorio

        if (removedFromGlobal) {
            System.out.println("Vehículo " + placa + " eliminado del registro global.");
            // Recorrer todos los conductores y desvincular el vehículo si lo tienen
            conductorRepository.findAll().forEach(conductor -> {
                boolean removedFromConductor = conductor.eliminarVehiculo(placa);
                if (removedFromConductor) {
                    conductorRepository.save(conductor); // Guardar el conductor actualizado
                    System.out.println("Vehículo " + placa + " también eliminado del conductor " + conductor.getNombre());
                }
            });
            return true;
        }
        System.out.println("Vehículo con placa " + placa + " no encontrado para eliminar.");
        return false;
    }

}
