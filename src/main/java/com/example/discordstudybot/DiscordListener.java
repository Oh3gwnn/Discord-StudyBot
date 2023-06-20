package com.example.discordstudybot;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
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
        // 각 사용자, 채팅 채널, 메세지

        log.info("get message : " + message.getContentDisplay() + ", user: " + user.getName());

        if (user.isBot()) return; // 사용자가 봇인지

        // !명령어
        if (message.getContentRaw().charAt(0) == '!') {
            String[] messageArray = message.getContentRaw().substring(1).split(" ");
            if (messageArray.length > 3) {
                log.info("messageArray length over 3");
                return;
            }
            if (messageArray[0].equals(user.getName())) {

                String[] messageArgs = Arrays.copyOfRange(messageArray, 1, messageArray.length);

                if (messageArgs[0].equals("삭제")) {
                    if (messageArgs[1] != null) {
                        try {
                            deleteMessages(user, textChannel, Integer.parseInt(messageArgs[1]));
                        } catch (Exception e) {
                            log.error("An error occurred during event handling", e);
                        }

                        String returnMessage = sendMessage(event, messageArgs);
                        textChannel.sendMessage(returnMessage).queue();
                    }
                }

            }
        }



    }

    private String sendMessage(MessageReceivedEvent event, String[] message) {
        User user = event.getAuthor();
        String returnMessage = "";

        switch (message[0]) {
            case "안녕", "하이" -> returnMessage = user.getName() + "님 안녕하세요. 환영합니다.";
            case "test" -> returnMessage = user.getAsTag() + "test";
            case "누구" -> returnMessage = user.getAsMention() + "님, 저는 StudyBot입니다.";
            case "삭제" -> returnMessage = user.getName() + "님이 메세지를 "+ message[1] +"개 삭제하셨습니다.";

            default -> returnMessage = "명령어를 확인해주세요.";
        } return returnMessage;
    }

    private static void deleteMessages(User user, TextChannel channel, int messageCount) {
        MessageHistory history = channel.getHistory();
        List<Message> messages = history.retrievePast(messageCount).complete();
        int tmp = 0;
        for (Message message : messages) {
            deleteSelfMessage(channel, user.toString());
        }
    }

    private static void deleteSelfMessage(TextChannel channel, String messageId) {
        channel.retrieveMessageById(messageId).queue(message -> {
            if (message != null && message.getAuthor().getId().equals(channel.getJDA().getSelfUser().getId())) {
                message.delete().queue(
                        success -> System.out.println("Message deleted successfully: " + messageId),
                        error -> System.err.println("Failed to delete message: " + messageId + ", Error: " + error.getMessage())
                );
            } else {
                System.out.println("Message not found or not sent by self: " + messageId);
            }
        });
    }

    private static void deleteMultipleMessages(TextChannel channel, int messageCount) {
        MessageHistory history = channel.getHistory();
        List<Message> messages = history.retrievePast(messageCount).complete();
        channel.deleteMessages(messages).queue(
                success -> System.out.println("Messages deleted successfully."),
                error -> System.err.println("Failed to delete messages: " + error.getMessage())
        );
    }


}
