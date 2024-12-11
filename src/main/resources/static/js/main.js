"use strict";

var messageForm = document.querySelector("#messageForm");
var messageInput = document.querySelector("#message");
var messageArea = document.querySelector("#messageArea");
var connectingElement = document.querySelector(".connecting");

var stompClient = null;
var username = null;
//mycode
var password = null;

var colors = [
    "#2196F3",
    "#32c787",
    "#00BCD4",
    "#ff5652",
    "#ffc107",
    "#ff85af",
    "#FF9800",
    "#39bbb0",
    "#fcba03",
    "#fc0303",
    "#de5454",
    "#b9de54",
    "#54ded7",
    "#54ded7",
    "#1358d6",
    "#d611c6",
];


username = document.body.dataset.username;
password = document.body.dataset.password;

var socket = new SockJS("/websocket");
stompClient = Stomp.over(socket);

stompClient.connect({}, onConnected, onError);




function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe("/topic/public", onMessageReceived);

    // Tell your username to the server

    stompClient.send(
        "/app/chat.register",
        {},
        JSON.stringify({ sender: username, type: "JOIN" })
    );

}

function onError(error) {
    connectingElement.textContent =
        "Could not connect to WebSocket! Please refresh the page and try again or contact your administrator.";
    connectingElement.style.color = "red";
}

function send(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: "CHAT",
        };

        stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
        messageInput.value = "";
    }
    event.preventDefault();
}


function onMessageReceived(payload) {

    var currentDate = new Date();
    var hours = currentDate.getHours();
    var minutes = currentDate.getMinutes();
    minutes = minutes < 10 ? '0' + minutes : minutes;
    var timeString = hours + ':' + minutes;
    var message = JSON.parse(payload.body);

    // Selecciona el elemento en el que deseas insertar HTML
    if(message.type == 'CHAT'){
        const container = document.getElementById("conteiner-chat");
        const slide = document.getElementById("barra");
        var htmlCode;
        if(message.sender === username){
            var messageText = document.createTextNode(message.content);
            htmlCode = `<div class="d-flex justify-content-end mb-3">
                            <div>
                                <h6 class="mb-1 text-end">
                                    <span type="text">` + message.sender + `</span>
                                    <small class="text-muted">` + timeString + `</small>
                                </h6>
                                <p class="bg-primary text-white p-2 rounded">` + message.content + `</p>
                            </div>
                            <div class="avatar ms-3">
                                <span class="avatar-title bg-primary text-white rounded-circle" style="width: 50px; height: 50px; display: flex; justify-content: center; align-items: center; border-radius: 50%;">
                                    <i class="ri-user-fill"></i>
                                </span>
                            </div>
                        </div>`;

        } else{
            var messageText = document.createTextNode(message.content);
            htmlCode = `<div class="d-flex mb-3">
                            <div class="avatar me-3">
                                <span class="avatar-title bg-primary-subtle text-primary rounded-circle" style="width: 50px; height: 50px; display: flex; justify-content: center; align-items: center; border-radius: 50%;">
                                    <i class="ri-user-fill"></i>
                                </span>
                            </div>

                            <div>
                                <h6 class="mb-1">` + message.sender + `</h6>
                                <small class="text-muted">` + timeString + `</small>
                                <p class="bg-light p-2 rounded">` + message.content + `</p>
                            </div>
                        </div>`;
        }

        if (container) {
            container.insertAdjacentHTML("beforeend", htmlCode);
            container.scrollTop = container.scrollHeight;
            console.log("Elemento encontrado.");

        } else {
            console.log("Elemento no encontrado.");
        }
    }

    // Define el código HTML que deseas insertar


}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

messageForm.addEventListener("submit", send, true);
