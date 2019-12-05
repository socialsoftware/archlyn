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

import plugin.classes.Artifact;
import plugin.classes.Share;

public class EditArtifactShell extends Shell {

	public EditArtifactShell(Display display, TableViewer tableA) {
		super(display, SWT.SHELL_TRIM);
		createContents(tableA);
	}

	protected void createContents(TableViewer tableA) {
		setText("Edit Package");
		setSize(300, 300);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);

		Label labelSelectArtifactType = new Label (composite, SWT.NONE);
		labelSelectArtifactType.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboSelectArtifactTypes = new Combo (composite, SWT.READ_ONLY);
		GridData gd_comboSelectArtifactTypes = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_comboSelectArtifactTypes.minimumWidth = 110;
		comboSelectArtifactTypes.setLayoutData(gd_comboSelectArtifactTypes);
		Button buttonUpdateArtifact = new Button (composite, SWT.CENTER);
		buttonUpdateArtifact.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

		labelSelectArtifactType.setText("Artifact Type");		
		comboSelectArtifactTypes.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				comboSelectArtifactTypes.setItems(Share.status.getArtifactTypesSA());	
			}
			public void focusLost(FocusEvent event){
			}
		});			
		
		buttonUpdateArtifact.setText ("OK");
		buttonUpdateArtifact.addSelectionListener(widgetSelectedAdapter(f -> {	
			if(!comboSelectArtifactTypes.getText().equals("")) {
				for(TableItem a: tableA.getTable().getItems()) {
					if (a.getChecked() && !a.getText(0).equals("Start")) {
						Artifact checked = Share.status.findArtifact(a.getText(5));
						checked.setType(Share.status.findArtifactType(comboSelectArtifactTypes.getText()));	
						a.setChecked(false);
					}
				}					
				Share.project.saveStatus();
			}
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