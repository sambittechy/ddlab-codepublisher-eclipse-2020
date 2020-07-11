/*
 * Copyright 2020 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.gitpusher.gitlab.core;

//import static com.ddlab.gitpusher.util.CommonConstants.GENERIC_LOGIN_ERR_MSG;
import static com.ddlab.gitpusher.util.CommonConstants.*;
//import static com.ddlab.gitpusher.util.CommonConstants.GENERIC_COMIT_MSG;
//import static com.ddlab.gitpusher.util.CommonConstants.GENERIC_LOCAL_GIT_EXIST;
//import static com.ddlab.gitpusher.util.CommonConstants.GENERIC_LOGIN_ERR_MSG;
//import static com.ddlab.gitpusher.util.CommonConstants.GIST_ERR_MSG;
//import static com.ddlab.gitpusher.util.CommonConstants.GITLAB;
//import static com.ddlab.gitpusher.util.CommonConstants.GITLAB_CLONE_ERR_MSG;
//import static com.ddlab.gitpusher.util.CommonConstants.GITLAB_CREATE_SNIPPET_API;
//import static com.ddlab.gitpusher.util.CommonConstants.GITLAB_ERR_TXT_2;
//import static com.ddlab.gitpusher.util.CommonConstants.GITLAB_GET_REPOS_API;
//import static com.ddlab.gitpusher.util.CommonConstants.GITLAB_GET_SNIIPET_API;
//import static com.ddlab.gitpusher.util.CommonConstants.GITLAB_REPO_CLONE_API;
//import static com.ddlab.gitpusher.util.CommonConstants.GITLAB_REPO_CREATE_API;
//import static com.ddlab.gitpusher.util.CommonConstants.GITLAB_REPO_EXIST_API;
//import static com.ddlab.gitpusher.util.CommonConstants.GITLAB_USER_SEARCH_API;
//import static com.ddlab.gitpusher.util.CommonConstants.HTTP_200;
//import static com.ddlab.gitpusher.util.CommonConstants.HTTP_201;
//import static com.ddlab.gitpusher.util.CommonConstants.HTTP_400;
//import static com.ddlab.gitpusher.util.CommonConstants.HTTP_401;
//import static com.ddlab.gitpusher.util.CommonConstants.ORIGIN;
//import static com.ddlab.gitpusher.util.CommonConstants.PARSE_ERR_1;
//import static com.ddlab.gitpusher.util.CommonConstants.REMOTE;
//import static com.ddlab.gitpusher.util.CommonConstants.URL;
//import static com.ddlab.gitpusher.util.CommonConstants.UTF_8;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
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
import com.ddlab.gitpusher.core.IGistResponseParser;
import com.ddlab.gitpusher.core.IGitHandler;
import com.ddlab.gitpusher.core.IResponseParser;
import com.ddlab.gitpusher.core.UserAccount;
import com.ddlab.gitpusher.exception.GenericGitPushException;
import com.ddlab.gitpusher.util.HTTPUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class GitLabHandlerImpl.
 * 
 * @author Debadatta Mishra
 */
public class GitLabHandlerImpl implements IGitHandler {

	/** The user account. */
	private UserAccount userAccount;

