/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.gitpusher.util;

/**
 * The Class CommonConstants.
 *
 * @author Debadatta Mishra
 */
public class CommonConstants {

	/** The Constant HTTP_400. */
	public static final String HTTP_400 = "400";

	/** The Constant HTTP_401. */
	public static final String HTTP_401 = "401";

	/** The Constant HTTP_200. */
	public static final String HTTP_200 = "200";

	/** The Constant HTTP_201. */
	public static final String HTTP_201 = "201";

	/** The Constant REMOTE. */
	public static final String REMOTE = "remote";

	/** The Constant ORIGIN. */
	public static final String ORIGIN = "origin";

	/** The Constant URL. */
	public static final String URL = "url";

	/** The Constant FILE. */
	public static final String FILE = "file";

	/** The Constant TITLE. */
	public static final String TITLE = "title";

	/** The Constant DOT_GIT. */
	public static final String DOT_GIT = ".git";

	/** The Constant UTF_8. */
	public static final String UTF_8 = "UTF-8";

	/** The Constant GITHUB. */
	public static final String GITHUB = "GitHub";

	/** The Constant GITLAB. */
	public static final String GITLAB = "GitLab";

	/** The Constant BITBUCKET. */
	public static final String BITBUCKET = "Bitbucket";

	/** The Constant GENERIC_COMIT_MSG. */
	public static final String GENERIC_COMIT_MSG = "Code committed and pushed by {0} " + "on "
			+ CommonUtil.getTodayDateTime() + " " + "using DDLAB CodePusblisher tool";

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~ API DETAILS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// ~~~~~~~~~~~~~~~~~~~~~~~~~ GITHUB API DETAILS ~~~~~~~~~~~~~~~~~~~~~~

	/** The Constant GIT_API_URI. */
	public static final String GIT_API_URI = "https://api.github.com";

	/** The Constant REPO_API. */
	public static final String REPO_API = "/user/repos"; // To get all repos, also used for creating a repo

	/** The Constant USER_API. */
	public static final String USER_API = "/user"; // To get user details

	/** The Constant GIST_API. */
	public static final String GIST_API = "/users/{0}/gists";

	/** The Constant SEARCH_REPO_API. */
	public static final String SEARCH_REPO_API = "/repos/{0}/{1}"; // To search the repo /repos/<loginUser>/<repoName>

	/** The github repo clone uri. */
	public static String GITHUB_REPO_CLONE_URI = "https://github.com/{0}/{1}.git"; // "https://github.com/debjava/Hello-World.git"

	/** The Constant GITHUB_GET_GIST_API. */
	public static final String GITHUB_GET_GIST_API = GIT_API_URI + GIST_API;

	/** The Constant GITHUB_CREATE_GIST_API. */
	public static final String GITHUB_CREATE_GIST_API = "/gists";

	/** The Constant GITLAB_USER_SEARCH_API. */
	// ~~~~~~~~~~~~~~~~~~~~~~~~~ GITLAB API DETAILS ~~~~~~~~~~~~~~~~~~~~~~
	public static final String GITLAB_USER_SEARCH_API = "https://gitlab.com/api/v4/users?search={0}&private_token={1}";

	/** The Constant GITLAB_GET_REPOS_API. */
	public static final String GITLAB_GET_REPOS_API = "https://gitlab.com/api/v4/projects?"
			+ "private_token={0}&username={1}&owned=true&simple=true&per_page=10";

	/** The Constant GITLAB_REPO_EXIST_API. */
	public static final String GITLAB_REPO_EXIST_API = "https://gitlab.com/api/v4/projects?"
			+ "private_token={0}&username={1}&owned=true&simple=true&per_page=10&search={2}";

	/** The Constant GITLAB_REPO_CLONE_API. */
	public static final String GITLAB_REPO_CLONE_API = "https://gitlab.com/{0}/{1}.git";

