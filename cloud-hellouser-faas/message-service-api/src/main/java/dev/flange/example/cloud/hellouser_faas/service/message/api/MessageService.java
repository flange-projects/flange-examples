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

package dev.flange.example.cloud.hellouser_faas.service.message.api;

import java.util.concurrent.*;

import javax.annotation.*;

import dev.flange.cloud.CloudFunctionApi;

/**
 * Service for producing messages.
 * @author Garret Wilson
 */
@CloudFunctionApi
public interface MessageService {

	/**
	 * Returns a message to be used in a greeting to a user.
	 * @param username The username of the user for which the message is intended.
	 * @return A greeting message to the user.
	 * @throws CompletionException wrapping an {@link IllegalArgumentException} if there is no such user.
	 */
	CompletableFuture<String> getGreetingForUser(@Nonnull String username);

}
