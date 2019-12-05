package plugin.shells;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import plugin.classes.Goal;
import plugin.classes.Share;

public class EditGoalShell extends Shell {

	ArrayList<Goal> goals = new ArrayList<Goal>();
	ArrayList<String> activityNames = new ArrayList<String>();
	String textGdue = new String();
	Date dueDate = new Date();	

	public EditGoalShell(Display display, TableViewer tableG, boolean edit) {
		super(display, SWT.SHELL_TRIM);
		goals = new ArrayList<Goal>();
		for(TableItem g: tableG.getTable().getItems()) {
			if (g.getChecked()) {
				goals.add(Share.status.findGoal(g.getText(0)));
			}
		}
		createContents(tableG, edit);
	}

	protected void createContents(TableViewer tableG, boolean edit) {
		if(edit) {
			setText("Edit Goal");
		}else {
			setText("New Goal");
		}			
		setSize(300, 300);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);

		Label labelChooseGdescription = new Label (composite, SWT.NONE);
		labelChooseGdescription.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Text textChooseGdescription = new Text (composite, SWT.NONE);
		GridData gd_textChooseGdescription = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_textChooseGdescription.minimumWidth = 140;		
		textChooseGdescription.setLayoutData(gd_textChooseGdescription);
		Label labelChooseGactivity = new Label (composite, SWT.NONE);
		labelChooseGactivity.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Text textChooseGactivity = new Text (composite, SWT.NONE);
		textChooseGactivity.setLayoutData(gd_textChooseGdescription);
		Button buttonAddGactivity = new Button (composite, SWT.CENTER);
		buttonAddGactivity.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));			
		Label labelChooseGdue = new Label (composite, SWT.NONE);
		labelChooseGdue.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		DateTime calendar = new DateTime(composite, 100);
		Button buttonUpdateG = new Button (composite, SWT.CENTER);
		buttonUpdateG.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

		labelChooseGdescription.setText("Description");
		if(edit && goals.size() == 1) {
			textChooseGdescription.setText(goals.get(0).getDescription());
		}

		labelChooseGactivity.setText("Activity Description");	
		if(edit && goals.size() == 1) {
			textChooseGactivity.setText(goals.get(0).getActivityNames().get(0));
		}
		
		buttonAddGactivity.setText("Add Activity Name");
		buttonAddGactivity.addSelectionListener(widgetSelectedAdapter(e -> {
			this.activityNames.add(textChooseGactivity.getText());
		}));	

		labelChooseGdue.setText("Due: ");	
	
		calendar.addSelectionListener(widgetSelectedAdapter(e -> {
			textGdue = calendar.getMonth()+1+"/"+calendar.getDay()+"/"+calendar.getYear();
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			Date dueDateTry = null;
			try {
				dueDateTry = format.parse(textGdue);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			dueDate.setTime(dueDateTry.getTime());
		}));
		    
		buttonUpdateG.setText ("OK");
		buttonUpdateG.addSelectionListener(widgetSelectedAdapter(f -> {	
			if(!edit && !textChooseGdescription.getText().equals("") && !activityNames.isEmpty() && !textGdue.equals("")) {
				Goal g = new Goal(textChooseGdescription.getText(), this.activityNames, dueDate);
				Share.status.getGoals().add(g);
				if(Share.status.getGithub() != null && Share.status.getGithub().getClient() != null) {
					Share.status.getGithub().createMilestone(g);
				}		
			} else if(edit && !this.activityNames.isEmpty()) {
				if(!textChooseGdescription.getText().equals("") && goals.size() == 1 ) {
					Goal g = goals.get(0);
					g.setDescription(textChooseGdescription.getText());
					g.getActivityNames().clear();
					g.setActivityNames(this.activityNames);
					if(Share.status.getGithub() != null && Share.status.getGithub().getClient() != null) {
						Share.status.getGithub().updateMilestone(g);
					}			
				}
				else if(!textChooseGdescription.getText().equals("")) {
					for(Goal g: goals) {
						g.setDue(this.dueDate);
						if(Share.status.getGithub() != null && Share.status.getGithub().getClient() != null) {
							Share.status.getGithub().updateMilestone(g);
						}			
					}
					
				}
			}
			for(TableItem t: tableG.getTable().getItems()) {
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