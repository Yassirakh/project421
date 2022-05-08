<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h2>Authentification</h2>

<a href="${pageContext.request.contextPath}/game?operation=home">Accueil.</a>
<a href="${pageContext.request.contextPath}/game?operation=register">Inscription</a>
<c:catch var="exception">
    <c:if test="${requestScope.bad_auth == true}">
        <p>Pseudo ou mot de passe incorrect, veuillez réessayer à nouveau.</p>
    </c:if>
    <c:if test="${requestScope.bad_auth == false}">
        <p>${requestScope.joueur.pseudo} bienvenue.</p>
    </c:if>
</c:catch>
<form action="/game?operation=auth&status=auth-request" method="post">
    <p>
    <table>
        <tr> <td>Pseudo</td> <td><input name="pseudo" required > </td> </tr>
        <tr> <td>Mot de passe</td> <td><input type="password" name="mdp" required > </td> </tr>
    </table>
    </p>
    <p><input type="submit" value="Se connecter"></p>
</form>