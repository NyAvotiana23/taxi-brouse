<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/WEB-INF/includes/header.jsp" />

<jsp:include page="/WEB-INF/includes/navbar.jsp" />

<div class="d-flex">
    <jsp:include page="/WEB-INF/includes/sidebar.jsp" />

    <main class="flex-grow-1 p-4">
        <jsp:include page="${contentPage}" />
    </main>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />