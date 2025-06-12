<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Detalle de Vehículo</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; color: #333; }
        h1 { color: #2c3e50; text-align: center; margin-bottom: 30px; }
        .container { max-width: 700px; margin: 0 auto; background-color: #fff; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .details-box { border: 1px solid #ddd; padding: 15px; margin-top: 20px; border-radius: 5px; background-color: #fdfdfd; }
        .details-box p { margin: 8px 0; line-height: 1.5; }
        .details-box strong { color: #555; }
        .back-link { display: inline-block; margin-top: 25px; text-decoration: none; color: #007bff; font-weight: bold; }
        .back-link:hover { text-decoration: underline; }
        .form-actions { margin-top: 30px; padding: 20px; border: 1px solid #eee; border-radius: 5px; background-color: #f9f9f9; }
        .form-actions h2 { margin-top: 0; color: #34495e; font-size: 1.2em; border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 20px;}
        .form-actions h3 { margin-top: 15px; font-size: 1.1em; color: #555; }
        .form-actions select, .form-actions button { padding: 10px 15px; margin-right: 10px; border-radius: 4px; border: 1px solid #ccc; font-size: 0.95em; }
        .form-actions button { background-color: #007bff; color: white; border: none; cursor: pointer; }
        .form-actions button:hover { background-color: #0056b3; }
        .delete-btn { background-color: #dc3545; }
        .delete-btn:hover { background-color: #c82333; }
        .desasignar-btn { background-color: #ffc107; color: #333; border-color: #ffc107; }
        .desasignar-btn:hover { background-color: #e0a800; }
        .message-success { color: #28a745; background-color: #d4edda; border: 1px solid #c3e6cb; padding: 10px; border-radius: 5px; margin-bottom: 15px; }
        .message-error { color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 5px; margin-bottom: 15px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Detalle del Vehículo: ${vehiculo.placa}</h1>

        <c:if test="${not empty mensaje}">
            <p class="message-success">${mensaje}</p>
        </c:if>
        <c:if test="${not empty error}">
            <p class="message-error">${error}</p>
        </c:if>

        <div class="details-box">
            <p><strong>ID Interno:</strong> ${vehiculo.id}</p>
            <p><strong>Placa:</strong> ${vehiculo.placa}</p>
            <p><strong>Marca:</strong> ${vehiculo.marca}</p>
            <p><strong>Modelo/Año:</strong> ${vehiculo.modeloAno}</p>
            <p><strong>Cilindraje:</strong> ${vehiculo.cilindraje}</p>
            <p><strong>Tipo de Combustible:</strong> ${vehiculo.tipoCombustible}</p>
            <p><strong>Número de Motor:</strong> ${vehiculo.numeroMotor}</p>
            <p><strong>Conductor Asignado:</strong>
                <%-- Lógica para encontrar el conductor asignado a este vehículo --%>
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
            </p>
        </div>

        <div class="form-actions">
            <h2>Acciones del Vehículo</h2>

            <%-- Formulario para asignar a un conductor --%>
            <c:if test="${empty conductorAsignado}">
                <h3>Asignar a Conductor:</h3>
                <form action="<c:url value='/vehiculos/${vehiculo.placa}/asignar'/>" method="post">
                    <label for="conductorId">Seleccionar Conductor:</label>
                    <select name="numeroIdentificacionConductor" id="conductorId">
                        <c:forEach var="conductor" items="${conductoresDisponibles}">
                            <option value="${conductor.numeroIdentificacion}">${conductor.nombre} (${conductor.numeroIdentificacion})</option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-primary">Asignar Vehículo</button>
                </form>
            </c:if>

            <%-- Formulario para desasignar de un conductor --%>
            <c:if test="${not empty conductorAsignado}">
                <h3>Desasignar Vehículo:</h3>
                <form action="<c:url value='/vehiculos/${vehiculo.placa}/desasignar'/>" method="post">
                    <button type="submit" class="btn desasignar-btn">Desasignar de ${conductorAsignado.nombre}</button>
                </form>
            </c:if>

            <%-- Formulario para eliminar vehículo --%>
            <h3>Eliminar Vehículo:</h3>
            <form action="<c:url value='/vehiculos/${vehiculo.placa}/eliminar'/>" method="post" onsubmit="return confirm('¿Estás seguro de que quieres eliminar este vehículo? Esto lo desvinculará de cualquier conductor y lo removerá del sistema.');">
                <button type="submit" class="btn delete-btn">Eliminar Vehículo</button>
            </form>
        </div>

        <a href="<c:url value='/vehiculos'/>" class="back-link">Volver a la lista de Vehículos</a>
    </div>
</body>
</html>