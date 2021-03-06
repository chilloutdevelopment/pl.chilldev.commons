/*
 * This file is part of the pl.wrzasq.commons.
 *
 * @license http://mit-license.org/ The MIT license
 * @copyright 2017, 2019 - 2020 © by Rafał Wrzeszcz - Wrzasq.pl.
 */

package pl.wrzasq.commons.client.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;

/**
 * Feign request interceptor that injects custom HTTP authorization with Bearer token.
 */
@AllArgsConstructor
public class BearerTokenAuthorizer implements RequestInterceptor {
    /**
     * HTTP header name.
     */
    private static final String HEADER_NAME_AUTHORIZATION = "Authorization";

    /**
     * Bearer token prefix.
     */
    private static final String TYPE_BEARER = "Bearer ";

    /**
     * HTTP access token..
     */
    private String token;

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // set the same value as incoming request to execute requests on behalf of the user
        requestTemplate.header(
            BearerTokenAuthorizer.HEADER_NAME_AUTHORIZATION,
            BearerTokenAuthorizer.TYPE_BEARER + this.token
        );
    }
}
