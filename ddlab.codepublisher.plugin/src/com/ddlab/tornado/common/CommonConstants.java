/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.tornado.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * The Class CommonConstants.
 *
 * @author Debadatta Mishra
 */
public class CommonConstants {

	/** The Constant DISPLAY. */
	public static final Display DISPLAY = Display.getDefault();

	/** The Constant PLAIN_FONT_DATA. */
	public static final FontData PLAIN_FONT_DATA = new FontData("Times New Roman", 11, SWT.NORMAL); // Courier New

	/** The Constant BOLD_FONT_DATA. */
	public static final FontData BOLD_FONT_DATA = new FontData("Times New Roman", 10, SWT.BOLD);

	/** The Constant PLAIN_TXT_FONT. */
	public static final Font PLAIN_TXT_FONT = new Font(DISPLAY, PLAIN_FONT_DATA);

	/** The Constant BOLD_FONT. */
	public static final Font BOLD_FONT = new Font(DISPLAY, BOLD_FONT_DATA);

	/** The Constant GIT_ACCOUNTS. */
	public static final String[] GIT_ACCOUNTS = new String[] { "GitHub", "GitLab", "Bitbucket" };

	/** The Constant DLG_SHELL_TXT. */
	public static final String DLG_SHELL_TXT = "Git account information";

	/** The Constant SHORT_DESC_TXT. */
	public static final String SHORT_DESC_TXT = "Short description for project";

	/** The Constant DLG_TITLE_TXT. */
	public static final String DLG_TITLE_TXT = "Git account information";

	/** The Constant SHELL_IMG_16. */
	public static final String SHELL_PUBLISH_IMG_16 = "publish-16.png";//"github16.png";
	
	public static final String SHELL_SHARE_IMG_16 = "code-16.png";//"github16.png";

	/** The Constant SHELL_IMG_64. */
//	public static final String SHELL_IMG_64 = "github64.png";
	public static final String SHELL_IMG_64 = "m1.png";

	/** The Constant ACT_TYPE_LBL_TXT. */
	public static final String ACT_TYPE_LBL_TXT = "Select Git account type";

	/** The Constant ACT_TYPE_DECORATOR_TXT. */
	public static final String ACT_TYPE_DECORATOR_TXT = "Select account type, default is GitHub";

	/** The Constant USER_NAME_TEXT. */
	public static final String USER_NAME_TEXT = "User name";

	/** The Constant USER_NAME_DECORATOR_TXT. */
	public static final String USER_NAME_DECORATOR_TXT = "User name cannot be blank or empty";

	/** The Constant PWD_LBL_TXT. */
	public static final String PWD_LBL_TXT = "Password/Token";

	/** The Constant PWD_DECORATOE_TXT. */
	public static final String PWD_DECORATOE_TXT = "Password/Token cannot be blank or empty, recommended to use token";

	/** The Constant REPO_BTN_TOOL_TIP_TXT. */
	public static final String REPO_BTN_TOOL_TIP_TXT = "Click to see the list of repositories";

	/** The Constant GIST_BTN_TOOL_TIP_TXT. */
	public static final String GIST_BTN_TOOL_TIP_TXT = "Click to see the list of code snippets";

	/** The Constant REPO_COMBO_DECORATOR_TXT. */
	public static final String REPO_COMBO_DECORATOR_TXT = "Displays the list of repositories";

	/** The Constant GIST_COMBO_DECORATOR_TXT. */
	public static final String GIST_COMBO_DECORATOR_TXT = "Displays the list of gist";

	/** The Constant REPO_BTN_TXT. */
	public static final String REPO_BTN_TXT = "Test and show my repositories";

	/** The Constant GIST_BTN_TXT. */
	public static final String GIST_BTN_TXT = "Test and show my gists";

	/** The Constant GIST_LBL_TXT. */
	public static final String GIST_LBL_TXT = "Provide brief description of the code snippet";

	/** The Constant GIST_LBL_INFO_TXT. */
	public static final String GIST_LBL_INFO_TXT = "The below description has been made mandatory to help \nthe developer/s "
			+ "know the significance of your code snippet." + "\nThis desciption will be indexed by "
			+ "all the search engines \n to find the code quickly";

	/** The Constant UNAME_NOT_EMPTY_TXT. */
	public static final String UNAME_NOT_EMPTY_TXT = "User name cannot be empty";

	/** The Constant SHORT_DESC_NOT_EMPTY_TXT. */
	public static final String SHORT_DESC_NOT_EMPTY_TXT = "Short decription cannot be empty";

	/** The Constant PWD_NOT_EMPTY_TXT. */
	public static final String PWD_NOT_EMPTY_TXT = "Password cannot be empty";

