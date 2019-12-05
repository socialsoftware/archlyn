package plugin.classes;

import java.util.ArrayList;

public class Activity {

	private ArrayList<Task> tasks;
	private String name;
	private String projectType;
	
	public Activity(ArrayList<Task> tasks) {
		this.tasks = tasks;
		this.name = String.valueOf(Share.status.getTaskContext().size());
		this.projectType = Share.status.getProjectType();
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	
	public ArrayList<Artifact> getArtifacts(){
	     	ArrayList<Artifact> artifactsInActivity = new ArrayList<Artifact>();
			for(Task t: this.getTasks()) {
				for(Artifact a: t.getUsed()) {
					artifactsInActivity.add(a);
				}			
			}
			return artifactsInActivity;	
	}
}