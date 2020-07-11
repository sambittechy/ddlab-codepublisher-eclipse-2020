/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.gitpusher.github.core;

import static com.ddlab.gitpusher.util.CommonUtil.HOME_GIT_PATH;
import static com.ddlab.gitpusher.util.CommonConstants.*;
import java.io.File;
import java.text.MessageFormat;

import org.apache.commons.io.FileUtils;

import com.ddlab.gitpusher.core.IGitHandler;
import com.ddlab.gitpusher.core.NoGitHandler;
import com.ddlab.gitpusher.exception.GenericGitPushException;

/**
 * The Class NoGitHubHandlerImpl.
 *
 * @author Debadatta Mishra
 */
public class NoGitHubHandlerImpl implements NoGitHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.NoGitHandler#handle(java.io.File,
	 * com.ddlab.gitpusher.core.IGitHandler)
	 */
	@Override
	public void handle(File projectDir, String projDesc, IGitHandler gitHandler) throws Exception {
		String repoName = projectDir.getName();
		boolean repoExistsFlag = gitHandler.repoExists(repoName);
		if (repoExistsFlag) {
//      System.out.println(
//          "A repository with the same project name already exists in Github, create different name");
			String errMsg = new MessageFormat(REPO_CREATE_ERR_MSG).format(new String[] { GITHUB });
			throw new GenericGitPushException(errMsg);
		} else {
			createCloneCopy(repoName, projDesc, projectDir, gitHandler);
			// update the project
			gitHandler.update(projectDir, null);
		}
	}

	/**
	 * Creates the clone copy.
	 *
	 * @param repoName   the repo name
	 * @param projectDir the project dir
	 * @param gitHandler the git handler
	 * @throws Exception the exception
	 */
	private void createCloneCopy(String repoName, String projDesc, File projectDir, IGitHandler gitHandler)
			throws Exception {
		// Create a Hosted Repo
		File tempCloneDir = null;
		try {
			gitHandler.createHostedRepo(repoName, projDesc);
			tempCloneDir = new File(HOME_GIT_PATH);
			// clone project
			gitHandler.clone(repoName, tempCloneDir);
			// copy to project directory
			FileUtils.copyDirectory(tempCloneDir, projectDir);
		} catch (Exception e) {
			throw e;
		} finally {
			String dotGitDir = tempCloneDir.getAbsolutePath() + File.separator + DOT_GIT;
			FileUtils.deleteQuietly(new File(dotGitDir));
		}
	}
}
