/*
 *  Copyright (C) 2015 mafiagame.ir
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package co.mafiagame.bot;

import co.mafiagame.bot.telegram.TResult;
import co.mafiagame.bot.telegram.TUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Esa Hekmatizadeh
 */
@RestController
public class CommandController {
	private static final Logger logger = LoggerFactory.getLogger(CommandController.class);
	@Autowired
	private CommandDispatcher commandDispatcher;
	@Value("${mafia.telegram.token}")
	private String telegramToken;
	@Value("${mafia.telegram.api.url}")
	private String telegramUrl;
	@Value("${mafia.telegram.use.webhook}")
	private Boolean useWebHook;

	@PostConstruct
	public void init() {
		if (!useWebHook) {
			RestTemplate restTemplate = new RestTemplate();
			setErrorHandler(restTemplate);
			Thread thread = new Thread(() -> {
				try {
					long offset = 1;
					Thread.sleep(TimeUnit.SECONDS.toMillis(5));
					while (true) {
						try {
							TResult tResult = restTemplate.getForObject(
											telegramUrl + telegramToken + "/getUpdates?offset=" + String.valueOf(offset + 1),
											TResult.class);
							for (TUpdate update : tResult.getResult()) {
								if (offset < update.getId()) {
									offset = update.getId();
									handleMessage(update);
									logger.debug("offset set to {}", offset);
								}
							}
							Thread.sleep(200);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			thread.start();
		}
	}

	private void setErrorHandler(RestTemplate restTemplate) {
		restTemplate.setErrorHandler(new ResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
				return !clientHttpResponse.getStatusCode().equals(HttpStatus.OK);
			}

			@Override
			public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
				logger.error("error calling telegram getUpdate\n code:{}\n{}",
								clientHttpResponse.getStatusCode(),
								org.apache.commons.io.IOUtils.toString(clientHttpResponse.getBody()));
			}
		});
	}

	@RequestMapping(value = "/{token}/update", method = RequestMethod.POST)
	public void getUpdate(@PathVariable String token, @RequestBody TUpdate update) {
		logger.info("receive: {}", update);
		if (!validate(token)) {
			logger.warn("Suspicious connection with token {} and update {}", token, update);
			return;
		}
		handleMessage(update);
	}

	private void handleMessage(TUpdate update) {
		if (Objects.nonNull(update.getMessage())) {
			logger.info("receive: {}", update);
			commandDispatcher.handleMessage(update.getMessage());
		} else if (Objects.nonNull(update.getCallBackQuery())) {
			commandDispatcher.handleCallback(update.getCallBackQuery());
		}
	}

	private boolean validate(String token) {
		return telegramToken.equalsIgnoreCase(token);
	}

}
