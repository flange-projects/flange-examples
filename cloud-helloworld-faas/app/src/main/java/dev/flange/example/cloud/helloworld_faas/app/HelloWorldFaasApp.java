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

package dev.flange.example.cloud.helloworld_faas.app;

import static java.util.Objects.*;
import static java.util.concurrent.TimeUnit.*;

import java.util.concurrent.*;

import javax.annotation.*;

import dev.flange.Flange;
import dev.flange.cloud.ServiceConsumer;
import dev.flange.example.cloud.helloworld_faas.service.api.MessageService;

/**
 * Example application showing usage of a Flange Function-as-a-Service (FaaS).
 * @author Garret Wilson
 */
@ServiceConsumer(MessageService.class)
public class HelloWorldFaasApp implements Runnable {

	private final MessageService messageService;

	/**
	 * Message service constructor
	 * @param messageService The message service to use.
	 */
	public HelloWorldFaasApp(@Nonnull final MessageService messageService) {
		this.messageService = requireNonNull(messageService);
	}

	@Override
	public void run() {
		final CompletableFuture<String> futureGreeting = messageService.getGreeting().completeOnTimeout("Hi (timeout)", 5, SECONDS);
		final CompletableFuture<String> futureName = messageService.getName().completeOnTimeout("Everybody (timeout)", 5, SECONDS);

		final String message;
		try {
			message = futureGreeting.thenCombine(futureName, (greeting, name) -> "%s, %s!".formatted(greeting, name)).get();
		} catch(final InterruptedException interruptedException) {
			throw new RuntimeException(interruptedException);
		} catch(final ExecutionException exceptionExecutionException) {
			final Throwable cause = exceptionExecutionException;
			System.err.println("Error accessing message service: %s".formatted(cause.getMessage()));
			throw cause instanceof RuntimeException runtimeException ? runtimeException : new RuntimeException(cause);
		}

		System.out.println(message);
	}

	/**
	 * Main application entry point.
	 * @param args application arguments.
	 */
	public static void main(final String[] args) {
		for(int i = 0; i < args.length - 1; i++) { //TODO detect configuration in `flange-config.*` as well
			if(args[i].equals("--flange-env")) { //TODO use constants; improve with CLI library
				System.out.println("Using Flange environment `%s`.".formatted(args[i + 1])); //TODO log as debug
				System.setProperty("flange.env", args[i + 1]); //TODO use constants
			} else if(args[i].equals("--flange-platform") && args[i + 1].equals("aws")) { //TODO use constants; improve with CLI library
				System.out.println("Selected Flange AWS cloud platform."); //TODO log as debug
				System.setProperty("flange.platform", "aws"); //TODO use constants
			} else if(args[i].equals("--flange-platform-aws-profile")) { //TODO use constants; improve with CLI library
				System.out.println("Using Flange AWS profile `%s`.".formatted(args[i + 1])); //TODO log as debug
				System.setProperty("aws.profile", args[i + 1]); //detected and accessed directly by the AWS SDK TODO use constants
			}
		}

		final MessageService messageService = Flange.getDependencyConcern().getDependencyInstanceByType(MessageService.class);
		final HelloWorldFaasApp app = new HelloWorldFaasApp(messageService);
		app.run();
	}

}
