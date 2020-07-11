/*
 * Copyright 2020 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.gitpusher.gitlab.core;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class GitLabRepo.
 * 
 * @author Debadatta Mishra
 */
public class GitLabRepo {

	/** The id. */
	@JsonProperty("id")
	private Long id;

	/** The name. */
	@JsonProperty("name")
	private String name;

	/** The user name. */
	@JsonProperty("username")
	private String userName;

	/** The repo git. */
	@JsonProperty("http_url_to_repo")
	private String repoGit;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the repo git.
	 *
	 * @return the repo git
	 */
	public String getRepoGit() {
		return repoGit;
	}

	/**
	 * Sets the repo git.
	 *
	 * @param repoGit the new repo git
	 */
	public void setRepoGit(String repoGit) {
		this.repoGit = repoGit;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
