package com.seroja.TrainingChekInBot.bot;

import com.seroja.TrainingChekInBot.dtos.StudentRegistrationDTO;
import com.seroja.TrainingChekInBot.enums.Role;
import com.seroja.TrainingChekInBot.mappers.UserMapper;
import com.seroja.TrainingChekInBot.services.UserService;
import lombok.SneakyThrows;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class UpdateConsumer implements LongPollingUpdateConsumer {

    private final TelegramClient telegramClient;

    private final UserService userService;

    public UpdateConsumer(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        String token = System.getenv("BOT_TOKEN");
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    @SneakyThrows
    @Override
    public void consume(@NonNull List<Update> list) {
        Update update = list.getLast();
        if (update.hasMessage()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            var userTelegramId = update.getMessage().getFrom().getId();

            if (messageText.equals("/start")) {
                /*if (userService.findUserByTelegramId(userTelegramId)) {
                    sendMessage(chatId, "You already registered");
                } else sendMainMenu(chatId);*/
                sendMainMenu(chatId);
            } else {
                sendMessage(chatId, "Wrong command");
            }
        } else if (update.hasCallbackQuery()) {
            handleCallBack(update.getCallbackQuery());
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
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getFrom().getId();
        User user = callbackQuery.getFrom();
        switch (data) {
            case "student_registration" -> registerStudent(chatId, user);
            case "teacher_registration" -> sendRandom(chatId);
            case "long_process" -> sendImage(chatId);
            default -> sendMessage(chatId, "Unknown command");
        }

    }

    private void registerStudent(Long chatId, User user) {
        StudentRegistrationDTO newStudent = new StudentRegistrationDTO(user.getFirstName(), user.getLastName(), Role.STUDENT, chatId);
        userService.addUser(newStudent);
        sendMessage(chatId, "You successfully registered as student");
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


}
