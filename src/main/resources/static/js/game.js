var id = localStorage.getItem("userId");
var username = localStorage.getItem("username");
var partieid = document.querySelector('#partieId').innerText.trim();
var numberOfPlayers = 0;
var whichPlayerToPlay = 0;
var playersOrder = [];
var toursIds = [];
var chargeTourNum = 1;
var pot = 21;
var joueur_lance_charge = []
var lance_combinaison_charge =[];
var joueur_combinaison_charge = {};
var joueur_jetons = {};
var relance_done = 0;
var update_done = 0;
var tour_charge_loser = '';
var counter_nenette = 0;
localStorage.setItem("partieid", partieid)

function connect() {
    var socket = new SockJS('/lobby');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
    //stompClient.connect({username: username, userId: userId}, onConnected, onError);
}
function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/lobby', startCharge);
    stompClient.subscribe('/topic/lobby', startTourCharge);
    stompClient.subscribe('/topic/lobby', nextTourCharge);
    stompClient.subscribe('/topic/lobby', relanceDesDonnes);
    stompClient.subscribe('/topic/lobby', loserJetons);
    stompClient.subscribe('/topic/lobby', updateTokensUI);
    console.log('before listplayergame')
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/listPlayerGame",
        data: partieid,
        success: function (data) {
            playersOrder = data
            numberOfPlayers = data.length;
            var ol = document.getElementById("playersList");
            data.forEach(element => {

                var li = document.createElement("li");
                li.appendChild(document.createTextNode(element));
                ol.appendChild(li);
            });
            var ol = document.getElementById("playersJetonsList");
            data.forEach(element => {
                joueur_jetons[element] = 0;
                var li = document.createElement("li");
                li.appendChild(document.createTextNode(element + ' : 0'));
                ol.appendChild(li);
            });
            var span = document.getElementById("jetons-pot");
            span.appendChild(document.createTextNode("Le pot contient : " + pot))
            data.push(partieid)
            data = JSON.parse(JSON.stringify(data))
            stompClient.send("/app/lobby.startCharge", {}, data);
        },
    })

}

function startCharge(data) {

    var data = data.body.split('/')
    if (data[1] == "startCharge"){
        console.log("startCharge")
        data = data[0].split(',')
        if (partieid == data[data.length - 1]) {
            $('#phaseName').text( "Phase de charge");
            var data = [playersOrder[whichPlayerToPlay], partieid]
            stompClient.send("/app/lobby.startTourCharge", {}, data);

        }
    }
}

function startTourCharge(data) {
    var data = data.body.split('/')
    if (data[1] == "startTourCharge"){
        console.log("startTourCharge")
        data = data[0].split(',')
        if (partieid == data[data.length - 1]) {
            // show tour player
            console.log(username)
            if (data[0] == username) {
                $("#create_charge").css("display", "block");
                console.log("You are the one to play now")
            }

        }
    }
}

