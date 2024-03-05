/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aidev.restozen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Security configuration for the main application.
 *
 * @author Josh Cummings
 */
@Configuration
public class RestConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers(HttpMethod.POST, "/users/customers").permitAll()
						.requestMatchers(HttpMethod.GET, "/h2-console/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/h2-console/**").permitAll()
						.anyRequest().authenticated()
				)
				.cors(withDefaults())
				.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
				.csrf(AbstractHttpConfigurer::disable)
				.httpBasic(withDefaults())
				.oauth2ResourceServer(configurer -> configurer.jwt(withDefaults()))
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling((exceptions) -> exceptions
						.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
						.accessDeniedHandler(new BearerTokenAccessDeniedHandler())
				);
		// @formatter:on
		return http.build();
	}

}
