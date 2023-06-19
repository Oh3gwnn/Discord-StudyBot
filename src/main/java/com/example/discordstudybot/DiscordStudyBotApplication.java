package com.example.discordstudybot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DiscordStudyBotApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DiscordStudyBotApplication.class, args);
		DiscordBotToken discordBotTokenEntity = context.getBean(DiscordBotToken.class);
		String discordBotToken = discordBotTokenEntity.getDiscordBotToken();

		JDA jda = JDABuilder.createDefault(discordBotToken)
				.setActivity(Activity.playing("메시지 기다리는 중!"))
				.enableIntents(GatewayIntent.MESSAGE_CONTENT)
				.addEventListeners(new DiscordListener())
				.build();
	}
}