package plugin.classes;

import java.nio.file.Paths;
import java.util.Arrays;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class ArtifactEventsHandler implements IResourceChangeListener {
	
	private String movedFromA = null;
	private String movedFromAName = null;
	private String movedFromC = null;
	private String movedFromCName = null;
	private String movedToA = null;
	private String movedToAName = null;	
	private String movedToC = null;
	private String movedToCName = null;
	private Integer done = 0;	
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		
		IPath listenLocation = new Path(Share.project.getProject().getName());
		
		if (event.getType() != IResourceChangeEvent.POST_CHANGE)
			return;
		IResourceDelta rootDelta = event.getDelta();
		IResourceDelta docDelta = rootDelta.findMember(listenLocation);
		if (docDelta == null)
			return;
		
		IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
			public boolean visit(IResourceDelta delta) {
				IResource resource = delta.getResource();
				System.out.println("event");
				
				if (delta.getKind() == IResourceDelta.REMOVED) {
					System.out.println("Removed: " + resource.getLocation().toString());
				}

				if (delta.getKind() != IResourceDelta.CHANGED) {
					System.out.println("Not changed");
					if (delta.getMovedFromPath() != null) {
						if (resource.getType() == IResource.FILE){
							movedToA = resource.getLocation().toString().substring(Share.project.getLocationBase().length());
							movedToAName = resource.getName();
							System.out.println("movedToA: " + movedToA);
						} else if (resource.getType() == IResource.FOLDER){
							movedToC = resource.getLocation().toString().substring(Share.project.getLocationBase().length());
							movedToCName = resource.getName();
							System.out.println("movedToC: " + movedToC + " movetoCName: " + movedToCName);
						}
					}
					if (delta.getMovedToPath() != null) {
						if (resource.getType() == IResource.FILE){
							movedFromA = resource.getLocation().toString().substring(Share.project.getLocationBase().length());
							movedFromAName = resource.getName();
							System.out.println("movedFromA:" + movedFromA);
						} else if (resource.getType() == IResource.FOLDER){
							movedFromC = resource.getLocation().toString().substring(Share.project.getLocationBase().length());
							movedFromCName = resource.getName();
							System.out.println("movedFromC:" + movedFromC);
						}

					}	
					if(movedFromA != null && movedToA != null) {
						System.out.println("Moved");
						if(done == 0) {
							System.out.println("Moved relevant - file");
							Artifact artifact = Share.status.findArtifact(resource.getLocation().toString().substring(Share.project.getLocationBase().length()));
							if(artifact == null) {
								artifact = Share.status.findArtifact(movedFromA);
							}
							if(artifact == null) {
								System.out.println(movedFromAName +"its null");
							}					 				
							Component component = Share.status.findComponent(artifact.getComponentPath());

							System.out.println(artifact.getPath());
							System.out.println(component.getPath());
							Artifact artifactToRemove = null;
							for(Artifact a: component.getArtifacts()){
								if(a.getName().equals(artifact.getName())){
									artifactToRemove = a;
									break;
								}
							}
							if(artifactToRemove != null){
								component.getArtifacts().remove(artifactToRemove);
							}

							artifact.setPath(movedToA);					 				
							artifact.setName(movedToAName);
							component = Share.status.findComponent(artifact.getComponentPath());
							if(component == null) {
								component = new Component(Paths.get(Share.project.getLocationBase()+artifact.getComponentPath()));
								component.getArtifacts().add(artifact);
								Share.status.getComponents().add(component);					 					
							}	
							else {
								component.getArtifacts().add(artifact);
							}
							System.out.println(artifact.getPath());
							System.out.println(component.getPath());			
							Share.project.saveStatus();
							movedFromA = null;
							movedToA = null;
						}
						else {
							done -= 1;
						}
						
					}
					if(movedFromC != null && movedToC != null) {
						System.out.println("Moved relevant - folder");
						Component component = Share.status.findComponent(movedFromC);
						System.out.println(component.getName());
						component.setPath(movedToC);
						component.setName(movedToCName);
						for (Artifact a: component.getArtifacts()) {
							a.setPath(component.getPath()+"/"+a.getName());
						}

						Share.project.saveStatus();				                
						movedFromC = null;
						movedToC = null;
						done = component.getArtifacts().size();
						System.out.println(Arrays.toString(Share.status.getComponents().toArray()));


					}
				} else if (resource.getType() == IResource.FILE) {
					System.out.println("Changed");
					if(Share.status.getActiveTask() != null) {
						boolean found = false;
						if(!Share.status.getActiveTask().getProduced().isEmpty()) {
							for(Artifact a: Share.status.getActiveTask().getProduced()) {
								if(a.getFullPath().equals(resource.getLocation().toString())){
									found = true;
									break;
								}
							}
						}
						if(!found) {
							System.out.println(resource.getName());
							
							String activeResourceName = "";							
					        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					        if (page != null) {
					            IEditorPart editor = page.getActiveEditor();
					            if (editor != null) {
					                IEditorInput input = editor.getEditorInput();
					                if (input instanceof IFileEditorInput) {
					                    activeResourceName = ((IFileEditorInput)input).getFile().getLocation().toOSString();
					                }
					            }
					        } 
					        
					        if(resource.getLocation().toOSString().equals(activeResourceName)) {							
								Artifact toAdd = Share.status.findArtifact(resource.getLocation().toString().substring(Share.project.getLocationBase().length()));
								if(toAdd != null) {
									System.out.println(toAdd.getName());
									Share.status.getActiveTask().getProduced().add(toAdd);
									Share.status.quickPrint();	
								}	
					        }
						}
					}
					Artifact changed = Share.status.findArtifact(resource.getLocation().toString().substring(Share.project.getLocationBase().length()));
					if(changed != null) {
						System.out.println("Setting Redo");
						Share.status.setReactivated(changed);							
					}
				}
				
				return true;
			}
		};
		try {
			docDelta.accept(visitor);
		} catch (CoreException e) {		           
		}
	}
}