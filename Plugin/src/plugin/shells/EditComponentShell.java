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

import plugin.classes.Component;
import plugin.classes.Share;

public class EditComponentShell extends Shell {

	public EditComponentShell(Display display, TableViewer tableC) {
		super(display, SWT.SHELL_TRIM);
		createContents(tableC);
	}

	protected void createContents(TableViewer tableC) {
		setText("Edit Package");
		setSize(300, 300);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);

		Label labelSelectComponentType = new Label (composite, SWT.NONE);
		labelSelectComponentType.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboSelectComponentTypes = new Combo (composite, SWT.READ_ONLY);
		GridData gd_comboSelectComponentTypes = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_comboSelectComponentTypes.minimumWidth = 110;
		comboSelectComponentTypes.setLayoutData(gd_comboSelectComponentTypes);		
		Button checkboxComponentDefault = new Button(composite, SWT.CHECK);
		Button buttonUpdateComponent = new Button (composite, SWT.CENTER);
		buttonUpdateComponent.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

		labelSelectComponentType.setText("Choose Type");		
		comboSelectComponentTypes.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				comboSelectComponentTypes.setItems(Share.status.getArtifactTypesSA());	
			}
			public void focusLost(FocusEvent event){
			}
		});		

		checkboxComponentDefault.setText("Is Package Default for Type?");
		checkboxComponentDefault.setSelection(true);

		buttonUpdateComponent.setText ("OK");
		buttonUpdateComponent.addSelectionListener(widgetSelectedAdapter(f -> {	
				if(!comboSelectComponentTypes.getText().equals("")) {
					for(TableItem c: tableC.getTable().getItems()) {
						if (c.getChecked()) {
							Component checked = Share.status.findComponent(c.getText(3));
							checked.setType(Share.status.findArtifactType(comboSelectComponentTypes.getText()));
							if (checkboxComponentDefault.getSelection()) {
								checked.setDefaultForType(true);
							} else {
								checked.setDefaultForType(false);	
							}
							c.setChecked(false);
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