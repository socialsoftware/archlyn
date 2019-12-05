package plugin.classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;

public class ProjectCreator {
	
	private ArrayList<String> projectTypes;
	private String projectType = null;
	private String projectName = null;
	private Project project;
	private Status status;
	private Boolean newProject = true;
	
	public ProjectCreator(){
		projectTypes = new ArrayList<String>();
		projectTypes.add("X");
		projectTypes.add("Empty");	
		projectTypes.add("ES_TopDown");
		projectTypes.add("ES_BottomUp");
		projectTypes.add("ES_BottomUp_TestFirst");
		projectTypes.add("Validation");
	}
	
	public void createProject(String projectType, String projectName) {
	
		IProject iproject = null;
		this.projectName = projectName;
		
		if(projectType.equals("X")) {
			projectType = "X";
			iproject = createProjectTypeX();
			
		} else if(projectType.equals("Empty")) {
			projectType = "Empty";
			iproject = createProjectTypeEmpty();
			
		} else if(projectType.equals("ES_TopDown")){
			projectType = "ES_TopDown";
			iproject = createProjectTypeES();
		} else if(projectType.equals("ES_BottomUp")){
			projectType = "ES_BottomUp";
			iproject = createProjectTypeES();		
		} else if(projectType.equals("ES_BottomUp_TestFirst")){
			projectType = "ES_BottomUp_TestFirst";
			iproject = createProjectTypeES();
		} else if(projectType.equals("Validation")){
			projectType = "Validation";
			iproject = createProjectTypeValidation();
		}
		
		Share.project = new Project(iproject);
		project = Share.project;
		File f = new File(iproject.getLocation()+"/Status.json");
		Share.status = new Status(project);
		status = Share.status;
		status.setProjectType(projectType);
		applyMetadataTemplate(projectType,false);
		applyDataTemplate(projectType,false);
		new JsonTranslator().write(new File(iproject.getLocation()+"/Status.json"), status);	
		try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(org.eclipse.core.resources.IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private IProject createProjectTypeX() {
		
		IJavaProject javaProject = createProjectBase();		
		
		try {
			
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);			
			IPackageFragment packC = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("packageClass", false, null);
			StringBuffer buffer = new StringBuffer();
			buffer.append("package " + packC.getElementName() + ";\n");
			buffer.append("\n");
			buffer.append("public class Class1 {");
			buffer.append("\n");
			buffer.append("}");
			ICompilationUnit cuClass1 = packC.createCompilationUnit("Class1.java", buffer.toString(), false, null);
			buffer = new StringBuffer();
			buffer.append("package " + packC.getElementName() + ";\n");
			buffer.append("\n");
			buffer.append("public class Class2 {");
			buffer.append("\n");
			buffer.append("}");
			ICompilationUnit cuClass2 = packC.createCompilationUnit("Class2.java", buffer.toString(), false, null);
			IPackageFragment packF = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("packageFinal", false, null);
			IPackageFragment packS = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("packageStory", false, null);
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(project.getFolder(Share.pluginFolderName).getLocation()+"/packageStory/"+"Story1.txt"), "utf-8"));
			writer.write("story1");
			writer.close();
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(project.getFolder(Share.pluginFolderName).getLocation()+"/packageStory/"+"Story2.txt"), "utf-8"));
			writer.write("story2");		
			writer.close();
			IPackageFragment packT = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("packageTest", false, null);
			buffer = new StringBuffer();
			buffer.append("package " + packT.getElementName() + ";\n");
			buffer.append("\n");
			buffer.append("import static org.junit.jupiter.api.Assertions.*;");
			buffer.append("\n");
			buffer.append("import org.junit.jupiter.api.Test;");
			buffer.append("\n");
			buffer.append("class Test1 {");
			buffer.append("\n");
			buffer.append("@Test");
			buffer.append("\n");
			buffer.append("void test() {");
			buffer.append("\n");
			buffer.append("fail(\"Not yet implemented\");");
			buffer.append("}");
			buffer.append("}");
			ICompilationUnit cuTest1 = packT.createCompilationUnit("Test1.java", buffer.toString(), false, null);
			buffer = new StringBuffer();
			buffer.append("package " + packT.getElementName() + ";\n");
			buffer.append("\n");
			buffer.append("import static org.junit.jupiter.api.Assertions.*;");
			buffer.append("\n");
			buffer.append("import org.junit.jupiter.api.Test;");
			buffer.append("\n");
			buffer.append("class Test2 {");
			buffer.append("\n");
			buffer.append("@Test");
			buffer.append("\n");
			buffer.append("void test() {");
			buffer.append("\n");
			buffer.append("fail(\"Not yet implemented\");");
			buffer.append("}");
			buffer.append("}");
			ICompilationUnit cuTest2 = packT.createCompilationUnit("Test2.java", buffer.toString(), false, null);
			
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(org.eclipse.core.resources.IResource.DEPTH_INFINITE, null);
			
			return project;
			
		} catch (IOException | CoreException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private IProject createProjectTypeEmpty() {
		
		IJavaProject javaProject = createProjectBase();		
		
		try {
			
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(org.eclipse.core.resources.IResource.DEPTH_INFINITE, null);			
			return project;		
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private IProject createProjectTypeES() {
		
		IJavaProject javaProject = createProjectBase();		
		
		try {	
			
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);			
			IPackageFragment packBase1 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("main.dml", false, null);
			IPackageFragment packBase2 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel", false, null);
			IPackageFragment packBase3 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.domain", false, null);
			IPackageFragment packBase4 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.exception", false, null);
			IPackageFragment packBase5 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.presentation", false, null);
			IPackageFragment packBase6 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.services.local", false, null);
			IPackageFragment packBase7 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects", false, null);
			IPackageFragment packBase8 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.services.remote", false, null);
			IPackageFragment packBase9 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.services.remote.dataobjects", false, null);
			IPackageFragment packBase10 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.services.remote.exceptions", false, null);
			IPackageFragment packBase11 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("main.resources", false, null);
			IPackageFragment packBase12 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("main.resources.templates", false, null);
			IPackageFragment packBase13 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("test.groovy.pt.ulisboa.tecnico.softeng.hotel.domain", false, null);
			IPackageFragment packBase14 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("test.groovy.pt.ulisboa.tecnico.softeng.hotel.services.local", false, null);
			IPackageFragment packBase15 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("target", false, null);
			IPackageFragment packBase16 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("target.classes", false, null);
			IPackageFragment packBase17 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("main.resources.stories", false, null);
			IPackageFragment packTemp1 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("main.dml", false, null);
			IPackageFragment packTemp2 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel", false, null);
			IPackageFragment packTemp3 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.domain", false, null);
			IPackageFragment packTemp4 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.exception", false, null);
			IPackageFragment packTemp5 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.presentation", false, null);
			IPackageFragment packTemp6 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.services.local", false, null);
			IPackageFragment packTemp7 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects", false, null);
			IPackageFragment packTemp8 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.services.remote", false, null);
			IPackageFragment packTemp9 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.services.remote.dataobjects", false, null);
			IPackageFragment packTemp10 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("main.java.pt.ulisboa.tecnico.softeng.hotel.services.remote.exceptions", false, null);
			IPackageFragment packTemp11 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("main.resources", false, null);
			IPackageFragment packTemp12 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("main.resources.templates", false, null);
			IPackageFragment packTemp13 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("test.groovy.pt.ulisboa.tecnico.softeng.hotel.domain", false, null);
			IPackageFragment packTemp14 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("test.groovy.pt.ulisboa.tecnico.softeng.hotel.services.local", false, null);
			IPackageFragment packTemp15 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("target", false, null);
			IPackageFragment packTemp16 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("target.classes", false, null);
			IPackageFragment packTemp17 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("main.resources.stories", false, null);
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(org.eclipse.core.resources.IResource.DEPTH_INFINITE, null);
			
			return project;
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private IProject createProjectTypeValidation() {
		
		IJavaProject javaProject = createProjectBase();		
		
		try {
			
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);			
			IPackageFragment packBase1 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("version1", false, null);
			IPackageFragment packBase2 = javaProject.getPackageFragmentRoot(project.getFolder("src")).createPackageFragment("version2", false, null);
			IPackageFragment packTemp1 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("story", false, null);
			IPackageFragment packTemp2 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("domainClass", false, null);
			IPackageFragment packTemp3 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("domainTest", false, null);
			IPackageFragment packTemp4 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("interfaceClass", false, null);
			IPackageFragment packTemp5 = javaProject.getPackageFragmentRoot(project.getFolder(Share.pluginFolderName)).createPackageFragment("interfaceTest", false, null);
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(org.eclipse.core.resources.IResource.DEPTH_INFINITE, null);
			
			return project;
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	private IJavaProject createProjectBase() {
		try {			
			
			IJavaProject javaProject = null;
			
			if(newProject) {			
				
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IProject project = root.getProject(projectName);
				project.create(null);
				project.open(null);
				
				IProjectDescription description = project.getDescription();
				description.setNatureIds(new String[] { JavaCore.NATURE_ID });
				project.setDescription(description, null);
				
				javaProject = JavaCore.create(project); 
				
				IFolder binFolder = project.getFolder("bin");
				binFolder.create(false, true, null);
				javaProject.setOutputLocation(binFolder.getFullPath(), null);
				
				List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
				IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
				LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
				for (LibraryLocation element : locations) {
				 entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
				}
				
				//add libs to project class path
				javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
				
				IFolder sourceFolder = project.getFolder("src");
				sourceFolder.create(false, true, null);
				
				IPackageFragmentRoot fragroot = javaProject.getPackageFragmentRoot(sourceFolder);
				IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
				IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
				System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
				newEntries[oldEntries.length] = JavaCore.newSourceEntry(fragroot.getPath());
				javaProject.setRawClasspath(newEntries, null);	
				
				IFolder pluginFolder = project.getFolder(Share.pluginFolderName);
				pluginFolder.create(false, true, null);
				
			} else {
				
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IProject project = root.getProject(projectName);
				javaProject = JavaCore.create(project); 
				
				IFolder pluginFolder = project.getFolder(Share.pluginFolderName);
				pluginFolder.create(false, true, null);
			}		
			
			return javaProject;
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public void applyMetadataTemplate(String projectTypeToApply, boolean current) {
		
		if(current) {
			status = Share.status;
		}		
		if(projectTypeToApply.equals("X")) {
			applyMetadataTemplateProjectX();			
		} else if(projectTypeToApply.equals("Empty")) {
			applyMetadataTemplateProjectEmpty();
		} else if(projectTypeToApply.equals("ES_TopDown")) {
			applyMetadataTemplateProjectES_TD();			
		} else if(projectTypeToApply.equals("ES_BottomUp")) {
			applyMetadataTemplateProjectES_BU();
		} else if(projectTypeToApply.equals("ES_BottomUp_TestFirst")) {
			applyMetadataTemplateProjectES_BU_TF();
		} else if(projectTypeToApply.equals("Validation")) {
			applyMetadataTemplateProjectValidation();
		}
		else {
			for(CustomMetadata cm: Share.status.getCustomMetadata()) {
				if(cm.getName().equals(projectTypeToApply)) {
					status.setArtifactTypes(cm.getArtifactTypes());
					status.setDependencyTypes(cm.getDependencyTypes());
					status.setTaskTypes(cm.getTaskTypes());	
					status.setRuleTypes(cm.getRuleTypes());		
					status.setRules(new ArrayList<Rule>());
					break;
				}
			}
		}
	}

	private void applyMetadataTemplateProjectX() {
				
		ArrayList<ArtifactType> artifactTypes = new ArrayList<ArtifactType>();
		ArrayList<DependencyType> dependencyTypes = new ArrayList<DependencyType>();
		ArrayList<TaskType> taskTypes = new ArrayList<TaskType>();
		
		ArrayList<ArtifactType> u = new ArrayList<ArtifactType>();
		ArrayList<ArtifactType> p = new ArrayList<ArtifactType>();
		ArrayList<DependencyType> d = new ArrayList<DependencyType>();
		String description = "";
		
		ArtifactType ATstory = new ArtifactType("Story",".txt");
		ArtifactType ATclass = new ArtifactType("Class",".java");
		ArtifactType ATtest = new ArtifactType("Test",".java");
		ArtifactType ATfinal = new ArtifactType("Final",".java");
		ArtifactType ATstart = new ArtifactType("Start",".start");
		artifactTypes.add(ATstory);
		artifactTypes.add(ATclass);
		artifactTypes.add(ATtest);
		artifactTypes.add(ATfinal);	
		artifactTypes.add(ATstart);	
		
		u.add(ATstart);
		p.add(ATstory);
		DependencyType startStory = new DependencyType(ATstart,ATstory,"Start");
		d.add(startStory);
		dependencyTypes.add(startStory);
		description = "Create a story for the project";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATstory);
		p.add(ATclass);
		DependencyType storyClass = new DependencyType(ATstory,ATclass,"StoryClass");
		d.add(storyClass);
		dependencyTypes.add(storyClass);
		description = "Create a class for the story";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATclass);
		p.add(ATtest);
		DependencyType classTest = new DependencyType(ATclass,ATtest,"ClassTest");
		d.add(classTest);
		dependencyTypes.add(classTest);
		description = "Create a test for a class";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATclass);
		u.add(ATtest);
		p.add(ATfinal);
		DependencyType classFinal = new DependencyType(ATclass,ATfinal,"ClassFinal");
		DependencyType testFinal = new DependencyType(ATtest,ATfinal,"TestFinal");
		d.add(classFinal);
		dependencyTypes.add(classFinal);
		d.add(testFinal);
		dependencyTypes.add(testFinal);
		description = "Create final class";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		status.setArtifactTypes(artifactTypes);
		status.setDependencyTypes(dependencyTypes);
		status.setTaskTypes(taskTypes);	
	}
	
	private void applyMetadataTemplateProjectEmpty() {
		
		ArrayList<ArtifactType> artifactTypes = new ArrayList<ArtifactType>();
		ArrayList<DependencyType> dependencyTypes = new ArrayList<DependencyType>();
		ArrayList<TaskType> taskTypes = new ArrayList<TaskType>();
		
		ArrayList<ArtifactType> u = new ArrayList<ArtifactType>();
		ArrayList<ArtifactType> p = new ArrayList<ArtifactType>();
		ArrayList<DependencyType> d = new ArrayList<DependencyType>();
		String description = "";

		ArtifactType ATstart = new ArtifactType("Start",".start");
		artifactTypes.add(ATstart);	
		
		status.setArtifactTypes(artifactTypes);
		status.setDependencyTypes(dependencyTypes);
		status.setTaskTypes(taskTypes);	
	}
	
	private void applyMetadataTemplateProjectES_TD() {
		
		ArrayList<ArtifactType> artifactTypes = new ArrayList<ArtifactType>();
		ArrayList<DependencyType> dependencyTypes = new ArrayList<DependencyType>();
		ArrayList<TaskType> taskTypes = new ArrayList<TaskType>();
		ArrayList<RuleType> ruleTypes = new ArrayList<RuleType>();
		ArrayList<Rule> rules = new ArrayList<Rule>();
		
		ArrayList<ArtifactType> u = new ArrayList<ArtifactType>();
		ArrayList<ArtifactType> p = new ArrayList<ArtifactType>();
		ArrayList<DependencyType> d = new ArrayList<DependencyType>();
		String description = "";
		
		ArtifactType ATstart = new ArtifactType("Start",".start");
		ArtifactType ATstory = new ArtifactType("Story",".txt");
		ArtifactType ATstoryTest = new ArtifactType("StoryTest",".java");
		ArtifactType ATinterface = new ArtifactType("Interface",".java");
		ArtifactType ATdomainClass = new ArtifactType("DomainClass",".java");
		ArtifactType ATdomainTest = new ArtifactType("DomainTest",".java");
		artifactTypes.add(ATstart);
		artifactTypes.add(ATstory);
		artifactTypes.add(ATstoryTest);
		artifactTypes.add(ATinterface);		
		artifactTypes.add(ATdomainClass);
		artifactTypes.add(ATdomainTest);
		
		RuleType AddStatusInProgress = new RuleType("AddStatusInProgress","This artifact type stays in In Progress status upon completion until forced Completed");
		ruleTypes.add(AddStatusInProgress);
		
		rules.add(new Rule(ATstory, AddStatusInProgress));
		rules.add(new Rule(ATstoryTest, AddStatusInProgress));
		rules.add(new Rule(ATinterface, AddStatusInProgress));
		rules.add(new Rule(ATdomainTest, AddStatusInProgress));
		rules.add(new Rule(ATdomainClass, AddStatusInProgress));		
		
		u.add(ATstart);
		p.add(ATstory);
		DependencyType DTstory = new DependencyType(ATstart,ATstory,"DTstory");
		d.add(DTstory);
		dependencyTypes.add(DTstory);
		description = "Create a story for the project";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATstory);
		p.add(ATstoryTest);
		DependencyType DTstoryTest = new DependencyType(ATstory,ATstoryTest,"DTstoryTest");
		d.add(DTstoryTest);
		dependencyTypes.add(DTstoryTest);
		description = "Create a test for the story";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATstoryTest);
		p.add(ATinterface);
		DependencyType DTinterface = new DependencyType(ATstoryTest,ATinterface,"DTinterface");
		d.add(DTinterface);
		dependencyTypes.add(DTinterface);
		description = "Create an interface for the storyTest";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATinterface);
		p.add(ATdomainClass);
	  	DependencyType DTdomainClass = new DependencyType(ATinterface,ATdomainClass,"DTdomainClass");
		DTdomainClass.setMultiplicity(0);
		d.add(DTdomainClass);
		dependencyTypes.add(DTdomainClass);
		description = "Create a domainClass for the interface";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATdomainClass);
	    p.add(ATdomainTest);
		DependencyType DTdomainTest = new DependencyType(ATdomainClass,ATdomainTest,"DTdomainTest");
		d.add(DTdomainTest);
		dependencyTypes.add(DTdomainTest);
		description = "Create a domainTest for the domainClass";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		status.setArtifactTypes(artifactTypes);
		status.setDependencyTypes(dependencyTypes);
		status.setTaskTypes(taskTypes);	
		status.setRuleTypes(ruleTypes);
		status.setRules(rules);
	}
	
	private void applyMetadataTemplateProjectES_BU() {
		
		ArrayList<ArtifactType> artifactTypes = new ArrayList<ArtifactType>();
		ArrayList<DependencyType> dependencyTypes = new ArrayList<DependencyType>();
		ArrayList<TaskType> taskTypes = new ArrayList<TaskType>();
		ArrayList<RuleType> ruleTypes = new ArrayList<RuleType>();
		ArrayList<Rule> rules = new ArrayList<Rule>();
		
		ArrayList<ArtifactType> u = new ArrayList<ArtifactType>();
		ArrayList<ArtifactType> p = new ArrayList<ArtifactType>();
		ArrayList<DependencyType> d = new ArrayList<DependencyType>();
		String description = "";
		
		ArtifactType ATstart = new ArtifactType("Start",".start");
		ArtifactType ATstory = new ArtifactType("Story",".txt");
		ArtifactType ATstoryTest = new ArtifactType("StoryTest",".java");
		ArtifactType ATinterface = new ArtifactType("Interface",".java");
		ArtifactType ATdomainClass = new ArtifactType("DomainClass",".java");
		ArtifactType ATdomainTest = new ArtifactType("DomainTest",".java");
		artifactTypes.add(ATstart);
		artifactTypes.add(ATstory);
		artifactTypes.add(ATstoryTest);
		artifactTypes.add(ATinterface);		
		artifactTypes.add(ATdomainClass);
		artifactTypes.add(ATdomainTest);

		u.add(ATstart);
		p.add(ATstory);
		DependencyType DTstory = new DependencyType(ATstart,ATstory,"DTstory");
		d.add(DTstory);
		dependencyTypes.add(DTstory);
		description = "Create a story for the project";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATstory);
		p.add(ATdomainClass);
		DependencyType DTdomainClass = new DependencyType(ATstory,ATdomainClass,"DTdomainClass");
		d.add(DTdomainClass);
		DTdomainClass.setMultiplicity(0);
		dependencyTypes.add(DTdomainClass);
		description = "Create a domain class for the story";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATdomainClass);
		p.add(ATdomainTest);
		DependencyType DTdomainTest = new DependencyType(ATdomainClass,ATdomainTest,"DTdomainTest");
		d.add(DTdomainTest);
		dependencyTypes.add(DTdomainTest);
		description = "Create a domain test for the domain class";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATdomainTest);
		p.add(ATinterface);
	  	DependencyType DTinterface = new DependencyType(ATdomainTest,ATinterface,"DTinterface");
		d.add(DTinterface);
		dependencyTypes.add(DTinterface);
		description = "Create a interface for the domain test";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATinterface);
	    p.add(ATstoryTest);
		DependencyType DTstoryTest = new DependencyType(ATinterface,ATstoryTest,"DTstoryTest");
		d.add(DTstoryTest);
		dependencyTypes.add(DTstoryTest);
		description = "Create a story test for the interface";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		status.setArtifactTypes(artifactTypes);
		status.setDependencyTypes(dependencyTypes);
		status.setTaskTypes(taskTypes);	
		status.setRuleTypes(ruleTypes);
		status.setRules(rules);
	}
	
	private void applyMetadataTemplateProjectES_BU_TF() {
		
		//create artifact types, task types and dependency types
		ArrayList<ArtifactType> artifactTypes = new ArrayList<ArtifactType>();
		ArrayList<DependencyType> dependencyTypes = new ArrayList<DependencyType>();
		ArrayList<TaskType> taskTypes = new ArrayList<TaskType>();
		ArrayList<RuleType> ruleTypes = new ArrayList<RuleType>();
		ArrayList<Rule> rules = new ArrayList<Rule>();
		
		ArrayList<ArtifactType> u = new ArrayList<ArtifactType>();
		ArrayList<ArtifactType> p = new ArrayList<ArtifactType>();
		ArrayList<DependencyType> d = new ArrayList<DependencyType>();
		String description = "";
		
		ArtifactType ATstart = new ArtifactType("Start",".start");
		ArtifactType ATstory = new ArtifactType("Story",".txt");
		ArtifactType ATstoryTest = new ArtifactType("StoryTest",".java");
		ArtifactType ATinterface = new ArtifactType("Interface",".java");
		ArtifactType ATdomainClass = new ArtifactType("DomainClass",".java");
		ArtifactType ATdomainTest = new ArtifactType("DomainTest",".java");
		artifactTypes.add(ATstart);
		artifactTypes.add(ATstory);
		artifactTypes.add(ATstoryTest);
		artifactTypes.add(ATinterface);		
		artifactTypes.add(ATdomainClass);
		artifactTypes.add(ATdomainTest);

		u.add(ATstart);
		p.add(ATstory);
		DependencyType DTstory = new DependencyType(ATstart,ATstory,"DTstory");
		d.add(DTstory);
		dependencyTypes.add(DTstory);
		description = "Create a story for the project";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATstory);
		p.add(ATdomainTest);
		DependencyType DTdomainTest = new DependencyType(ATstory,ATdomainTest,"DTdomainTest");
		d.add(DTdomainTest);
		DTdomainTest.setMultiplicity(0);
		dependencyTypes.add(DTdomainTest);
		description = "Create a domain test for the story";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATdomainTest);
		p.add(ATdomainClass);
		DependencyType DTdomainClass = new DependencyType(ATdomainTest,ATdomainClass,"DTdomainClass");
		d.add(DTdomainClass);
		dependencyTypes.add(DTdomainClass);
		description = "Create a domain class for the domain test";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATdomainTest);
		p.add(ATinterface);
	  	DependencyType DTinterface = new DependencyType(ATdomainTest,ATinterface,"DTinterface");
		d.add(DTinterface);
		dependencyTypes.add(DTinterface);
		description = "Create a interface for the domain test";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATinterface);
	    p.add(ATstoryTest);
		DependencyType DTstoryTest = new DependencyType(ATinterface,ATstoryTest,"DTstoryTest");
		d.add(DTstoryTest);
		dependencyTypes.add(DTstoryTest);
		description = "Create a story test for the interface";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		status.setArtifactTypes(artifactTypes);
		status.setDependencyTypes(dependencyTypes);
		status.setTaskTypes(taskTypes);	
		status.setRuleTypes(ruleTypes);
		status.setRules(rules);
	}		
	
	private void applyMetadataTemplateProjectValidation() {
		
		ArrayList<ArtifactType> artifactTypes = new ArrayList<ArtifactType>();
		ArrayList<DependencyType> dependencyTypes = new ArrayList<DependencyType>();
		ArrayList<TaskType> taskTypes = new ArrayList<TaskType>();
		ArrayList<RuleType> ruleTypes = new ArrayList<RuleType>();
		ArrayList<Rule> rules = new ArrayList<Rule>();
		
		ArrayList<ArtifactType> u = new ArrayList<ArtifactType>();
		ArrayList<ArtifactType> p = new ArrayList<ArtifactType>();
		ArrayList<DependencyType> d = new ArrayList<DependencyType>();
		String description = "";
		
		ArtifactType ATstart = new ArtifactType("Start",".start");
		ArtifactType ATstory = new ArtifactType("Story",".txt");
		ArtifactType ATdomainClass = new ArtifactType("DomainClass",".java");
		ArtifactType ATdomainTest = new ArtifactType("DomainTest",".java");
		ArtifactType ATinterface = new ArtifactType("InterfaceClass",".java");
		ArtifactType ATinterfaceTest = new ArtifactType("InterfaceTest",".java");		
	
		artifactTypes.add(ATstart);
		artifactTypes.add(ATstory);
		artifactTypes.add(ATdomainClass);
		artifactTypes.add(ATdomainTest);
		artifactTypes.add(ATinterface);
		artifactTypes.add(ATinterfaceTest);		

		u.add(ATstart);
		p.add(ATstory);
		DependencyType DTstory = new DependencyType(ATstart,ATstory,"DTstory");
		d.add(DTstory);
		dependencyTypes.add(DTstory);
		description = "Create a story for the project";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATstory);
		p.add(ATdomainClass);
		DependencyType DTdomainClass = new DependencyType(ATstory,ATdomainClass,"DTdomainClass");
		d.add(DTdomainClass);
		DTdomainClass.setMultiplicity(0);
		dependencyTypes.add(DTdomainClass);
		description = "Create a domain class for the story";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATdomainClass);
		p.add(ATdomainTest);
		DependencyType DTdomainTest = new DependencyType(ATdomainClass,ATdomainTest,"DTdomainTest");
		d.add(DTdomainTest);
		dependencyTypes.add(DTdomainTest);
		description = "Create a domain test for the domain class";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATdomainTest);
		p.add(ATinterface);
	  	DependencyType DTinterface = new DependencyType(ATdomainTest,ATinterface,"DTinterface");
		d.add(DTinterface);
		dependencyTypes.add(DTinterface);
		description = "Create an interface class for the domain test";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		u.add(ATinterface);
	    p.add(ATinterfaceTest);
		DependencyType DTstoryTest = new DependencyType(ATinterface,ATinterfaceTest,"DTstoryTest");
		d.add(DTstoryTest);
		dependencyTypes.add(DTstoryTest);
		description = "Create an interface test for the interface class";
		taskTypes.add(new TaskType(new ArrayList<ArtifactType>(u),new ArrayList<ArtifactType>(p),new ArrayList<DependencyType>(d),description));
		u.clear(); p.clear(); d.clear();
		
		status.setArtifactTypes(artifactTypes);
		status.setDependencyTypes(dependencyTypes);
		status.setTaskTypes(taskTypes);	
		status.setRuleTypes(ruleTypes);
		status.setRules(rules);
	}
	
	public void applyDataTemplate(String projectTypeToApply, boolean current) {
		
		if(projectTypeToApply.equals("X")) {
			applyDataTemplateProjectX();
			
		}	else if(projectTypeToApply.equals("Empty")) {
			applyDataTemplateProjectEmpty();
			
		}	else if(projectTypeToApply.equals("ES_TopDown") 
				|| projectTypeToApply.equals("ES_BottomUp") 
				|| projectTypeToApply.equals("ES_BottomUp_TestFirst")) {
			applyDataTemplateProjectES();
			
		}	else if(projectTypeToApply.equals("Validation")) {
			applyDataTemplateProjectValidation();
		}			
	}
	
	private void applyDataTemplateProjectX() {
		
		status.rescanArtifacts();		
	
	    List<Component> components = new ArrayList<Component>();
		Boolean found = true;
		for (Artifact a: status.getArtifacts()) {
			if(a.getName().contains("Story")) {
				a.setType(status.getArtifactTypes().get(0));
			}
			else if(a.getName().contains("Class")) {
				a.setType(status.getArtifactTypes().get(1));
			}
			else if(a.getName().contains("Test")) {
				a.setType(status.getArtifactTypes().get(2));
			}
			else if(a.getName().contains("Final")) {
				a.setType(status.getArtifactTypes().get(3));
			}
		}
		
		ArtifactType ATstart = new ArtifactType("Start",".start");
		status.getArtifacts().add(new Artifact(Paths.get(project.getLocationPlugin()+"/Start"),ATstart));
		
		for (Component c: status.getComponents()) {
			if(c.getName().contains("Story")) {
				c.setType(status.getArtifactTypes().get(0));
			}
			else if(c.getName().contains("Class")) {
				c.setType(status.getArtifactTypes().get(1));
			}
			else if(c.getName().contains("Test")) {
				c.setType(status.getArtifactTypes().get(2));
			}
			else if(c.getName().contains("Final")) {
				c.setType(status.getArtifactTypes().get(3));
			}
		}	
	
		List<Dependency> dependencies = new ArrayList<Dependency>();
		dependencies.add(new Dependency(status.getArtifacts().get(6), status.getArtifacts().get(2), status.getDependencyTypes().get(0)));
		dependencies.add(new Dependency(status.getArtifacts().get(6), status.getArtifacts().get(3), status.getDependencyTypes().get(0)));
		dependencies.add(new Dependency(status.getArtifacts().get(2), status.getArtifacts().get(0), status.getDependencyTypes().get(1)));
		dependencies.add(new Dependency(status.getArtifacts().get(3), status.getArtifacts().get(1), status.getDependencyTypes().get(1)));
		status.setDependencies(dependencies);
	}
	
	private void applyDataTemplateProjectEmpty() {		
		status.rescanArtifacts();
		ArtifactType ATstart = new ArtifactType("Start",".start");
		status.getArtifacts().add(new Artifact(Paths.get(project.getLocationPlugin()+"/Start"),ATstart));		
	}
	
	private void applyDataTemplateProjectES() {
		
		status.rescanArtifacts();
		ArtifactType ATstart = new ArtifactType("Start",".start");
		status.getArtifacts().add(new Artifact(Paths.get(project.getLocationPlugin()+"/Start"),ATstart));	
		
		for (Component c: status.getComponents()) {
			if(c.getPath().equals("/"+Share.pluginFolderName+"/main/resources/stories")) {
				c.setType(status.getArtifactTypes().get(1));
				c.setDefaultForType(true);
			}
			else if(c.getPath().equals("/"+Share.pluginFolderName+"/test/groovy/pt/ulisboa/tecnico/softeng/hotel/services/local")) {
				c.setType(status.getArtifactTypes().get(2));
				c.setDefaultForType(true);
			}
			else if(c.getPath().equals("/"+Share.pluginFolderName+"/main/java/pt/ulisboa/tecnico/softeng/hotel/services/local")) {
				c.setType(status.getArtifactTypes().get(3));
				c.setDefaultForType(true);
			}
			else if(c.getPath().equals("/"+Share.pluginFolderName+"/main/java/pt/ulisboa/tecnico/softeng/hotel/domain")) {
				c.setType(status.getArtifactTypes().get(4));
				c.setDefaultForType(true);
			}
			else if(c.getPath().equals("/"+Share.pluginFolderName+"/test/groovy/pt/ulisboa/tecnico/softeng/hotel/domain")) {
				c.setType(status.getArtifactTypes().get(5));
				c.setDefaultForType(true);
			}
		}	
	}
	
	private void applyDataTemplateProjectValidation() {
		
		status.rescanArtifacts();
		ArtifactType ATstart = new ArtifactType("Start",".start");
		status.getArtifacts().add(new Artifact(Paths.get(project.getLocationPlugin()+"/Start"),ATstart));	
		
		for (Component c: status.getComponents()) {
			if(c.getPath().equals("/"+Share.pluginFolderName+"/story")) {
				c.setType(status.getArtifactTypes().get(1));
				c.setDefaultForType(true);
			}
			else if(c.getPath().equals("/"+Share.pluginFolderName+"/domainClass")) {
				c.setType(status.getArtifactTypes().get(2));
				c.setDefaultForType(true);
			}
			else if(c.getPath().equals("/"+Share.pluginFolderName+"/domainTest")) {
				c.setType(status.getArtifactTypes().get(3));
				c.setDefaultForType(true);
			}
			else if(c.getPath().equals("/"+Share.pluginFolderName+"/interfaceClass")) {
				c.setType(status.getArtifactTypes().get(4));
				c.setDefaultForType(true);
			}
			else if(c.getPath().equals("/"+Share.pluginFolderName+"/interfaceTest")) {
				c.setType(status.getArtifactTypes().get(5));
				c.setDefaultForType(true);
			}
		}	
	}
	
	public ArrayList<String> getProjectTypes() {
		return projectTypes;
	}
	
	public String[] getProjectTypesSA() {
		return projectTypes.toArray(new String[projectTypes.size()]);
	}	

	public void setProjectTypes(ArrayList<String> projectTypes) {
		this.projectTypes = projectTypes;
	}

	public void verifyProject(String projectName) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);			
		if(!project.getFolder(Share.pluginFolderName).exists()) {
			newProject = false;
			createProject("TypeEmpty", projectName);
			newProject = true;
		}		
	}
}