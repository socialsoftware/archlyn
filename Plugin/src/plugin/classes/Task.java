package plugin.classes;
import java.util.List;

public class Task {
	
	private List<Artifact> used;
	private List<Artifact> produced;
	private TaskType taskType;
	private String description;
	private String status;
	private String user;
	private String userStatus;
	private static String[] possibleStatus 
		= new String[] {"Unnavailable","To Redo","Available","Active","In Progress","Complete"};
	private static String[] possibleFilters 
		= new String[] {"All","All but Complete","New","Unnavailable","To Redo","Available","Active","In Progress","Complete"};
	private static String activeFilter = "All";
	
	public Task(List<Artifact> used, List<Artifact> produced, TaskType taskType, 
			String description ) {
		
		this.setUsed(used);
		this.setProduced(produced);
		this.setTaskType(taskType);
		this.setStatus("Unnavailable");
		this.setDescription(description);
	}
	
	public static boolean completeTask(Task task, Boolean redo) {
		int canComplete = 0;	
		for(DependencyType dt: task.getTaskType().getDependencies()) {
			int count = 0;
			if(dt.getMultiplicity() == 0) {
				for(Artifact a: task.getProduced()) {
					if(a.getType().getType().equals(dt.getTargetAT().getType())) {
						count++;
					}
				}
				if(count > 0) {  //assuming * is 1..*
					canComplete++;
				}
			}
			else {			
				for(Artifact a: task.getProduced()) {
					if(a.getType().getType().equals(dt.getTargetAT().getType())) {
						count++;
					}
				}
				if(count >= dt.getMultiplicity()) {  
					canComplete++;
				}
			}			
		}
		if(canComplete == task.getTaskType().getDependencies().size()) {
			for(Artifact a: task.getProduced()) {
				a.setComplete(true);
			}
			task.setStatus("Complete");
			if(!Share.status.getRules().isEmpty()) {
				Rule r = Share.status.findRule(task.getProduced().get(0).getType().getType(),Share.status.getRuleTypes().get(0).getName());
				if (r != null) {
					task.setStatus("In Progress");
				}
			}
			Share.status.setActiveTask(null);
			if(redo) {
				Share.status.setReactivated(task.getProduced().get(0));
			}			
			if(Share.status.getGithub() != null && Share.status.getGithub().getClient() != null) {
				Share.status.getGithub().updateIssue(task);
			}
			Share.project.saveStatus();	
			new TaskSuggester().suggest();
			return true;
		}
		return false;
	}
	
	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String string) {
		this.status = string;
	}

	public String toString() {
		return this.description;		
	}

	public List<Artifact> getUsed() {
		return used;
	}

	public void setUsed(List<Artifact> used) {
		this.used = used;
	}

	public List<Artifact> getProduced() {
		return produced;
	}

	public void setProduced(List<Artifact> produced) {
		this.produced = produced;
	}	
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public static String[] getPossibleStatus() {
		return possibleStatus;
	}

	public static void setPossibleStatus(String[] possibleStatus) {
		Task.possibleStatus = possibleStatus;
	}

	public static String[] getPossibleFilters() {
		return possibleFilters;
	}

	public static void setPossibleFilters(String[] possibleFilters) {
		Task.possibleFilters = possibleFilters;
	}

	public static String getActiveFilter() {
		return activeFilter;
	}

	public static void setActiveFilter(String activeFilter) {
		Task.activeFilter = activeFilter;
	}
}