/*
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.passwordsafeswt.dialog;

import java.util.List;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pwsafe.passwordsafeswt.util.IOUtils;
import org.pwsafe.passwordsafeswt.util.ShellHelpers;
import org.pwsafe.passwordsafeswt.util.VersionInfo;

/**
 * StartupDialog is shown when the app starts up and is modal in front on the
 * main window.
 * 
 * @author Glen Smith
 */
public class StartupDialog extends Dialog {

	private Combo cboFilename;
	private Text txtPassword;
	protected String result;
	protected Shell shell;
	private List<String> mruList;

	private boolean readOnly;
	private String selectedFile;
	private final StringBuilder selectedPassword = new StringBuilder();

	public static final String OPEN_FILE = "open-selected"; // open the selected file //$NON-NLS-1$
	public static final String OPEN_OTHER = "open-other"; // open file dialog for other file //$NON-NLS-1$
	public static final String NEW_FILE = "new"; // create a new safe //$NON-NLS-1$
	public static final String CANCEL = "cancel"; // exit the app //$NON-NLS-1$

	public StartupDialog(Shell parent, int style) {
		super(parent, style);
	}

	public StartupDialog(final Shell parent, final List<String> mru, final boolean forReadOnly) {
		this(parent, SWT.NONE);
		this.mruList = mru;
		this.readOnly = forReadOnly;
	}

	public String open() {
		result = StartupDialog.CANCEL;
		createContents();
		ShellHelpers.centreShell(getParent(), shell);
		shell.open();
		shell.layout();
		if (mruList != null && mruList.size() > 0) {
			for (String filename : mruList) {
				cboFilename.add(filename);
			}
			cboFilename.setText(mruList.get(0));
			txtPassword.setFocus();
		}

		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}

	/**
	 * Create dialog elements.
	 */
	protected void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setLayout(new FormLayout());
		shell.setImage(IOUtils.getImage(StartupDialog.class,
				"/org/pwsafe/passwordsafeswt/images/clogo.gif")); //$NON-NLS-1$
		shell.setSize(550, 348);
		shell.setText(Messages.getString("StartupDialog.Title")); //$NON-NLS-1$

		final Label lblTextLogo = new Label(shell, SWT.NONE);
		lblTextLogo.setAlignment(SWT.CENTER);
		lblTextLogo.setImage(IOUtils.getImage(StartupDialog.class,
				"/org/pwsafe/passwordsafeswt/images/psafetxtNew.gif")); //$NON-NLS-1$
		final FormData formData_10 = new FormData();
		formData_10.left = new FormAttachment(24, 0);
		formData_10.top = new FormAttachment(0, 15);
		lblTextLogo.setLayoutData(formData_10);

		final Label lblPleaseEnter = new Label(shell, SWT.NONE);
//		lblPleaseEnter.getFont().
		final FormData formData = new FormData();
		// formData.top = new FormAttachment(0, 55);
		formData.top = new FormAttachment(lblTextLogo, 15);
		formData.left = new FormAttachment(0, 15);
		lblPleaseEnter.setLayoutData(formData);
		lblPleaseEnter.setText(Messages.getString("StartupDialog.Info")); //$NON-NLS-1$

		final Label lblFilename = new Label(shell, SWT.NONE);
		lblFilename.setText(Messages.getString("StartupDialog.Filename")); //$NON-NLS-1$
		final FormData formData_1 = new FormData();
		formData_1.top = new FormAttachment(lblPleaseEnter, 15, SWT.BOTTOM);
		formData_1.left = new FormAttachment(lblPleaseEnter, 0, SWT.LEFT);
		lblFilename.setLayoutData(formData_1);

		final Label lblSafeCombination = new Label(shell, SWT.NONE);
		final FormData formData_2 = new FormData();
		formData_2.top = new FormAttachment(lblFilename, 20);
		formData_2.left = new FormAttachment(lblFilename, 0, SWT.LEFT);
		lblSafeCombination.setLayoutData(formData_2);
		lblSafeCombination.setText(Messages.getString("StartupDialog.SafeCombination")); //$NON-NLS-1$

		cboFilename = new Combo(shell, SWT.READ_ONLY);
		final FormData formData_1b = new FormData();
		formData_1b.top = new FormAttachment(lblFilename, 0, SWT.TOP);
		formData_1b.left = new FormAttachment(lblSafeCombination, 15, SWT.RIGHT);
		formData_1b.right = new FormAttachment(100, -160);
		cboFilename.setLayoutData(formData_1b);

		txtPassword = new Text(shell, SWT.BORDER);
		txtPassword.setEchoChar('*');
		final FormData formData_3 = new FormData();
		formData_3.top = new FormAttachment(lblSafeCombination, 0, SWT.TOP);
		formData_3.left = new FormAttachment(lblSafeCombination, 15, SWT.RIGHT);
		formData_3.right = new FormAttachment(100, -160);
		
