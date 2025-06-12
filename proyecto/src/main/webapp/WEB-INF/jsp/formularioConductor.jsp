<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> <%-- Para el formulario de Spring --%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agregar Nuevo Conductor</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h1 { color: #333; }
        form { margin-top: 20px; padding: 20px; border: 1px solid #ddd; border-radius: 5px; }
        form div { margin-bottom: 10px; }
        form label { display: inline-block; width: 150px; font-weight: bold; }
        form input[type="text"] { width: 300px; padding: 8px; border: 1px solid #ccc; border-radius: 4px; }
        form button { padding: 10px 15px; background-color: #28a745; color: white; border: none; border-radius: 5px; cursor: pointer; }
        form button:hover { background-color: #218838; }
        .mensaje-error { color: red; font-weight: bold; margin-bottom: 15px; }
        .back-link { display: inline-block; margin-top: 20px; text-decoration: none; color: #007bff; }
    </style>
</head>
<body>
    <h1>Agregar Nuevo Conductor</h1>

    <c:if test="${not empty error}">
        <p class="mensaje-error">${error}</p>
    </c:if>

    <%-- El 'commandName' debe coincidir con el nombre del atributo en el modelo del controlador --%>
    <form:form action="${pageContext.request.contextPath}/conductores/guardar" method="post" modelAttribute="conductor">
        <%-- El ID interno será generado automáticamente, no se necesita en el formulario --%>
        <div>
            <label for="nombre">Nombre:</label>
            <form:input path="nombre" id="nombre" type="text" required="true"/>
        </div>
        <div>
            <label for="cargo">Cargo:</label>
            <form:input path="cargo" id="cargo" type="text" required="true"/>
        </div>
        <div>
            <label for="tipoIdentificacion">Tipo Identificación:</label>
            <form:input path="tipoIdentificacion" id="tipoIdentificacion" type="text" required="true"/>
        </div>
        <div>
            <label for="numeroIdentificacion">Número Identificación:</label>
            <form:input path="numeroIdentificacion" id="numeroIdentificacion" type="text" required="true"/>
        </div>
        <div>
            <button type="submit">Guardar Conductor</button>
        </div>
    </form:form>

    <a href="<c:url value='/conductores'/>" class="back-link">Volver a la lista de Conductores</a>
</body>
</html>