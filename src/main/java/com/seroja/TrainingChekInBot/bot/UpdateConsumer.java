package com.seroja.TrainingChekInBot.bot;

import com.seroja.TrainingChekInBot.enums.Role;
import com.seroja.TrainingChekInBot.services.UserService;
import org.jspecify.annotations.NonNull;
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
import java.net.URI;
import java.net.URL;
import java.util.List;

@Component
public class UpdateConsumer implements LongPollingUpdateConsumer {

    private final TelegramClient telegramClient;

    private final UserService userService;

    public UpdateConsumer(UserService userService) {
        this.userService = userService;
        String token = System.getenv("BOT_TOKEN");
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    public void consume(@NonNull List<Update> list) {
        Update update = list.getLast();
        if (update.hasMessage()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            Long userTelegramId = update.getMessage().getFrom().getId();

            switch (messageText) {
                case "/start" -> {
                    if (userService.checkUserByTelegramId(userTelegramId)) {
                        sendMessage(chatId, "Ви вже зареэстровані!");
                    } else sendMainMenu(chatId);
                }
                case "/adminpanel" -> sendAdminMenu(chatId);
                default -> sendMessage(chatId, "Хибна команда!");
            }
        } else if (update.hasCallbackQuery()) {
            handleCallBack(update.getCallbackQuery());
        }
    }

    private void sendAdminMenu(Long chatId) {

        SendMessage message = SendMessage.builder()
                .text("Simple keyboard example")
                .chatId(chatId)
                .build();

        List<KeyboardRow> keyboardRows = List.of(
                new KeyboardRow("Показати список учнів", "Показати список вчителів")
        );

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(keyboardRows);
        message.setReplyMarkup(markup);

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendMainMenu(Long chatId) {
        SendMessage message = SendMessage.builder()
                .text("Вітаю! Оберіть опцію:")
                .chatId(chatId)
                .build();

        var button1 = InlineKeyboardButton.builder()
                .text("Я новий учень")
                .callbackData("student_registration")
                .build();

        var button2 = InlineKeyboardButton.builder()
                .text("Я новий вчитель")
                .callbackData("teacher_registration")
                .build();

        List<InlineKeyboardRow> keyboardRows = List.of(
                new InlineKeyboardRow(button1),
                new InlineKeyboardRow(button2)
        );

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(keyboardRows);

        message.setReplyMarkup(markup);
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Something happen with telegram!");
        }
    }

    private void handleCallBack(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getFrom().getId();
        User user = callbackQuery.getFrom();
        switch (data) {
            case "student_registration" -> registerStudent(chatId, user);
            case "teacher_registration" -> registerTeacher(chatId, user);
            case "long_process" -> sendImage(chatId);
            default -> sendMessage(chatId, "Unknown command");
        }

    }

    private void registerTeacher(Long chatId, User user) {
        userService.addUser(user, Role.TEACHER, chatId);
        sendMessage(chatId, "Ваша заявка на реєстрацію надіслана!");


    }

    private void registerStudent(Long chatId, User user) {
        userService.addUser(user, Role.STUDENT, chatId);
        sendMessage(chatId, "Ви успішно зареєструвалися як студент!");
    }

    private void sendMessage(Long chatId, String messageText) {
        SendMessage message = SendMessage.builder()
                .text(messageText)
                .chatId(chatId)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Message was not sent!");
        }

    }

    private void sendImage(Long chatId) {
        sendMessage(chatId, "Image download started");

        new Thread(() -> {
            var imageUrl = "https://picsum.photos/200";
            try {
                URL url = URI.create(imageUrl).toURL();
                var inputStream = url.openStream();

                SendPhoto sendPhoto = SendPhoto.builder()
                        .chatId(chatId)
                        .photo(new InputFile(inputStream, "random.jpg"))
                        .caption("Your image:")
                        .build();

                telegramClient.execute(sendPhoto);

            } catch (TelegramApiException | IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }


}
