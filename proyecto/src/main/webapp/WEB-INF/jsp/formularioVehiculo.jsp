<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agregar Nuevo Vehículo</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; color: #333; }
        h1 { color: #2c3e50; text-align: center; margin-bottom: 30px; }
        .container { max-width: 600px; margin: 0 auto; background-color: #fff; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        form { margin-top: 20px; padding: 20px; border: 1px solid #ddd; border-radius: 5px; background-color: #fdfdfd; }
        form div { margin-bottom: 15px; }
        form label { display: block; margin-bottom: 5px; font-weight: bold; color: #555; }
        form input[type="text"], form input[type="number"] { width: calc(100% - 22px); padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        form button { padding: 10px 20px; background-color: #28a745; color: white; border: none; border-radius: 5px; cursor: pointer; font-size: 1em; font-weight: bold; }
        form button:hover { background-color: #218838; }
        .message-error { color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 5px; margin-bottom: 15px; }
        .back-link { display: inline-block; margin-top: 25px; text-decoration: none; color: #007bff; font-weight: bold; }
        .back-link:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Agregar Nuevo Vehículo</h1>

        <c:if test="${not empty error}">
            <p class="message-error">${error}</p>
        </c:if>

        <form:form action="${pageContext.request.contextPath}/vehiculos/guardar" method="post" modelAttribute="vehiculo">
            <div>
                <label for="placa">Placa:</label>
                <form:input path="placa" id="placa" type="text" required="true"/>
            </div>
            <div>
                <label for="marca">Marca:</label>
                <form:input path="marca" id="marca" type="text" required="true"/>
            </div>
            <div>
                <label for="modeloAno">Modelo/Año:</label>
                <form:input path="modeloAno" id="modeloAno" type="number" required="true"/>
            </div>
            <div>
                <label for="cilindraje">Cilindraje:</label>
                <form:input path="cilindraje" id="cilindraje" type="number" required="true"/> <%-- Cambiado a number --%>
            </div>
            <div>
                <label for="tipoCombustible">Tipo de Combustible:</label>
                <form:input path="tipoCombustible" id="tipoCombustible" type="text" required="true"/>
            </div>
            <div>
                <label for="numeroMotor">Número de Motor:</label>
                <form:input path="numeroMotor" id="numeroMotor" type="text" required="true"/>
            </div>
            <div>
                <button type="submit" class="btn">Guardar Vehículo</button>
            </div>
        </form:form>

        <a href="<c:url value='/vehiculos'/>" class="back-link">Volver a la lista de Vehículos</a>
    </div>
</body>
</html>