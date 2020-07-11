package com.ddlab.gitpusher.github.core;

import java.io.File;

import com.ddlab.gitpusher.core.IGitHandler;
import com.ddlab.gitpusher.core.IGitPusher;
import com.ddlab.gitpusher.core.UserAccount;
import com.ddlab.gitpusher.gitlab.core.GitLabPusherImpl;

public class TestGitPusher1 {
	private static String USERNAME = "sambittechy@gmail.com";
	private static String PASSWORD = "3T-nJN-y5i6qX2huYRyz";
	private static File PROJECTDIR = new File("E:/sure-delete-1/temp1");
	private static String PROJECTDESC = "Very short description";

	public static void main(String[] args) {
		UserAccount userAccount = new UserAccount(USERNAME, PASSWORD);
		IGitHandler gitHubHandler = new GitHubHandlerImpl(userAccount);
		IGitPusher pusher = new GitLabPusherImpl(gitHubHandler);
		try {
			pusher.pushCodeDirectly(PROJECTDIR, PROJECTDESC);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
