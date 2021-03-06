/*
 * Copyright 2016 git-changelog-maven-plugin contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xfyre.maven.plugins.changelog.handlers;

import com.xfyre.maven.plugins.changelog.model.CommitWrapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.stripEnd;
import static org.apache.commons.lang3.StringUtils.stripStart;

/**
 * {@link CommitHandler} capable of detecting Pull Request references in commit messages.
 */
public class PullRequestHandler implements CommitHandler {
    private static final String SEPARATOR = "/";
    private static final Pattern PATTERN = Pattern.compile("Merge pull request #(\\d+).*");
    private final String repositoryUrl;
    private final String pullRequestPrefix;

    public PullRequestHandler(String repositoryUrl, String pullRequestPrefix) {
        this.repositoryUrl = repositoryUrl;
        this.pullRequestPrefix = pullRequestPrefix;
    }

    @Override
    public void handle(CommitWrapper commit) {
        final String shortMessage = commit.getCommit().getShortMessage();

        final Matcher matcher = PATTERN.matcher(shortMessage);
        if (matcher.matches()) {
            final String title = commit.getCommit().getFullMessage().split("\n")[2];

            commit.setTitle(title);
            final String id = matcher.group(1);
            final String pullRequestLink = join(SEPARATOR,
                    stripEnd(repositoryUrl, SEPARATOR),
                    stripEnd(stripStart(pullRequestPrefix, SEPARATOR), SEPARATOR),
                    id
            );
            commit.getExtensions().put("pullRequest", new PullRequest(id, title, pullRequestLink));
        }
    }

    public static class PullRequest {
        private final String id;
        private final String title;
        private final String link;

        public PullRequest(final String id, final String title, String link) {
            this.id = id;
            this.title = title;
            this.link = link;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }
    }
}
