<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Vehículos</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; color: #333; }
        h1 { color: #2c3e50; text-align: center; margin-bottom: 30px; }
        .container { max-width: 900px; margin: 0 auto; background-color: #fff; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background-color: #e8e8e8; font-weight: bold; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        .message-success { color: #28a745; background-color: #d4edda; border: 1px solid #c3e6cb; padding: 10px; border-radius: 5px; margin-bottom: 15px; }
        .message-error { color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 5px; margin-bottom: 15px; }
        .btn { display: inline-block; padding: 10px 18px; border-radius: 5px; text-decoration: none; font-weight: bold; transition: background-color 0.3s ease; }
        .btn-primary { background-color: #007bff; color: white; border: none; }
        .btn-primary:hover { background-color: #0056b3; }
        .btn-info { background-color: #17a2b8; color: white; border: none; }
        .btn-info:hover { background-color: #138496; }
        .nav-links { text-align: center; margin-bottom: 25px; }
        .nav-links a { margin: 0 10px; color: #007bff; text-decoration: none; font-weight: bold; }
        .nav-links a:hover { text-decoration: underline; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input[type="text"] { width: calc(100% - 22px); padding: 10px; border: 1px solid #ddd; border-radius: 4px; }
        .form-group button { padding: 10px 20px; background-color: #28a745; color: white; border: none; border-radius: 5px; cursor: pointer; transition: background-color 0.3s ease; }
        .form-group button:hover { background-color: #218838; }
    </style>
</head>
<body>
    <div class="container">
        <div class="nav-links">
            <a href="<c:url value='/conductores'/>">Ver Conductores</a>
            <a href="<c:url value='/vehiculos'/>">Ver Vehículos</a>
        </div>

        <h1>Lista de Vehículos</h1>

        <c:if test="${not empty mensaje}">
            <p class="message-success">${mensaje}</p>
        </c:if>
        <c:if test="${not empty error}">
            <p class="message-error">${error}</p>
        </c:if>

        <div class="search-form-section" style="margin-bottom: 30px; padding: 20px; background-color: #e9ecef; border-radius: 8px;">
            <h2>Buscar Vehículo por Placa</h2>
            <form action="<c:url value='/vehiculos/buscar'/>" method="get">
                <div class="form-group">
                    <label for="placaBusqueda">Placa del Vehículo:</label>
                    <input type="text" id="placaBusqueda" name="placa" placeholder="Ingrese la placa" required>
                </div>
                <button type="submit" class="btn btn-primary">Buscar Vehículo</button>
            </form>
        </div>

        <div class="action-buttons">
            <a href="<c:url value='/'/>" class="btn btn-primary">Volver al Inicio</a>
            <a href="<c:url value='/vehiculos/nuevo'/>" class="btn btn-primary">Agregar Nuevo Vehículo</a>
        </div>

        <c:choose>
            <c:when test="${not empty vehiculos}">
                <table>
                    <thead>
                        <tr>
                            <th>ID Interno</th>
                            <th>Placa</th>
                            <th>Marca</th>
                            <th>Modelo/Año</th>
                            <th>Cilindraje</th>
                            <th>Tipo Combustible</th>
                            <th>Número de Motor</th>
                            <th>Conductor Asignado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="vehiculo" items="${vehiculos}">
                            <tr>
                                <td>${vehiculo.id}</td>
                                <td>${vehiculo.placa}</td>
                                <td>${vehiculo.marca}</td>
                                <td>${vehiculo.modeloAno}</td>
                                <td>${vehiculo.cilindraje}</td>
                                <td>${vehiculo.tipoCombustible}</td>
                                <td>${vehiculo.numeroMotor}</td>
                                <td>
                                    <c:set var="conductorAsignado" value="${null}"/>
                                    <c:forEach var="conductor" items="${conductoresDisponibles}">
                                        <c:forEach var="v" items="${conductor.vehiculos}">
                                            <c:if test="${v.placa eq vehiculo.placa}">
                                                <c:set var="conductorAsignado" value="${conductor}"/>
                                            </c:if>
                                        </c:forEach>
                                    </c:forEach>

                                    <c:if test="${not empty conductorAsignado}">
                                        <a href="<c:url value='/conductores/${conductorAsignado.numeroIdentificacion}'/>">
                                            ${conductorAsignado.nombre} (${conductorAsignado.numeroIdentificacion})
                                        </a>
                                    </c:if>
                                    <c:if test="${empty conductorAsignado}">
                                        No asignado
                                    </c:if>
                                </td>
                                <td>
                                    <a href="<c:url value='/vehiculos/${vehiculo.placa}'/>" class="btn btn-info btn-sm">Ver Detalles</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p>No hay vehículos registrados.</p>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>