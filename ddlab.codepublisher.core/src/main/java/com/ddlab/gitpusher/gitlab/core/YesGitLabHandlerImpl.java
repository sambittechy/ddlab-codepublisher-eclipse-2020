/*
 * Copyright 2020 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.gitpusher.gitlab.core;

import java.io.File;

import com.ddlab.gitpusher.core.IGitHandler;
import com.ddlab.gitpusher.core.YesGitHandler;

/**
 * The Class YesGitLabHandlerImpl.
 * 
 * @author Debadatta Mishra
 */
public class YesGitLabHandlerImpl implements YesGitHandler {

	/**
	 * Handle.
	 *
	 * @param projectDir the project dir
	 * @param gitHandler the git handler
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.YesGitHandler#handle(java.io.File,
	 * com.ddlab.gitpusher.core.IGitHandler)
	 */
	@Override
	public void handle(File projectDir, String projDesc, IGitHandler gitHandler) throws Exception {
		gitHandler.update(projectDir, projDesc);
	}
}
