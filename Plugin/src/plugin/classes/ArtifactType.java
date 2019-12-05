package plugin.classes;

public class ArtifactType {
	
	private String type;
	private String extension;
	private Double reward;
	
	public ArtifactType(String type, String extension){
		this.type = type;
		this.extension = extension;
		this.reward = 0.0;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
		
	public Double getReward() {
		return reward;
	}

	public void setReward(Double reward) {
		this.reward = reward;
	}

	@Override
	public String toString() {
		return this.getType();
	}
}