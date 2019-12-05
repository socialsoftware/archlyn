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
import plugin.classes.TaskType;

public class EditTaskTypeShell extends Shell {

	public EditTaskTypeShell(Display display, TableViewer tableTT, boolean edit) {
		super(display, SWT.SHELL_TRIM);
		createContents(tableTT, edit);
	}

	protected void createContents(TableViewer tableTT, boolean edit) {
		if(edit) {
			setText("Edit Task Type");
		}else {
			setText("New Task Type");
		}	
		
		setSize(300, 300);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);

		Label labelCurrentDT = new Label (composite, SWT.NONE);
		labelCurrentDT.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboDT = new Combo (composite, SWT.READ_ONLY);
		GridData gd_comboDT = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_comboDT.minimumWidth = 110;
		comboDT.setLayoutData(gd_comboDT);				
		Button buttonAddDT = new Button (composite, SWT.CENTER);
		buttonAddDT.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));	
		Label labelDescription = new Label (composite, SWT.NONE);
		labelDescription.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Text textDescription = new Text (composite, SWT.NONE);
		GridData gd_textDescription = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_textDescription.minimumWidth = 140;		
		textDescription.setLayoutData(gd_textDescription);
		Button buttonUpdateTaskType = new Button (composite, SWT.CENTER);
		buttonUpdateTaskType.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
	    ArrayList<DependencyType> dts = new ArrayList<DependencyType>();
	    
	    labelCurrentDT.setText("");
	    
	    comboDT.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				comboDT.setItems(Share.status.getDependencyTypesSA());	
			}
			public void focusLost(FocusEvent event){
			}
		});			
	 
	    buttonAddDT.setText("Add Dependency Type");
	    buttonAddDT.addSelectionListener(widgetSelectedAdapter(e -> {
			dts.add(Share.status.findDependencyType(comboDT.getText()));
			labelCurrentDT.setText(comboDT.getText()+","+labelCurrentDT.getText());
		}));			
		
	    labelDescription.setText("Description");
			
	    buttonUpdateTaskType.setText("OK");
	    buttonUpdateTaskType.addSelectionListener(widgetSelectedAdapter(e -> {
			if(!dts.isEmpty() && !labelDescription.getText().equals("")) {
				if(edit) {
					for(TableItem tt: tableTT.getTable().getItems()) {
						if (tt.getChecked()) {
							TaskType checked = Share.status.findTaskType(tt.getText(3));
							checked = new TaskType(new ArrayList<DependencyType>(dts),textDescription.getText());
							tt.setChecked(false);
							break;
						}
					}
				}
				else {
					Share.status.getTaskTypes().add(new TaskType(new ArrayList<DependencyType>(dts),textDescription.getText()));
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