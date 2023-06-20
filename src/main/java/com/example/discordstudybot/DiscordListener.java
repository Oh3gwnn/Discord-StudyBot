package com.example.discordstudybot;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

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
                            deleteSelfMessages(event, Integer.parseInt(messageArgs[1]));
                        } catch (Exception e) {
                            log.error("An error occurred during event handling", e);
                        }

                        sendDeleteEmbedMessage(event, messageArgs[1]);
//                        String returnMessage = sendMessage(event, messageArgs);
//                        textChannel.sendMessage(returnMessage).queue();
                    }
                }

            }
        }



    }

//    private String sendMessage(MessageReceivedEvent event, String[] message) {
//        User user = event.getAuthor();
//        String returnMessage = "";
//
//        switch (message[0]) {
//            case "안녕", "하이" -> returnMessage = user.getName() + "님 안녕하세요. 환영합니다.";
//            case "삭제" -> {
//                if (Integer.parseInt(message[1]) <= 1) returnMessage = "명령어 포함 2개 이상 입력해주세요.";
//                else returnMessage = user.getAsMention() + "님이 메세지를 "+ message[1] +"개 삭제하셨습니다.";
//            }
//            default -> returnMessage = "명령어를 확인해주세요.";
//        } return returnMessage;
//    }

    private static void deleteSelfMessages(MessageReceivedEvent event, int messageCount) {
        // event -> 사용자, 채널 정보 가져옴
        User user = event.getAuthor();
        TextChannel channel = event.getChannel().asTextChannel();

        // 채널 메시지 히스토리 최근 100개 조회
        channel.getIterableHistory().takeAsync(100).thenAccept(messages -> {
            // AtomicInteger(멀티스레드 환경 안전 보장) -> count 관리
            AtomicInteger count = new AtomicInteger(0);

            messages.stream()
                    .filter(message -> message.getAuthor().getId().equals(user.getId())) // 명령자 == 사용자
                    .limit(messageCount) // messageCount만큼 메세지 선택
                    .forEach(message -> {
                        message.delete().queue(
                                success -> System.out.println("Message deleted successfully: " + message.getId()),
                                error -> System.err.println("Failed to delete message: " + message.getId() + ", Error: " + error.getMessage())
                        );
                        count.incrementAndGet(); // 메세지 삭제 시 count 증가
                    });
        });
    }

//    private static void deleteMultipleMessages(TextChannel channel, int messageCount) {
//        MessageHistory history = channel.getHistory();
//        List<Message> messages = history.retrievePast(messageCount).complete();
//        channel.deleteMessages(messages).queue(
//                success -> System.out.println("Messages deleted successfully."),
//                error -> System.err.println("Failed to delete messages: " + error.getMessage())
//        );
//    }

    private static void sendDeleteEmbedMessage(MessageReceivedEvent event, String count) {
        User user = event.getAuthor();
        TextChannel channel = event.getChannel().asTextChannel();

        // 지우고 싶은 메세지 개수 > 1
        if (Integer.parseInt(count) > 1) {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle(":wastebasket: Delete Messages")
                    .setColor(Color.lightGray)
                    .setDescription(user.getAsMention() + "님이 자신의 메세지를 "+ count +"개 삭제하셨습니다.")
                    .setFooter("삭제 시간: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
        }
        else channel.sendMessage("삭제하고 싶은 메세지 개수를 2개 이상 입력해주세요.").queue();
    }
}
