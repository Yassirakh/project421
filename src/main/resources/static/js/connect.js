'use strict';

var stompClient = null;
var username = document.querySelector('#username').innerText.trim();
var userid = document.querySelector('#userid').innerText.trim();
localStorage.setItem("username", username)
localStorage.setItem("userId", userid)
console.log("username wa3 "  + username);


function connect() {
    var socket = new SockJS('/connect');
    stompClient = Stomp.over(socket);

    //stompClient.connect({username: username, userId: userId}, onConnected, onError);
    stompClient.connect({username: username, userId: userid}, onConnected, onError);
}

// Connect to WebSocket Server.
connect();


function onConnected() {

}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}