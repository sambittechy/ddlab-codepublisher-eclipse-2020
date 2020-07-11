/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.tornado.common;

import static com.ddlab.tornado.common.CommonConstants.CREDENTIALS;
import static com.ddlab.tornado.common.CommonConstants.ERROR;
import static com.ddlab.tornado.common.CommonConstants.GEN_ERR_MSG;
import static com.ddlab.tornado.common.CommonConstants.GITIGNORE;
import static com.ddlab.tornado.common.CommonConstants.GITPUSHER;
import static com.ddlab.tornado.common.CommonConstants.LAST_SAVED;
import static com.ddlab.tornado.common.CommonConstants.LATEST;
import static com.ddlab.tornado.common.CommonConstants.PASSWORD;
import static com.ddlab.tornado.common.CommonConstants.README_FILE_NAME;
import static com.ddlab.tornado.common.CommonConstants.SAVE;
import static com.ddlab.tornado.common.CommonConstants.SETTING_FILE_NAME;
import static com.ddlab.tornado.common.CommonConstants.SUCCESS;
import static com.ddlab.tornado.common.CommonConstants.UPDATED_LATER;
import static com.ddlab.tornado.common.CommonConstants.USER_HOME;
import static com.ddlab.tornado.common.CommonConstants.USER_NAME;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ddlab.generator.IGitIgnoreGen;
import com.ddlab.generator.IReadMeGen;
import com.ddlab.generator.gitignore.GitIgnoreGenerator;
import com.ddlab.generator.readme.ReadMeGenerator;

/**
 * The Class CommonUtil.
 *
 * @author Debadatta Mishra
 */
public class CommonUtil {

	// public static final Font BOLD_FONT =
	// JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);

	/**
	 * Sets the proposal decorator.
	 *
	 * @param control the control
	 * @param message the message
	 */
	public static void setProposalDecorator(Control control, String message) {
		ControlDecoration decorator = getDecorator(control, message);
		decorator.setImage(ImageUtil.PROPOSAL_IMAGE);
		decorator.show();
	}

	/**
	 * Sets the info decorator.
	 *
	 * @param control the control
	 * @param message the message
	 */
	public static void setInfoDecorator(Control control, String message) {
		ControlDecoration decorator = getDecorator(control, message);
		decorator.setImage(ImageUtil.INFO_IMAGE);
		decorator.show();
	}

	/**
	 * Sets the required decorator.
	 *
	 * @param control the control
	 * @param message the message
	 */
	public static void setRequiredDecorator(Control control, String message) {
		ControlDecoration decorator = getDecorator(control, message);
		// decorator.setImage(ImageUtil.ERROR_IMAGE);
		decorator.setImage(ImageUtil.PROPOSAL_IMAGE);
		decorator.show();
	}

	/**
	 * Gets the decorator.
	 *
	 * @param control the control
	 * @param message the message
	 * @return the decorator
	 */
	public static ControlDecoration getDecorator(Control control, String message) {
		ControlDecoration decorator = new ControlDecoration(control, SWT.TOP);
		decorator.setDescriptionText(message);
		return decorator;
	}

	/**
	 * Sets the layout data.
	 *
	 * @param control the new layout data
	 */
	public static void setLayoutData(Control control) {
		GridData gData = new GridData();
		gData.heightHint = 29;
		gData.grabExcessHorizontalSpace = true;
		gData.horizontalAlignment = GridData.FILL;
		control.setLayoutData(gData);
	}

	/**
	 * Sets the right side control decorator.
	 *
	 * @param control the control
	 * @param message the message
	 */
	public static void setRightSideControlDecorator(Control control, String message) {
		ControlDecoration decorator = new ControlDecoration(control, SWT.CENTER | SWT.RIGHT);
		decorator.setDescriptionText(message);
		decorator.setImage(ImageUtil.INFO_IMAGE);
		decorator.setMarginWidth(2);
		decorator.show();
	}

	/**
	 * Show success.
	 *
	 * @param message the message
	 */
	public static void showSuccess(String message) {
		Display.getDefault().syncExec(() -> {
			MessageDialog.openInformation(new Shell(), SUCCESS, message);
		});
	}

	/**
	 * Show failure.
	 *
	 * @param errMsg the err msg
	 */
	public static void showFailure(String errMsg) {
		errMsg = errMsg == null || errMsg.isEmpty() ? GEN_ERR_MSG: errMsg;
		final String errMsgTxt = errMsg; 
		Display.getDefault().syncExec(() -> {
			MessageDialog.openError(new Shell(), ERROR, errMsgTxt);
		});
	}

