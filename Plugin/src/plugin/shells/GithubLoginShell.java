package plugin.shells;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import plugin.classes.GithubConnector;
import plugin.classes.Share;

public class GithubLoginShell extends Shell {

	public GithubLoginShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		createContents();
	}

	protected void createContents() {
		setText("GitHub Login");
		setSize(300, 300);
		setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);
		
		Label labelGitAuthName = new Label (composite, SWT.PUSH);
		labelGitAuthName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true, 1, 1));
		labelGitAuthName.setAlignment(SWT.CENTER);
		Text gitAuthName = new Text (composite, SWT.READ_ONLY | SWT.CENTER);
		gitAuthName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		Label labelgitAuthPassword = new Label (composite, SWT.PUSH);
		labelgitAuthPassword.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		Text gitAuthPassword = new Text (composite, SWT.PUSH | SWT.PASSWORD);
		gitAuthPassword.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		Button gitAuth = new Button (composite, SWT.PUSH);
		gitAuth.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		gitAuthName.setEditable(true);		

		labelGitAuthName.setText("User Name");
		
		labelgitAuthPassword.setText("Password");
		
		gitAuthPassword.setEditable(true);		
	
		gitAuth.setText("OK");
		gitAuth.addSelectionListener(widgetSelectedAdapter(g -> {
			Share.status.setGithub(new GithubConnector());	
			Share.status.getGithub().authenticate(gitAuthName.getText(),gitAuthPassword.getText());
			this.dispose();
		}));
		
		this.pack();
		this.setMinimumSize(500, 300);
		this.redraw();
		this.update();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}