package com.MiProyecto.proyecto.service;


//Para el tema de las anotaciones
import com.MiProyecto.proyecto.model.Vehiculo;
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

    // Constructor para inyección de dependencias. Spring buscará un ConductorService para inyectar aquí.
    public VehiculoService(ConductorService conductorService) {
        this.conductorService = conductorService;
    }


    //Permite la inyeccion de dependencias
    public void setConductorService(ConductorService conductorService){
        this.conductorService = conductorService;
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

    private void registrarVehiculosGlobalmente(Vehiculo vehiculo){
        if (vehiculo != null && vehiculo.getPlaca() != null) {
            vehiculosRegistrados.put(vehiculo.getPlaca().trim().toUpperCase(), vehiculo);
        }
    }

     // Agregar un vehículo A UN CONDUCTOR
    public boolean agregarVehiculoAConductor(Vehiculo nuevVehiculo, String numeroIdentificacionConductor){
        if (nuevVehiculo == null && numeroIdentificacionConductor == null) {
            return false;
        }

        //Guarda el vehiculo en el hashmap
        registrarVehiculosGlobalmente(nuevVehiculo);

        if (conductorService == null) {
            System.err.println("Error: ConductorService no ha sido inyectado en VehiculoService.");
            return false;
        }
        Optional<Conductor> conductorOpt = conductorService.obtenerConductorPorIdentificacion(numeroIdentificacionConductor);

        //Si esta presente sigue con el codigo y con la funcion .get() en conductorOpt se extrae el objeto.
        if (conductorOpt.isPresent()) {

            Conductor conductor = conductorOpt.get();
            //Se agrega el vehiculo al conductor
            conductor.agregarVehiculo(nuevVehiculo);
            System.out.println("Se ha agregado el vehiculo "+nuevVehiculo.getPlaca()+" Al conductor "+conductor.getNombre());
            return true;
        }else {
            System.out.println("Error al encontrar el Conductor"+numeroIdentificacionConductor);
            return false;
        }

    
    }

    // Método para agregar un vehículo al registro general sin asignarlo a un conductor
    public void registrarVehiculo(Vehiculo vehiculo){
        registrarVehiculosGlobalmente(vehiculo);
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
