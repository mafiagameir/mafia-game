/*
 *     Copyright (c) 2018 Isa Hekmatizadeh.
 *     This file is part of mafiagame.
 *
 *     Mafiagame is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Mafiagame is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Mafiagame.  If not, see <http://www.gnu.org/licenses/>.
 */

package co.mafiagame.bot;

import co.mafiagame.bot.telegram.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class TelegramClient {
	private static final Logger logger = LoggerFactory.getLogger(TelegramClient.class);

	@Value("${mafia.telegram.api.url}")
	private String telegramUrl;
	@Value("${mafia.telegram.token}")
	private String telegramToken;
	private final RestTemplate restTemplate;
	private ObjectMapper mapper;

	@Autowired
	public TelegramClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public SendMessageResult send(SendMessage message) {
		try {
			logger.info("sending message: {}", message);
			return restTemplate.postForEntity(telegramUrl + telegramToken + "/sendMessage", message, SendMessageResult.class).getBody();
		} catch (HttpClientErrorException e) {
			logger.error("error sending message {}: {}", message, e.getResponseBodyAsString(), e);
			try {
				return mapper.readValue(e.getResponseBodyAsByteArray(), SendMessageResult.class);
			} catch (IOException e1) {
				logger.error("could not read json value of SendMessageResult form {}", e.getResponseBodyAsString());
				e1.printStackTrace();
			}
		}
		return null;
	}

	public TMessage editMessageReplyMarkup(EditMessageReplyMarkupRequest request) {
		try {
			return restTemplate.postForEntity(telegramUrl + telegramToken +
					"/editMessageReplyMarkup", request, TMessage.class).getBody();
		} catch (Exception e) {
			logger.error("error updating message reply markup {}", request, e);
		}
		return null;
	}

	public TMessage editMessageText(EditMessageTextRequest request) {
		try {
			return restTemplate.postForEntity(telegramUrl + telegramToken + "/editMessageText",
					request, TMessage.class).getBody();
		} catch (Exception e) {
			logger.error("error updating message " + request, e);
		}
		return null;
	}

}
