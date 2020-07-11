/*
 * Copyright 2020 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.gitpusher.gitlab.core;

import static com.ddlab.gitpusher.util.CommonConstants.GITLAB;
import static com.ddlab.gitpusher.util.CommonConstants.PARSE_ERR_1;

import java.io.IOException;
import java.text.MessageFormat;

import com.ddlab.gitpusher.core.IErrorResponseParser;
import com.ddlab.gitpusher.core.IResponseParser;
import com.ddlab.gitpusher.exception.GenericGitPushException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class GitLabResponseParserImpl.
 * 
 * @author Debadatta Mishra
 */
public class GitLabResponseParserImpl implements IResponseParser<String, GitLabRepo> {

	/**
	 * Gets the all repos.
	 *
	 * @param jsonResponse the json response
	 * @return the all repos
	 * @throws GenericGitPushException the generic git push exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IResponseParser#getAllRepos(java.lang.Object)
	 */
	@Override
	public GitLabRepo getAllRepos(String jsonResponse) throws GenericGitPushException {
		GitLabRepo gitRepo = new GitLabRepo();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return gitRepo;
	}

	/**
	 * Gets the user.
	 *
	 * @param jsonResponse the json response
	 * @return the user
	 * @throws GenericGitPushException the generic git push exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IResponseParser#getUser(java.lang.Object)
	 */
	@Override
	public GitLabRepo getUser(String jsonResponse) throws GenericGitPushException {
		GitLabRepo[] gitRepo = new GitLabRepo[] {};
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			gitRepo = mapper.readValue(jsonResponse, GitLabRepo[].class);
		} catch (IOException e) {
			e.printStackTrace();
			String errMsg = new MessageFormat(PARSE_ERR_1).format(new String[] { GITLAB });
			throw new GenericGitPushException(errMsg);
		}
		return gitRepo[0];
	}

	/**
	 * Gets the newly created hosted repo.
	 *
	 * @param jsonResponse the json response
	 * @param errorParser  the error parser
	 * @return the newly created hosted repo
	 * @throws GenericGitPushException the generic git push exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ddlab.gitpusher.core.IResponseParser#getNewlyCreatedHostedRepo(java.lang.
	 * Object, com.ddlab.gitpusher.core.IErrorResponseParser)
	 */
	@Override
	public GitLabRepo getNewlyCreatedHostedRepo(String jsonResponse, IErrorResponseParser<String, String> errorParser)
			throws GenericGitPushException {
		GitLabRepo gitRepo = new GitLabRepo();
		return gitRepo;
	}

	/**
	 * Gets the all gist snippets.
	 *
	 * @param s the s
	 * @return the all gist snippets
	 * @throws GenericGitPushException the generic git push exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ddlab.gitpusher.core.IResponseParser#getAllGistSnippets(java.lang.Object)
	 */
	@Override
	public GitLabRepo getAllGistSnippets(String s) throws GenericGitPushException {
		return null;
	}

}
