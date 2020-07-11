/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.gitpusher.github.core;

import static com.ddlab.gitpusher.util.CommonConstants.CLONE_ERR_MSG;
import static com.ddlab.gitpusher.util.CommonConstants.GENERIC_COMIT_MSG;
import static com.ddlab.gitpusher.util.CommonConstants.GENERIC_LOCAL_GIT_EXIST;
import static com.ddlab.gitpusher.util.CommonConstants.GENERIC_LOGIN_ERR_MSG;
import static com.ddlab.gitpusher.util.CommonConstants.GIST_API;
import static com.ddlab.gitpusher.util.CommonConstants.GIST_ERR_MSG;
import static com.ddlab.gitpusher.util.CommonConstants.GITHUB;
import static com.ddlab.gitpusher.util.CommonConstants.GITHUB_CREATE_GIST_API;
import static com.ddlab.gitpusher.util.CommonConstants.GITHUB_REPO_CLONE_URI;
import static com.ddlab.gitpusher.util.CommonConstants.GIT_API_URI;
import static com.ddlab.gitpusher.util.CommonConstants.HTTP_200;
import static com.ddlab.gitpusher.util.CommonConstants.HTTP_201;
import static com.ddlab.gitpusher.util.CommonConstants.HTTP_401;
import static com.ddlab.gitpusher.util.CommonConstants.ONE_ANOTHER_REPO_ERR;
import static com.ddlab.gitpusher.util.CommonConstants.ORIGIN;
import static com.ddlab.gitpusher.util.CommonConstants.REMOTE;
import static com.ddlab.gitpusher.util.CommonConstants.REPO_API;
import static com.ddlab.gitpusher.util.CommonConstants.SEARCH_REPO_API;
import static com.ddlab.gitpusher.util.CommonConstants.URL;
import static com.ddlab.gitpusher.util.CommonConstants.USER_API;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.ddlab.gitpusher.core.GitResponse;
import com.ddlab.gitpusher.core.IErrorResponseParser;
import com.ddlab.gitpusher.core.IGistResponseParser;
import com.ddlab.gitpusher.core.IGitHandler;
import com.ddlab.gitpusher.core.IResponseParser;
import com.ddlab.gitpusher.core.UserAccount;
import com.ddlab.gitpusher.exception.GenericGitPushException;
import com.ddlab.gitpusher.github.bean.CodeFiles;
import com.ddlab.gitpusher.github.bean.CodeSnippet;
import com.ddlab.gitpusher.github.bean.GitHubRepo;
import com.ddlab.gitpusher.github.bean.Repo;
import com.ddlab.gitpusher.util.HTTPUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class GitHubHandlerImpl.
 *
 * @author Debadatta Mishra
 */
public class GitHubHandlerImpl implements IGitHandler {

	/** The user account. */
	private UserAccount userAccount;

