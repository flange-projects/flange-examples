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

package dev.flange.example.cloud.hellouser_faas.service.user.impl;

import static java.util.Objects.*;
import static java.util.concurrent.CompletableFuture.*;

import java.util.*;
import java.util.concurrent.*;

import dev.flange.cloud.CloudFunctionService;
import dev.flange.example.cloud.hellouser_faas.service.user.api.*;

/**
 * Implementation of a service for accessing users.
 * @author Garret Wilson
 */
@CloudFunctionService
public class UserServiceImpl implements UserService {

	private final Map<String, UserProfile> userDatabase = new ConcurrentHashMap<>();

	/** Constructor. */
	public UserServiceImpl() {
		//populate "user database" with example data
		userDatabase.put("jdoe", new UserProfile("jdoe", "Jay", "Doe"));
	}

	@Override
	public CompletableFuture<Optional<UserProfile>> findUserProfileByUsername(final String username) {
		return completedFuture(Optional.ofNullable(userDatabase.get(requireNonNull(username))));
	}

}