	/** The Constant GITLAB_REPO_CREATE_API. */
	public static final String GITLAB_REPO_CREATE_API = "https://gitlab.com/api/v4/projects?"
			+ "private_token={0}&username={1}&name={2}&description={3}&visibility=public";

	/** The Constant GITLAB_GET_SNIIPET_API. */
	public static final String GITLAB_GET_SNIIPET_API = "https://gitlab.com/api/v4/snippets?"
			+ "private_token={0}&username={1}&owned=true&simple=true&per_page=10";

	/** The Constant GITLAB_CREATE_SNIPPET_API. */
	public static final String GITLAB_CREATE_SNIPPET_API = "https://gitlab.com/api/v4/snippets?"
			+ "private_token={0}&username={1}&visibility=public";

	// ~~~~~~~~~~~~~~~~~~~~ BITBUCKET API DETAILS ~~~~~~~~~~~~~~~~~~~~~~~~
	/** The Constant BITBUCKET_API_URI. */
	public static final String BITBUCKET_API_URI = "https://api.bitbucket.org/2.0";

	/** The Constant BITBUCKET_USER_API_URI. */
	public static final String BITBUCKET_USER_API_URI = "/user";

	/** The Constant BITBUCKET_ALL_REPO_API_URI. */
	public static final String BITBUCKET_ALL_REPO_API_URI = "/repositories/{0}";

	/** The Constant BITBUCKET_EXISTING_REPO_API_URI. */
	public static final String BITBUCKET_EXISTING_REPO_API_URI = "/repositories/{0}/{1}";

	/** The Constant BITBUCKET_CREATE_API_URI. */
	public static final String BITBUCKET_CREATE_API_URI = "/repositories/{0}/{1}";

	/** The Constant BITBUCKET_CLONE_API_URI. */
	public static final String BITBUCKET_CLONE_API_URI = "https://{0}@bitbucket.org/{1}/{2}.git";

	/** The Constant BITBUCKET_GET_OR_CREATE_GIST_API_URI. */
	public static final String BITBUCKET_GET_OR_CREATE_GIST_API_URI = "/snippets/{0}";

	/** The Constant PARSE_ERR_1. */
	public static final String PARSE_ERR_1 = "Unable to parse the json response coming from {0}.";

	/** The Constant ONE_ANOTHER_REPO_ERR. */
	public static final String ONE_ANOTHER_REPO_ERR = "\nIt looks like your repository belongs to one "
			+ "Collaborative Git-repository hosting site(GitHub, GitLab, BitBucket) "
			+ "and you are trying to push/update code to another Collaborative Git-repository hosting site(GitHub, GitLab, BitBucket).";

	/** The Constant REPO_CREATE_ERR_MSG. */
	public static final String REPO_CREATE_ERR_MSG = "Unable to create a repo in {0}, "
			+ "a repository/project with the same name may already exist."
			+ "\nYou can refactor the existing project/repository with a different name.";

	/** The Constant SNIPPET_CREATE_ERR_MSG. */
	public static final String SNIPPET_CREATE_ERR_MSG = "Unable to create a Snippet in {0}.";

	/** The Constant CLONE_ERR_MSG. */
	public static final String CLONE_ERR_MSG = "Unable to clone the project from {0}.";

	/** The Constant GENERIC_LOGIN_ERR_MSG. */
	public static final String GENERIC_LOGIN_ERR_MSG = "Invalid credentials, check your user name and password combination."
			+ "\nIf you are using token, ensure that the token is valid and"
			+ "\ntoken has rights to share repository and code snippet.";

	/** The Constant GENERIC_LOCAL_GIT_EXIST. */
	public static final String GENERIC_LOCAL_GIT_EXIST = "Unable to read the local .git directory to find out the Repo URL";

	/** The Constant NO_CONNECTION_MSG. */
	public static final String NO_CONNECTION_MSG = "Unable to connect, check your network connection.";

	/** The Constant GIST_ERR_MSG. */
	public static final String GIST_ERR_MSG = "Unable to create a Code Snippet/Gist in ";

}