	/** The Constant GIST_NOT_EMPTY_TXT. */
	public static final String GIST_NOT_EMPTY_TXT = "Gist/Snippet description cannot be empty";

	/** The Constant READ_ME_INFO_TXT. */
	public static final String READ_ME_INFO_TXT = "Provide description which will be visible in  README.md";

	/** The Constant READ_ME_DECO_TXT. */
	public static final String READ_ME_DECO_TXT = "This description helps the developer/s to know about your project.";

	/** The Constant SHORT_DESC_ERR_TXT. */
	public static final String SHORT_DESC_ERR_TXT = "Short description of your project cannot be left blank.";

	/** The Constant ABOUT_PROJ_TXT. */
	public static final String ABOUT_PROJ_TXT = "Short description is brief details about your project.";

	/** The Constant SAVE_CREDENTIAL_TXT. */
	public static final String SAVE_CREDENTIAL_TXT = "Save credentials";

	/** The Constant GIT_SELECTION_TXT. */
	public static final String GIT_SELECTION_TXT = "gitSelection";

	/** The Constant USER_NAME. */
	public static final String USER_NAME = "username";

	/** The Constant PASSWORD. */
	public static final String PASSWORD = "password";

	/** The Constant SAVE. */
	public static final String SAVE = "save";

	/** The Constant REPO_HOST_TXT. */
	public static final String REPO_HOST_TXT = "Repository hosted successfully in ";

	/** The Constant CODE_HOST_TXT. */
	public static final String CODE_HOST_TXT = "Created successfully in ";

	/** The Constant COMPLETE_TXT. */
	public static final String COMPLETE_TXT = "Completing all operations and finalizing.";

	/** The Constant VERIFY_TXT. */
	public static final String VERIFY_REPO_TXT = "Verifying repository in  ";

	/** The Constant VERIFY_CODE_TXT. */
	public static final String VERIFY_CODE_TXT = "Verifying code in  ";

	/** The Constant PUSH_PROJ_TXT. */
	public static final String PUSH_PROJ_TXT = "Pushing project details to  ";

	/** The Constant PUSH_CODE_TXT. */
	public static final String PUSH_CODE_TXT = "Pushing code fragments to  ";

	/** The Constant CONNECT_TXT. */
	public static final String CONNECT_TXT = "Connecting to  ";

	/** The Constant README_TXT. */
	public static final String README_TXT = "Creating README.md file ...";

	/** The Constant GIT_IGNORE_TXT. */
	public static final String GIT_IGNORE_TXT = "Creating .gitignore file ...";

	/** The Constant REPO_CREATE_TXT. */
	public static final String REPO_CREATE_TXT = "Repository creation in progress for project ";

	/** The Constant CODE_CREATE_TXT. */
	public static final String CODE_CREATE_TXT = "Snippet Creation in Progress for file ";

	/** The Constant USER_HOME. */
	public static final String USER_HOME = "user.home";

	/** The Constant GITPUSHER. */
	public static final String GITPUSHER = "codepublisher";

	/** The Constant SETTING_FILE_NAME. */
	public static final String SETTING_FILE_NAME = "settings.txt";

	/** The Constant CREDENTIALS. */
	public static final String CREDENTIALS = "credentials";

	/** The Constant LATEST. */
	public static final String LATEST = "latest";

	/** The Constant LAST_SAVED. */
	public static final String LAST_SAVED = "lastSaved";

	/** The Constant SUCCESS. */
	public static final String SUCCESS = "Success";

	/** The Constant ERROR. */
	public static final String ERROR = "Error";

	/** The Constant README_FILE_NAME. */
	public static final String README_FILE_NAME = "README.md";

	/** The Constant UPDATED_LATER. */
	public static final String UPDATED_LATER = "To be updated later";

	/** The Constant GITIGNORE. */
	public static final String GITIGNORE = ".gitignore";

	/** The Constant NO_SNIPPET_TXT. */
	public static final String NO_SNIPPET_TXT = "There are no Snippet/s available, create one.";

	/** The Constant PRJ_CREATE_TXT. */
	public static final String PRJ_CREATE_TXT = "Creating a project in {0} for {1}";

	/** The Constant SNIPPET_CREATE_TXT. */
	public static final String SNIPPET_CREATE_TXT = "Creating a project in {0} for {1}";

	/** The Constant BITBUCKET. */
	public static final String BITBUCKET = "bitbucket";

	/** The Constant NOT_200_TXT. */
	public static final String NOT_200_TXT = "Characters more than 200 are not allowed.";

	/** The Constant NO_SNIPPET. */
	public static final String NO_SNIPPET = "There are no Snippet/s available, create one.";
	
	public static final String GEN_ERR_MSG = "Unexpected exception while making connection, please try again, "
			+ "\nin case of any issue, report to deba.java@gmail.com";

}
