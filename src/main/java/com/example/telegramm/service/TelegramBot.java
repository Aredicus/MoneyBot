package com.example.telegramm.service;

import com.example.telegramm.config.BotConfig;
import com.example.telegramm.model.Valute;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;

@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        ArrayList<Valute> valutes = null;
        try {
            valutes = CurrencyService.getCurrencyRate();
        } catch (IOException e) {
        } catch (ParseException e) {
            throw new RuntimeException("Unable to parse date");
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/list":
                    StringBuilder tmp = new StringBuilder();
                    for (Valute v : valutes) {
                        tmp.append(v.getCharCode() + " " + v.getName().toString() + "\n");
                    }
                    String res = new String(tmp.toString().getBytes("cp1251"), StandardCharsets.UTF_8);
                    sendMessage(chatId, res.toString());
                    break;
                default:
                    Valute valute = null;
                    for (Valute v : valutes) {
                        if (v.getCharCode().equals(messageText)) {
                            valute = v;
                            break;
                        }
                    }
                    sendMessage(chatId, valute.toString());
            }
        }

    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!" + "\n" +
                "Enter the currency whose official exchange rate" + "\n" +
                "you want to know in relation to RUB." + "\n" +
                "For example: USD" + "\n" +
                "You can use /list for I show all valutes";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        System.out.println(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
}
