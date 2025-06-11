package com.MiProyecto.proyecto.model;

import java.util.ArrayList;
import java.util.List; // Importar List

public class Conductor {
    private String id;
    private String nombre;
    private String cargo; 
    private String tipoIdentificacion; 
    private String numeroIdentificacion;
    private List<Vehiculo> vehiculos; 

  
    public Conductor(String id, String nombre, String cargo, String tipoIdentificacion, String numeroIdentificacion) {
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.tipoIdentificacion = tipoIdentificacion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.vehiculos = new ArrayList<>(); 
    }


    // --- Getters y Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }
    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

  
    public List<Vehiculo> getVehiculos() {
        return new ArrayList<>(vehiculos);
    }

   
    public void agregarVehiculo(Vehiculo vehiculo) {
        this.vehiculos.add(vehiculo);
    }

    public boolean eliminarVehiculo(String placa) {
        return this.vehiculos.removeIf(v -> v.getPlaca().equalsIgnoreCase(placa));
    }

    @Override
    public String toString() {
        return "Conductor{" +
               "id='" + id + '\'' +
               ", nombre='" + nombre + '\'' +
               ", numeroIdentificacion='" + numeroIdentificacion + '\'' +
               ", vehiculos=" + vehiculos.size() + " vehículos" +
               '}';
    }
}