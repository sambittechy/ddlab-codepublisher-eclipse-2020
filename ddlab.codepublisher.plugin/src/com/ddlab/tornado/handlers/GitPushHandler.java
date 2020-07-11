/*
 * Copyright 2018 Tornado Project from DDLAB Inc. or its subsidiaries. All Rights Reserved.
 */
package com.ddlab.tornado.handlers;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.ddlab.tornado.dialog.GitPushDialog;

/**
 * The Class GitPushHandler.
 *
 * @author Debadatta Mishra
 */
public class GitPushHandler extends AbstractHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		Shell shell = window.getShell();
		ISelectionService service = window.getSelectionService();
		IStructuredSelection structuredSelection = (IStructuredSelection) service.getSelection();
		Object selctionElement = structuredSelection.getFirstElement();
		if (selctionElement instanceof IAdaptable) {
			IResource resource = (IResource) ((IAdaptable) selctionElement).getAdapter(IResource.class);
//      String resourcePath = resource.getLocationURI().getPath();
//      System.out.println("Resource path : " + resourcePath);
//      System.out.println("Resource name:::" + resource.getName());
//      System.out.println("resource.getLocation()===>" + resource.getLocation());
//      System.out.println("resource.getRawLocation()===>" + resource.getRawLocation());
//      System.out.println(resource.getFullPath().toFile().getAbsolutePath());
			File selectedFile = resource.getLocation().toFile();

			GitPushDialog dialog = new GitPushDialog(shell, selectedFile);
			dialog.create();

			dialog.open();
		}
		// System.out.println(structuredSelection.getFirstElement());

		// GitPushDialog dialog = new GitPushDialog(shell, File selectedFile);
		// dialog.create();
		//
		// dialog.open();
		return null;
	}

}
