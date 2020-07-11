package com.ddlab.tornado;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class PluginLogger {

	public static void error(String msg) {
		IStatus st = new Status(IStatus.ERROR, Activator.PLUGIN_ID, msg);
		Activator.getDefault().getLog().log(st);
	}

	public static void debug(String msg) {
		IStatus st = new Status(IStatus.INFO, Activator.PLUGIN_ID, msg);
		Activator.getDefault().getLog().log(st);
	}

	public static void warn(String msg) {
		IStatus st = new Status(IStatus.WARNING, Activator.PLUGIN_ID, msg);
		Activator.getDefault().getLog().log(st);
	}
}