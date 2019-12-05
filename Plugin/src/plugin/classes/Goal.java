package plugin.classes;
import java.util.ArrayList;
import java.util.Date;

public class Goal {
	
	private ArrayList<String> activityNames;
	private String description;
	private Date due;
	private String status;
	
	public Goal(String description, ArrayList<String> activityNames, Date due) {
		this.activityNames = activityNames;
		this.description = description;
		this.due = due;
		this.status = "open";
	}

	public ArrayList<String> getActivityNames() {
		return activityNames;
	}

	public void setActivityNames(ArrayList<String> activityNames) {
		this.activityNames = activityNames;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDue() {
		return due;
	}

	public void setDue(Date due) {
		this.due = due;
	}

	public String toString() {
		return description;
	}
}