function nextTourCharge(data) {
    data = data.body.split('/')
    var currentplayers = [...playersOrder];
    currentplayers.splice(-1,1);
    console.log("current mfs : ")
    console.log(currentplayers)
    console.log("playersOrder : ")
    console.log(playersOrder)
    if (data[1] == "nextTourCharge") {
        console.log("TESTEST")
        data = data[0].split(',')
        console.log(data)

        if (partieid == data[data.length - 1]) {

            // displaying player dés
            var div = document.getElementById("game-details");
            var span = document.createElement("span");
            span.appendChild(document.createTextNode(playersOrder[whichPlayerToPlay] + ' a eu : ' + data[0] + ' ' + data[1] + ' ' + data[2]));
            div.appendChild(span);
            var br = document.createElement("br");
            div.appendChild(br);

            // adding necessary data
            joueur_lance_charge[data[data.length - 2]] = data[data.length - 3]
            //joueur_lance_charge.push([data[data.length - 2], data[data.length - 3]])
            lance_combinaison_charge[data[data.length - 3]] = [data[data.length - 6],data[data.length - 5],data[data.length - 4]]
            console.log("lance_combinaison_charge")
            console.log(lance_combinaison_charge)
            console.log("joueur_lance_charge")
            console.log(joueur_lance_charge)
            $("#play_charge").css("display", "none");
            // new tour
            whichPlayerToPlay += 1
            if (whichPlayerToPlay != currentplayers.length) {
                if (username == currentplayers[whichPlayerToPlay]) {
                    $("#create_charge").css("display", "block");
                }
            }
            else {
                //lance_combinaison_charge["data[data.length - 3]"] = ["data[data.length - 6]","data[data.length - 5]","data[data.length - 4]"]
                //lance_combinaison_charge.slice(-(chargeTourNum*(whichPlayerToPlay-2), -(chargeTourNum*(whichPlayerToPlay-1))))
                var current_tour_data = []
                for (let i=0; i < currentplayers.length; i++) {
                    current_tour_data[i] =  lance_combinaison_charge[Object.keys(lance_combinaison_charge)[Object.keys(lance_combinaison_charge).length - i - 1]]
                }
                current_tour_data = current_tour_data.reverse();
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: "/combinaisonsLances",
                    data: JSON.stringify(Object.values(joueur_lance_charge)),
                    success: function (data) {
                        console.log("success 01")
                        console.log(data)
                        let json_data = JSON.parse(data)
                        joueur_combinaison_charge = json_data;

                        var div = document.getElementById("game-details");
                        var span = document.createElement("span");
                        span.appendChild(document.createTextNode("Tour termine"));
                        div.appendChild(span);
                        var br = document.createElement("br");
                        div.appendChild(br);
                        Object.keys(json_data).forEach(function(key) {
                            var span = document.createElement("span");
                            var br = document.createElement("br");
                            span.appendChild(document.createTextNode(key + ' a eu : ' + json_data[key] + ' jetons'));
                            div.appendChild(span);
                            div.appendChild(br);
                        })
                        minimum = 10
                        count_ppl = []
                        Object.keys(json_data).forEach(function(key) {
                            if (json_data[key] < minimum) {
                                count_ppl = []
                                minimum = json_data[key]
                            }
                            if (json_data[key] == minimum) {
                                count_ppl.push(key)
                            }
                        })
                        if(count_ppl.length > 1) {
                            relancedes(count_ppl);
                        }
                        else {
                            let min = 7
                            Object.keys(joueur_combinaison_charge).forEach(function(key) {
                                if (min > json_data[key]) {
                                    min = json_data[key]
                                    tour_charge_loser = key;
                                }
                            })
                            stompClient.send("/app/lobby.loserJeton", {}, tour_charge_loser+'/'+partieid);
                        }
                    },
                })
                console.log(current_tour_data)

            }
        }
    }
    //data = data[]
}

function relanceDesDonnes(data) {
    data = data.body.split('/')
    //TODO : add partie check
    if (data[1] == "relanceDesDonnes" && relance_done == 0) {
        relance_done = 1;
        data = data[0];
        let json_data = JSON.parse(data)
        console.log("apres relancement ");
        console.log(data);
        let minimum = 7;

        var div = document.getElementById("game-details");
        Object.keys(json_data).forEach(function(key) {
            var span = document.createElement("span");
            var br = document.createElement("br");
            span.appendChild(document.createTextNode(key + ' a eu : ' + json_data[key]));
            div.appendChild(span);
            div.appendChild(br);
            if (minimum > json_data[key]) {
                minimum = json_data[key]
                tour_charge_loser = key;
            }
        })

        stompClient.send("/app/lobby.loserJeton", {}, tour_charge_loser+'/'+partieid);
    }
}


function loserJetons(data) {
    data = data.body.split('/')
    console.log('fucker m here')
    if (data[2] = 'loserJeton' && data[1] == partieid) {
        console.log('fucker m here 2')
        data = data[0]
        var max_token = 0;
        console.log("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        console.log(joueur_combinaison_charge)
        Object.keys(joueur_combinaison_charge).forEach(function(key) {
            console.log("token value : " + joueur_combinaison_charge[key])
            if (max_token < joueur_combinaison_charge[key]) {
                max_token = joueur_combinaison_charge[key]
            }
        })
        console.log(max_token)
        if (pot - max_token < 0) {
            max_token = pot;
        }

        if (username == data && update_done == 0) {
            update_done = 1
            //joueur_jetons[data] += max_token;
            //pot -= max_token
            console.log("tours : ")
            console.log(toursIds)
            // update tour tokens value
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/addTokenToTour",
                data: JSON.stringify([max_token, toursIds[toursIds.length - 1]]),
                success: function (data) {
                    console.log('success on add token')
                    stompClient.send("/app/lobby.updateTokensUI", {},  JSON.parse(JSON.stringify([username, max_token])));
                }
            })
        }
    }
}

