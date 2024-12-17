"use strict";

var messageForm = document.querySelector("#messageForm");
var messageInput = document.querySelector("#message");
var messageArea = document.querySelector("#messageArea");
var connectingElement = document.querySelector(".connecting");
const container = document.getElementById("conteiner-chat");
const slide = document.getElementById("barra");

var stompClient = null;
var username = null;
var orderId = null;
var usuarioId = null;
var apellido = null;

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
orderId = document.body.dataset.idorden;
usuarioId = document.body.dataset.idusuario;
apellido = document.body.dataset.apellido;

var socket = new SockJS('/websocket');
stompClient = Stomp.over(socket);

stompClient.connect({}, onConnected, onError);


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe(`/topic/public/${orderId}`, onMessageReceived);

    // Tell your username to the server
    slide.scrollTop = slide.scrollHeight;

    stompClient.send(
        `/app/chat.register/${orderId}`,
        {},
        JSON.stringify({ sender: username, type: "JOIN" , idSender: usuarioId, idOrder: orderId, lastname : apellido,})
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
            idSender: usuarioId,
            idOrder: orderId,
            type: "CHAT",
            lastname : apellido,
        };

        stompClient.send(`/app/chat.send/${orderId}`, {}, JSON.stringify(chatMessage));
        stompClient.send(`/app/chat.save/${orderId}`, {}, JSON.stringify(chatMessage));
        messageInput.value = "";
    }
    event.preventDefault();
}


function onMessageReceived(payload) {

    var currentDate = new Date();
    var hours = currentDate.getHours().toString().padStart(2, "0"); // Formato de 2 dígitos
    var minutes = currentDate.getMinutes().toString().padStart(2, "0"); // Formato de 2 dígitos
    var day = currentDate.getDate().toString().padStart(2, "0"); // Día con 2 dígitos
    var month = currentDate.toLocaleString("es-ES", { month: "short" }); // Mes abreviado en español

    var formattedDate = `${hours}:${minutes} - ${day} ${month}`;
    var message = JSON.parse(payload.body);

    // Selecciona el elemento en el que deseas insertar HTML
    if(message.type == 'CHAT'){
        var htmlCode;
        if(message.sender === username){
            var messageText = document.createTextNode(message.content);
            htmlCode = `<div class="d-flex justify-content-end mb-3">
                            <div>
                                <h6 class="mb-1 text-end">
                                    <span type="text">` + message.sender + " " + message.lastname +`</span>
                                    <small class="text-muted">` + formattedDate + `</small>
                                </h6>
                                <p class="bg-primary text-white p-2 rounded">` + message.content + `</p>
                            </div>
                            <div class="avatar ms-3">
                                <span class="avatar-title bg-primary text-white rounded-circle" style="width: 40px; height: 40px; display: flex; justify-content: center; align-items: center; border-radius: 50%;">
                                    <i class="ri-user-fill"></i>
                                </span>
                            </div>
                        </div>`;

        } else{
            var messageText = document.createTextNode(message.content);
            htmlCode = `<div class="d-flex mb-3">
                            <div class="avatar me-3">
                                <span class="avatar-title bg-primary-subtle text-primary rounded-circle" style="width: 40px; height: 40px; display: flex; justify-content: center; align-items: center; border-radius: 50%;">
                                    <i class="ri-user-fill"></i>
                                </span>
                            </div>

                            <div>
                                <h6 class="mb-1">
                                    <span type="text">` + message.sender + " " + message.lastname +`</span>
                                    <small class="text-muted">` + formattedDate + `</small>
                                </h6>
                                <p class="bg-light p-2 rounded">` + message.content + `</p>
                            </div>
                        </div>`;
        }

        if (container) {
            container.insertAdjacentHTML("beforeend", htmlCode);
            slide.scrollTop = slide.scrollHeight;
            console.log("Elemento encontrado.");

        } else {
            console.log("Elemento no encontrado.");
        }
    }


}


messageForm.addEventListener("submit", send, true);
