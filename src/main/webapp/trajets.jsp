<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/includes/header.jsp" />
<jsp:include page="/WEB-INF/includes/navbar.jsp" />

<div class="d-flex">
    <jsp:include page="/WEB-INF/includes/sidebar.jsp" />

    <main class="flex-grow-1 p-4">
        <jsp:include page="/WEB-INF/content/trajets-content.jsp" />
    </main>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
