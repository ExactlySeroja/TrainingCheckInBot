package com.seroja.TrainingChekInBot.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
public class UpdateConsumer implements LongPollingUpdateConsumer {

    private final TelegramClient telegramClient;


    public UpdateConsumer() {
        String token = System.getenv("BOT_TOKEN");
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    @SneakyThrows
    @Override
    public void consume(List<Update> list) {
        System.out.printf("Received message %s from %s ",
                list.getLast().getMessage().getText(),
                list.getLast().getMessage().getChatId()
                );
        var chatId = list.getLast().getMessage().getChatId();
        SendMessage message = SendMessage.builder()
                .text("Hello, your message is: " + list.getLast().getMessage().getText())
                .chatId(chatId)
                .build();

        telegramClient.execute(message);
    }
}
