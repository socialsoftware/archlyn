package plugin.classes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.resources.IProject;

public class Status {
	
	private List<Artifact> artifacts;
	private List<ArtifactType> artifactTypes;
	private List<Task> newTasks;
	private List<Activity> taskContext;
	private String projectType;
	private List<TaskType> taskTypes;
	private List<Dependency> dependencies;
	private List<DependencyType> dependencyTypes;
	private List<Component> components;
	private List<Goal> goals;
	private List<Rule> rules;
	private List<RuleType> ruleTypes;
	private List<CustomMetadata> customMetadata;
	private Activity activeActivity;
	private Task activeTask;
	private String projectLocation;
	private int newArtifactNumber;
	private transient IProject project;
	private transient GithubConnector github;


	public Status(Project currentProject) {
		this.artifacts = new ArrayList<Artifact>();
		this.artifactTypes = new ArrayList<ArtifactType>();
		this.newTasks = new ArrayList<Task>();
		this.taskContext = new  ArrayList<Activity>();
		this.taskTypes = new ArrayList<TaskType>();
		this.dependencies = new ArrayList<Dependency>();
		this.dependencyTypes = new ArrayList<DependencyType>();
		this.components = new ArrayList<Component>();
		this.goals = new ArrayList<Goal>();
		this.rules = new ArrayList<Rule>();
		this.ruleTypes = new ArrayList<RuleType>();	
		this.customMetadata =  new ArrayList<CustomMetadata>();	
		this.activeTask = null;
		this.activeTask = null;
		this.project = currentProject.getProject();
		this.projectLocation = project.getLocation().toString();
		this.github = new GithubConnector();
		this.newArtifactNumber = 0;
	}

	public void Innitialize(String projectLocationPlugin) {		
		ArtifactType ATstart = new ArtifactType("Start",".start");
		artifactTypes.add(ATstart);		
		artifacts.add(new Artifact(Paths.get(projectLocationPlugin+"/Start"),ATstart));
		rescanArtifacts();
	}

	public void quickPrint() {

		System.out.println("\n" + "Created Artifact types: " + "\n");
		for (ArtifactType at : getArtifactTypes()) {
			System.out.println(at.getType());
		}

		System.out.println("\n" + "Created Task types: " + "\n");
		for (TaskType t : getTaskTypes()) {
			System.out.println(t.toString());
		}		

		System.out.println("\n" + "Created Dependency types: " + "\n");
		for (DependencyType d : getDependencyTypes()) {
			System.out.println(d.toString());
		}

		System.out.println("\n" + "Created Artifacts: " + "\n");
		for (Artifact a : getArtifacts()) {
			System.out.println(a.toString() + " of Type " + a.getType().getType());
		}

		System.out.println("\n" + "Created Dependencies: " + "\n");
		for (Dependency d: getDependencies()) {
			System.out.println(d.toString());
		}

		System.out.println("\n" + "Created Components: " + "\n");
		for (Component c: getComponents()) {
			System.out.println(c.toString() + " of Type " + c.getType().getType());
		}

		System.out.println("\n" + "Created Goals: " + "\n");
		for (Goal g: getGoals()) {
			System.out.println(g.toString());
		}

		System.out.println("\n" + "Created Rules: " + "\n");
		for (Rule r: getRules()) {
			System.out.println(r.toString());
		}

		System.out.println("\n" + "Created RuleTypes: " + "\n");
		for (RuleType rt: getRuleTypes()) {
			System.out.println(rt.toString());
		}
	}

