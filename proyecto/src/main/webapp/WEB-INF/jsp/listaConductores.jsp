<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Conductores</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .mensaje-exito { color: green; font-weight: bold; }
        .mensaje-error { color: red; font-weight: bold; }
        .btn-nuevo { display: inline-block; padding: 10px 15px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; margin-bottom: 20px; }
    </style>
</head>
<body>
    <h1>Lista de Conductores</h1>

    <%-- Mensajes Flash (desde RedirectAttributes) --%>
    <c:if test="${not empty mensaje}">
        <p class="mensaje-exito">${mensaje}</p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="mensaje-error">${error}</p>
    </c:if>

    <a href="<c:url value='/conductores/nuevo'/>" class="btn-nuevo">Agregar Nuevo Conductor</a>

    <c:choose>
        <c:when test="${not empty conductores}">
            <table>
                <thead>
                    <tr>
                        <th>ID Interno</th>
                        <th>Nombre</th>
                        <th>Cargo</th>
                        <th>Tipo Identificación</th>
                        <th>Número Identificación</th>
                        <th>Vehículos Asignados</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="conductor" items="${conductores}">
                        <tr>
                            <td>${conductor.id}</td>
                            <td>${conductor.nombre}</td>
                            <td>${conductor.cargo}</td>
                            <td>${conductor.tipoIdentificacion}</td>
                            <td>${conductor.numeroIdentificacion}</td>
                            <td>${conductor.vehiculos.size()}</td>
                            <td>
                                <a href="<c:url value='/conductores/${conductor.numeroIdentificacion}'/>">Ver Detalles</a>
                                <%-- Aquí podrías añadir enlaces para editar o eliminar --%>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>No hay conductores registrados.</p>
        </c:otherwise>
    </c:choose>
</body>
</html>