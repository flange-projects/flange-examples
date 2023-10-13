/*
 * Copyright © 2023 GlobalMentor, Inc. <https://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.flange.example.cloud.hellouser_faas.service.message.impl;

import static java.util.Objects.*;
import static java.util.concurrent.TimeUnit.*;

import java.util.concurrent.CompletableFuture;

import javax.annotation.*;

import dev.flange.cloud.CloudFunctionService;
import dev.flange.cloud.ServiceConsumer;
import dev.flange.example.cloud.hellouser_faas.service.message.api.MessageService;
import dev.flange.example.cloud.hellouser_faas.service.user.api.UserService;

/**
 * Implementation of a service for producing messages.
 * @author Garret Wilson
 */
@CloudFunctionService
@ServiceConsumer(UserService.class)
public class MessageServiceImpl implements MessageService {

	private final UserService userService;

	/**
	 * User service constructor.
	 * @param userService The service providing access to user information.
	 */
	public MessageServiceImpl(@Nonnull final UserService userService) {
		this.userService = requireNonNull(userService);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation calls {@link UserService#getUserProfileByUsername(String)}.
	 */
	@Override
	public CompletableFuture<String> getGreetingForUser(final String username) {
		final CompletableFuture<String> futureUserDisplayName = userService.getUserProfileByUsername(username)
				//make sure user exists
				.thenApply(userProfile -> {
					if(userProfile == null) {
						throw new IllegalArgumentException("No user with username `%s`.".formatted(username));
					}
					return userProfile;
				})
				//determine user display name from profile
				.thenApply(user -> "%s %s".formatted(user.firstName(), user.lastName()))
				//if timeout, simply use the login username as the display name
				.completeOnTimeout(username + " (timeout)", 5, SECONDS);
		return futureUserDisplayName.thenApply("Hello, %s!"::formatted);
	}

}
