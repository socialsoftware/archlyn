package plugin.shells;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import plugin.classes.ArtifactEventsHandler;
import plugin.classes.Project;
import plugin.classes.ProjectCreator;
import plugin.classes.Share;
import plugin.classes.TaskSuggester;

public class NewProjectShell extends Shell {

	public NewProjectShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		createContents();
	}

	protected void createContents() {
		setText("New Project");
		setSize(300, 300);
		setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);
		
		Label labelSelectProjectType = new Label (composite, SWT.NONE);
		labelSelectProjectType.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		labelSelectProjectType.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Combo comboSelectProjectType = new Combo (composite, SWT.READ_ONLY);
		GridData gd_comboSelectProjectType = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_comboSelectProjectType.minimumWidth = 110;
		comboSelectProjectType.setLayoutData(gd_comboSelectProjectType);
		Label labelWriteProjectName = new Label (composite, SWT.NONE);
		labelWriteProjectName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		Text textChooseProjectName = new Text (composite, SWT.NONE);
		GridData gd_textChooseProjectName = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		gd_textChooseProjectName.minimumWidth = 140;
		textChooseProjectName.setLayoutData(gd_textChooseProjectName);
		Button buttonCreateProject = new Button (composite, SWT.CENTER);
		buttonCreateProject.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		labelSelectProjectType.setText("Choose Project Type");
		
		comboSelectProjectType.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent event) {
				comboSelectProjectType.setItems(new ProjectCreator().getProjectTypesSA());
			}
			public void focusLost(FocusEvent event){
				if(!comboSelectProjectType.getText().equals("")) {
					Share.message.setText("Project Type "+comboSelectProjectType.getText()+" chosen. Please write the project name");
				}
				
			}
		});		
		
		labelWriteProjectName.setText("Choose Project Name");
		
		textChooseProjectName.setEditable(true);	
		
		buttonCreateProject.setText("OK");
		buttonCreateProject.addSelectionListener(widgetSelectedAdapter(g -> {
			if(!comboSelectProjectType.getText().equals("") && !textChooseProjectName.getText().isEmpty()) {
				if(!ResourcesPlugin.getWorkspace().getRoot().getProject(textChooseProjectName.getText()).exists()) {
					new ProjectCreator().createProject(comboSelectProjectType.getText(), textChooseProjectName.getText());
					Share.message.setText("Project " + textChooseProjectName.getText() + " created. Please activate.");
					new ProjectCreator().verifyProject(textChooseProjectName.getText());
					IProject iproject = Share.findProject(textChooseProjectName.getText(),ResourcesPlugin.getWorkspace().getRoot().getProjects());
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