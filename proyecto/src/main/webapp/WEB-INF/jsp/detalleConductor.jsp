<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Detalle de Conductor</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        .details-box { border: 1px solid #ddd; padding: 15px; margin-top: 20px; border-radius: 5px; }
        .details-box p { margin: 5px 0; }
        .vehiculos-list { margin-top: 15px; }
        .vehiculos-list ul { list-style-type: none; padding: 0; }
        .vehiculos-list li { background-color: #f9f9f9; border: 1px solid #eee; padding: 8px; margin-bottom: 5px; border-radius: 3px; }
        .back-link { display: inline-block; margin-top: 20px; text-decoration: none; color: #007bff; }
    </style>
</head>
<body>
    <h1>Detalle del Conductor: ${conductor.nombre}</h1>

    <div class="details-box">
        <p><strong>ID Interno:</strong> ${conductor.id}</p>
        <p><strong>Nombre:</strong> ${conductor.nombre}</p>
        <p><strong>Cargo:</strong> ${conductor.cargo}</p>
        <p><strong>Tipo de Identificación:</strong> ${conductor.tipoIdentificacion}</p>
        <p><strong>Número de Identificación:</strong> ${conductor.numeroIdentificacion}</p>
    </div>

    <div class="vehiculos-list">
        <h2>Vehículos Asignados (${conductor.vehiculos.size()}):</h2>
        <c:choose>
            <c:when test="${not empty conductor.vehiculos}">
                <ul>
                    <c:forEach var="vehiculo" items="${conductor.vehiculos}">
                        <li>
                            <strong>Placa:</strong> ${vehiculo.placa} |
                            <strong>Marca:</strong> ${vehiculo.marca} |
                            <strong>Modelo:</strong> ${vehiculo.modeloAno} |
                            <strong>Cilindraje:</strong> ${vehiculo.cilindraje} |
                            <strong>Tipo Combustible:</strong> ${vehiculo.tipoCombustible}
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <p>Este conductor no tiene vehículos asignados.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <a href="<c:url value='/conductores'/>" class="back-link">Volver a la lista de Conductores</a>
</body>
</html>