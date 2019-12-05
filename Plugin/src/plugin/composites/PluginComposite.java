package plugin.composites;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import plugin.classes.ArtifactEventsHandler;
import plugin.classes.JsonTranslator;
import plugin.classes.Project;
import plugin.classes.ProjectCreator;
import plugin.classes.Share;
import plugin.classes.TaskSuggester;
import plugin.shells.EditActivityShell;
import plugin.shells.GithubLoginShell;
import plugin.shells.NewProjectShell;
import plugin.shells.OpenActivityShell;
import plugin.shells.OpenProjectShell;
import plugin.views.DataView;
import plugin.views.MainActivityView;
import plugin.views.MainTaskView;
import plugin.views.MetadataView;

public class PluginComposite extends Composite {

	private Label labelMessages;
	private Composite compositeMain;

	public PluginComposite(Composite parent, int style) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));

		ToolBar toolBar = new ToolBar(this, SWT.BORDER | SWT.FLAT | SWT.RIGHT);	
		ToolItem toolItemActivity = new ToolItem(toolBar, SWT.NONE);		
		ToolItem sepatator1 = new ToolItem(toolBar, SWT.SEPARATOR);		
		ToolItem toolItemDesign = new ToolItem(toolBar, SWT.NONE);		
		ToolItem separator2 = new ToolItem(toolBar, SWT.SEPARATOR);		
		ToolItem toolItemOptions = new ToolItem(toolBar, SWT.DROP_DOWN);
		toolItemOptions.setText(".");		

		Menu menuOptions = new Menu(toolBar);
		MenuItem menuItemResume = new MenuItem(menuOptions, SWT.NONE);	
		new MenuItem(menuOptions, SWT.SEPARATOR);	
		MenuItem menuItemNewProject = new MenuItem(menuOptions, SWT.NONE);		
		MenuItem menuItemOpenProject = new MenuItem(menuOptions, SWT.NONE);
		new MenuItem(menuOptions, SWT.SEPARATOR);		
		MenuItem menuItemOpenActivity = new MenuItem(menuOptions, SWT.NONE);
		MenuItem menuItemEditActivity = new MenuItem(menuOptions, SWT.NONE);
		new MenuItem(menuOptions, SWT.SEPARATOR);		
		MenuItem menuItemGithubLoginStatus = new MenuItem(menuOptions, SWT.NONE);	
		MenuItem menuItemGithubLogin = new MenuItem(menuOptions, SWT.NONE);		
		MenuItem menuItemGithubSync = new MenuItem(menuOptions, SWT.NONE);
		MenuItem menuItemGitHubIssues = new MenuItem(menuOptions, SWT.NONE);
		MenuItem menuItemGitHubContribution = new MenuItem(menuOptions, SWT.NONE);

		labelMessages = new Label(this, SWT.CENTER);
		compositeMain = new Composite(this, SWT.NONE);
		toolBar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));

		toolItemActivity.addSelectionListener(widgetSelectedAdapter(g -> {
			this.openMainActivityView();	
		}));
		toolItemActivity.setText("Activity");

		toolItemDesign.setText("Design");
		toolItemDesign.addSelectionListener(widgetSelectedAdapter(g -> {
			this.disposeCompositeMain();
			Share.dataView = new DataView();
			Share.dataView.create(compositeMain);
			Share.metadataView = new MetadataView();
			Share.metadataView.create(compositeMain);
			refreshSizeCompositeMain();				
		}));
		
		toolItemOptions.addSelectionListener(widgetSelectedAdapter(g -> {
			menuOptions.setVisible(true);
		}));	
				
		menuItemResume.setText("Resume");
		menuItemResume.addSelectionListener(widgetSelectedAdapter(g -> {
			String projectName = new JsonTranslator().readCurrentProjectName();
			if(projectName != null) {
				new ProjectCreator().verifyProject(projectName);
				IProject iproject = Share.findProject(projectName,ResourcesPlugin.getWorkspace().getRoot().getProjects());
				Share.project = new Project(iproject);
				Share.project.checkStatus();
				Share.project.saveStatus();
				ResourcesPlugin.getWorkspace().removeResourceChangeListener(new ArtifactEventsHandler());
				ResourcesPlugin.getWorkspace().addResourceChangeListener(new ArtifactEventsHandler());	
				Share.message.setText("Project activated. Please choose an activity. (Optional: Github Login)");
				new TaskSuggester().suggest();
				openMainActivityView();
			}
		}));	

		menuItemNewProject.setText("New Project");
		menuItemNewProject.addSelectionListener(widgetSelectedAdapter(g -> {
			Shell shellNewProject = new NewProjectShell(parent.getDisplay());
			shellNewProject.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
			shellNewProject.open();
			shellNewProject.layout();
		}));	
		
		menuItemOpenProject.setText("Open Project");
		menuItemOpenProject.addSelectionListener(widgetSelectedAdapter(g -> {
			Shell shellNewProject = new OpenProjectShell(parent.getDisplay());
			shellNewProject.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
			shellNewProject.open();
			shellNewProject.layout();
		}));	

		menuItemOpenActivity.setText("Open Activity");
		menuItemOpenActivity.addSelectionListener(widgetSelectedAdapter(g -> {
			Shell shellNewActivity = new OpenActivityShell(parent.getDisplay());
			shellNewActivity.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
			shellNewActivity.open();
			shellNewActivity.layout();
		}));	
	
		menuItemEditActivity.setText("Edit Activity");
		menuItemEditActivity.addSelectionListener(widgetSelectedAdapter(g -> {
			Shell shellEditActivity = new EditActivityShell(parent.getDisplay());
			shellEditActivity.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
			shellEditActivity.open();
			shellEditActivity.layout();
		}));	

		menuItemGithubLoginStatus.setText("Please Log In");
		
		menuItemGithubLogin.setText("GitHub Login");
		menuItemGithubLogin.addSelectionListener(widgetSelectedAdapter(g -> {
			Shell shellGithubLogin = new GithubLoginShell(parent.getDisplay());
			shellGithubLogin.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
			shellGithubLogin.open();
			shellGithubLogin.layout();		
		}));	

		menuItemGithubSync.setText("GitHub Sync");
		menuItemGithubSync.addSelectionListener(widgetSelectedAdapter(g -> {
			Share.status.getGithub().updateTaskUserInfo();
			Share.status.getGithub().print();
		}));		

		menuItemGitHubIssues.setText("GitHub Issues");
		menuItemGitHubIssues.addSelectionListener(widgetSelectedAdapter(g -> {
			try {
				java.awt.Desktop.getDesktop().browse(new URI(Share.status.getGithub().getRepository().getHtmlUrl()+"/issues"));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}		
		}));	

		menuItemGitHubContribution.setText("Contribution");
		menuItemGitHubContribution.addSelectionListener(widgetSelectedAdapter(g -> {
			try {
				java.awt.Desktop.getDesktop().browse(new URI(Share.status.getGithub().getRepository().getHtmlUrl()+"/pulse"));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}		
		}));	

		labelMessages.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		labelMessages.setText("Messages");

		compositeMain.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		compositeMain.setLayout(new GridLayout(1, false));
	
		Share.message = labelMessages;
		Share.message.setText("Create new template projet OR Activate existing project");	
	}

	public void disposeCompositeMain() {
		if(Share.dataView != null) {
			Share.dataView.stopRefresh();
		}
		if(Share.metadataView != null) {
			Share.metadataView.stopRefresh();
		}
		if(Share.mainActivityView != null) {
			Share.mainActivityView.stopRefresh();
		}
		if(Share.mainTaskView != null) {
			Share.mainTaskView.stopRefresh();
		}
		
		Share.dataView = null;
		Share.metadataView = null;
		Share.mainActivityView = null;
		Share.mainTaskView = null;
		
		for(Control c: compositeMain.getChildren()) {
			if(!c.isDisposed()) {
				c.dispose();
			}
		}	
	}
	
	public void openMainActivityView() {
		this.disposeCompositeMain();
		new TaskSuggester().suggest();
	    Share.mainActivityView = new MainActivityView();
		Share.mainActivityView.create(compositeMain);
		refreshSizeCompositeMain();					
	}
	
	public void openMainTaskView() {
		this.disposeCompositeMain();
	    Share.mainTaskView = new MainTaskView();
		Share.mainTaskView.create(compositeMain);
		refreshSizeCompositeMain();					
	}
	
	private void refreshSizeCompositeMain() {
		Point resizePoint = compositeMain.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		resizePoint.y = resizePoint.y + 500;
		compositeMain.layout(true, true);							
		compositeMain.setSize(resizePoint);
	}

	public Label getLabelMessages() {
		return labelMessages;
	}

	public void setLabelMessages(Label labelMessages) {
		this.labelMessages = labelMessages;
	}

	public Composite getCompositeMain() {
		return compositeMain;
	}

	public void setCompositeMain(Composite compositeMain) {
		this.compositeMain = compositeMain;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}