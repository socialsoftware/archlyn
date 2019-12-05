package plugin.shells;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
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

import plugin.classes.ArtifactEventsHandler;
import plugin.classes.Project;
import plugin.classes.ProjectCreator;
import plugin.classes.Share;
import plugin.classes.TaskSuggester;

public class OpenProjectShell extends Shell {

	public OpenProjectShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		createContents();
	}

	protected void createContents() {
		setText("Open Project");
		setSize(300, 300);
		setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);
		
		Label labelSelectProject = new Label (composite, SWT.NONE);
		labelSelectProject.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboSelectProject = new Combo (composite, SWT.READ_ONLY);
		GridData gd_comboSelectProject = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_comboSelectProject.minimumWidth = 110;
		comboSelectProject.setLayoutData(gd_comboSelectProject);		
		Button buttonOpenProject = new Button (composite, SWT.CENTER);
		buttonOpenProject.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		labelSelectProject.setText("Choose Project");
		
		comboSelectProject.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				ArrayList<String> projectNames = new ArrayList<String>();
				for (IProject ip: ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
					projectNames.add(ip.getName());
				}
				comboSelectProject.setItems(projectNames.toArray(new String[0]));
			}
			public void focusLost(FocusEvent event){				
			}
		});		
		
		buttonOpenProject.setText("OK");
		buttonOpenProject.addSelectionListener(widgetSelectedAdapter(g -> {
			if(!comboSelectProject.getText().isEmpty()) {
				new ProjectCreator().verifyProject(comboSelectProject.getText());
				IProject iproject = Share.findProject(comboSelectProject.getText(),ResourcesPlugin.getWorkspace().getRoot().getProjects());
				Share.project = new Project(iproject);
				Share.project.checkStatus();
				Share.project.saveStatus();
				ResourcesPlugin.getWorkspace().removeResourceChangeListener(new ArtifactEventsHandler());
				ResourcesPlugin.getWorkspace().addResourceChangeListener(new ArtifactEventsHandler());	
				Share.message.setText("Project activated. Please choose an activity. (Optional: Github Login)");
				
				new TaskSuggester().suggest();
				Share.message.setText("Activity " + Share.status.getActiveActivity().getName() + " opened. Please move to Activity tab");
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