	public void rescanArtifacts() {

		List<Artifact> artifactScan = new ArrayList<Artifact>();

		try (Stream<Path> walk = Files.walk(Paths.get(projectLocation))) {

			artifactScan  = walk.filter(Files::isRegularFile)
					.filter(f -> !f.getFileName().toString().equals("Status.json"))
					.filter(f -> !f.getFileName().toString().equals(".gitignore"))
					.filter(f -> !f.getFileName().toString().equals(".project"))
					.filter(f -> !f.getFileName().toString().equals(".classpath"))
					.filter(f -> !f.toString().contains("bin"))
					.filter(f -> !f.toString().contains(".settings"))
					.map(x -> new Artifact(x))
					.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(Arrays.toString(artifactScan.toArray()));

		List<Artifact> artifactsToDelete = new ArrayList<Artifact>(artifacts);

		Boolean found = false;
		for(Artifact na: artifactScan) {
			for(Artifact oa: artifacts) {
				if(na.getPath().equals(oa.getPath())) {
					found = true;
					artifactsToDelete.remove(oa);
					break;
				}
			}
			if(!found) {
				artifacts.add(na);
			}
			found = false;			
		}	

		for(Artifact ad: artifactsToDelete) {
			if(!ad.getName().equals("Start")) {
				System.out.println("removing"+ad);
				for(Dependency d: findDependenciesContaining(ad.getPath())){
					dependencies.remove(d);
				}
				artifacts.remove(ad);				
			}
		}

		List<Component> componentScan = new ArrayList<Component>();

		try (Stream<Path> walk = Files.walk(Paths.get(projectLocation))) {

			componentScan = walk.filter(Files::isDirectory)
					.filter(f -> !f.toString().contains("bin"))
					.filter(f -> !f.getFileName().toString().equals("src"))
					.filter(f -> !f.getFileName().toString().equals("bin"))
					.filter(f -> !f.getFileName().toString().equals(".settings"))
					.filter(f -> !f.getFileName().toString().equals(project.getName()))
					.map(x -> new Component(x))
					.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}

		Boolean foundC = true;
		for (Artifact a: artifacts) {
			for (Component c: components) {
				if(c.getPath().equals(a.getComponentPath())){
					if(!c.getArtifacts().contains(a)) {
						c.getArtifacts().add(a);
					}					
					foundC = true;
					break;
				}
			}
			if(foundC) {
				foundC = false;
			}
			else {
				if(!a.getName().equals("Status.json") && !a.getName().equals("Start")) {
					Component component = new Component(Paths.get(Share.project.getLocationBase()+a.getComponentPath()));
					component.getArtifacts().add(a);
					components.add(component);
				}

			}
		}

		found = false;
		for(Component na: componentScan) {
			for(Component oa: components) {
				if(na.getPath().equals(oa.getPath())) {
					found = true;
					break;
				}
			}
			if(!found) {
				components.add(na);
			}
			found = false;			
		}

	}


	public Task getActiveTask() {
		return activeTask;
	}

	public void setActiveTask(Task activeTask) {
		this.activeTask = activeTask;
	}

	public String[] getArtifactsSA() {
		List<String> artifacts = this.artifacts.stream()
				.map(Artifact::getName)
				.collect(Collectors.toList());	
		return artifacts.toArray(new String[0]);
	}

	public String[] getArtifactsPathsSA() {
		List<String> artifacts = this.artifacts.stream()
				.map(Artifact::getPath)
				.collect(Collectors.toList());		
		return artifacts.toArray(new String[0]);
	}

	public List<Artifact> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(List<Artifact> artifacts) {
		this.artifacts = artifacts;
	}

	public String[] getArtifactTypesSA() {
		List<String> artifactTypes = this.artifactTypes.stream()
				.map(ArtifactType::getType)
				.collect(Collectors.toList());	
		return artifactTypes.toArray(new String[0]);
	}

	public List<ArtifactType> getArtifactTypes() {
		return artifactTypes;
	}

	public void setArtifactTypes(List<ArtifactType> artifactType) {
		this.artifactTypes = artifactType;
	}

	public List<TaskType> getTaskTypes() {
		return taskTypes;
	}

	public void setTaskTypes(List<TaskType> taskTypes) {
		this.taskTypes = taskTypes;
	}

	
	public List<Task> getTasks() {
		List<Task> tasks = new ArrayList<Task>();
		for(Activity a: this.getTaskContext()) {
			for(Task t: a.getTasks()) {
				tasks.add(t);
			}
		}
		return tasks;
	}

	public List<Dependency> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<Dependency> dependencies) {
		this.dependencies = dependencies;
	}

	public String[] getDependencyTypesSA() {
		List<String> dependencyTypes = this.dependencyTypes.stream()
				.map(DependencyType::getDependency)
				.collect(Collectors.toList());	
		return dependencyTypes.toArray(new String[0]);
	}	

	public List<Dependency> findDependenciesContaining(String artifactPath) {
		List<Dependency> dependenciesContaining = this.dependencies.stream()
				.filter(d -> d.getOrigin().getPath().equals(artifactPath) || d.getTarget().getPath().equals(artifactPath))
				.collect(Collectors.toList());		
		return dependenciesContaining;
	}

	public List<DependencyType> getDependencyTypes() {
		return dependencyTypes;
	}

