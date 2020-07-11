package com.ddlab.gitpusher.gitlab;

import java.io.File;
import java.util.Arrays;

import com.ddlab.gitpusher.core.IGitHandler;
import com.ddlab.gitpusher.core.IGitPusher;
import com.ddlab.gitpusher.core.UserAccount;
import com.ddlab.gitpusher.gitlab.core.GitLabHandlerImpl;
import com.ddlab.gitpusher.gitlab.core.GitLabPusherImpl;

public class TestGitLabPusher1 {
//	private static String USERNAME = "sambittechy@gmail.com";
	private static String USERNAME = "asdf";
//	private static String USERNAME = "sambittechy";
//	private static String PASSWORD = "3T-nJN-y5i6qX2huYRyz";
	private static String PASSWORD = "asdf";
	private static File PROJECTDIR = new File("E:/sure-delete-1/check9");
//	private static String REPONAME = "temp1";
	private static String REPONAME = "check7";
	private static String PROJECTDESC = "Very short description for check 9";
	
	private static String SNIP_FILE_PATH = "E:/sure-delete-1/check11/settings.txt";

	public static void doesRepoExist(IGitHandler gitHubHandler) {
		try {
			boolean repoFlag = gitHubHandler.repoExists(REPONAME);
			System.out.println("ReposFlag: " + repoFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showUserName(IGitHandler gitHubHandler) {
		try {
			String gitUserName = gitHubHandler.getUserName();
			System.out.println("GitUserName: " + gitUserName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showAllRepos(IGitHandler gitHubHandler) {
		try {
			String[] repos = gitHubHandler.getAllRepositories();
			for(String repoName: repos)
				System.out.println("Repo Name: "+repoName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showAllSnippets(IGitHandler gitHubHandler) {
		try {
			String[] snippets = gitHubHandler.getGists();
			for(String snippetName: snippets)
				System.out.println("Snippet Name: "+snippetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createSnippet(IGitHandler gitHubHandler) {
		File file = new File(SNIP_FILE_PATH);
		try {
			gitHubHandler.createGist(file, "From Eclipse");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		UserAccount userAccount = new UserAccount(USERNAME, PASSWORD);
		IGitHandler gitHubHandler = new GitLabHandlerImpl(userAccount);
		IGitPusher pusher = new GitLabPusherImpl(gitHubHandler);
		
//		showUserName(gitHubHandler);
//		doesRepoExist(gitHubHandler);
//		showAllRepos(gitHubHandler);
//		showAllSnippets(gitHubHandler);
//		createSnippet(gitHubHandler);
		
//		try {
//			gitHubHandler.createHostedRepo(REPONAME, PROJECTDESC);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		try {
//			pusher.pushCodeDirectly(PROJECTDIR, PROJECTDESC);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		String[] GIT_ACCOUNTS = new String[] { "GitHub", "GitLab", "Bitbucket"  };
		String gitType = "Bitbucket";
		int index = Arrays.asList(GIT_ACCOUNTS).indexOf(gitType);
		System.out.println("Index: "+index);
	}
}
