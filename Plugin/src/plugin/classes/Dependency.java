package plugin.classes;
public class Dependency {	
	
	private Artifact origin;
	private Artifact target;
	private DependencyType dependency;
	
	public Dependency(Artifact origin, Artifact target, DependencyType dependency) {
		this.setOrigin(origin);
		this.setTarget(target);
		this.setDependency(dependency);
	}
	
	public String toString() {
		return this.getDependency().toString();
	}

	public Artifact getOrigin() {
		return origin;
	}

	public void setOrigin(Artifact origin) {
		this.origin = origin;
	}

	public Artifact getTarget() {
		return target;
	}

	public void setTarget(Artifact target) {
		this.target = target;
	}

	public DependencyType getDependency() {
		return dependency;
	}

	public void setDependency(DependencyType dependency) {
		this.dependency = dependency;
	}	
}