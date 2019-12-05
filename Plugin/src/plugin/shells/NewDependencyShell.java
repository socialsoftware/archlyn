package plugin.shells;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.jface.viewers.TableViewer;
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
import org.eclipse.swt.widgets.TableItem;

import plugin.classes.Dependency;
import plugin.classes.Share;

public class NewDependencyShell extends Shell {

	public NewDependencyShell(Display display, TableViewer tableD, boolean edit) {
		super(display, SWT.SHELL_TRIM);
		createContents(tableD, edit);
	}

	protected void createContents(TableViewer tableD, boolean edit) {
		if(edit) {
			setText("Edit Dependency");
		}else {
			setText("New Dependency");
		}			
		setSize(300, 300);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);

		Label labelSelectOriginArtifact = new Label (composite, SWT.NONE);
		labelSelectOriginArtifact.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboSelectOriginArtifact = new Combo (composite, SWT.READ_ONLY);
		GridData gd_comboSelectOriginArtifact = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_comboSelectOriginArtifact.minimumWidth = 110;
		comboSelectOriginArtifact.setLayoutData(gd_comboSelectOriginArtifact);		
		Label labelSelectTargetArtifact = new Label (composite, SWT.NONE);
		labelSelectTargetArtifact.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboSelectTargetArtifact = new Combo (composite, SWT.READ_ONLY);
		comboSelectTargetArtifact.setLayoutData(gd_comboSelectOriginArtifact);	
		Label labelSelectDependencyType = new Label (composite, SWT.NONE);
		labelSelectDependencyType.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboSelectDependencyType = new Combo (composite, SWT.READ_ONLY);
		comboSelectDependencyType.setLayoutData(gd_comboSelectOriginArtifact);			
		Button buttonNewDependency = new Button (composite, SWT.CENTER);
		buttonNewDependency.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

		labelSelectOriginArtifact.setText("Choose Origin Artifact");
		comboSelectOriginArtifact.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				comboSelectOriginArtifact.setItems(Share.status.getArtifactsPathsSA());
			}
			public void focusLost(FocusEvent event){
			}
		});	

		labelSelectTargetArtifact.setText("Choose Target Artifact");	
		comboSelectTargetArtifact.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				comboSelectTargetArtifact.setItems(Share.status.getArtifactsPathsSA());
			}
			public void focusLost(FocusEvent event){
			}
		});

		labelSelectDependencyType.setText("Choose Dependency Type");
		comboSelectDependencyType.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				comboSelectDependencyType.setItems(Share.status.getDependencyTypesSA());
			}
			public void focusLost(FocusEvent event){
			}
		});	  

		buttonNewDependency.setText("OK");
		if(edit) {
			buttonNewDependency.addSelectionListener(widgetSelectedAdapter(g -> {
				if(!comboSelectOriginArtifact.getText().equals("") && !comboSelectTargetArtifact.getText().equals("") && !comboSelectDependencyType.getText().equals("")) {
					for(TableItem d: tableD.getTable().getItems()) {
						if (d.getChecked()) {
							Dependency checked = Share.status.findDependency(d.getText(0),d.getText(1),d.getText(2));
							checked.setOrigin(Share.status.findArtifact(comboSelectOriginArtifact.getText()));
							checked.setTarget(Share.status.findArtifact(comboSelectTargetArtifact.getText()));
							checked.setDependency(Share.status.findDependencyType(comboSelectDependencyType.getText()));
							d.setChecked(false);
							break;
						}
					}			
					Share.project.saveStatus();
				}
				this.dispose();
			}));			
		}else {
			buttonNewDependency.addSelectionListener(widgetSelectedAdapter(g -> {
				if(!comboSelectOriginArtifact.getText().equals("") && !comboSelectTargetArtifact.getText().equals("") && !comboSelectDependencyType.getText().equals("")) {
					Share.status.getDependencies().add(new Dependency(Share.status.findArtifact(comboSelectOriginArtifact.getText()),
							Share.status.findArtifact(comboSelectTargetArtifact.getText()),
							Share.status.findDependencyType(comboSelectDependencyType.getText())));		
					tableD.setInput(Share.status.getDependencies());			
					Share.project.saveStatus();
				}
				this.dispose();
			}));			

		}	
		
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