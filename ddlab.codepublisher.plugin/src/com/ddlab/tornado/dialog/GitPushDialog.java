/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.tornado.dialog;

import static com.ddlab.tornado.common.CommonConstants.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.ddlab.gitpusher.core.GitType;
import com.ddlab.gitpusher.core.IGitPusher;
import com.ddlab.gitpusher.core.UserAccount;
import com.ddlab.gitpusher.exception.GenericGitPushException;
import com.ddlab.tornado.Activator;
import com.ddlab.tornado.common.CommonUtil;
import com.ddlab.tornado.common.CryptoUtil;
import com.ddlab.tornado.common.ImageUtil;
import com.ddlab.tornado.threads.ReposLoaderThread;

/**
 * The Class GitPushDialog.
 *
 * @author Debadatta Mishra
 */
public class GitPushDialog extends TitleAreaDialog {

	/** The git act combo. */
	private Combo gitActCombo = null;

	/** The user name text. */
	private Text userNameText = null;

	/** The password text. */
	private Text passwordText = null;

	/** The show repo btn. */
	private Button showRepoBtn = null;

	/** The my repo combo. */
	private Combo repoCombo = null;

	/** The read me txt. */
	private Text readMeTxt = null;

	/** Short description text. */
	private Text shortDescTxt = null;

	/** The selected file. */
	private File selectedFile;

	/** The save credential btn. */
	private Button saveCredentialBtn;

	/**
	 * Instantiates a new git push dialog.
	 *
	 * @param parentShell  the parent shell
	 * @param selectedFile the selected file
	 */
	public GitPushDialog(Shell parentShell, File selectedFile) {
		super(parentShell);
		this.selectedFile = selectedFile;
	}

	/**
	 * Creates the.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#create()
	 */
	@Override
	public void create() {
		super.create();
		getShell().setText(DLG_SHELL_TXT);
		getShell().setImage(ImageUtil.getImage(SHELL_PUBLISH_IMG_16));
		setTitle(DLG_TITLE_TXT);
		setTitleImage(ImageUtil.getImage(SHELL_IMG_64));
	}

	/**
	 * Checks if is help available.
	 *
	 * @return true, if is help available
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.TrayDialog#isHelpAvailable()
	 */
	@Override
	public boolean isHelpAvailable() {
		return false;
	}

