package plugin.shells;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.util.ArrayList;

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
import org.eclipse.swt.widgets.Text;

import plugin.classes.DependencyType;
import plugin.classes.Share;

public class EditDependencyTypeShell extends Shell {

	ArrayList<DependencyType> dependencyTypes;

	public EditDependencyTypeShell(Display display, TableViewer tableDT, boolean edit) {
		super(display, SWT.SHELL_TRIM);
		dependencyTypes = new ArrayList<DependencyType>();
		for(TableItem t: tableDT.getTable().getItems()) {
			if (t.getChecked()) {
				dependencyTypes.add(Share.status.findDependencyType(t.getText(2)));
			}
		}
		createContents(tableDT, edit);
	}

	protected void createContents(TableViewer tableDT, boolean edit) {
		if(edit) {
			setText("Edit Dependency Type");
		}else {
			setText("New Dependency Type");
		}				
		setSize(300, 300);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);

		Label labelOriginAT = new Label (composite, SWT.NONE);
		labelOriginAT.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboOriginAT = new Combo (composite, SWT.READ_ONLY);
		GridData gd_comboOriginAT = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_comboOriginAT.minimumWidth = 110;
		comboOriginAT.setLayoutData(gd_comboOriginAT);		
		Label labelTargetAT = new Label (composite, SWT.NONE);
		labelTargetAT.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboTargetAT = new Combo (composite, SWT.READ_ONLY);
		comboTargetAT.setLayoutData(gd_comboOriginAT);
		Label labelDependency = new Label (composite, SWT.NONE);
		labelDependency.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Text textDependency = new Text (composite, SWT.NONE);
		GridData gd_textDependency = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_textDependency.minimumWidth = 140;		
		textDependency.setLayoutData(gd_textDependency);
		Label labelMultiplicity = new Label (composite, SWT.NONE);
		labelMultiplicity.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Text textMultiplicity = new Text (composite, SWT.NONE);
		textMultiplicity.setLayoutData(gd_textDependency);		
		Button buttonUpdateDependencyType = new Button (composite, SWT.CENTER);
		buttonUpdateDependencyType.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

		labelOriginAT.setText("Origin Artifact Type");	
		comboOriginAT.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				comboOriginAT.setItems(Share.status.getArtifactTypesSA());	
			}
			public void focusLost(FocusEvent event){
			}
		});			

		labelTargetAT.setText("Target Artifact Type");	
		comboTargetAT.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				comboTargetAT.setItems(Share.status.getArtifactTypesSA());	
			}
			public void focusLost(FocusEvent event){
			}
		});		

		labelDependency.setText("Dependency Name");		
		if(edit && dependencyTypes.size() == 1) {
			textDependency.setText(dependencyTypes.get(0).getDependency());
		}		

		labelMultiplicity.setText("Multiplicity (0 for 1...*)");
		if(edit && dependencyTypes.size() == 1) {
			textMultiplicity.setText(Integer.toString(dependencyTypes.get(0).getMultiplicity()));
		}	

		buttonUpdateDependencyType.setText ("OK");
		buttonUpdateDependencyType.addSelectionListener(widgetSelectedAdapter(g -> {	
			if(!edit && !comboOriginAT.getText().equals("") && !comboTargetAT.getText().equals("") && !textDependency.getText().equals("") && !textMultiplicity.getText().equals("")) {
				DependencyType dt = new DependencyType(Share.status.findArtifactType(comboOriginAT.getText()),Share.status.findArtifactType(comboTargetAT.getText()),textDependency.getText());
				try {						
					Integer.parseInt(textMultiplicity.getText());
					dt.setMultiplicity(Integer.parseInt(textMultiplicity.getText()));						
				} catch (NumberFormatException e1) {
					System.out.println("Catch NumberFormatException: " + textMultiplicity.getText());
				}
				Share.status.getDependencyTypes().add(dt);

			} else if(edit && !dependencyTypes.isEmpty()){
				if(!comboOriginAT.getText().equals("")) {
					for(DependencyType dt: dependencyTypes) {
						dt.setOriginAT(Share.status.findArtifactType(comboOriginAT.getText()));
					}
				}
				if(!comboTargetAT.getText().equals("")) {
					for(DependencyType dt: dependencyTypes) {
						dt.setTargetAT(Share.status.findArtifactType(comboTargetAT.getText()));
					}
				}						
				if(!textDependency.getText().equals("") && dependencyTypes.size() == 1 ) {
					dependencyTypes.get(0).setDependency(textDependency.getText());
				}
				if(!textMultiplicity.getText().equals("")) {
					for(DependencyType dt: dependencyTypes) {
						try {						
							Integer.parseInt(textMultiplicity.getText());
							dt.setMultiplicity(Integer.parseInt(textMultiplicity.getText()));						
						} catch (NumberFormatException e1) {
							System.out.println("Catch NumberFormatException: " + textMultiplicity.getText());
						}
					}					
				}
			}
			for(TableItem t: tableDT.getTable().getItems()) {
				if (t.getChecked()) {
					t.setChecked(false);
				}
			}
			Share.project.saveStatus();
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