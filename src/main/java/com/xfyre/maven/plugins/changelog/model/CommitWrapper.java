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

package com.xfyre.maven.plugins.changelog.model;

import org.eclipse.jgit.revwalk.RevCommit;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.stripEnd;
import static org.apache.commons.lang3.StringUtils.stripStart;

/**
 * Model class representing one GIT commit.
 */
public class CommitWrapper {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final long MILLISECONDS = 1000L;
    private static final String SEPARATOR = "/";

    private final String repositoryUrl;
    private final RevCommit commit;
    private final String commitPrefix;
    private String title;

    private final Map<String, Object> extensions = new HashMap<>();
    private final List<CommitWrapper> children = new ArrayList<>();

    public CommitWrapper(RevCommit commit, String repositoryUrl, String commitPrefix) {
        this.repositoryUrl = repositoryUrl;
        this.commitPrefix = commitPrefix;
        this.commit = commit;
        this.title = commit.getShortMessage();
    }

    public RevCommit getCommit() {
        return commit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortHash() {
        return left(commit.getName(), 7);
    }

    public String getCommitLink() {
        return join(SEPARATOR,
                stripEnd(repositoryUrl, SEPARATOR),
                stripEnd(stripStart(commitPrefix, SEPARATOR), SEPARATOR),
                commit.getName());
    }

    public List<CommitWrapper> getChildren() {
        return children;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public String getCommitTime() {
        return Instant.ofEpochMilli(commit.getCommitTime() * MILLISECONDS).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DATE_FORMAT);
    }
}
