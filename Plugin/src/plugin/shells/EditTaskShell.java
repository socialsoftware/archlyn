package plugin.shells;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.util.ArrayList;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
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

import plugin.classes.Share;
import plugin.classes.Task;

public class EditTaskShell extends Shell {

	private ArrayList<Task> tasks;	

	public EditTaskShell(Display display, TableViewer tableT) {
		super(display, SWT.SHELL_TRIM);
		tasks = new ArrayList<Task>();
		for(TableItem t: tableT.getTable().getItems()) {
			if (t.getChecked()) {
				Task task = Share.status.findTask(t.getText(0));
				if(task == null) {
					task = Share.status.findTask(t.getText(3));
				}
				tasks.add(task);
			}
		}
		createContents(tableT);
	}

	protected void createContents(TableViewer tableT) {
		setText("Edit Task");				
		setSize(300, 300);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);

		Label labelEditDescription = new Label (composite, SWT.NONE);
		labelEditDescription.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Text textEditDescription = new Text(composite, SWT.NONE);	
		GridData gd_textEditDescription = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_textEditDescription.minimumWidth = 140;		
		textEditDescription.setLayoutData(gd_textEditDescription);
		Label labelSelectStatus = new Label (composite, SWT.NONE);
		labelSelectStatus.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboSelectStatus = new Combo (composite, SWT.READ_ONLY);
		GridData gd_comboSelectStatus = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_comboSelectStatus.minimumWidth = 110;
		comboSelectStatus.setLayoutData(gd_comboSelectStatus);					
		Button buttonEditTask = new Button (composite, SWT.CENTER);
		buttonEditTask.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

		labelEditDescription.setText("Task Description");	
		if(tasks.size() == 1) {
			textEditDescription.setText(tasks.get(0).getDescription());
		}

		labelSelectStatus.setText("Task Status");		
		comboSelectStatus.setItems(Task.getPossibleStatus());
		if(tasks.size() == 1) {
			comboSelectStatus.setText(tasks.get(0).getStatus());
		}		

		buttonEditTask.setText("OK");
		buttonEditTask.addSelectionListener(widgetSelectedAdapter(f -> {
			if(!textEditDescription.getText().equals("")) {
				tasks.get(0).setDescription(textEditDescription.getText());		
			}			

			if(!comboSelectStatus.getText().equals("")) {
				for(Task t: tasks) {
					if(comboSelectStatus.getText().equals("Active")) {
						t.setStatus(comboSelectStatus.getText());
						Share.status.setActiveTask(t);
						if(Share.status.getGithub() != null && Share.status.getGithub().getClient() != null) {
							Share.status.getGithub().updateIssue(t);
						}						
					}
					else if(comboSelectStatus.getText().equals("Complete")) {
						Task.completeTask(t, false);
					} else {
						t.setStatus(comboSelectStatus.getText());
					}
					t.setStatus(comboSelectStatus.getText());
				}
			}	
			for(TableItem t: tableT.getTable().getItems()) {
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