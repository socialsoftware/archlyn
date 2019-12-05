package plugin.classes;

public class Rule {
	
	private ArtifactType artifactType;
	private RuleType ruleType;
	
	public Rule(ArtifactType artifactType, RuleType ruleType){
		this.artifactType = artifactType;
		this.ruleType = ruleType;
	}

	public String toString() {
		return this.getArtifactType().toString();
	}
	public ArtifactType getArtifactType() {
		return artifactType;
	}

	public void setArtifactType(ArtifactType artifactType) {
		this.artifactType = artifactType;
	}

	public RuleType getRuleType() {
		return ruleType;
	}

	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}
}