	public void setDependencyTypes(List<DependencyType> dependencyTypes) {
		this.dependencyTypes = dependencyTypes;
	}

	public List<Component> getComponents() {
		return components;
	}

	public String[] getComponentsSA() {
		List<String> components = this.components.stream()
				.map(Component::getName)
				.collect(Collectors.toList());	
		return components.toArray(new String[0]);
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	public List<Goal> getGoals() {
		return goals;
	}

	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}

	public String[] getTasksSA(){
		List<String> tasks = this.getActiveActivity().getTasks().stream()
				.map(Task::getDescription)
				.collect(Collectors.toList());
		return tasks.toArray(new String[0]);
	}	

	public List<Task> getTasksOfStatus(List<Task> tasks, String status){
		List<Task> filteredTasks = null;

		if(status.equals("All")) {
			filteredTasks = tasks;
		}
		else if(status.equals("All but Complete")) {
			filteredTasks = tasks.stream()
					.filter(t -> !t.getStatus().equals("Complete"))
					.collect(Collectors.toList());	
		}
		else if(status.equals("New")) {
			filteredTasks = this.getNewTasks();
		} else {
			filteredTasks = tasks.stream()
					.filter(t -> t.getStatus().equals(status))
					.collect(Collectors.toList());	
		}
		return filteredTasks;
	}

	public List<Task> getCompletedContextTasksUsing(Artifact used){
		List<Task> tasksUsing = new ArrayList<Task>();
		for(Task t: this.getActiveActivity().getTasks()){
			for(Artifact a: t.getUsed()) {
				if(a.getPath().equals(used.getPath()) && t.getStatus().equals("Complete")){
					tasksUsing.add(t);
					break;
				}
			}
		}
		if(this.getTaskContext().size() > 1) {
			for(Activity act: this.getTaskContext()) {
				if(act.getName() != this.getActiveActivity().getName()) {
					for(Task t: act.getTasks()){
						for(Artifact a: t.getUsed()) {
							if(a.getPath().equals(used.getPath()) && t.getStatus().equals("Complete")){
								boolean found = false;
								for(Task tu: tasksUsing){
									if(t.getProduced().get(0).getFullPath().equals(tu.getProduced().get(0).getFullPath())){
										found = true;
									}
								}
								if(!found) {
								tasksUsing.add(t);
								break;
								}
							}
						}
					}
				}
			}
		}
		return tasksUsing;		
	}

	public ArtifactType findArtifactType(String text) {
		for (ArtifactType at: artifactTypes) {
			if(at.getType().equals(text)) {
				return at;
			}
		}
		return null;
	}

	public DependencyType findDependencyType(String text) {
		for (DependencyType dt: dependencyTypes) {
			if(dt.getDependency().equals(text)) {
				return dt;
			}
		}
		return null;
	}

	public Dependency findDependency(String origin,String target, String dependency) {
		for (Dependency d: dependencies) {
			if(d.getOrigin().getName().equals(origin) &&
					d.getTarget().getName().equals(target) &&
					d.getDependency().getDependency().equals(dependency)) {
				return d;
			}
		}
		return null;
	}

	public TaskType findTaskType(String text) {
		for (TaskType tt: taskTypes) {
			if(tt.getDescription().equals(text)) {
				return tt;
			}
		}
		return null;
	}

	public Task findTask(String text) {
		for (Task t: this.getActiveActivity().getTasks()) {
			if(t.getDescription().equals(text)) {
				return t;
			}
		}
		return null;
	}

	public ArrayList<Task> findTasks(Goal g) {
		ArrayList<Task> tasks = new ArrayList<Task>();
		for(String s: g.getActivityNames()) {
			Activity a = Share.status.findActivity(s);
			if (a != null) {
				for (Task t: a.getTasks()) {			
						tasks.add(t);
				}
			}
		}
		return tasks;
	}

	public Artifact findArtifact(String text) {
		for (Artifact a: artifacts) {
			if(a.getPath().equals(text)) {
				return a;
			}
		}
		return null;
	}

	public Component findComponent(String text) {
		for (Component c: Share.status.getComponents()) {
			if(c.getPath().equals(text)) {
				return c;
			}
		}
		return null;
	}

	public Component findDefaultComponentForAT(String text) {
		for (Component c: Share.status.getComponents()) {
			if(c.getType().getType().equals(text) && c.isDefaultForType()) {
				return c;
			}
		}
		return Share.status.findComponent("/"+Share.pluginFolderName+"/package"+text);
	}

