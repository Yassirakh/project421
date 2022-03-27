<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h2>Stats</h2>
<c:forEach var="stat" items="${requestScope.stats}">
    <p>Joueur : ${stat.key}, a joue : ${stat.value.partieJouees}, a gagne : ${stat.value.partieGagnees}  / ${stat.value.partieGagneesPrct},
    Moyenne jetons charge : ${stat.value.jetonsChargePrct}, Moyenne jetons decharge : ${stat.value.jetonsDechargePrct}</p>
</c:forEach>