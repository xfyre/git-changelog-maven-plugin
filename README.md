# git-changelog-maven-plugin

[![Build Status](https://travis-ci.org/xfyre/git-changelog-maven-plugin.svg?branch=master)](https://travis-ci.org/xfyre/git-changelog-maven-plugin) 
[![Coverage Status](https://coveralls.io/repos/github/xfyre/git-changelog-maven-plugin/badge.svg?branch=master)](https://coveralls.io/github/xfyre/git-changelog-maven-plugin?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.xfyre.maven.plugins/git-changelog-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.xfyre.maven.plugins/git-changelog-maven-plugin)

## Note

Originally forked from [git-changelog-maven-plugin](https://github.com/jakubplichta/git-changelog-maven-plugin), to
accommodate changes necessary to properly generate changelogs in BitBucket environment.

## Introduction

The _git-changelog-maven-plugin_ is Maven plugin for generating change log from GIT repository using [Mustache](http://mustache.github.io/)
templates.

This plugin is currently designed to work with mainline repositories with one main (_master_) branch.

## Usage

The _git-changelog-maven-plugin_ is available in Maven Central Repository, to use it from Maven add to `pom.xml`:

```xml
<plugin>
    <groupId>com.xfyre.maven.plugins</groupId>
    <artifactId>git-changelog-maven-plugin</artifactId>
    <version>0.4.3</version>
</plugin>
```

See [releases page](https://github.com/xfyre/git-changelog-maven-plugin/releases) for information about versions
and notable changes.

### Configuration parameters

Following configuration parameters are supported by the _git-changelog-maven-plugin_.

**repoRoot**, default: `${project.basedir}`
* path to GIT repository root.

**outputFile**, default: `${project.basedir}/CHANGELOG.md`
* location of the generated change log file.

**reportTitle**, default: `Change Log`
* string passed as report title to templates.

**templateFile**, default: `${project.basedir}/changelog.mustache`
* location of the template file. If not found default `changelog.mustache` resource from plugin will be used.

**includeCommits**, default: `.*`
* which commits are to be included.

**excludeCommits**, default: `^\\[maven-release-plugin\\].*`
* which commits are to be skipped.

**nextRelease**, default: `${project.version}`
* string representing unreleased project version.

**deduplicateChildCommits**, default: `true`
* when set to _true_ child commits containing same message as pull request are not included in resulting change log.

**toRef**, default: `HEAD`
* latest GIT commit to be used.

**jiraServer**, _optional_
* Jira server URL to be used. If present commit messages containing issue references are extended with ticket details.  

**repositoryUrl**, _optional_
* GitHub repository URL to be used. If present commit messages containing GitHub pull request references are extended
with relevant details.

**commitPrefix**, _optional_
* URL path prefix for commit links. Defaults to `/commit/` (which is valid for GitHub), needs to be changed if you
are using something else (like BitBucket or Stash).

**pullRequestPrefix**, _optional_
* URL path prefix for pull request links. Defaults to `/pull/` (which is valid for GitHub), needs to be changed if you
are using something else (like BitBucket or Stash).

**ignoreOlderThen**, _optional_
* Ignore commits older than date (format: YYYY-MM-dd HH:mm:ss)

**releaseTagFilter**, _optional_
* Filter release tags by regex. By default it's assumed to be `^.+$` 

### Automatic change log generation during Maven release

You can configure Maven release plugin to update change log with each release. 

```xml
<plugin>
    <artifactId>maven-release-plugin</artifactId>
    <configuration>
        <preparationGoals>clean git-changelog:git-changelog scm:checkin -DpushChanges=false -Dincludes=CHANGELOG.md -Dmessage="[maven-release-plugin] Update CHANGELOG.md" verify</preparationGoals>
        <completionGoals>git-changelog:git-changelog scm:checkin -DpushChanges=false -Dincludes=CHANGELOG.md -Dmessage="[maven-release-plugin] Update CHANGELOG.md"</completionGoals>
    </configuration>
</plugin>
```

## Mustache templates

The _git-changelog-maven-plugin_ contains [default template](src/main/resources/changelog.mustache) for change log
generation but you can define any customized template you want.

### Data structure provided to template

The _git-changelog-maven-plugin_ provides following data structures to _Mustache_ templates:

```
- reportTitle
* tags
    - name
    * commits
        - title
        - shortHash
        - commitLink
        * children
            - title
            - shortHash
            - commitLink
            - commitTime
        - extensions
            - jira
                * title
                    - token
                    - link
                        - id
                        - link
            - pullRequest
                - id
                - title
                - link
```

## License and conditions

The _git-changelog-maven-plugin_ is free and open-source software provided under [The Apache License, Version 2.0](LICENSE).