	/**
	 * Instantiates a new git hub handler impl.
	 *
	 * @param userAccount the user account
	 */
	public GitHubHandlerImpl(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#getUserName()
	 */
	@Override
	public String getUserName() throws GenericGitPushException {
		GitHubRepo gitRepo = null;
		String uri = GIT_API_URI + USER_API;
		HttpGet httpGet = new HttpGet(uri);
		String encodedUser = HTTPUtil.getEncodedUser(userAccount.getUserName(), userAccount.getPassword());
		httpGet.setHeader("Authorization", "Basic " + encodedUser);
		try {
			GitResponse gitResponse = HTTPUtil.getHttpGetOrPostResponse(httpGet);
			gitRepo = getGitHubUser(gitResponse);
		} catch (GenericGitPushException e) {
			throw e;
		}
		return gitRepo.getLoginUser();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#getAllRepositories()
	 */
	@Override
	public String[] getAllRepositories() throws Exception {
		GitHubRepo gitRepo = null;
		String uri = GIT_API_URI + REPO_API;
		HttpGet httpGet = new HttpGet(uri);
		String encodedUser = HTTPUtil.getEncodedUser(userAccount.getUserName(), userAccount.getPassword());
		httpGet.setHeader("Authorization", "Basic " + encodedUser);
		try {
			GitResponse gitResponse = HTTPUtil.getHttpGetOrPostResponse(httpGet);
			gitRepo = getAllGitHubRepos(gitResponse);
		} catch (GenericGitPushException e) {
			throw e;
		}
		Repo[] repos = gitRepo.getRepos();
		List<String> repoList = new ArrayList<String>();
		for (Repo repo : repos)
			repoList.add(repo.getName());
		return repoList.toArray(new String[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#repoExists(java.lang.String)
	 */
	@Override
	public boolean repoExists(String repoName) throws GenericGitPushException {
		boolean existsFlag = false;
		String uri = GIT_API_URI + SEARCH_REPO_API;
		MessageFormat formatter = new MessageFormat(uri);
		String loginUser = getUserName();
		uri = formatter.format(new String[] { loginUser, repoName });
		HttpGet httpGet = new HttpGet(uri);
		String encodedUser = HTTPUtil.getEncodedUser(userAccount.getUserName(), userAccount.getPassword());
		httpGet.setHeader("Authorization", "Basic " + encodedUser);
		try {
			GitResponse gitResponse = HTTPUtil.getHttpGetOrPostResponse(httpGet);
			existsFlag = gitResponse.getStatusCode().equals(HTTP_200) ? true : false;
		} catch (GenericGitPushException e) {
			throw e;
		}
		return existsFlag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#clone(java.lang.String,
	 * java.io.File)
	 */
	@Override
	public void clone(String repoName, File dirPath) throws GenericGitPushException {
		String uri = GITHUB_REPO_CLONE_URI;
		String loginUser = getUserName();
		MessageFormat formatter = new MessageFormat(uri);
		uri = formatter.format(new String[] { loginUser, repoName });
//    System.out.println("Git clone URI : " + uri);
		CredentialsProvider crProvider = new UsernamePasswordCredentialsProvider(userAccount.getUserName(),
				userAccount.getPassword());
		Git git = null;
		try {
			git = Git.cloneRepository().setCredentialsProvider(crProvider).setURI(uri).setDirectory(dirPath).call();
		} catch (GitAPIException e) {
			String errMsg = new MessageFormat(CLONE_ERR_MSG).format(new String[] { GITHUB });
			throw new GenericGitPushException(errMsg);
		} finally {
			if (git != null)
				git.close();
		}
	}

	/**
	 * Gets the all git hub repos.
	 *
	 * @param gitResponse the git response
	 * @return the all git hub repos
	 * @throws GenericGitPushException the generic git push exception
	 */
	private GitHubRepo getAllGitHubRepos(GitResponse gitResponse) throws GenericGitPushException {
		GitHubRepo gitRepo = null;
		if (gitResponse.getStatusCode().equals(HTTP_200)) {
			IResponseParser<String, GitHubRepo> responseParser = new GitHubResponseParserImpl();
			gitRepo = responseParser.getAllRepos(gitResponse.getResponseText());
		} else if (gitResponse.getStatusCode().equals(HTTP_401)) {
			throw new GenericGitPushException(GENERIC_LOGIN_ERR_MSG);
		}
		return gitRepo;
	}

	/**
	 * Gets the git hub user.
	 *
	 * @param gitResponse the git response
	 * @return the git hub user
	 * @throws GenericGitPushException the generic git push exception
	 */
	private GitHubRepo getGitHubUser(GitResponse gitResponse) throws GenericGitPushException {
		GitHubRepo gitRepo = null;
		if (gitResponse.getStatusCode().equals(HTTP_200)) {
			IResponseParser<String, GitHubRepo> responseParser = new GitHubResponseParserImpl();
			gitRepo = responseParser.getUser(gitResponse.getResponseText());
		} else if (gitResponse.getStatusCode().equals(HTTP_401)) {
			throw new GenericGitPushException(GENERIC_LOGIN_ERR_MSG);
		}
		return gitRepo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ddlab.gitpusher.core.IGitHandler#getUrlFromLocalRepsitory(java.io.File)
	 */
	@Override
	public String getUrlFromLocalRepsitory(File gitDirPath) throws GenericGitPushException {
		String existingRepoUrl = null;
		Git git = null;
		Repository repository = null;
		try {
			git = Git.open(gitDirPath);
			repository = git.getRepository();
			existingRepoUrl = repository.getConfig().getString(REMOTE, ORIGIN, URL);
		} catch (IOException e) {
			throw new GenericGitPushException(GENERIC_LOCAL_GIT_EXIST);
		}
		return existingRepoUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#isGitDirAvailable(java.io.File)
	 */
	@Override
	public boolean isGitDirAvailable(File gitDirPath) {
		boolean isAvailable = false;
		Git git = null;
		try {
			git = Git.open(gitDirPath);
			isAvailable = true;
		} catch (IOException e) {
		} finally {
			if (git != null)
				git.close();
		}
		return isAvailable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#update(java.io.File,
	 * java.lang.String)
	 */
	@Override
	public void update(File cloneDirPath, String message) throws GenericGitPushException {
		Git git = null;
		RevCommit revisionCommit = null;
		String comitMsg = GENERIC_COMIT_MSG;
		MessageFormat formatter = new MessageFormat(comitMsg);
		String userName = getUserName();
		userName = (userName == null) ? userAccount.getUserName() : userName;
		PersonIdent authorDetails = new PersonIdent(userName, userAccount.getUserName());
		message = (message == null) ? formatter.format(new String[] { userAccount.getUserName() }) : message;
		try {
			git = Git.open(cloneDirPath);
			git.add().addFilepattern(".").call();
			CommitCommand commitCommand = git.commit().setAuthor(authorDetails).setCommitter(authorDetails).setAll(true)
					.setMessage(message);
			revisionCommit = commitCommand.call();

			CredentialsProvider cp = new UsernamePasswordCredentialsProvider(userAccount.getUserName(),
					userAccount.getPassword());
			PushCommand pushCommand = git.push();
			pushCommand.setCredentialsProvider(cp).setForce(true).setPushAll();

			Set<RemoteRefUpdate.Status> statusSet = new HashSet<RemoteRefUpdate.Status>();
			Iterable<PushResult> resultIterable = pushCommand.call();
			PushResult pushResult = resultIterable.iterator().next();
			for (final RemoteRefUpdate rru : pushResult.getRemoteUpdates()) {
				RemoteRefUpdate.Status status = rru.getStatus();
				statusSet.add(status);
			}
			if (statusSet.contains(RemoteRefUpdate.Status.OK)) {
				System.out.println("All files pushed to GitHub successfully");
			}
		} catch (IOException | GitAPIException e) {
			revertChanges(git, revisionCommit);
			throw new GenericGitPushException(e.getMessage() + ONE_ANOTHER_REPO_ERR);
		} finally {
			if (git != null)
				git.close();
		}
	}

	/**
	 * Revert changes.
	 *
	 * @param git            the git
	 * @param revisionCommit the revision commit
	 */
	private void revertChanges(Git git, RevCommit revisionCommit) {
//    System.out.println("Going to revert the changes");
		try {
			git.revert().call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#createHostedRepo(java.lang.String)
	 */
	@Override
	public void createHostedRepo(String repoName, String repoDescription) throws Exception {
		String jsonRepo = new HostedRepo(repoName, repoDescription).toJson();
		String uri = GIT_API_URI + REPO_API;
		HttpPost httpPost = new HttpPost(uri);
		String encodedUser = HTTPUtil.getEncodedUser(userAccount.getUserName(), userAccount.getPassword());
		httpPost.setHeader("Authorization", "Basic " + encodedUser);
		StringEntity jsonBodyRequest = new StringEntity(jsonRepo);
		httpPost.setEntity(jsonBodyRequest);
		try {
			GitResponse gitResponse = HTTPUtil.getHttpGetOrPostResponse(httpPost);
			if (gitResponse.getStatusCode().equals(HTTP_401))
				throw new GenericGitPushException(GENERIC_LOGIN_ERR_MSG);
			IErrorResponseParser<String, String> errorParser = new RepoCreateErrorResponseParser();
			IResponseParser<String, GitHubRepo> responseParser = new GitHubResponseParserImpl();
			@SuppressWarnings("unused")
			GitHubRepo gitRepo = responseParser.getNewlyCreatedHostedRepo(gitResponse.getResponseText(), errorParser);
		} catch (GenericGitPushException e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#getGists()
	 */
	@Override
	public String[] getGists() throws Exception {
		String[] gists = null;
		String uri = GIT_API_URI + GIST_API;
		MessageFormat formatter = new MessageFormat(uri);
		String loginUser = getUserName();
		uri = formatter.format(new String[] { loginUser });
		HttpGet httpGet = new HttpGet(uri);
		String encodedUser = HTTPUtil.getEncodedUser(userAccount.getUserName(), userAccount.getPassword());
		httpGet.setHeader("Authorization", "Basic " + encodedUser);

		try {
			GitResponse gitResponse = HTTPUtil.getHttpGetOrPostResponse(httpGet);
			if (gitResponse.getStatusCode().equals(HTTP_401))
				throw new GenericGitPushException(GENERIC_LOGIN_ERR_MSG);
			IGistResponseParser<String, String[]> gistParser = new GitHubGistParserImpl();
			gists = gistParser.parse(gitResponse.getResponseText());
		} catch (GenericGitPushException e) {
			throw e;
		}
		return gists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#createGist(java.io.File,
	 * java.lang.String)
	 */
	@Override
	public void createGist(File file, String description) throws Exception {
		String uri = GIT_API_URI + GITHUB_CREATE_GIST_API;
		MessageFormat formatter = new MessageFormat(uri);
		String loginUser = getUserName();
		uri = formatter.format(new String[] { loginUser });
		HttpPost httpPost = new HttpPost(uri);
		String encodedUser = HTTPUtil.getEncodedUser(userAccount.getUserName(), userAccount.getPassword());
		httpPost.setHeader("Authorization", "Basic " + encodedUser);
		String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

		CodeFiles codeFile = new CodeFiles();
		// codeFile.setFileName("test.java");
		codeFile.setContent(content);
		Map<String, CodeFiles> codeMap = new HashMap<>();
		codeMap.put(file.getName(), codeFile);

		CodeSnippet snippet = new CodeSnippet();
		snippet.setDescription(description);
		snippet.setFiles(codeMap);

		String gistJson = snippet.toJSON();

		StringEntity jsonBodyRequest = new StringEntity(gistJson);
		httpPost.setEntity(jsonBodyRequest);

		try {
			GitResponse gitResponse = HTTPUtil.getHttpGetOrPostResponse(httpPost);
			if (!gitResponse.getStatusCode().equals(HTTP_201))
				throw new GenericGitPushException(GIST_ERR_MSG + GITHUB);
		} catch (GenericGitPushException e) {
			throw e;
		}
	}

	/**
	 * The Class HostedRepo.
	 *
	 * @author Debadatta Mishra
	 */
	private static class HostedRepo {

		/** The name. */
		@JsonProperty("name")
		private String name;

		@JsonProperty("description")
		private String shortDescription;

		/**
		 * Instantiates a new hosted repo.
		 *
		 * @param name the name
		 */
		public HostedRepo(String name) {
			this.name = name;
		}

		public HostedRepo(String name, String shortDescription) {
			this.name = name;
			this.shortDescription = shortDescription;
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

		public String getShortDescription() {
			return shortDescription;
		}

		public void setShortDescription(String shortDescription) {
			this.shortDescription = shortDescription;
		}

		/**
		 * To json.
		 *
		 * @return the string
		 */
		public String toJson() {
			ObjectMapper mapper = new ObjectMapper();
			String toJson = null;
			try {
				toJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return toJson;
		}
	}
}
