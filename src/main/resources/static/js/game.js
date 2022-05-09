var id = localStorage.getItem("userId");
var username = localStorage.getItem("username");
var partieid = document.querySelector('#partieId').innerText.trim();

localStorage.setItem("partieid", partieid)

function connect() {

    var socket = new SockJS('/lobby');
    stompClient = Stomp.over(socket);

    //stompClient.connect({username: username, userId: userId}, onConnected, onError);
    stompClient.connect({}, onConnected, onError);
    console.log('before listplayergame')
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/listPlayerGame",
        data: partieid,
        success: function (data) {
            console.log(data)
            var ol = document.getElementById("playersList");
            data.forEach(element => {

                var li = document.createElement("li");
                li.appendChild(document.createTextNode(element));
                ol.appendChild(li);
            }
            );

            console.log('success')
        },
    })
}
function onConnected() {
    // Subscribe to the Public Topic

}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

connect();