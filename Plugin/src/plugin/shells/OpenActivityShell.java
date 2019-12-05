package plugin.shells;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import plugin.classes.Share;
import plugin.classes.TaskSuggester;

public class OpenActivityShell extends Shell {

	public OpenActivityShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		createContents();
	}

	protected void createContents() {
		setText("Open Activity");
		setSize(300, 300);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);

		Label labelSelectActivity = new Label (composite, SWT.NONE);
		labelSelectActivity.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboSelectActivity = new Combo (composite, SWT.READ_ONLY);
		GridData gd_comboSelectActivity = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_comboSelectActivity.minimumWidth = 110;
		comboSelectActivity.setLayoutData(gd_comboSelectActivity);		
		Button buttonOpenActivity = new Button (composite, SWT.CENTER);
		buttonOpenActivity.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

		labelSelectActivity.setText("Choose Activity");

		comboSelectActivity.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				new TaskSuggester().suggest();
				comboSelectActivity.setItems(Share.status.getActivitiesSA());
			}
			public void focusLost(FocusEvent event){				
			}
		});		

		buttonOpenActivity.setText("OK");
		buttonOpenActivity.addSelectionListener(widgetSelectedAdapter(g -> {
			if(!comboSelectActivity.getText().isEmpty()) {
				Share.status.setActiveActivity(Share.status.findActivity(comboSelectActivity.getText()));
				Share.message.setText("Activity " + comboSelectActivity.getText() + " opened. Please move to Activity tab");
				Share.pluginView.getPluginComposite().openMainActivityView();
				this.dispose();
			}			
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