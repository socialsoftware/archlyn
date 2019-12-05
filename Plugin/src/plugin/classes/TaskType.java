package plugin.classes;
import java.util.ArrayList;
import java.util.List;

public class TaskType {
	
	private List<ArtifactType> used;
	private List<ArtifactType> produced;
	private List<DependencyType> dependencyTypes;
	private String description;
	
	public TaskType(List<ArtifactType> used, List<ArtifactType> produced, List<DependencyType> dependencyTypes, 
			String description ) {
		
		this.setUsed(used);
		this.setProduced(produced);
		this.setDependencies(dependencyTypes);
		this.setDescription(description);
	}
	
	public TaskType(List<DependencyType> dependencyTypes, String description ) {	
		this.setDependencies(dependencyTypes);
		this.setDescription(description);
		used = new ArrayList<ArtifactType>();
		produced = new ArrayList<ArtifactType>();		
		for(DependencyType dt: this.getDependencies()) {
			used.add(dt.getOriginAT());
			produced.add(dt.getTargetAT());
		}
	}
	
	public String toString() {
		return this.getDescription();		
	}

	public List<ArtifactType> getUsed() {
		return used;
	}

	public void setUsed(List<ArtifactType> used) {
		this.used = used;
	}

	public List<ArtifactType> getProduced() {
		return produced;
	}

	public void setProduced(List<ArtifactType> produced) {
		this.produced = produced;
	}

	public List<DependencyType> getDependencies() {
		return dependencyTypes;
	}

	public void setDependencies(List<DependencyType> dependencyTypes) {
		this.dependencyTypes = dependencyTypes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}