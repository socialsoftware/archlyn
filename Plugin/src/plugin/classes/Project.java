package plugin.classes;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.FrameworkUtil;

public class Project {
	
	private IProject project;
	
	public Project(IProject project) {
		this.project = project;
	}

	public String getLocationBase(){
		return project.getLocation().toString();
	}
	
	public String getLocationSrc(){
		return project.getFolder("src").getLocation().toString();
	}

	public String getLocationPlugin() {
		return project.getFolder(Share.pluginFolderName).getLocation().toString();
	}	
	
	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public void checkStatus() {
		File f = new File(project.getLocation()+"/Status.json");
		if (f.exists()) {
			Share.status = new JsonTranslator().read(f);
			Share.status.setProject(project);
		}
		else {
			Share.status = new Status(this);
			Share.status.Innitialize(getLocationPlugin());
		}
	}	
	
	public void saveStatus() {
		new JsonTranslator().write(new File(project.getLocation()+"/Status.json"), Share.status);
		new JsonTranslator().writeCurrentProjectName(new File(Platform.getStateLocation(FrameworkUtil.getBundle(getClass()))+"/CurrentProjectName.json"), project.getName());
	}
}