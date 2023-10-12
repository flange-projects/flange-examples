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

package dev.flange.example.cloud.hellouser_faas.service.user.api;

import java.util.concurrent.CompletableFuture;

import javax.annotation.*;

import dev.flange.cloud.CloudFunctionApi;

/**
 * Service for accessing users.
 * @author Garret Wilson
 */
@CloudFunctionApi
public interface UserService {

	/**
	 * Finds the profile for a user with the given username.
	 * @param username The username of the user the profile of which to be retrieved.
	 * @return The user's profile; the future will return <code>null</code> if the user does not exist.
	 */
	CompletableFuture<UserProfile> getUserProfileByUsername(@Nonnull String username); //TODO improve with `Optional<UserProfile>`

}
