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

import static java.util.Objects.*;

import javax.annotation.*;

/**
 * A user's profile information.
 * @param username The user's login identifier.
 * @param firstName The first name of the user.
 * @param lastName The last name of the user.
 * @author Garret Wilson
 */
public record UserProfile(@Nonnull String username, @Nonnull String firstName, @Nonnull String lastName) {

	/**
	 * Argument validation constructor.
	 * @param username The user's login identifier.
	 * @param firstName The first name of the user.
	 * @param lastName The last name of the user.
	 */
	public UserProfile {
		requireNonNull(username);
		requireNonNull(firstName);
		requireNonNull(lastName);
	}

}
