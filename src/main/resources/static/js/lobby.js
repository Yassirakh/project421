var id = localStorage.getItem("userId");
var username = localStorage.getItem("username");

var hostId = null;
var hostUsername = null;
var invitedUser1 = null;
var invitedUser2 = null;
var invitedUser3 = null;
var invitePeople = null;
var acceptedPeople = 0;
var refusedPeople = 0;
function connect() {

    var socket = new SockJS('/lobby');
    stompClient = Stomp.over(socket);

    //stompClient.connect({username: username, userId: userId}, onConnected, onError);
    stompClient.connect({}, onConnected, onError);
}
function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/lobby', onInviteReceived);
    stompClient.subscribe('/topic/lobby', onInviteCanceled);
    stompClient.subscribe('/topic/lobby', onInviteRefused);
    stompClient.subscribe('/topic/lobby', onInviteAccepted);
    stompClient.subscribe('/topic/lobby', onGameLaunched);
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function onInviteReceived(data) {
    var data = data.body.split('/')
    if (data[1] == "inviteReceived"){
        var inviting_player_array = data[0].split(',')
        hostId = inviting_player_array[inviting_player_array.length - 2]
        hostUsername = inviting_player_array[inviting_player_array.length - 1]
        inviting_player_array.pop()
        inviting_player_array.pop()

        if (inviting_player_array.includes(id)) {
            $('#invite_msg_received').text( "Vous êtes invité par : " + hostUsername)
            $("#modal-content-received").css("display", "block");
        }
    }
}

function onInviteCanceled(data) {

    var data = data.body.split('/')
    if (data[1] == "inviteCanceled"){
        var inviting_player_array = data[0].split(',')
        if (inviting_player_array.includes(id)) {
            $("#modal-content-received").css("display", "none");
        }
    }
}

function onInviteRefused(data) {
    var data = data.body.split('/')
    if (data[1] == "inviteRefused"){
        if (invitedUser1 == data[0]) {
            refusedPeople += 1;
            invitedUser1 = null
        }
        else if (invitedUser2 == data[0]) {
            refusedPeople += 1;
            invitedUser2 = null
        }
        else if (invitedUser3 == data[0]) {
            refusedPeople += 1;
            invitedUser3 = null
        }
        if (invitedUser1 == null && invitedUser2 == null && invitedUser3 == null) {
            $("#modal-content-sent").css("display", "none");
        }
    }

    console.log("refusedPeople: " + refusedPeople)
}

function onInviteAccepted(data) {
    var data = data.body.split('/')
    var gameplayers = [];
    if (data[1] == "inviteAccepted"){
        if (invitedUser1 == data[0]) {
            acceptedPeople += 1;
        }
        else if (invitedUser2 == data[0]) {
            acceptedPeople += 1;
        }
        else if (invitedUser3 == data[0]) {
            acceptedPeople += 1;
        }
        if (acceptedPeople + refusedPeople == invitePeople) {
                if (invitedUser1 != null) {
                    gameplayers.push(invitedUser1)
                }
                if (invitedUser2 != null) {
                    gameplayers.push(invitedUser2)
                }
                if (invitedUser3 != null) {
                    gameplayers.push(invitedUser3)
                }
                gameplayers.push(id)
                var gameplayersjson = JSON.stringify(gameplayers);
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: "/createGame",
                    data: gameplayersjson,
                    success: function (data) {
                        gameplayers.push(data)
                        stompClient.send("/app/lobby.gameLaunch", {}, gameplayers);
                    },
                })
               // stompClient.send("/app/lobby.gameLaunch", {}, gameplayers);
        }
    }
}

function onGameLaunched(data) {
    var data = data.body.split('/')
    if (data[1] == "gameLaunched"){
        console.log(data[0])
        var player_to_play = data[0].split(',')
        //removing partie id
        console.log(player_to_play)
        player_to_play.pop()
        console.log(player_to_play)
        if (player_to_play.includes(id)) {
            var user_partie = [id, data[0].split(',')[data[0].split(',').length - 1]]
            console.log(data[0].split(',')[data[0].split(',').length - 1])
            console.log("game info")
            console.log(user_partie)
/*            var xhr = new XMLHttpRequest();
            xhr.open('GET', '/startgame', true);
            xhr.send();
            $.get('/startgame')*/
            //$.post('/startgame')
            //var user_partie = JSON.stringify(user_partie);
            /*$.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/startgame",
                data: user_partie,
                success: function (data) {
                    console.log('post request success')
                    console.log(data)
                },
            })*/
            // Redirecting player to game page
            window.location.href = '/startgame?id=' + user_partie[0] + '&id_partie=' + user_partie[1];
            //console.log('post request failed')
        }
    }
}

