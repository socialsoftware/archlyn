package plugin.shells;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.apache.commons.lang3.ArrayUtils;
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
import org.eclipse.swt.widgets.Text;

import plugin.classes.Activity;
import plugin.classes.ProjectCreator;
import plugin.classes.Share;
import plugin.classes.TaskSuggester;

public class EditActivityShell extends Shell {

	public EditActivityShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		createContents();
	}

	protected void createContents() {
		setText("Edit Activity");
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
		gd_comboSelectActivity.minimumWidth = 140;
		comboSelectActivity.setLayoutData(gd_comboSelectActivity);		
		Label labelRename = new Label (composite, SWT.NONE);
		labelRename.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Text textRename = new Text (composite, SWT.NONE);
		textRename.setLayoutData(gd_comboSelectActivity);	
		Label labelSelectType= new Label (composite, SWT.NONE);
		labelSelectType.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboSelectType = new Combo (composite, SWT.READ_ONLY);
		comboSelectType.setLayoutData(gd_comboSelectActivity);			
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
		
		labelRename.setText("Rename");	
		textRename.setText(Share.status.getActiveActivity().getName());
		
		labelSelectType.setText("Type");
		
		comboSelectType.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {						
				comboSelectType.setItems(ArrayUtils.addAll(new ProjectCreator().getProjectTypesSA(),Share.status.getCustomMetadataSA()));
			}
			public void focusLost(FocusEvent event){				
			}
		});			

		buttonOpenActivity.setText("OK");
		buttonOpenActivity.addSelectionListener(widgetSelectedAdapter(g -> {
			if(!comboSelectActivity.getText().isEmpty()) {
				Activity a = Share.status.findActivity(comboSelectActivity.getText());
				if(!textRename.getText().isEmpty()) {
					a.setName(textRename.getText());
				}
				if(!comboSelectType.getText().isEmpty()) {
					a.setProjectType(comboSelectType.getText());
				}
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