	/**
	 * Instantiates a new git hub handler impl.
	 *
	 * @param userAccount the user account
	 */
	public GitLabHandlerImpl(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 * @throws GenericGitPushException the generic git push exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#getUserName()
	 */
	@Override
	public String getUserName() throws GenericGitPushException {
		GitLabRepo gitRepo = null;
		MessageFormat formatter = new MessageFormat(GITLAB_USER_SEARCH_API);
		String uri = formatter.format(new String[] { userAccount.getUserName(), userAccount.getPassword() });
		HttpGet httpGet = new HttpGet(uri);
		try {
			GitResponse gitResponse = HTTPUtil.getHttpGetOrPostResponse(httpGet);
			gitRepo = getGitLabUser(gitResponse);
		} catch (GenericGitPushException e) {
			throw e;
		}
		return gitRepo.getUserName();
	}

	/**
	 * Gets the all repositories.
	 *
	 * @return the all repositories
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#getAllRepositories()
	 */
	@Override
	public String[] getAllRepositories() throws Exception {
		GitLabRepo[] gitRepos = null;
		String token = userAccount.getPassword();
		String userName = getUserName();
		MessageFormat formatter = new MessageFormat(GITLAB_GET_REPOS_API);
		String uri = formatter.format(new String[] { token, userName });
		HttpGet httpGet = new HttpGet(uri);
		try {
			GitResponse gitResponse = HTTPUtil.getHttpGetOrPostResponse(httpGet);
			gitRepos = getAllGitLabRepos(gitResponse);
		} catch (GenericGitPushException e) {
			throw e;
		}
		List<String> repoList = new ArrayList<String>();
		for (GitLabRepo repo : gitRepos)
			repoList.add(repo.getName());
		return repoList.toArray(new String[0]);
	}

	/**
	 * Repo exists.
	 *
	 * @param repoName the repo name
	 * @return true, if successful
	 * @throws GenericGitPushException the generic git push exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#repoExists(java.lang.String)
	 */
	@Override
	public boolean repoExists(String repoName) throws GenericGitPushException {
		boolean existsFlag = false;
		String userName = userAccount.getUserName();
		String token = userAccount.getPassword();
		MessageFormat formatter = new MessageFormat(GITLAB_REPO_EXIST_API);
		String uri = formatter.format(new String[] { token, userName, repoName });
		HttpGet httpGet = new HttpGet(uri);
		try {
			GitResponse gitResponse = HTTPUtil.getHttpGetOrPostResponse(httpGet);
			if (gitResponse.getStatusCode().equals("401"))
				throw new GenericGitPushException(GENERIC_LOGIN_ERR_MSG);
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode nodes = objectMapper.readTree(gitResponse.getResponseText());
			int length = 0;
			if (nodes.isArray())
				length = nodes.size();
			existsFlag = gitResponse.getStatusCode().equals(HTTP_200) && length != 0 ? true : false;
		} catch (GenericGitPushException e) {
			throw e;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return existsFlag;
	}

	/**
	 * Clone.
	 *
	 * @param repoName the repo name
	 * @param dirPath  the dir path
	 * @throws GenericGitPushException the generic git push exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#clone(java.lang.String,
	 * java.io.File)
	 */
	@Override
	public void clone(String repoName, File dirPath) throws GenericGitPushException {
		MessageFormat formatter = new MessageFormat(GITLAB_REPO_CLONE_API);
		String gitLabUserName = getUserName();
		String uri = formatter.format(new String[] { gitLabUserName, repoName });
		CredentialsProvider crProvider = new UsernamePasswordCredentialsProvider(userAccount.getUserName(),
				userAccount.getPassword());
		Git git = null;
		try {
			git = Git.cloneRepository().setCredentialsProvider(crProvider).setURI(uri).setDirectory(dirPath).call();
		} catch (GitAPIException e) {
			String errMsg = new MessageFormat(CLONE_ERR_MSG).format(new String[] { GITLAB });
			throw new GenericGitPushException(errMsg);
		} finally {
			if (git != null)
				git.close();
		}
	}

	/**
	 * Gets the all git lab repos.
	 *
	 * @param gitResponse the git response
	 * @return the all git lab repos
	 * @throws GenericGitPushException the generic git push exception
	 */
	private GitLabRepo[] getAllGitLabRepos(GitResponse gitResponse) throws GenericGitPushException {
		GitLabRepo[] gitRepos = null;
		if (gitResponse.getStatusCode().equals(HTTP_200)) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			try {
				gitRepos = mapper.readValue(gitResponse.getResponseText(), GitLabRepo[].class);
			} catch (IOException e) {
				e.printStackTrace();
				String errMsg = new MessageFormat(PARSE_ERR_1).format(new String[] { GITLAB });
				throw new GenericGitPushException(errMsg);
			}
		} else if (gitResponse.getStatusCode().equals(HTTP_401)) {
			throw new GenericGitPushException(GENERIC_LOGIN_ERR_MSG);
		}
		return gitRepos;
	}

	/**
	 * Gets the git hub user.
	 *
	 * @param gitResponse the git response
	 * @return the git hub user
	 * @throws GenericGitPushException the generic git push exception
	 */
	private GitLabRepo getGitLabUser(GitResponse gitResponse) throws GenericGitPushException {
		GitLabRepo gitRepo = null;
		if (gitResponse.getStatusCode().equals(HTTP_200)) {
			IResponseParser<String, GitLabRepo> responseParser = new GitLabResponseParserImpl();
			gitRepo = responseParser.getUser(gitResponse.getResponseText());
		} else if (gitResponse.getStatusCode().equals(HTTP_401)) {
			throw new GenericGitPushException(GENERIC_LOGIN_ERR_MSG);
		}
		return gitRepo;
	}

	/**
	 * Gets the url from local repsitory.
	 *
	 * @param gitDirPath the git dir path
	 * @return the url from local repsitory
	 * @throws GenericGitPushException the generic git push exception
	 */
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

	/**
	 * Checks if is git dir available.
	 *
	 * @param gitDirPath the git dir path
	 * @return true, if is git dir available
	 */
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

	/**
	 * Update.
	 *
	 * @param cloneDirPath the clone dir path
	 * @param message      the message
	 * @throws GenericGitPushException the generic git push exception
	 */
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
				System.out.println("All files pushed to GitLab successfully");
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
//		System.out.println("Going to revert the changes");
		try {
			git.revert().call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the hosted repo.
	 *
	 * @param repoName        the repo name
	 * @param repoDescription the repo description
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#createHostedRepo(java.lang.String)
	 */
	@Override
	public void createHostedRepo(String repoName, String repoDescription) throws Exception {
		repoDescription = URLEncoder.encode(repoDescription, UTF_8);
		repoName = URLEncoder.encode(repoName, UTF_8);
		MessageFormat formatter = new MessageFormat(GITLAB_REPO_CREATE_API);
		String uri = formatter.format(
				new String[] { userAccount.getPassword(), userAccount.getUserName(), repoName, repoDescription });
		try {
			HttpPost httpPost = new HttpPost(uri);
			GitResponse gitResponse = HTTPUtil.getHttpGetOrPostResponse(httpPost);
			if (gitResponse.getStatusCode().equals("401"))
				throw new GenericGitPushException(GENERIC_LOGIN_ERR_MSG);
			else if (gitResponse.getStatusCode().equals(HTTP_400)) {
				String errMsg = new MessageFormat(REPO_CREATE_ERR_MSG).format(new String[] { GITLAB });
				throw new GenericGitPushException(errMsg);
			}
		} catch (GenericGitPushException e) {
			throw e;
		}
	}

	/**
	 * Gets the gists.
	 *
	 * @return the gists
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#getGists()
	 */
	@Override
	public String[] getGists() throws Exception {
		String[] gists = null;
		String token = userAccount.getPassword();
		String userName = getUserName();
		MessageFormat formatter = new MessageFormat(GITLAB_GET_SNIIPET_API);
		String uri = formatter.format(new String[] { token, userName });
		HttpGet httpGet = new HttpGet(uri);
		try {
			GitResponse gitResponse = HTTPUtil.getHttpGetOrPostResponse(httpGet);
			if (gitResponse.getStatusCode().equals(HTTP_401))
				throw new GenericGitPushException(GENERIC_LOGIN_ERR_MSG);
			IGistResponseParser<String, String[]> gistParser = new GitLabSnippetParserImpl();
			gists = gistParser.parse(gitResponse.getResponseText());
		} catch (GenericGitPushException e) {
			throw e;
		}
		return gists;
	}

	/**
	 * Creates the gist.
	 *
	 * @param file        the file
	 * @param description the description
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ddlab.gitpusher.core.IGitHandler#createGist(java.io.File,
	 * java.lang.String)
	 */
	@Override
	public void createGist(File file, String description) throws Exception {
		String createSnippetTemplate = "https://gitlab.com/api/v4/snippets?private_token={0}&username={1}&visibility=public";
		MessageFormat formatter = new MessageFormat(GITLAB_CREATE_SNIPPET_API);
		String loginUser = getUserName();
		String uri = formatter.format(new String[] { userAccount.getPassword(), loginUser });
		HttpPost httpPost = new HttpPost(uri);
		String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

		GitLabSnippet gitSnippet = new GitLabSnippet();
		gitSnippet.setContent(content);
		gitSnippet.setDescTxt(description);
		gitSnippet.setFileName(file.getName());
		gitSnippet.setTitle(description);
		String gistJson = gitSnippet.toJson();
		StringEntity jsonBodyRequest = new StringEntity(gistJson, ContentType.APPLICATION_JSON);
		httpPost.setEntity(jsonBodyRequest);

		try {
			GitResponse gitResponse = HTTPUtil.getHttpGetOrPostResponse(httpPost);
			if (!gitResponse.getStatusCode().equals(HTTP_201))
				throw new GenericGitPushException(GIST_ERR_MSG + GITLAB);
		} catch (GenericGitPushException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * The Class GitLabSnippet.
	 */
	private static class GitLabSnippet {

		/** The title. */
		@JsonProperty("title")
		private String title;

		/** The file name. */
		@JsonProperty("file_name")
		private String fileName;

		/** The content. */
		@JsonProperty("content")
		private String content;

		/** The desc txt. */
		@JsonProperty("description")
		private String descTxt;

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

		/**
		 * Gets the file name.
		 *
		 * @return the file name
		 */
		public String getFileName() {
			return fileName;
		}

		/**
		 * Sets the file name.
		 *
		 * @param fileName the new file name
		 */
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		/**
		 * Gets the content.
		 *
		 * @return the content
		 */
		public String getContent() {
			return content;
		}

		/**
		 * Sets the content.
		 *
		 * @param content the new content
		 */
		public void setContent(String content) {
			this.content = content;
		}

		/**
		 * Gets the desc txt.
		 *
		 * @return the desc txt
		 */
		public String getDescTxt() {
			return descTxt;
		}

		/**
		 * Sets the desc txt.
		 *
		 * @param descTxt the new desc txt
		 */
		public void setDescTxt(String descTxt) {
			this.descTxt = descTxt;
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