		txtPassword.setLayoutData(formData_3);

		final Button btnReadOnly = new Button(shell, SWT.CHECK);

		btnReadOnly.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				readOnly = btnReadOnly.getSelection();
			}
		});
		final FormData formData_4 = new FormData();
		formData_4.top = new FormAttachment(txtPassword, 15);
		formData_4.left = new FormAttachment(txtPassword, 0, SWT.LEFT);
		btnReadOnly.setLayoutData(formData_4);
		btnReadOnly.setSelection(readOnly);
		btnReadOnly.setText(Messages.getString("StartupDialog.ReadOnlyButton")); //$NON-NLS-1$

		final Button btnCreate = new Button(shell, SWT.NONE);
		btnCreate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = StartupDialog.NEW_FILE;
				readOnly = false;
				shell.dispose();
			}
		});
		final FormData formData_5 = new FormData();
		formData_5.top = new FormAttachment(cboFilename, 0, SWT.TOP);
		formData_5.right = new FormAttachment(100, -20);
		btnCreate.setLayoutData(formData_5);
		btnCreate.setText(Messages.getString("StartupDialog.CreateNewButton")); //$NON-NLS-1$

		final Button btnOpen = new Button(shell, SWT.NONE);
		btnOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = StartupDialog.OPEN_OTHER;
				selectedFile = cboFilename.getText();
				selectedPassword.setLength(0);
				selectedPassword.append(txtPassword.getTextChars());
				shell.dispose();
			}
		});
		final FormData formData_6 = new FormData();
		formData_6.top = new FormAttachment(txtPassword, 0, SWT.TOP);
		formData_6.left = new FormAttachment(btnCreate, 0, SWT.LEFT);
		formData_6.right = new FormAttachment(btnCreate, 0, SWT.RIGHT);
		btnOpen.setLayoutData(formData_6);
		btnOpen.setText(Messages.getString("StartupDialog.OpenOtherButton")); //$NON-NLS-1$

		final Button btnOk = new Button(shell, SWT.NONE);
		shell.setDefaultButton(btnOk);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedFile = cboFilename.getText();
				// set selected as first
				if (selectedFile != null) {
					mruList.remove(selectedFile);
					mruList.add(0, selectedFile);
				}

				selectedPassword.setLength(0);
				selectedPassword.append(txtPassword.getTextChars());
				result = StartupDialog.OPEN_FILE;
				shell.dispose();
			}
		});
		final FormData formData_7 = new FormData();
		formData_7.width = 80;
		formData_7.left = new FormAttachment(lblSafeCombination, 15, SWT.RIGHT);
		formData_7.bottom = new FormAttachment(100, -10);
		btnOk.setLayoutData(formData_7);
		btnOk.setText(Messages.getString("StartupDialog.OkButton")); //$NON-NLS-1$

		final Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = StartupDialog.CANCEL;
				shell.dispose();
			}
		});
		final FormData formData_8 = new FormData();
		formData_8.width = 80;
		formData_8.left = new FormAttachment(btnOk, 10);
		formData_8.top = new FormAttachment(btnOk, 0, SWT.TOP);
		btnCancel.setLayoutData(formData_8);
		btnCancel.setText(Messages.getString("StartupDialog.CancelButton")); //$NON-NLS-1$

		final Button btnHelp = new Button(shell, SWT.NONE);
		final FormData formData_9 = new FormData();
		formData_9.width = 80;
		formData_9.top = new FormAttachment(btnCancel, 0, SWT.TOP);
		formData_9.left = new FormAttachment(btnCancel, 10);
		btnHelp.setLayoutData(formData_9);
		btnHelp.setText(Messages.getString("StartupDialog.HelpButton")); //$NON-NLS-1$
		btnHelp.setEnabled(false); // there is no help yet

		final Label lblVersion = new Label(shell, SWT.NONE);
		final FormData formData_11 = new FormData();
		formData_11.top = new FormAttachment(btnReadOnly, 0, SWT.TOP);
		formData_11.left = new FormAttachment(btnOpen, 2, SWT.LEFT);
		lblVersion.setLayoutData(formData_11);
		String versionText = NLS.bind(
				Messages.getString("StartupDialog.Version"), VersionInfo.getVersion()); //$NON-NLS-1$
		lblVersion.setText("V: " + VersionInfo.getVersion());//$NON-NLS-1$
		lblVersion.setToolTipText(versionText);
	}

	/**
	 * Returns the filename selected in the dialog.
	 * 
	 * @return the filename selected in the dialog
	 */
	public String getFilename() {
		return selectedFile;
	}

	/**
	 * Returns the password entered in the dialog.
	 * 
	 * @return the password entered in the dialog
	 */
	public StringBuilder getPassword() {
		return selectedPassword;
	}

	/**
	 * Returns the read only state entered in the dialog.
	 * 
	 * @return whether a safe should be opened read
	 */
	public boolean getReadonly() {
		return readOnly;
	}

}
