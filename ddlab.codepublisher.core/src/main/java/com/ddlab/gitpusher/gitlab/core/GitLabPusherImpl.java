/*
 * Copyright 2020 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.gitpusher.gitlab.core;

import java.io.File;

import com.ddlab.gitpusher.core.IGitHandler;
import com.ddlab.gitpusher.core.IGitPusher;
import com.ddlab.gitpusher.core.NoGitHandler;
import com.ddlab.gitpusher.core.YesGitHandler;
import com.ddlab.gitpusher.exception.GenericGitPushException;

/**
 * The Class GitLabPusherImpl.
 * 
 * @author Debadatta Mishra
 */
public class GitLabPusherImpl implements IGitPusher {
	/** The git lab handler. */
	private IGitHandler gitLabHandler;

	/** The no git handler. */
	private NoGitHandler noGitHandler;

	/** The yes git handler. */
	private YesGitHandler yesGitHandler;

	/**
	 * Instantiates a new git hub pusher impl.
	 *
	 * @param gitLabHandler the git hub handler
	 */
	public GitLabPusherImpl(IGitHandler gitLabHandler) {
		this.gitLabHandler = gitLabHandler;
		noGitHandler = new NoGitLabHandlerImpl();
		yesGitHandler = new YesGitLabHandlerImpl();
	}

	/**
	 * Push code directly.
	 *
	 * @param projectDir the project dir
	 * @param projDesc   the proj desc
	 * @throws GenericGitPushException the generic git push exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitPusher#pushCodeDirectly(java.io.File)
	 */
	@Override
	public void pushCodeDirectly(File projectDir, String projDesc) throws GenericGitPushException {
		try {
			boolean isGitAvailable = gitLabHandler.isGitDirAvailable(projectDir);
			if (isGitAvailable) {
				yesGitHandler.handle(projectDir, projDesc, gitLabHandler);
			} else {
				noGitHandler.handle(projectDir, projDesc, gitLabHandler);
			}
		} catch (Exception e) {
			throw new GenericGitPushException(e.getMessage());
		}
	}

	/**
	 * Push snippet directly.
	 *
	 * @param file        the file
	 * @param description the description
	 * @throws GenericGitPushException the generic git push exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitPusher#pushSnippetDirectly(java.io.File,
	 * java.lang.String)
	 */
	public void pushSnippetDirectly(File file, String description) throws GenericGitPushException {
		try {
			gitLabHandler.createGist(file, description);
		} catch (Exception e) {
			throw new GenericGitPushException(e.getMessage());
		}
	}

	/**
	 * Gets the existing repos.
	 *
	 * @return the existing repos
	 * @throws GenericGitPushException the generic git push exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitPusher#getExistingRepos()
	 */
	@Override
	public String[] getExistingRepos() throws GenericGitPushException {
		String[] existingRepos = null;
		try {
			existingRepos = gitLabHandler.getAllRepositories();
		} catch (Exception e) {
			throw new GenericGitPushException(e.getMessage());
		}

		return existingRepos;
	}

	/**
	 * Gets the existing snippets.
	 *
	 * @return the existing snippets
	 * @throws GenericGitPushException the generic git push exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitPusher#getExistingSnippets()
	 */
	@Override
	public String[] getExistingSnippets() throws GenericGitPushException {
		String[] existingSnippets = null;
		try {
			existingSnippets = gitLabHandler.getGists();
		} catch (Exception e) {
			throw new GenericGitPushException(e.getMessage());
		}
		return existingSnippets;
	}

}