	public Goal findGoal(String text) {
		for (Goal g: Share.status.getGoals()) {
			if(g.getDescription().equals(text)) {
				return g;
			}
		}
		return null;
	}

	public Goal findGoal(Activity a) {
		for (Goal g: Share.status.getGoals()) {
			for (String s: g.getActivityNames()) {
				if(a.getName().equals(s)){
					return g;
				}
			}
		}
		return null;
	}	

	public String getProjectLocation() {
		return projectLocation;
	}

	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}

	public GithubConnector getGithub() {
		return github;
	}

	public void setGithub(GithubConnector github) {
		this.github = github;
	}

	public int getNewArtifactNumber() {
		return newArtifactNumber;
	}

	public void setNewArtifactNumber(int newArtifactNumber) {
		this.newArtifactNumber = newArtifactNumber;
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public List<RuleType> getRuleTypes() {
		return ruleTypes;
	}

	public String[] getRuleTypesSA() {
		List<String> rules = this.ruleTypes.stream()
				.map(RuleType::getName)
				.collect(Collectors.toList());	
		return rules.toArray(new String[0]);
	}

	public RuleType findRuleType(String text) {
		for (RuleType rt: ruleTypes) {
			if(rt.getName().equals(text)) {
				return rt;
			}
		}
		return null;
	}

	public Rule findRule(String at, String rt) {
		for (Rule r: rules) {
			if(r.getArtifactType().getType().equals(at) && r.getRuleType().getName().equals(rt)) {
				return r;
			}
		}
		return null;
	}

	public void setRuleTypes(List<RuleType> ruleTypes) {
		this.ruleTypes = ruleTypes;
	}

	public List<Task> getNewTasks() {
		return newTasks;
	}

	public void setNewTasks(List<Task> newTasks) {
		this.newTasks = newTasks;
	}

	public List<Activity> getTaskContext() {
		return taskContext;
	}

	public void setTaskContext(List<Activity> taskContext) {
		this.taskContext = taskContext;
	}

	public Activity getActiveActivity() {
		return activeActivity;
	}

	public void setActiveActivity(Activity activity) {
		this.activeActivity = activity;
	}

	public Activity findActivity(String name) {
		for(Activity a: this.getTaskContext()) {
			if(a.getName().equals(name)) {
				return a;
			}
		}			
		return null;
	}

	public String[] getActivitiesSA() {
		List<String> activitiesSA = this.taskContext.stream()
				.map(Activity::getName)
				.collect(Collectors.toList());	
		return activitiesSA.toArray(new String[0]);
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	
	

	public List<CustomMetadata> getCustomMetadata() {
		return customMetadata;
	}

	public void setCustomMetadata(List<CustomMetadata> customMetadata) {
		this.customMetadata = customMetadata;
	}
	
	public String[] getCustomMetadataSA() {
		List<String> customMetadata = this.customMetadata.stream()
				.map(CustomMetadata::getName)
				.collect(Collectors.toList());		
		return customMetadata.toArray(new String[0]);
	}

	public void fixReferences() {

		//A
		for (Artifact a: artifacts) {	
			for(ArtifactType at: artifactTypes) {
				if(at.getType().equals(a.getType().getType())) {
					a.setType(at);
				}
			}
		}

		//DT
		for (DependencyType dt: dependencyTypes) {
			for(ArtifactType at: artifactTypes) {
				if(dt.getOriginAT().getType().equals(at.getType())) {
					dt.setOriginAT(at);
					break;
				}
			}
			for(ArtifactType at: artifactTypes) {
				if(dt.getTargetAT().getType().equals(at.getType())) {
					dt.setTargetAT(at);
					break;
				}
			}
		}			

		//TT
		for (TaskType tt: taskTypes) {
			
			List<ArtifactType> used = new ArrayList<ArtifactType>();
			for(ArtifactType at1: tt.getUsed()) {
				for(ArtifactType at2: artifactTypes) {
					if(at1.getType().equals(at2.getType())) {
						used.add(at2);
					}
				}
			}
			tt.setUsed(new ArrayList<ArtifactType>(used));
			used.clear();
			List<ArtifactType> produced = new ArrayList<ArtifactType>();
			for(ArtifactType at1: tt.getProduced()) {
				for(ArtifactType at2: artifactTypes) {
					if(at1.getType().equals(at2.getType())) {
						produced.add(at2);
					}
				}
			}
			tt.setProduced(new ArrayList<ArtifactType>(produced));
			produced.clear();
			List<DependencyType> dependencies = new ArrayList<DependencyType>();
			for(DependencyType dt1: tt.getDependencies()) {
				for(DependencyType dt2: dependencyTypes) {
					if(dt1.getDependency().equals(dt2.getDependency())) {
						dependencies.add(dt2);
					}
				}
			}
			tt.setDependencies(new ArrayList<DependencyType>(dependencies));
			dependencies.clear();
		}

		//R
		for (Rule r: rules) {
			for(ArtifactType at: artifactTypes) {
				if(r.getArtifactType().getType().equals(at.getType())) {
					r.setArtifactType(at);
					break;
				}
			}
			for(RuleType rt: ruleTypes) {
				if(r.getRuleType().getName().equals(rt.getName())) {
					r.setRuleType(rt);
					break;
				}
			}
		}

		//C
		for (Component c: components) {
			ArrayList<Artifact> artifacts = new ArrayList<Artifact>();
			for (Artifact artifact: this.artifacts) {				
				for(Artifact a: c.getArtifacts()) {
					if(a.getPath().equals(artifact.getPath())) {
						artifacts.add(artifact);
					}
				}
				c.setArtifacts(new ArrayList<Artifact>(artifacts));
				artifacts.clear();
			}
			for(ArtifactType at: artifactTypes) {
				if(c.getType().getType().equals(at.getType())) {
					c.setTypeSelfOnly(at);
				}
			}
		}

		//D
		for (Dependency d: dependencies) {
			for (Artifact artifact: this.artifacts) {
				if(d.getOrigin().getPath().equals(artifact.getPath())) {
					d.setOrigin(artifact);
					break;
				}
			}
			for (Artifact artifact: this.artifacts) {
				if(d.getTarget().getPath().equals(artifact.getPath())) {
					d.setTarget(artifact);
					break;
				}
			}
			for(DependencyType dt: dependencyTypes) {
				if(d.getDependency().getDependency().equals(dt.getDependency())) {
					d.setDependency(dt);
					break;
				}
			}
		}

		//T
		
		for (Task t: this.getTasks()) { 
			if(!t.getProduced().isEmpty()) {
				ArrayList<Artifact> artifactsP = new ArrayList<Artifact>();
				for (Artifact artifact: this.artifacts) {						
					for(Artifact a: t.getProduced()) {
						if(a.getPath().equals(artifact.getPath())) {
							artifactsP.add(artifact);
						}
					}
				}
				t.setProduced(new ArrayList<Artifact>(artifactsP));
				artifactsP.clear();
			}
			ArrayList<Artifact> artifactsU = new ArrayList<Artifact>();
			for (Artifact artifact: this.artifacts) {				
				for(Artifact a: t.getUsed()) {
					if(a.getPath().equals(artifact.getPath())) {
						artifactsU.add(artifact);
					}
				}
			}
			t.setUsed(new ArrayList<Artifact>(artifactsU));
			artifactsU.clear();
			for(TaskType tt: taskTypes) {
				if(tt.getDescription().equals(t.getTaskType().getDescription())){
					t.setTaskType(tt);
				}
			}
		}
		 
		//active activity
		if(this.getActiveActivity() != null){
			for(Activity a: taskContext) {
				if(this.getActiveActivity().getName().equals(a.getName())){
					this.setActiveActivity(a);
					break;
				}
			}
		}else {
			//this.setActiveActivity(this.taskContext.get(0));
		}
		
		//active task
		if(this.getActiveTask() != null){
			for(Task t: this.getActiveActivity().getTasks()) {
				if(t.getDescription().equals(getActiveTask().getDescription())) {
					this.setActiveTask(t);
					break;
				}
			}
		}else {
			//this.setActiveTask(this.taskContext.get(0).getTasks().get(this.taskContext.get(0).getTasks().size()));
		}
		
		/*
		for(Activity a: taskContext) {
			System.out.println(a.getName());
			System.out.println(Arrays.toString(a.getTasks().toArray(new String[0])));
		}*/
	}

	public void setReactivated(Artifact changed) {
		if(changed != null) {
			for(Task t: this.getCompletedContextTasksUsing(changed)) {
				changed.setComplete(false);
				t.setStatus("To Redo");
				//setReactivated(t.getProduced().get(0));
			}			
		}		
	}
}