<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h2>Accueil</h2>
<c:catch var="exception">
    <c:if test="${requestScope.bad_auth == true}">
        <p>Pseudo ou mot de passe incorrect, veuillez réessayer à nouveau.</p>
    </c:if>
    <c:if test="${requestScope.bad_auth == false}">
        <p>${requestScope.joueur.pseudo} bienvenue.</p>
    </c:if>
</c:catch>
<a href="${pageContext.request.contextPath}/game?operation=auth">Authentification</a>
<a href="${pageContext.request.contextPath}/game?operation=register">Inscription</a>