// Connect to WebSocket Server.
connect();

$(document).ready(function() {

    $("#onlineusers-select").change(function() {
        if ($("#onlineusers-select option:selected").length > 3) {
            $(this).removeAttr("selected");
            alert('You can select upto 3 options only');
        }
    });


    $("#onlineusers-select > option").each(function() {
        if (this.value == id || this.value == 0) {
            this.remove();
        }
    });
    //$("#onlineusers-select  option[value=id]").remove();

    $("#refresh_button").on('click', function (event) {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/refresh-players",
            success: function (data) {
                var json = JSON.parse(data)
                $("#onlineusers-select > option").each(function() {
                        this.remove();
                });
                $('#onlineusers-select').append(`<option value="">
                                       --Please choose an option--
                                  </option>`);
                Object.keys(json).forEach(function(key) {
                    if (key != id && key != 0)
                    {
                        $('#onlineusers-select').append(`<option value="${key}">
                                       ${json[key]}
                                  </option>`);
                    }
                })
            },
        })

    })



    $("#invite_button").click(sendInvite);
    $("#cancel_invite_sent").click(cancelInvite);
    $("#refuse_invite_received").click(refuseInvite);
    $("#accept_invite_received").click(acceptInvite);
});

function sendInvite(event) {
    if ($("#onlineusers-select option:selected").length > 3) {
        alert('Vous pouvez chosir au max 3 joueurs');
    }
    else if ($("#onlineusers-select option:selected").length < 1) {
        alert('Vous devez choisir au moins 1 joueur');
    }
    else {
        var selected_userids = $('#onlineusers-select option:selected')
            .toArray().map(item => item.value);

        if (selected_userids.length == 1) {
            invitedUser1 = selected_userids[0]
        }
        else if (selected_userids.length == 2) {
            invitedUser1 = selected_userids[0]
            invitedUser2 = selected_userids[1]
        }
        else if (selected_userids.length == 3) {
            invitedUser1 = selected_userids[0]
            invitedUser2 = selected_userids[1]
            invitedUser3 = selected_userids[2]
        }
        invitePeople = selected_userids.length
        selected_userids.push(id)
        selected_userids.push(username)
        selected_userids = JSON.parse(JSON.stringify(selected_userids))
        if(stompClient) {
            stompClient.send("/app/lobby.sendInvite", {}, selected_userids);
            $("#modal-content-sent").css("display", "block");
        }
        event.preventDefault();
    }

}

function cancelInvite(event) {
    console.log('Canceling');
    var selected_userids = []
    if (invitePeople == 1) {
        selected_userids.push(invitedUser1)
    }
    else if (invitePeople == 2) {
        selected_userids.push(invitedUser1)
        selected_userids.push(invitedUser2)
    }
    else if (invitePeople == 3) {
        selected_userids.push(invitedUser1)
        selected_userids.push(invitedUser2)
        selected_userids.push(invitedUser3)
    }
    selected_userids = JSON.parse(JSON.stringify(selected_userids))
    if(stompClient) {
        stompClient.send("/app/lobby.cancelInvite", {}, selected_userids);
    }
    $("#modal-content-sent").css("display", "none");

}

function refuseInvite(event) {
    console.log('Refusing');
    if(stompClient) {
        stompClient.send("/app/lobby.refuseInvite", {}, id);
    }
    $("#modal-content-received").css("display", "none");
}

function acceptInvite(event) {
    console.log('accepting');
    if(stompClient) {
        stompClient.send("/app/lobby.acceptInvite", {}, id);
    }
    $("#modal-content-received").css("display", "none");
}

//messageForm.addEventListener('submit', sendMessage, true);
