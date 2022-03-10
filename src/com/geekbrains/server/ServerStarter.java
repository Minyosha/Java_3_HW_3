// 1. Реализовать личные сообщения, если клиент пишет «/w nick3 Привет», то только клиенту с ником nick3 должно прийти сообщение «Привет»
// 2. if (messageFromServer.contains("зашел в чат")) { Заменить эту строку на команду /enter,
// в случае если сервером была прислана такая команда + никнейм,
// нужно отобразить сообщение у других пользователей "Пользователь nickname зашел в чат"

package com.geekbrains.server;

public class ServerStarter {
    public static void main(String[] args) {
        new Server();
    }
}
