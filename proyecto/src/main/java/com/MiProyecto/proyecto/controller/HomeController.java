// src/main/java/com/MiProyecto/proyecto/controller/HomeController.java

package com.MiProyecto.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/") // Esta es la ruta raíz de tu aplicación
    public String home() {
        return "index"; // Esto buscará index.jsp en tu carpeta WEB-INF/jsp/
    }
}