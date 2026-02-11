package com.seroja.TrainingChekInBot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Component
public class TelegramBot implements SpringLongPollingBot {

    private final UpdateConsumer updateConsumer;

    public TelegramBot(UpdateConsumer updateConsumer) {
        this.updateConsumer = updateConsumer;
    }


    @Override
    public String getBotToken() {
        String token = System.getenv("BOT_TOKEN");

        if (token == null || token.isEmpty()) {
            throw new IllegalStateException("Token doesn't found. Check environment variable");
        }
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}