	/**
	 * Creates the dialog area.
	 *
	 * @param parent the parent
	 * @return the control
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.
	 * widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogComposite = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(dialogComposite, SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 417;
		container.setLayoutData(gridData);
		GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);

		createGitAccountCombo(container);
		createUserName(container);
		createPassword(container);
		createShowRepo(container);
		createForshortDescription(container);
		createForReadMe(container);
		populateSavedData();
		addGitComboListener();
		addTab2TextListener(shortDescTxt);
		addTab2TextListener(readMeTxt);

		return dialogComposite;
	}

	/**
	 * Adds the git combo listener.
	 */
	private void addGitComboListener() {
		gitActCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = gitActCombo.getSelectionIndex();
				String selectedGitType = GIT_ACCOUNTS[index];
				populateData(selectedGitType);
			}
		});
	}

	/**
	 * Creates the git account combo.
	 *
	 * @param container the container
	 */
	private void createGitAccountCombo(Composite container) {
		Label gitActLabel = new Label(container, SWT.NONE);
		gitActLabel.setText(ACT_TYPE_LBL_TXT);
		gitActLabel.setFont(BOLD_FONT);
		gitActLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		gitActCombo = new Combo(container, SWT.READ_ONLY);
		gitActCombo.setItems(GIT_ACCOUNTS);
		gitActCombo.select(0);
		gitActCombo.setFont(PLAIN_TXT_FONT);
		CommonUtil.setLayoutData(gitActCombo);
		CommonUtil.setProposalDecorator(gitActCombo, ACT_TYPE_DECORATOR_TXT);
		new Label(container, SWT.NONE); // Blank Label for adjustment Label for adjustment
	}

	/**
	 * Creates the user name.
	 *
	 * @param container the container
	 */
	private void createUserName(Composite container) {
		Label userNameLabel = new Label(container, SWT.NONE);
		userNameLabel.setText(USER_NAME_TEXT);
		userNameLabel.setFont(BOLD_FONT);
		userNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		CommonUtil.setRequiredDecorator(userNameLabel, USER_NAME_DECORATOR_TXT);

		userNameText = new Text(container, SWT.BORDER);
		userNameText.setFont(PLAIN_TXT_FONT);
		userNameText.setFocus();
		addUserNameTextListener();
		CommonUtil.setLayoutData(userNameText);
		new Label(container, SWT.NONE); // Blank Label for adjustment
	}

	/**
	 * Creates the password.
	 *
	 * @param container the container
	 */
	private void createPassword(Composite container) {
		Label passwordLabel = new Label(container, SWT.NONE);
		passwordLabel.setText(PWD_LBL_TXT);
		passwordLabel.setFont(BOLD_FONT);
		GridData pwdData = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		passwordLabel.setLayoutData(pwdData);
		CommonUtil.setRequiredDecorator(passwordLabel, PWD_DECORATOE_TXT);

		passwordText = new Text(container, SWT.PASSWORD | SWT.BORDER);
		addPwdTextListener();
		CommonUtil.setLayoutData(passwordText);

		saveCredentialBtn = new Button(container, SWT.CHECK);
		saveCredentialBtn.setText(SAVE_CREDENTIAL_TXT);
		saveCredentialBtn.setSelection(true);
	}

	/**
	 * Creates the show repo.
	 *
	 * @param container the container
	 */
	private void createShowRepo(Composite container) {
		showRepoBtn = new Button(container, SWT.PUSH);
		showRepoBtn.setText(REPO_BTN_TXT);
		showRepoBtn.setFont(BOLD_FONT);
		showRepoBtn.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
		showRepoBtn.setToolTipText(REPO_BTN_TOOL_TIP_TXT);

		repoCombo = new Combo(container, SWT.READ_ONLY);
		repoCombo.setFont(PLAIN_TXT_FONT);
		repoCombo.setToolTipText(REPO_COMBO_DECORATOR_TXT);
		CommonUtil.setLayoutData(repoCombo);
		addRepoBtnListener();
		new Label(container, SWT.NONE); // Blank Label for adjustment
	}

	/**
	 * Creates for short description.
	 *
	 * @param container the container
	 */
	private void createForshortDescription(Composite container) {
		Label shortDescLbl = new Label(container, SWT.NONE);
		shortDescLbl.setText(SHORT_DESC_TXT);
		shortDescLbl.setFont(BOLD_FONT);
		GridData shortDescData = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);

		CommonUtil.setRequiredDecorator(shortDescLbl, SHORT_DESC_ERR_TXT);
		CommonUtil.setRightSideControlDecorator(shortDescLbl, ABOUT_PROJ_TXT);
		shortDescLbl.setLayoutData(shortDescData);
		new Label(container, SWT.NONE); // Blank Label for adjustment Label for adjustment
		new Label(container, SWT.NONE); // Blank Label for adjustment Label for adjustment

		shortDescTxt = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		shortDescTxt.setFont(PLAIN_TXT_FONT);
		shortDescTxt.setTextLimit(255);

		GridData gData = new GridData();
		gData.heightHint = 60;
		gData.horizontalAlignment = SWT.FILL; // GridData.FILL;
		gData.horizontalSpan = 3;
		gData.verticalSpan = 5;
		shortDescTxt.setLayoutData(gData);
		addShortDescTextListener();
	}

	/**
	 * Creates the for read me.
	 *
	 * @param container the container
	 */
	private void createForReadMe(Composite container) {
		Label readMeLbl = new Label(container, SWT.NONE);
		readMeLbl.setText(READ_ME_INFO_TXT);
		readMeLbl.setFont(BOLD_FONT);
		GridData userNamegData = new GridData();
		userNamegData.horizontalSpan = 2;
		readMeLbl.setLayoutData(userNamegData);
		new Label(container, SWT.NONE); // Blank Label for adjustment Label for adjustment
		readMeTxt = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		readMeTxt.setFont(PLAIN_TXT_FONT);

		GridData gData = new GridData();
		gData.heightHint = 80;
		gData.horizontalAlignment = SWT.FILL; // GridData.FILL;
		gData.horizontalSpan = 3;
		gData.verticalSpan = 5;
		readMeTxt.setLayoutData(gData);
		CommonUtil.setRightSideControlDecorator(readMeLbl, READ_ME_DECO_TXT);
	}

	/**
	 * Adds the repo btn listener.
	 */
	private void addRepoBtnListener() {
		showRepoBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateRepoCombo();
			}
		});
	}

	/**
	 * Adds the user name text listener.
	 */
	private void addUserNameTextListener() {
		userNameText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				setMessage("");
			}
		});

	}

	/**
	 * Adds the pwd text listener.
	 */
	private void addPwdTextListener() {
		passwordText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				setMessage("");
			}
		});
	}

	/**
	 * Adds the pwd text listener.
	 */
	private void addShortDescTextListener() {
		shortDescTxt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				setMessage("");
			}
		});
	}

	/**
	 * Populate repo combo.
	 */
	private void populateRepoCombo() {
		// get the list of repositories;
		if (!isAccountValid())
			return;
		repoCombo.removeAll();
		String selectedGitType = GIT_ACCOUNTS[gitActCombo.getSelectionIndex()];
		UserAccount userAccount = new UserAccount(userNameText.getText(), passwordText.getText());
		IGitPusher gitPusher = GitType.fromString(selectedGitType).getGitPusher(userAccount);
		List<String> reposList = new ArrayList<>();

		IRunnableWithProgress op = new ReposLoaderThread(gitPusher, reposList);
		try {
			new ProgressMonitorDialog(new Shell()).run(true, true, op);
			if (reposList.size() != 0) {
				setMessage("");
				String[] repos = reposList.toArray(new String[0]);
				repoCombo.setItems(repos);
				repoCombo.select(0);
			} else
				setMessage(NO_SNIPPET_TXT, IMessageProvider.INFORMATION);

		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
			Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e);
			ErrorDialog.openError(new Shell(), ERROR, e.getMessage(), status);
		}

	}

	/**
	 * Ok pressed.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		if (isAccountValid() && isShortDescAvailable()) {
			String selectedGitType = GIT_ACCOUNTS[gitActCombo.getSelectionIndex()];
			UserAccount userAccount = new UserAccount(userNameText.getText(), passwordText.getText());
			IGitPusher gitPusher = GitType.fromString(selectedGitType).getGitPusher(userAccount);
			String readMeText = (readMeTxt.getText()) == null ? "" : readMeTxt.getText();
			String shotDescText = (shortDescTxt.getText()) == null ? "" : shortDescTxt.getText();
			saveSettings();
			executeInBackground(gitPusher, shotDescText, readMeText);
			close();
		}
	}

	/**
	 * Execute in background.
	 *
	 * @param gitPusher    the git pusher
	 * @param shotDescText the shot desc text
	 * @param readMetxt    the read metxt
	 */
	private void executeInBackground(IGitPusher gitPusher, String shotDescText, String readMetxt) {
		String gitWebType = GIT_ACCOUNTS[gitActCombo.getSelectionIndex()];
		String message = new MessageFormat(PRJ_CREATE_TXT).format(new String[] { gitWebType, selectedFile.getName() });
		Job job = new Job(message) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(REPO_CREATE_TXT + selectedFile.getName(), 7);
				monitor.worked(1);
				try {
					TimeUnit.SECONDS.sleep(2);
					monitor.subTask(GIT_IGNORE_TXT);
					CommonUtil.generateGitIgnoreFile(selectedFile);
					monitor.worked(1);
					TimeUnit.SECONDS.sleep(1);
					if (monitor.isCanceled())
						return Status.CANCEL_STATUS;
					monitor.subTask(README_TXT);
					CommonUtil.generateReadMeFile(selectedFile, readMetxt);
					monitor.worked(1);
					TimeUnit.SECONDS.sleep(1);
					if (monitor.isCanceled())
						return Status.CANCEL_STATUS;
					monitor.subTask(CONNECT_TXT + gitWebType);
					gitPusher.pushCodeDirectly(selectedFile, shotDescText);
					monitor.subTask(PUSH_PROJ_TXT + gitWebType);
					monitor.worked(1);
					TimeUnit.SECONDS.sleep(1);
					monitor.subTask(VERIFY_REPO_TXT + gitWebType);
					monitor.worked(1);
					TimeUnit.SECONDS.sleep(1);
					monitor.subTask(COMPLETE_TXT);
					TimeUnit.SECONDS.sleep(1);
					monitor.worked(1);
					monitor.worked(1);
					CommonUtil.showSuccess(REPO_HOST_TXT + gitWebType);
				} catch (GenericGitPushException e1) {
					CommonUtil.showFailure(e1.getMessage());
				} catch (InterruptedException ie) {
					CommonUtil.showFailure(ie.getMessage());
				}
				if (monitor.isCanceled())
					return Status.CANCEL_STATUS;

				monitor.done();
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
		PlatformUI.getWorkbench().getProgressService().showInDialog(getShell(), job);
	}

	/**
	 * Cancel pressed.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
	 */
	@Override
	protected void cancelPressed() {
		super.cancelPressed();
	}

	/**
	 * Checks if is resizable.
	 *
	 * @return true, if is resizable
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	@Override
	protected boolean isResizable() {
		return true;
	}

	/**
	 * Checks if is account valid.
	 *
	 * @return true, if is account valid
	 */
	private boolean isAccountValid() {
		boolean isValidFlag = false;
		if (userNameText.getText().isEmpty())
			setMessage(UNAME_NOT_EMPTY_TXT, IMessageProvider.ERROR);
		else if (passwordText.getText().isEmpty())
			setMessage(PWD_NOT_EMPTY_TXT, IMessageProvider.ERROR);
		else {
			isValidFlag = true;
			setMessage("");
		}
		return isValidFlag;
	}

	/**
	 * Checks if is short desc available.
	 *
	 * @return true, if is short desc available
	 */
	private boolean isShortDescAvailable() {
		boolean isValidFlag = false;
		if (shortDescTxt.getText().trim().isEmpty())
			setMessage(SHORT_DESC_NOT_EMPTY_TXT, IMessageProvider.ERROR);
		else {
			isValidFlag = true;
			setMessage("");
		}
		return isValidFlag;
	}

	/**
	 * Save settings.
	 */
	private void saveSettings() {
		int selectedGitIndex = gitActCombo.getSelectionIndex();
		String selectedGitType = GIT_ACCOUNTS[selectedGitIndex];
		if (saveCredentialBtn.getSelection()) {
			String userName = userNameText.getText();
			String password = passwordText.getText();
			CommonUtil.saveLatest(selectedGitType);
			CommonUtil.saveOrUpdate(selectedGitType, userName, password, true);
		} else {
			// if file exists, delete the settings
			CommonUtil.removeSaveSettings(selectedGitType);
		}
	}

	/**
	 * Populate saved data.
	 */
	private void populateSavedData() {
		DialogSettings settings = CommonUtil.loadSaveSettings();
		if (settings != null) {
			// Get the latest lastSaved
			IDialogSettings latestSetting = settings.getSection(LATEST);
			String gitType = latestSetting.get(LAST_SAVED);
			IDialogSettings gitSetting = settings.getSection(gitType);

			if (gitSetting != null) {
				String userName = gitSetting.get(USER_NAME) == null ? "" : gitSetting.get(USER_NAME);
				String password = gitSetting.get(PASSWORD) == null ? "" : gitSetting.get(PASSWORD);
				boolean isCheckSelected = gitSetting.get(SAVE) == null ? false
						: Boolean.parseBoolean(gitSetting.get(SAVE));

				String decryptedUserName = CryptoUtil.getDecryptedValue(userName);
				String decryptedPassword = CryptoUtil.getDecryptedValue(password);
				
				userNameText.setText(decryptedUserName);
				passwordText.setText(decryptedPassword);
				
//				userNameText.setText(userName);
//				passwordText.setText(password);
				saveCredentialBtn.setSelection(isCheckSelected);
				int gitSelectIndex = Arrays.asList(GIT_ACCOUNTS).indexOf(gitType);
				gitActCombo.select(gitSelectIndex);
			}

		}
	}

	/**
	 * Populate data.
	 *
	 * @param gitType the git type
	 */
	private void populateData(String gitType) {
		DialogSettings settings = CommonUtil.loadSaveSettings();
		if (settings != null) {
			IDialogSettings gitSetting = settings.getSection(gitType);
			if (gitSetting != null) {
				String userName = gitSetting.get(USER_NAME) == null ? "" : gitSetting.get(USER_NAME);
				String password = gitSetting.get(PASSWORD) == null ? "" : gitSetting.get(PASSWORD);
				boolean isCheckSelected = gitSetting.get(SAVE) == null ? false
						: Boolean.parseBoolean(gitSetting.get(SAVE));
				
				String decryptedUserName = CryptoUtil.getDecryptedValue(userName);
				String decryptedPassword = CryptoUtil.getDecryptedValue(password);
				
				userNameText.setText(decryptedUserName);
				passwordText.setText(decryptedPassword);
				
//				userNameText.setText(userName);
//				passwordText.setText(password);
				saveCredentialBtn.setSelection(isCheckSelected);
			} else {
				userNameText.setText("");
				passwordText.setText("");
			}

		}
	}
	
	public void addTab2TextListener(Text text) {
		text.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent event) {
				switch (event.detail) {
				case SWT.TRAVERSE_TAB_NEXT:
				case SWT.TRAVERSE_TAB_PREVIOUS:
					event.doit = true;
					break;
				}

			}
		});
	}

}
