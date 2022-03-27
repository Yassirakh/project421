<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h2>Stats</h2>
<c:forEach items="${requestScope.parties}" var="par">
    <h3><b>Partie :</b>${par.id_partie}</h3>
    <c:forEach items="${par.toursCollection}" var="tour">
        <c:choose>
            <c:when test="${tour.phase == 1}">
                <p>Tour : ${tour.id_tour} <i>Type de tour : Décharge</i></p>
                <p>Joueur : ${tour.joueur.pseudo}</p>
                    <c:forEach items="${tour.lancesCollection}" var="lance">
                        <p>Lance : ${lance.id_lance} <i>Des un : ${lance.desUn}</i> <i>Des deux : ${lance.desDeux}</i> <i>Des trois : ${lance.desTrois}</i></p>

                    </c:forEach>

            </c:when>
            <c:otherwise>
                <p>${tour.id_tour} <i>Type de tour : Charge</i></p>
                <c:forEach items="${tour.lancesCollection}" var="lance">
                    <p>Lance : ${lance.id_lance} <i>Des un : ${lance.desUn}</i> <i>Des deux : ${lance.desDeux}</i> <i>Des trois : ${lance.desTrois}</i></p>

                </c:forEach>
            </c:otherwise>

        </c:choose>
    </c:forEach>
</c:forEach>