package com.seroja.TrainingChekInBot.bot;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
        if (list.getLast().hasMessage()) {
            String messageText = list.getLast().getMessage().getText();
            Long chatId = list.getLast().getMessage().getChatId();

            if (messageText.equals("/start")) {
                sendMainMenu(chatId);
            } else if (messageText.equals("/keyboard")) {
                sendReplyKeyboard(chatId);
            } else {
                sendMessage(chatId, "Wrong command");
            }
        } else if (list.getLast().hasCallbackQuery()) {
            handleCallBack(list.getLast().getCallbackQuery());
        }
    }

    @SneakyThrows
    private void sendReplyKeyboard(Long chatId) {
        SendMessage message = SendMessage.builder()
                .text("Simple keyboard example")
                .chatId(chatId)
                .build();

        List<KeyboardRow> keyboardRows = List.of(
                new KeyboardRow("Hi!", "Image")
        );

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(keyboardRows);
        message.setReplyMarkup(markup);

        telegramClient.execute(message);
    }

    private void handleCallBack(CallbackQuery callbackQuery) {
        var data = callbackQuery.getData();
        var chatId = callbackQuery.getFrom().getId();
        var user = callbackQuery.getFrom();
        switch (data) {
            case "my_name" -> sendMyName(chatId, user);
            case "random" -> sendRandom(chatId);
            case "long_process" -> sendImage(chatId);
            default -> sendMessage(chatId, "Unknown command");
        }

    }

    @SneakyThrows
    private void sendMessage(Long chatId, String messageText) {

        SendMessage message = SendMessage.builder()
                .text(messageText)
                .chatId(chatId)
                .build();
        telegramClient.execute(message);


    }

    private void sendImage(Long chatId) {
        sendMessage(chatId, "Image download started");

        new Thread(() -> {
            var imageUrl = "https://picsum.photos/200";
            try {
                URL url = new URL(imageUrl);
                var inputStream = url.openStream();

                SendPhoto sendPhoto = SendPhoto.builder()
                        .chatId(chatId)
                        .photo(new InputFile(inputStream, "random.jpg"))
                        .caption("Your image:")
                        .build();

                telegramClient.execute(sendPhoto);

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    private void sendRandom(Long chatId) {
        var randomInt = ThreadLocalRandom.current().nextInt();
        sendMessage(chatId, "Your random number is: " + randomInt);
    }

    private void sendMyName(Long chatId, User user) {

        var text = "Hi!\n\nYour name is %s\nYour nickname: @%s"
                .formatted(
                        user.getFirstName() + " " + user.getLastName(),
                        user.getUserName()
                );
        sendMessage(chatId, text);
    }

    @SneakyThrows
    private void sendMainMenu(Long chatId) {
        SendMessage message = SendMessage.builder()
                .text("Welcome! Chose option:")
                .chatId(chatId)
                .build();

        var button1 = InlineKeyboardButton.builder()
                .text("What's my name?")
                .callbackData("my_name")
                .build();

        var button2 = InlineKeyboardButton.builder()
                .text("Random number")
                .callbackData("random")
                .build();

        var button3 = InlineKeyboardButton.builder()
                .text("Download picture")
                .callbackData("long_process")
                .build();

        List<InlineKeyboardRow> keyboardRows = List.of(
                new InlineKeyboardRow(button1),
                new InlineKeyboardRow(button2),
                new InlineKeyboardRow(button3)
        );


        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(keyboardRows);

        message.setReplyMarkup(markup);
        telegramClient.execute(message);
    }
}
