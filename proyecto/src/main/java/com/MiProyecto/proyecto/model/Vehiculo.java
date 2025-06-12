package com.MiProyecto.proyecto.model;

import java.util.ArrayList;
import java.util.UUID;

public class Vehiculo {
    private String id;
    private String placa;
    private int cilindraje;
    private String tipoCombustible; 
    private String numeroMotor; 
    private String marca;
    private int modeloAno; 

    public Vehiculo() {
        this.id = UUID.randomUUID().toString(); // Siempre genera un ID único al crear el objeto vacío
    }

    public Vehiculo(String id, String placa, int cilindraje, String tipoCombustible, String numeroMotor, String marca,
                    int modeloAno) {
        this.id = id;
        this.placa = placa;
        this.cilindraje = cilindraje;
        this.tipoCombustible = tipoCombustible;
        this.numeroMotor = numeroMotor;
        this.marca = marca;
        this.modeloAno = modeloAno;
    }

    // --- Getters y Setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public int getCilindraje() { return cilindraje; } // Getter actualizado
    public void setCilindraje(int cilindraje) { this.cilindraje = cilindraje; } // Setter actualizado
    public String getTipoCombustible() { return tipoCombustible; } // Getter actualizado
    public void setTipoCombustible(String tipoCombustible) { this.tipoCombustible = tipoCombustible; } // Setter actualizado
    public String getNumeroMotor() { return numeroMotor; } // Getter actualizado
    public void setNumeroMotor(String numeroMotor) { this.numeroMotor = numeroMotor; } // Setter actualizado
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public int getModeloAno() { return modeloAno; } // Getter actualizado
    public void setModeloAno(int modeloAno) { this.modeloAno = modeloAno; } // Setter actualizado

    @Override
    public String toString() {
        return "Vehiculo{" +
               "placa='" + placa + '\'' +
               ", marca='" + marca + '\'' +
               ", modeloAno=" + modeloAno +
               ", cilindraje=" + cilindraje +
               '}';
    }
}