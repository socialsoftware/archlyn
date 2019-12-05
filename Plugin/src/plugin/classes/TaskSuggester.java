package plugin.classes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TaskSuggester {
	
	private static int descriptionNumber = 0;

	public TaskSuggester() {
	}

	public void suggest() {
		Share.status.getNewTasks().clear();
		if(Share.status.getActiveActivity() == null && Share.status.getTaskContext().isEmpty()) {
			ArrayList<Task> tc = new ArrayList<Task>();
			Activity activity = new Activity(tc);
			Share.status.getTaskContext().add(activity);
			Share.status.setActiveActivity(activity);
		}
		if(Share.status.getActiveActivity() == null) {
			Share.status.setActiveActivity(Share.status.getTaskContext().get(0));
		}

		new ProjectCreator().applyMetadataTemplate(Share.status.getActiveActivity().getProjectType(),true);
		Share.project.saveStatus();

		System.out.println("Suggested tasks:\n");
		Set<Task> newTasksSet = new LinkedHashSet<>(suggestExisting());
		Set<Task> oldTasksSet = new LinkedHashSet<>(Share.status.getActiveActivity().getTasks());
		Boolean toAdd = true;
		for (Task nt: newTasksSet) {
			for (Task ot: oldTasksSet) {
				if (ot.getProduced() != null && nt.getProduced() != null && !nt.getProduced().isEmpty() &&
						ot.getProduced().get(0).getPath().equals(nt.getProduced().get(0).getPath())) {
					toAdd = false;
					break;
				}
			}
			if(toAdd) {
				oldTasksSet.add(nt);
				Share.status.getActiveActivity().getTasks().add(nt);
				Share.status.getNewTasks().add(nt);
			}
		}					
		Share.project.saveStatus();

		newTasksSet = new LinkedHashSet<>(suggestNew());
		oldTasksSet = new LinkedHashSet<>(Share.status.getActiveActivity().getTasks());
		toAdd = true;
		for (Task nt: newTasksSet) {
			for (Task ot: oldTasksSet) {
				if (ot.getProduced() != null && nt.getProduced() != null && !nt.getProduced().isEmpty() &&
						ot.getProduced().get(0).getPath().equals(nt.getProduced().get(0).getPath())) {
					toAdd = false;
					break;
				}
			}
			if(toAdd) {
				oldTasksSet.add(nt);
				Share.status.getActiveActivity().getTasks().add(nt);
				Share.status.getNewTasks().add(nt);
			}
		}		
		Share.project.saveStatus();

		boolean newActivity = true;
		for(Task t: Share.status.getActiveActivity().getTasks()) {
			if(!t.getStatus().equals("Complete")) {
				newActivity = false;
				break;
			}
		}

		for(Activity a: Share.status.getTaskContext()) {
			if(a.getTasks().size() < 2) {
				newActivity = false;
				break;
			}
		}

		if(newActivity) {
			ArrayList<Task> tc = new ArrayList<Task>();
			Activity activity = new Activity(tc);
			Share.status.getTaskContext().add(activity);
		}

		Share.project.saveStatus();

		System.out.println(Arrays.toString(Share.status.getActivitiesSA()));
		System.out.println(Arrays.toString(Share.status.getTasksSA()));	
	}

	public List<Task> suggestExisting(){
		
		List<Task> tasks = new ArrayList<Task>();
		List<Dependency> dependencies = new ArrayList<Dependency>();		
		List<Artifact> used = new ArrayList<Artifact>();
		List<Artifact> produced = new ArrayList<Artifact>();

		for (Artifact a: this.getArtifactsInTaskContext(Share.status.getActiveActivity().getTasks())) {
			if(!a.getComplete()) {
				System.out.println("Product " + a);	

				for (Dependency d: Share.status.getDependencies()){
					if(d.getTarget().getName().equals(a.getName()) && d.getOrigin().getComplete()) {
						dependencies.add(d);
					}
				}
				System.out.println(Arrays.toString(dependencies.toArray()));

				for (TaskType tt: Share.status.getTaskTypes()){
					for (Dependency d: dependencies) {
						for(DependencyType dt: tt.getDependencies()){
							System.out.println(d + " " + dt);
							if (d.getDependency().getDependency().equals(dt.getDependency())){
								System.out.println("match");
								used.add(d.getOrigin());
							}
						}
					}
					if (used.size() == tt.getDependencies().size()){
						System.out.println(Arrays.toString(used.toArray()));
						produced.add(a);
						Task toAdd = new Task(new ArrayList<Artifact>(used),new ArrayList<Artifact>(produced),tt,descriptionNumber+": "+produced.get(0).getName()+" - " + tt.getDescription());
						descriptionNumber++;
						toAdd.setStatus("Available");
						tasks.add(toAdd);
						used.clear();
						produced.clear();
						dependencies.clear();
						System.out.println(tasks);
						break;
					}
				}
			}
		}

		return tasks;
	}

	public List<Task> suggestNew(){
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		ArrayList<Dependency> dependencies = new ArrayList<Dependency>();		
		ArrayList<Artifact> used = new ArrayList<Artifact>();
		ArrayList<Artifact> produced = new ArrayList<Artifact>();
		if(!Share.status.getActiveActivity().getTasks().isEmpty()) {
			for (Artifact a: this.getArtifactsInTaskContext(Share.status.getActiveActivity().getTasks())) {
				System.out.println("Origin " + a);	
				Boolean foundTask = false;
				for (TaskType tt: Share.status.getTaskTypes()){
					System.out.println("TT " + tt);
					foundTask = false;
					for(ArtifactType atu: tt.getUsed()){
						if(atu.getType().equals(a.getType().getType())) {
							for (Task t: Share.status.getActiveActivity().getTasks()){
								System.out.println("T" + t);
								if(t.getTaskType().getDescription().equals(tt.getDescription())){
									for (Artifact au: t.getUsed()){
										if(au.getPath().equals(a.getPath())) {
											System.out.println("found T");
											foundTask = true;
										}
									}									
								}						
							}
							if(!foundTask) {
								System.out.println("not found T");
								for(ArtifactType at: tt.getUsed()) {
									if(at.getType().equals(a.getType().getType())) {
										used.add(a);
									}
									else {
										for(Artifact ua: this.getArtifactsInTaskContext(Share.status.getActiveActivity().getTasks())) {
											System.out.println(at + "" +ua.getType());
											if(ua.getType().getType().equals(at.getType()) && ua.getComplete()) {
												System.out.println("adding " + ua);
												used.add(ua);
												break;
											}
										}
									}
								}
								System.out.println("check size " + Arrays.toString(used.toArray()));
								if (used.size() == tt.getDependencies().size()){
									System.out.println(Arrays.toString(used.toArray()));
									Task toAdd = new Task(new ArrayList<Artifact>(used),new ArrayList<Artifact>(produced),tt,descriptionNumber+": "+tt.getProduced().get(0).getType()+" - " + tt.getDescription());
									descriptionNumber++;
									toAdd.setStatus("Available");
									tasks.add(toAdd);									
									used.clear();
									produced.clear();
									dependencies.clear();
									break;
								}	
							}
							used.clear();
							produced.clear();
							dependencies.clear();
						}
					}
				}
			}
		}
		else {
			for(TaskType tt: Share.status.getTaskTypes()) {
				if(tt.getUsed().get(0).getType().equals("Start")) {
					used.add(Share.status.getArtifacts().get(0));
					Task toAdd = new Task(new ArrayList<Artifact>(used),new ArrayList<Artifact>(produced),tt,descriptionNumber+": "+tt.getProduced().get(0).getType()+" - " + tt.getDescription());
					descriptionNumber++;
					toAdd.setStatus("Available");
					tasks.add(toAdd);
					break;
				}
			}
			used.clear();
			produced.clear();
			dependencies.clear();
		}
		return tasks;
	}

	private ArrayList<Artifact> getArtifactsInTaskContext(ArrayList<Task> alt){
		ArrayList<Artifact> artifacts = new ArrayList<Artifact>();
		for(Task t: alt) {
			if(!t.getProduced().isEmpty())
				if(t.getProduced().get(0).getComplete()) {
					artifacts.add(t.getProduced().get(0));
				}
		}
		artifacts.add(Share.status.getArtifacts().get(0));
		return artifacts;
	}
	
	/*
	private boolean multipleDependencies(TaskType tt) {
		for(DependencyType dt: tt.getDependencies()) {
			if(dt.getMultiplicity() == 0){
				for(Task t: Share.status.getTasks()) {
					if(t.getTaskType().getDescription().equals(tt.getDescription()) && t.getStatus().equals("Available")){
						return false;
					}
				}
				return true;
			}
		}
		return false;		
	}
	*/

	/*
	private boolean needNewStartTask(TaskType tt, ArrayList<Task> tasks) {
		if(tt.getUsed().get(0).getType().equals("Start")) {
			for(Task t: Share.status.getTasks()) {
				if(t.getTaskType().getDescription().equals(tt.getDescription()) && t.getStatus().equals("Available")){
					return false;
				}
			}
			for(Task t: tasks) {
				if(t.getTaskType().getDescription().equals(tt.getDescription()) && t.getStatus().equals("Available")){
					return false;
				}
			}
			return true;
		}
		return false;		
	}*/
}