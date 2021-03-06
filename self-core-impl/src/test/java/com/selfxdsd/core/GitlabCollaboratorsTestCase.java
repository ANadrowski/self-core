/**
 * Copyright (c) 2020, Self XDSD Contributors
 * All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to read the Software only. Permission is hereby NOT GRANTED to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.selfxdsd.core;

import com.selfxdsd.api.Provider;
import com.selfxdsd.api.User;
import com.selfxdsd.api.storage.Storage;
import com.selfxdsd.core.mock.MockJsonResources;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import javax.json.Json;
import java.net.HttpURLConnection;

/**
 * Unit tests for {@link GitlabCollaborators}.
 * @author criske
 * @version $Id$
 * @since 0.0.13
 */
public final class GitlabCollaboratorsTestCase {

    /**
     * A new Collaboration invitation can be sent ok (receives CREATED).
     */
    @Test
    public void sendsInvitationCreated() {
        final User user = Mockito.mock(User.class);
        Mockito.when(user.username()).thenReturn("amihaiemil");
        final Provider provider = new Gitlab(
            user,
            Mockito.mock(Storage.class),
            new MockJsonResources(
                new AccessToken.Gitlab("gitlab123"),
                req -> {
                    MatcherAssert.assertThat(
                        req.getAccessToken().value(),
                        Matchers.equalTo("gitlab123")
                    );
                    MatcherAssert.assertThat(
                        req.getMethod(),
                        Matchers.equalTo("POST")
                    );
                    MatcherAssert.assertThat(
                        req.getBody(),
                        Matchers.equalTo(Json.createObjectBuilder()
                            .add("user_id", "1234")
                            .add("access_level", 30)
                            .build())
                    );
                    MatcherAssert.assertThat(
                        req.getUri().toString(),
                        Matchers.equalTo("https://gitlab.com/api/v4/"
                                + "projects/amihaiemil%2Frepo/members")
                    );
                    return new MockJsonResources.MockResource(
                        HttpURLConnection.HTTP_CREATED,
                        Json.createObjectBuilder().build()
                    );
                }
            )
        );
        final boolean res = provider
            .repo("amihaiemil", "repo")
            .collaborators()
            .invite("1234", "30");
        MatcherAssert.assertThat(
            res, Matchers.is(Boolean.TRUE)
        );
    }

    /**
     * An existing Collaboration invitation can be sent ok
     * (receives CONFLICT).
     */
    @Test
    public void sendsInvitationConflict() {
        final User user = Mockito.mock(User.class);
        Mockito.when(user.username()).thenReturn("amihaiemil");
        final Provider provider = new Gitlab(
            user,
            Mockito.mock(Storage.class),
            new MockJsonResources(
                new AccessToken.Gitlab("gitlab123"),
                req -> {
                    MatcherAssert.assertThat(
                        req.getAccessToken().value(),
                        Matchers.equalTo("gitlab123")
                    );
                    MatcherAssert.assertThat(
                        req.getMethod(),
                        Matchers.equalTo("POST")
                    );
                    MatcherAssert.assertThat(
                        req.getBody(),
                        Matchers.equalTo(Json.createObjectBuilder()
                            .add("user_id", "1234")
                            .add("access_level", 30)
                            .build())
                    );
                    MatcherAssert.assertThat(
                        req.getUri().toString(),
                        Matchers.equalTo("https://gitlab.com/api/v4/"
                                + "projects/amihaiemil%2Frepo/members")
                    );
                    return new MockJsonResources.MockResource(
                        HttpURLConnection.HTTP_CONFLICT,
                        Json.createObjectBuilder().build()
                    );
                }
            )
        );
        final boolean res = provider
            .repo("amihaiemil", "repo")
            .collaborators()
            .invite("1234", "30");
        MatcherAssert.assertThat(
            res, Matchers.is(Boolean.TRUE)
        );
    }

    /**
     * The response should be false because the server sends us
     * NOT FOUND.
     */
    @Test
    public void sendsInvitationNotFound() {
        final User user = Mockito.mock(User.class);
        Mockito.when(user.username()).thenReturn("amihaiemil");
        final Provider provider = new Gitlab(
            user,
            Mockito.mock(Storage.class),
            new MockJsonResources(
                new AccessToken.Gitlab("gitlab123"),
                req -> {
                    MatcherAssert.assertThat(
                        req.getAccessToken().value(),
                        Matchers.equalTo("gitlab123")
                    );
                    MatcherAssert.assertThat(
                        req.getMethod(),
                        Matchers.equalTo("POST")
                    );
                    MatcherAssert.assertThat(
                        req.getBody(),
                        Matchers.equalTo(Json.createObjectBuilder()
                            .add("user_id", "534534")
                            .add("access_level", 30)
                            .build())
                    );
                    MatcherAssert.assertThat(
                        req.getUri().toString(),
                        Matchers.equalTo("https://gitlab.com/api/v4/"
                                + "projects/amihaiemil%2Frepo/members")
                    );
                    return new MockJsonResources.MockResource(
                        HttpURLConnection.HTTP_NOT_FOUND,
                        Json.createObjectBuilder().build()
                    );
                }
            )
        );
        final boolean res = provider
            .repo("amihaiemil", "repo")
            .collaborators()
            .invite("534534", "30");
        MatcherAssert.assertThat(
            res, Matchers.is(Boolean.FALSE)
        );
    }

}
