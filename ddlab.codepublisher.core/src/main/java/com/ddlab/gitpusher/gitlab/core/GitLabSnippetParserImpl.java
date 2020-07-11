/*
 * Copyright 2020 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.gitpusher.gitlab.core;

import static com.ddlab.gitpusher.util.CommonConstants.GITLAB;
import static com.ddlab.gitpusher.util.CommonConstants.PARSE_ERR_1;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.ddlab.gitpusher.core.IGistResponseParser;
import com.ddlab.gitpusher.exception.GenericGitPushException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class GitLabSnippetParserImpl.
 * 
 * @author Debadatta Mishra
 */
public class GitLabSnippetParserImpl implements IGistResponseParser<String, String[]> {

	/**
	 * Parses the.
	 *
	 * @param jsonResponse the json response
	 * @return the string[]
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGistResponseParser#parse(java.lang.Object)
	 */
	@Override
	public String[] parse(String jsonResponse) throws Exception {
		GitLabSnippets[] snippets = null;
		List<String> gistList = new ArrayList<String>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			try {
				snippets = mapper.readValue(jsonResponse, GitLabSnippets[].class);
				for (GitLabSnippets snippet : snippets)
					gistList.add(snippet.getTitle());
			} catch (IOException e) {
				e.printStackTrace();
				String errMsg = new MessageFormat(PARSE_ERR_1).format(new String[] { GITLAB });
				throw new GenericGitPushException(errMsg);
			}
		} catch (Exception e) {
			throw e;
		}
		return gistList.toArray(new String[0]);
	}

	/**
	 * The Class GitLabSnippets.
	 */
	private static class GitLabSnippets {

		/** The id. */
		@JsonProperty("id")
		private String id;

		/** The title. */
		@JsonProperty("title")
		private String title;

		/**
		 * Gets the id.
		 *
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * Sets the id.
		 *
		 * @param id the new id
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * Gets the title.
		 *
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * Sets the title.
		 *
		 * @param title the new title
		 */
		public void setTitle(String title) {
			this.title = title;
		}

	}
}
