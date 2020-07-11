package com.ddlab.tornado.common;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;

import com.ddlab.tornado.PluginLogger;

public class TestDialogSetting {

	public static DialogSettings loadSettings() {
		DialogSettings settings = new DialogSettings("credentials");
		String userHomeDirPath = System.getProperty("user.home");
		IPath userHomePath = new org.eclipse.core.runtime.Path(userHomeDirPath);
		IPath gitpusherPath = userHomePath.append("gitpusher");
		File gitpuhserFile = gitpusherPath.toFile();
		String filename = gitpusherPath.append("settings.txt").toOSString();
		File settingFile = new File(filename);
		if (gitpuhserFile.exists() && settingFile.exists()) {
			try {
				settings.load(filename);
			} catch (IOException ie) {
				ie.printStackTrace();
				PluginLogger.error("Unable to load the settings ..." + ie.getMessage());
				settings = null;
			}
		} else {
			settings = null;
		}
		return settings;
	}

	public static void saveAll() {
		DialogSettings credSettings = new DialogSettings("credentials");

		IDialogSettings bitSection = credSettings.addNewSection("Bitbucket");
		bitSection.put("gitSelection", 2);
		bitSection.put("username", "username value");
		bitSection.put("password", "password value");
		bitSection.put("save", "save value");

		IDialogSettings gitHubSection = credSettings.addNewSection("GitHub");
		gitHubSection.put("gitSelection", 0);
		gitHubSection.put("username", "username value");
		gitHubSection.put("password", "password value");
		gitHubSection.put("save", "save value");

		IDialogSettings gitLabSection = credSettings.addNewSection("GitLab");
		gitLabSection.put("gitSelection", 1);
		gitLabSection.put("username", "username value");
		gitLabSection.put("password", "password value");
		gitLabSection.put("save", "save value");

		save(credSettings);
	}

	private static String getSaveFileName() {
		String userHomeDirPath = System.getProperty("user.home");
		IPath userHomePath = new org.eclipse.core.runtime.Path(userHomeDirPath);
		IPath gitpusherPath = userHomePath.append("gitpusher");
		File gitpuhserFile = gitpusherPath.toFile();
		if (!gitpuhserFile.exists())
			gitpuhserFile.mkdirs();
		String fileName = gitpusherPath.append("settings.txt").toOSString();
		return fileName;
	}

	private static void save(DialogSettings credSettings) {
		String filename = getSaveFileName();
		try {
			credSettings.save(filename);
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DialogSettings settings = loadSettings();
//		settings.removeSection("Bitbucket");
//		save(settings);

		// Update Settings
		IDialogSettings gitHubSection = settings.getSection("GitHub");
		gitHubSection.put("gitSelection", 110);
		gitHubSection.put("username", "new username value");
		gitHubSection.put("password", "new password value");
		gitHubSection.put("save", "new save value");

		save(settings);

	}

}
