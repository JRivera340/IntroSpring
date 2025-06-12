package com.MiProyecto.proyecto.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Asegúrate de importar UUID

public class Conductor {
    private String id; // ID INTERNO del objeto (UUID generado)
    private String nombre;
    private String cargo;
    private String tipoIdentificacion;
    private String numeroIdentificacion; // ID DE NEGOCIO (ingresado por el usuario)
    private List<Vehiculo> vehiculos;

    // Constructor vacío (para JavaBeans y Spring Forms)
    public Conductor() {
        this.id = UUID.randomUUID().toString(); // Siempre genera un ID único al crear el objeto vacío
        this.vehiculos = new ArrayList<>();
    }

    // Constructor con campos para la carga inicial de datos o cuando se conoce el numeroIdentificacion
    // NOTA: No le pasamos el 'id' al constructor para que siempre se genere automáticamente
    public Conductor(String nombre, String cargo, String tipoIdentificacion, String numeroIdentificacion) {
        this.id = UUID.randomUUID().toString(); // Siempre genera un nuevo UUID
        this.nombre = nombre;
        this.cargo = cargo;
        this.tipoIdentificacion = tipoIdentificacion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.vehiculos = new ArrayList<>();
    }

    // --- Getters y Setters ---
    public String getId() { return id; }
    // No necesitamos un setter para el ID si siempre se genera automáticamente.
    // Si lo necesitas, solo asegúrate de no sobreescribir UUIDs existentes a menos que sea intencional.
    // public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }

    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public List<Vehiculo> getVehiculos() {
        return new ArrayList<>(vehiculos); // Devuelve una copia para evitar modificación externa
    }
    // Setter para la lista de vehículos (usado por ejemplo en deserialización, no por formulario)
    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    // --- Métodos de lógica de negocio del modelo ---
    public void agregarVehiculo(Vehiculo vehiculo) {
        if (vehiculo != null && !this.vehiculos.contains(vehiculo)) {
            this.vehiculos.add(vehiculo);
        }
    }

    public boolean eliminarVehiculo(String placa) {
        return this.vehiculos.removeIf(v -> v.getPlaca().equalsIgnoreCase(placa));
    }

    @Override
    public String toString() {
        return "Conductor{" +
               "id='" + id + '\'' +
               ", nombre='" + nombre + '\'' +
               ", cargo='" + cargo + '\'' +
               ", tipoIdentificacion='" + tipoIdentificacion + '\'' +
               ", numeroIdentificacion='" + numeroIdentificacion + '\'' +
               ", vehiculos=" + vehiculos.size() + " vehículos" +
               '}';
    }
}