package com.example.discordstudybot;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class DiscordListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        TextChannel textChannel = event.getChannel().asTextChannel();
        Message message = event.getMessage();

        log.info("get message : " + message.getContentDisplay());

        if (user.isBot()) return;
        if (message.getContentDisplay().equals("")) log.info("디스코드 Message 공백");

        String[] messageArray = message.getContentDisplay().split(" ");
        if (messageArray[0].equalsIgnoreCase("스터디봇")) {
            String[] messageArgs = Arrays.copyOfRange(messageArray, 1, messageArray.length);

            for (String msg : messageArgs) {
                String returnMessage = sendMessage(event, msg);
                textChannel.sendMessage(returnMessage).queue();
            }
        }

    }

    private String sendMessage(MessageReceivedEvent event, String message) {
        User user = event.getAuthor();
        String returnMessage = "";

        switch (message) {
            case "안녕" -> returnMessage = user.getName() + "님 안녕하세요. 환영합니다.";
            case "test" -> returnMessage = user.getAsTag() + "test";
            case "누구" -> returnMessage = user.getAsMention() + "님, 저는 StudyBot입니다.";
            default -> returnMessage = "명령어를 확인해주세요.";
        } return returnMessage;
    }
}
