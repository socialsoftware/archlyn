package plugin.classes;
public class DependencyType {	
	
	private ArtifactType originAT;
	private ArtifactType targetAT;
	private String dependency;
	private int multiplicity;
	
	public DependencyType(ArtifactType originAT, ArtifactType targetAT, String dependency) {
		this.setOriginAT(originAT);
		this.setTargetAT(targetAT);
		this.setDependency(dependency);
		this.setMultiplicity(1);
	}
	
	public String toString() {
		return this.getDependency();
	}

	public ArtifactType getOriginAT() {
		return originAT;
	}

	public void setOriginAT(ArtifactType originAT) {
		this.originAT = originAT;
	}

	public ArtifactType getTargetAT() {
		return targetAT;
	}

	public void setTargetAT(ArtifactType targetAT) {
		this.targetAT = targetAT;
	}

	public String getDependency() {
		return dependency;
	}

	public void setDependency(String dependency) {
		this.dependency = dependency;
	}

	public int getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(int multiplicity) {
		this.multiplicity = multiplicity;
	}
}