package com.selfxdsd.core;

import com.selfxdsd.api.Login;
import com.selfxdsd.api.Self;
import com.selfxdsd.api.User;
import com.selfxdsd.api.storage.Storage;
import com.selfxdsd.core.mock.InMemory;
import org.junit.Test;

import java.net.URL;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Unit tests for {@link GithubLogin}.
 * @author criske
 * @version $Id$
 * @since 0.0.1
 */
public final class GithubLoginTestCase {

    /**
     * SelfCore can sign up a Github User.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void githubLoginWorks() throws Exception {
        final Storage storage = new InMemory();
        final Self self = new SelfCore(storage);
        final Login githubLogin = new GithubLogin(
            "amihaiemil", "amihaiemil@gmail.com",
            new URL("https://gravatar.com/amihaiemil"), "gh123token"
        );
        final User amihaiemil = self.login(githubLogin);
        assertThat(amihaiemil.username(), equalTo("amihaiemil"));
        assertThat(amihaiemil.email(), equalTo("amihaiemil@gmail.com"));
        assertThat(amihaiemil.avatar(),
            equalTo(new URL("https://gravatar.com/amihaiemil")));
        assertThat(amihaiemil.provider(), instanceOf(Github.class));
        assertThat(amihaiemil.provider().name(), equalTo("github"));
        assertThat(storage.users(), iterableWithSize(1));
        assertThat(storage.users().user("amihaiemil", "github")
                .username(), equalTo("amihaiemil"));
    }
}