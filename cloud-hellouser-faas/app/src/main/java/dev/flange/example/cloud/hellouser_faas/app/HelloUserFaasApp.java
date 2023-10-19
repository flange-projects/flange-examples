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

package dev.flange.example.cloud.hellouser_faas.app;

import static java.util.Objects.*;
import static java.util.concurrent.TimeUnit.*;

import java.util.concurrent.*;

import javax.annotation.*;

import dev.flange.cloud.ServiceConsumer;
import dev.flange.cloud.application.FlangeCloudApplication;
import dev.flange.example.cloud.hellouser_faas.service.message.api.MessageService;

/**
 * Example application illustrating two cloud functions, one invoking the other..
 * @author Garret Wilson
 */
@ServiceConsumer(MessageService.class)
public class HelloUserFaasApp implements FlangeCloudApplication {

	private final MessageService messageService;

	/**
	 * Message service constructor
	 * @param messageService The message service to use.
	 */
	public HelloUserFaasApp(@Nonnull final MessageService messageService) {
		this.messageService = requireNonNull(messageService);
	}

	@Override
	public void run() {
		final String username = "jdoe"; //hard-coded username for purposes of example

		try {
			messageService.getGreetingForUser(username)
					//provide fallback value on timeout
					.completeOnTimeout("Howdy!", 10, SECONDS)
					//print "Hello, Jay Doe!", or a variation if timeout(s)
					.thenAccept(System.out::println)
					//wait until finished before exiting the application
					.join();
		} catch(final CompletionException completionException) {
			final Throwable cause = completionException.getCause(); //process underlying exception as appropriate
			System.err.println("Error accessing message service: %s".formatted(cause.getMessage()));
		}
	}

	/**
	 * Main application entry point.
	 * @param args The application arguments.
	 */
	public static void main(@Nonnull final String[] args) {
		FlangeCloudApplication.start(HelloUserFaasApp.class, args);
	}

}
