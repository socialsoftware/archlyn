package plugin.classes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Artifact {
	
	private String path;
	private String name;
	private ArtifactType type;
	private boolean complete;
	private boolean beeingWorkedOn;
	
	public Artifact(Path path, ArtifactType type) {
		this.setPath(path.toString().substring(Share.project.getLocationBase().length()).replace("\\","/"));
		this.name = type.getType();
		this.type = type;
		this.complete = true;
		this.beeingWorkedOn = false;
	}
		
	public Artifact(Path path) {
		this.setPath(path.toString().substring(Share.project.getLocationBase().length()).replace("\\","/"));
		this.setName(path.getFileName().toString());
		this.setType(new ArtifactType("untyped",".java"));
		this.setComplete(false);
	}
	
	public Artifact(String path, String name, ArtifactType type) {
		try {
			new File(path+"/"+name+type.getExtension()).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setPath(path.substring(Share.project.getLocationBase().length()).replace("\\","/") + "/"+name+type.getExtension());
		this.setName(name+type.getExtension());
		this.setType(type);
		this.setComplete(false);
		
	}
	
	public String toString() {
		return this.getPath();
	}

	public String getPath() {
		return path;
	}
	
	public String getFullPath() {
		return Share.project.getLocationBase()+path;
	}
	
	public String getComponentPath() {
		return path.substring(0, path.length()-name.length()-1);
	}
	
	public String getComponentFullPath() {
		return Share.project.getLocationBase()+path.substring(0, path.length()-name.length()-1);
	}
	
	public String getComponentName() {
		String s = path.substring(0, path.length()-name.length()-1);
		String ss = s.substring(s.lastIndexOf("/")+1);		
		return ss;
	}	

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArtifactType getType() {
		return type;
	}

	public void setType(ArtifactType type) {
		this.type = type;
	}

	public String isComplete() {
		if(complete)
			return "Complete";
		else
			return "Incomplete";
	}
	
	public boolean getComplete() {
		return this.complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public String isBeeingWorkedOn() {
		if (beeingWorkedOn) {
			return "Yes";
		}
		return "No";
	}

	public void setBeeingWorkedOn(boolean beeingWorkedOn) {
		this.beeingWorkedOn = beeingWorkedOn;
	}
}