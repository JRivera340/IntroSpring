package com.MiProyecto.proyecto.service;


//Para el tema de las anotaciones
import com.MiProyecto.proyecto.model.Vehiculo;

import jakarta.annotation.PostConstruct;

import com.MiProyecto.proyecto.model.Conductor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;  // Para manejar casos donde un vehículo no se encuentra

@Service
public class VehiculoService {
    private final Map<String, Vehiculo> vehiculosRegistrados = new HashMap<>();

    private ConductorService conductorService; // Esta dependencia será inyectada

   
    

    //Permite la inyeccion de dependencias
    public VehiculoService(ConductorService conductorService) {
        this.conductorService = conductorService;
        // Opcional: Aquí podrías inicializar tus vehículos de prueba si no lo haces en otro lugar
        // para cumplir con el punto 3 del taller.
        // Por ejemplo:
        // inicializarVehiculosDePrueba();
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
        return new ArrayList<>(vehiculosRegistrados.values());
    }

    public Optional<Vehiculo> obtenerVehiculoPorPlaca(String placa){
        if (placa == null || placa.trim().isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(vehiculosRegistrados.get(placa.trim().toUpperCase()));
    }

     // --- El método problemático: ASIGNAR un VEHÍCULO EXISTENTE a un CONDUCTOR ---
    // Este método es el que el controlador espera cuando llama:
    // vehiculoService.asignarVehiculoAConductor(placa, numeroIdentificacionConductor);
    public boolean asignarVehiculoAConductor(String placaVehiculo, String numeroIdentificacionConductor) {
        if (placaVehiculo == null || placaVehiculo.trim().isEmpty() ||
            numeroIdentificacionConductor == null || numeroIdentificacionConductor.trim().isEmpty()) {
            return false;
        }

        Optional<Vehiculo> vehiculoOpt = obtenerVehiculoPorPlaca(placaVehiculo); // Obtener el vehículo EXISTENTE
        Optional<Conductor> conductorOpt = conductorService.obtenerConductorPorIdentificacion(numeroIdentificacionConductor);

        if (vehiculoOpt.isPresent() && conductorOpt.isPresent()) {
            Vehiculo vehiculoParaAsignar = vehiculoOpt.get(); // El vehículo a asignar
            Conductor conductorObjetivo = conductorOpt.get(); // El conductor al que se asignará

            // Validar que el vehículo no esté ya asignado a otro conductor
            // Para hacer esto, necesitamos buscar el vehículo en las listas de TODOS los conductores.
            for (Conductor c : conductorService.obtenerTodosLosConductores()) {
                if (c.getVehiculos().stream().anyMatch(v -> v.getPlaca().equalsIgnoreCase(placaVehiculo))) {
                    // Si el vehículo está en la lista de CUALQUIER conductor, lo desvinculamos primero.
                    // Esto evita que un mismo vehículo esté asignado a múltiples conductores.
                    if (!c.getNumeroIdentificacion().equalsIgnoreCase(conductorObjetivo.getNumeroIdentificacion())) {
                        // Si está asignado a un conductor DIFERENTE al objetivo, lo desasignamos.
                        c.eliminarVehiculo(placaVehiculo);
                        System.out.println("Vehículo " + placaVehiculo + " desasignado de su conductor anterior: " + c.getNombre());
                    } else {
                        // Si ya está asignado al conductor objetivo, no hacemos nada y consideramos exitoso
                        System.out.println("El vehículo " + placaVehiculo + " ya está asignado al conductor " + conductorObjetivo.getNombre());
                        return true;
                    }
                }
            }

            // Finalmente, agrega el vehículo a la lista del conductor objetivo
            conductorObjetivo.agregarVehiculo(vehiculoParaAsignar);
            System.out.println("Vehículo " + placaVehiculo + " asignado a " + conductorObjetivo.getNombre());
            return true;
        } else {
            System.out.println("Error al asignar vehículo: Vehículo (" + placaVehiculo + ") o Conductor (" + numeroIdentificacionConductor + ") no encontrado.");
            return false;
        }
    }

         // Método para desasignar un vehículo de CUALQUIER conductor
    public boolean desasignarVehiculoDeConductor(String placaVehiculo) {
        if (placaVehiculo == null || placaVehiculo.trim().isEmpty()) {
            return false;
        }

        // Buscamos en las listas de vehículos de TODOS los conductores
        for (Conductor conductor : conductorService.obtenerTodosLosConductores()) {
            if (conductor.eliminarVehiculo(placaVehiculo)) {
                System.out.println("Vehículo " + placaVehiculo + " desasignado del conductor " + conductor.getNombre());
                return true; // Se encontró y se eliminó de un conductor
            }
        }
        System.out.println("Vehículo " + placaVehiculo + " no estaba asignado a ningún conductor o no se encontró.");
        return false;
    }

    // Método para agregar un vehículo al registro general sin asignarlo a un conductor
    public Boolean registrarVehiculo(Vehiculo vehiculo){
        if (vehiculo == null || vehiculo.getPlaca().trim().isEmpty()) {
            return false;
        }
        String idVehiculo = vehiculo.getPlaca().trim().toUpperCase();
        if (vehiculosRegistrados.containsKey(idVehiculo)) {
            System.out.println("Este vehiculo ya ha sido registrado");
            return false;
        }
        vehiculosRegistrados.put(idVehiculo, vehiculo);
        System.out.println("Vehiculo "+ vehiculo.getPlaca()+" Agregado");
        return true;
    }

    // Eliminar un vehículo específico usando la placa
    public boolean eliminarVehiculo(String placa){
        if (placa == null || placa.trim().isEmpty()) {
            return false;
        }

        Vehiculo vehiculoEliminado = vehiculosRegistrados.remove(placa.trim().toUpperCase());

       if (vehiculoEliminado != null) {
            System.out.println("Vehículo " + placa + " eliminado del registro global.");
            if (conductorService != null) {
                conductorService.obtenerTodosLosConductores().forEach(conductor -> {
                    boolean removedFromConductor = conductor.eliminarVehiculo(placa);
                    if (removedFromConductor) {
                        System.out.println("Vehículo " + placa + " también eliminado del conductor " + conductor.getNombre());
                    }
                });
            } else {
                System.err.println("Advertencia: ConductorService no está disponible para desvincular el vehículo de los conductores.");
            }
            return true;
        }
        System.out.println("Vehículo con placa " + placa + " no encontrado para eliminar.");
        return false;
    }
}
