<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h2>Inscription</h2>
<a href="${pageContext.request.contextPath}/game?operation=home">Accueil.</a>
<a href="${pageContext.request.contextPath}/game?operation=auth">Authentification</a>

<c:catch var="exception">
    <c:if test="${requestScope.bad_auth == true}">
        <p>Pseudo existant, veuillez choisir un autre pseudo.</p>
    </c:if>
    <c:if test="${requestScope.bad_auth == false}">
        <p>${requestScope.joueur.pseudo} bienvenue.</p>
    </c:if>
</c:catch>
<form action="/game?operation=auth&status=register-request" onsubmit="return validateForm()" method="post">
    <p>
    <table>
        <tr> <td>Pseudo</td> <td><input name="pseudo" required > </td> </tr>
    <tr> <td>Mot de passe</td> <td><input id="mdp" type="password" name="mdp" required > </td> </tr>
    <tr> <td>Confirmation Mot de passe</td> <td><input id="mdp2" type="password" name="confir_mdp" required > </td> </tr>
    <tr> <td>Age</td> <td><input name="age" type="number" required > </td> </tr>
    <tr>
        <td>Sex</td>
        <td>
        <select name="sexe" id="sex" required>
            <option value="">--Please choose an option--</option>
            <option value="M">Male</option>
            <option value="F">Female</option>
        </select>
        </td>
    </tr>
    <tr> <td>Ville</td> <td><input name="ville" required > </td> </tr>
    </table>
    </p>
    <p><input type="submit" value="Se connecter"></p>
</form>

<script type="text/javascript">
    var mdp = document.getElementById('mdp');
    var mdp2 = document.getElementById('mdp2');
    var form  = document.getElementsByTagName('form')[0];

    function validateForm() {
        console.log(document.getElementById('sex').value)
        if (mdp.value != mdp2.value) {
            console.log(mdp)
            console.log(mdp2.value)

            alert("Les mots de passe doivent être identiques");
            return false;
        }
    }
</script>