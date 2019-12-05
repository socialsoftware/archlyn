package plugin.classes;

import java.util.ArrayList;

public class CustomMetadata {
	
	private String name;
	private ArrayList<ArtifactType> artifactTypes;
	private ArrayList<DependencyType> dependencyTypes;
	private ArrayList<TaskType> taskTypes;
	private ArrayList<RuleType> ruleTypes;
	
	public CustomMetadata(String name) {
		this.name = name;
		this.artifactTypes = new ArrayList<ArtifactType>(Share.status.getArtifactTypes());
		this.dependencyTypes = new ArrayList<DependencyType>(Share.status.getDependencyTypes());
		this.taskTypes = new ArrayList<TaskType>(Share.status.getTaskTypes());
		this.ruleTypes = new ArrayList<RuleType>(Share.status.getRuleTypes());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<ArtifactType> getArtifactTypes() {
		return artifactTypes;
	}

	public void setArtifactTypes(ArrayList<ArtifactType> artifactTypes) {
		this.artifactTypes = artifactTypes;
	}

	public ArrayList<DependencyType> getDependencyTypes() {
		return dependencyTypes;
	}

	public void setDependencyTypes(ArrayList<DependencyType> dependencyTypes) {
		this.dependencyTypes = dependencyTypes;
	}

	public ArrayList<TaskType> getTaskTypes() {
		return taskTypes;
	}

	public void setTaskTypes(ArrayList<TaskType> taskTypes) {
		this.taskTypes = taskTypes;
	}

	public ArrayList<RuleType> getRuleTypes() {
		return ruleTypes;
	}

	public void setRuleTypes(ArrayList<RuleType> ruleTypes) {
		this.ruleTypes = ruleTypes;
	}	
}