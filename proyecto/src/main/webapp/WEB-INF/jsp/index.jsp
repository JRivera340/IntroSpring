<%-- src/main/webapp/WEB-INF/jsp/index.jsp --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inicio - Gestión de Vehículos y Conductores</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; color: #333; display: flex; justify-content: center; align-items: center; min-height: 90vh; }
        .container { background-color: #fff; padding: 40px; border-radius: 8px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); text-align: center; max-width: 500px; width: 100%; }
        h1 { color: #2c3e50; margin-bottom: 40px; font-size: 2.2em; }
        .nav-buttons a {
            display: block; /* Cada botón en su propia línea */
            width: 80%; /* Ancho de los botones */
            margin: 15px auto; /* Centrar y espaciar */
            padding: 15px 25px;
            text-decoration: none;
            color: white;
            background-color: #007bff;
            border-radius: 6px;
            font-size: 1.1em;
            font-weight: bold;
            transition: background-color 0.3s ease, transform 0.2s ease;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .nav-buttons a:hover {
            background-color: #0056b3;
            transform: translateY(-2px); /* Pequeño efecto al pasar el ratón */
        }
        .nav-buttons a:nth-child(2) { background-color: #28a745; } /* Color diferente para Ver Conductores */
        .nav-buttons a:nth-child(2):hover { background-color: #218838; }
        .nav-buttons a:nth-child(3) { background-color: #ffc107; color: #333; } /* Color diferente para Asignar Vehículo */
        .nav-buttons a:nth-child(3):hover { background-color: #e0a800; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Bienvenido al Sistema de Gestión</h1>
        <div class="nav-buttons">
            <a href="<c:url value='/vehiculos'/>">Ver y Gestionar Vehículos</a>
            <a href="<c:url value='/conductores'/>">Ver y Gestionar Conductores</a>
        </div>
    </div>
</body>
</html>