function updateTokensUI(data) {
    data = data.body.split('/')
    if (data[1] == 'updateTokensUI') {
        data = data[0].split(',');
        console.log('updateTokensUI')
        console.log(data)
        let max_token = data[1]
        data = data[0]
        joueur_jetons[data] = parseInt(joueur_jetons[data]) + parseInt(max_token);
        pot -= max_token
        $('#jetons-pot').empty();
        var span = document.getElementById("jetons-pot");
        span.appendChild(document.createTextNode("Le pot contient : " + pot))

        $('#playersJetonsList').empty();
        var ol = document.getElementById("playersJetonsList");
        Object.keys(joueur_jetons).forEach(function(key) {
            var li = document.createElement("li");
            li.appendChild(document.createTextNode(key + ' : ' + joueur_jetons[key]));
            ol.appendChild(li);
        });
        update_done = 0;
        relance_done = 0;
        //checkNenette();

        if (pot > 0) {

            whichPlayerToPlay = 0
            var data = [playersOrder[whichPlayerToPlay], partieid]
            stompClient.send("/app/lobby.startTourCharge", {}, data);
        }
        else {
            var div = document.getElementById("game-details");
            var span = document.createElement("span");
            span.appendChild(document.createTextNode("Phase de charge est terminee"));
            div.appendChild(span);
        }
    }
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

connect();


function createCharge (event) {
    data = [id, partieid]
    if (toursIds.length > 0) {
        data.push(toursIds[toursIds.length - 1])
    }
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/createChargeTour",
        data:  JSON.stringify(data),
        success: function (data) {
            toursIds.push(data)
            console.log("old tours : " + toursIds)
            $("#create_charge").css("display", "none");
            $("#play_charge").css("display", "block");
        }
    })
}

function playCharge(event) {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/createChargeLance",
        data:  JSON.stringify(toursIds[toursIds.length - 1]),
        success: function (data) {
            stompClient.send("/app/lobby.nextTourCharge", {}, data);
        }
    })
}



$(document).ready(function() {
    $("#create_charge").click(createCharge);
    $("#play_charge").click(playCharge);
    $("span").after("<br />");
});


function relancedes (count_ppl) {
    var div = document.getElementById("game-details");
    var span = document.createElement("span");
    span.appendChild(document.createTextNode("Relancement d'un dés"));
    div.appendChild(span);
    var br = document.createElement("br");
    div.appendChild(br);

    stompClient.send("/app/lobby.relanceDesDonnes", {}, count_ppl);
}

function checkNenette() {
    console.log("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
    var current_tour_data = [];
    var combinaison_nenette = [];
    var currentplayers = [...playersOrder];
    currentplayers.splice(-1,1);
    for (let i=0; i < currentplayers.length; i++) {
        combinaison_nenette = [];
        //current_tour_data[i] =  lance_combinaison_charge[Object.keys(lance_combinaison_charge)[Object.keys(lance_combinaison_charge).length - i - 1]]
        combinaison_nenette = lance_combinaison_charge[Object.keys(lance_combinaison_charge)[Object.keys(lance_combinaison_charge).length - i - 1]]
        for (let j=0; j < combinaison_nenette.length; j++) {
            combinaison_nenette[j] = Number(combinaison_nenette[j])
        }
        current_tour_data[i] = combinaison_nenette.sort().reverse();
        console.log(combinaison_nenette)
    }
    current_tour_data = current_tour_data.reverse();
    console.log(current_tour_data)
    var found_nenette = 0;
    var found_nenette_joueur = null;
    for (let i = 0; i < current_tour_data.length; i++) {
        if(current_tour_data[i][0] == 2 && current_tour_data[i][1] == 2 && current_tour_data[i][2] == 1) {
            found_nenette = 1;
            found_nenette_joueur = i;
            break;
        }
    }
    if (found_nenette == 1 && pot > 1) {
        /*if (username == playersOrder[found_nenette_joueur]) {
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/addTokenToTour",
                data: JSON.stringify([2, toursIds[toursIds.length - 1]]),
                success: function (data) {
                    console.log('success on add token')
                    stompClient.send("/app/lobby.updateTokensUIAfterNenette", {},  JSON.parse(JSON.stringify([username, 2])));
                }
            })
        }*/
    }
    else {
    }
    if (pot > 0) {
        var data = [playersOrder[whichPlayerToPlay], partieid]
        stompClient.send("/app/lobby.startTourCharge", {}, data);
    }
    else {
        var div = document.getElementById("game-details");
        var span = document.createElement("span");
        span.appendChild(document.createTextNode("Phase de charge est terminee"));
        div.appendChild(span);
    }
}