	/**
	 * Generate read me file.
	 *
	 * @param selectedFile the selected file
	 * @param description  the description
	 */
	public static void generateReadMeFile(File selectedFile, String description) {
		IReadMeGen readMeGen = new ReadMeGenerator();
		String projectName = selectedFile.getName();
		description = (description == null || description.trim().isEmpty()) ? UPDATED_LATER : description;
		String readMeContents = readMeGen.generateReadMeMdContents(projectName, description, null);
		Path readMePath = Paths.get(selectedFile.getAbsolutePath() + File.separator + README_FILE_NAME);
		try {
			if (Files.exists(readMePath))
				return;
			Files.write(readMePath, readMeContents.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generate git ignore file.
	 *
	 * @param selectedFile the selected file
	 */
	public static void generateGitIgnoreFile(File selectedFile) {
		IGitIgnoreGen gitIgnoreGenerator = new GitIgnoreGenerator();
		String gitIgnoreContents = gitIgnoreGenerator.generateGitIgnoreContents();
		Path gitIgnorePath = Paths.get(selectedFile.getAbsolutePath() + File.separator + GITIGNORE);
		try {
			if (Files.exists(gitIgnorePath))
				return;
			Files.write(gitIgnorePath, gitIgnoreContents.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the save file name.
	 *
	 * @return the save file name
	 */
	private static String getSaveFileName() {
		String userHomeDirPath = System.getProperty(USER_HOME);
		IPath userHomePath = new org.eclipse.core.runtime.Path(userHomeDirPath);
		IPath gitpusherPath = userHomePath.append(GITPUSHER);
		File gitpuhserFile = gitpusherPath.toFile();
		if (!gitpuhserFile.exists())
			gitpuhserFile.mkdirs();
		String fileName = gitpusherPath.append(SETTING_FILE_NAME).toOSString();
		return fileName;
	}

	/**
	 * Save or update.
	 *
	 * @param selectedGitType the selected git type
	 * @param userName        the user name
	 * @param password        the password
	 * @param isChecked       the is checked
	 */
	public static void saveOrUpdate(String selectedGitType, String userName, String password, boolean isChecked) {
		DialogSettings settings = null;
		if (settingsExist()) {
			// It means setting file exists, load the setting and update
			settings = loadSaveSettings();
		} else {
			// It means setting file does not exist, create a new one.
			settings = new DialogSettings(CREDENTIALS);
		}
		IDialogSettings gitSection = settings.addNewSection(selectedGitType);
		String encUserName = CryptoUtil.getEncryptedValue(userName);
		String encPwd = CryptoUtil.getEncryptedValue(password);
		gitSection.put(USER_NAME, encUserName);
		gitSection.put(PASSWORD, encPwd);
//		gitSection.put(USER_NAME, userName);
//		gitSection.put(PASSWORD, password);
		gitSection.put(SAVE, isChecked);
		save(settings);
	}

	/**
	 * Save latest.
	 *
	 * @param selectedGitType the selected git type
	 */
	public static void saveLatest(String selectedGitType) {
		DialogSettings settings = null;
		if (settingsExist()) {
			// It means setting file exists, load the setting and update
			settings = loadSaveSettings();
		} else {
			// It means setting file does not exist, create a new one.
			settings = new DialogSettings(CREDENTIALS);
		}
		IDialogSettings latestSection = settings.addNewSection(LATEST);
		latestSection.put(LAST_SAVED, selectedGitType);
		save(settings);
	}

	/**
	 * Removes the save settings.
	 *
	 * @param selectedGitType the selected git type
	 */
	public static void removeSaveSettings(String selectedGitType) {
		DialogSettings settings = null;
		if (settingsExist()) {
			settings = loadSaveSettings();
			settings.removeSection(selectedGitType);
		}
		save(settings);
	}

	/**
	 * Load save settings.
	 *
	 * @return the dialog settings
	 */
	public static DialogSettings loadSaveSettings() {
		DialogSettings settings = new DialogSettings(CREDENTIALS);
		String settingsFileName = getSaveFileName();
		try {
			if (settingsExist())
				settings.load(settingsFileName);
			else
				settings = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return settings;
	}

	/**
	 * Save.
	 *
	 * @param credSettings the cred settings
	 */
	private static void save(DialogSettings credSettings) {
		String filename = getSaveFileName();
		try {
			credSettings.save(filename);
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	/**
	 * Settings exist.
	 *
	 * @return true, if successful
	 */
	public static boolean settingsExist() {
		File saveFile = new File(getSaveFileName());
		return saveFile.exists();
	}
}
