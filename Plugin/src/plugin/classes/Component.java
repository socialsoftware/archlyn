package plugin.classes;
import java.nio.file.Path;
import java.util.ArrayList;

public class Component {
	
	private String path;
	private String name;
	private ArtifactType type;
	private ArrayList<Artifact> artifacts;
	private boolean defaultForType;
	
	public Component(Path path) {
		this.path = path.toString().substring(Share.project.getLocationBase().length()).replace("\\","/");
		this.name = path.getFileName().toString();
		this.type = new ArtifactType("untyped",".java");
		this.artifacts = new ArrayList<Artifact>();
		this.defaultForType = false;
	}

	public String getPath() {
		return path;
	}
	
	public String getFullPath() {
		return Share.project.getLocationBase()+path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ArtifactType getType() {
		return type;
	}
	
	public void setTypeSelfOnly(ArtifactType type) {
		this.type = type;
	}

	public void setType(ArtifactType type) {
		this.type = type;
		setChildArtifactsType();
	}

	private void setChildArtifactsType() {
		for(Artifact a: Share.status.getArtifacts()) {
			System.out.println(a.getName());
			if(a.getComponentPath().equals(path)) {
				a.setType(this.type);
				System.out.println("found and type to" + a.getType());
			}
		}	
	}

	public ArrayList<Artifact> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(ArrayList<Artifact> artifacts) {
		this.artifacts = artifacts;
	}
	
	public String toString() {
		return this.getPath();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDefaultForType() {
		return defaultForType;
	}

	public void setDefaultForType(boolean defaultForType) {
		this.defaultForType = defaultForType;
	}
	
	public String getDefaultForType() {
		if(defaultForType) {
			return "Yes";
		}
		else return "No";
	}	
}