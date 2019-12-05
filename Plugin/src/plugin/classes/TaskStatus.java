package plugin.classes;
import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public enum TaskStatus implements Serializable {
	@SerializedName("Active") Active,
	@SerializedName("Available") Available,
	@SerializedName("Unnavailable") Unnavailable,
	@SerializedName("Finished") Finished,
	@SerializedName("Completed") Completed;
}