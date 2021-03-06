/*
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.pwsafe.lib.datastore.PwsEntryBean;
import org.pwsafe.passwordsafeswt.PasswordSafeJFace;
import org.pwsafe.passwordsafeswt.preference.JpwPreferenceConstants;

/**
 * Copyies the username to the clipboard.
 * 
 * @author Glen Smith
 */
public class CopyUsernameAction extends Action {

	public CopyUsernameAction() {
		super(Messages.getString("CopyUsernameAction.Label")); //$NON-NLS-1$
		setAccelerator(SWT.MOD1 | 'U');
		setImageDescriptor(ImageDescriptor.createFromURL(this.getClass().getClassLoader()
				.getResource("org/pwsafe/passwordsafeswt/images/tool_newbar_user.gif"))); //$NON-NLS-1$
		setToolTipText(Messages.getString("CopyUsernameAction.Tooltip")); //$NON-NLS-1$

	}

	/**
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		PasswordSafeJFace app = PasswordSafeJFace.getApp();

		PwsEntryBean selected = app.getSelectedRecord();
		if (selected == null)
			return;

		// TODO: only fetch a filled entry if username is not part of sparse
		// fields (-> never).
		PwsEntryBean theEntry;
		if (selected.getUsername() != null && selected.getUsername().length() > 0) {
			theEntry = selected;
		} else {// retrieve filled Entry for sparse
			theEntry = app.getPwsDataStore().getEntry(selected.getStoreIndex());
		}

		Clipboard cb = new Clipboard(app.getShell().getDisplay());

		app.copyToClipboard(cb, theEntry.getUsername());

		final IPreferenceStore thePrefs = JFacePreferences.getPreferenceStore();
		final boolean recordAccessTime = thePrefs
				.getBoolean(JpwPreferenceConstants.RECORD_LAST_ACCESS_TIME);
		if (recordAccessTime) { // this could/should be sent to a background
								// thread
			app.updateAccessTime(theEntry);
		}

		cb.dispose();
	}
}