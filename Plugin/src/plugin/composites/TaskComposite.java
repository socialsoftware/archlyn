package plugin.composites;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import plugin.classes.Artifact;
import plugin.classes.ArtifactType;
import plugin.classes.Component;
import plugin.classes.Share;
import plugin.classes.TableCreator;
import plugin.classes.Task;
import plugin.views.MainTaskView;

public class TaskComposite extends Composite {
	
	private Artifact newArtifact;
	private MainTaskView parent;
	private Task task;

	public TaskComposite(Composite scrolledComposite, int style) {
		super(scrolledComposite, SWT.None);
		setLayout(new GridLayout(1, false));	
		setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
	}

	public void createTaskView() {
		
		task = Share.status.getActiveTask();

		Label labelDescription = new Label (this, SWT.BORDER);
		Label labelUsedArtifacts = new Label (this, SWT.BORDER);
		TableViewer tableUA = new TableCreator().createTable(this, "ua", 6);	
		Label labelProducedArtifacts= new Label (this, SWT.BORDER);	
		TableViewer tablePA = new TableCreator().createTable(this, "pa", 6);	

		Menu menuT = new Menu(this);
		MenuItem miCompleteT = new MenuItem(menuT, SWT.NONE);	
		MenuItem miRedoNext = new MenuItem(menuT, SWT.CHECK);	
		new MenuItem(menuT, SWT.SEPARATOR);
		MenuItem miOpenContext = new MenuItem(menuT, SWT.NONE);	
		MenuItem miCreateContext = new MenuItem(menuT, SWT.NONE);
		new MenuItem(menuT, SWT.SEPARATOR);
		MenuItem miRemovePA = new MenuItem(menuT, SWT.NONE);

		labelDescription.setText(task.getDescription());
		labelUsedArtifacts.setText("Used Artifacts");
		parent.getViewers().add(tableUA);	
		labelProducedArtifacts.setText("Produced Artifacts");		
		parent.getViewers().add(tablePA);
		tableUA.getTable().addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				menuT.setVisible(true);
			}
		});		
		tablePA.getTable().addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				menuT.setVisible(true);
			}
		});	

		miCompleteT.setText ("Complete Task");
		miCompleteT.addSelectionListener(widgetSelectedAdapter(f -> {
			if(Task.completeTask(task, miRedoNext.getSelection())){
				Share.message.setText("Task "+ task.getDescription()+ " completed. New Tasks suggested. Please activate a new Task.");
				Share.pluginView.getPluginComposite().openMainActivityView();
			}
		}));
		
		miRedoNext.setText ("Redo Next");
		miRedoNext.setSelection(false);
		miRedoNext.addSelectionListener(widgetSelectedAdapter(f -> {
			if(miRedoNext.getSelection()) {
				miRedoNext.setSelection(false);		
			}else {
				miRedoNext.setSelection(true);
			}
		}));

		miOpenContext.setText ("Open Context");
		miOpenContext.addSelectionListener(widgetSelectedAdapter(g -> {
			this.openContext();
		}));

		miCreateContext.setText ("Create Context");
		miCreateContext.addSelectionListener(widgetSelectedAdapter(f -> {
			this.createContext();
		}));

		miRemovePA.setText("Remove Produced Artifact");
		miRemovePA.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem a: tablePA.getTable().getItems()) {
				if (a.getChecked()) {
					int index = 0;
					for(Artifact ua: task.getProduced()) {
						if (ua.getName().equals(a.getText(0))) {
							break;								
						}
						index +=1;
					}
					task.getProduced().remove(index);
					index = 0;
					a.setChecked(false);
				}				
			}
			Share.project.saveStatus();	
		}));
		
		this.createContext();
		this.openContext();
		this.openContext();
	}

	private void openContext() {
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		page.closeAllEditors(true);

		for (Artifact a: task.getUsed()) {
			if(!a.getName().equals("Start")) {
				File f = new File(a.getFullPath());
				IFile fi = parent.getRoot().getFileForLocation(new Path(f.getPath()));
				IEditorDescriptor desc = PlatformUI.getWorkbench().
						getEditorRegistry().getDefaultEditor(f.getName());
				try {
					page.openEditor(new FileEditorInput(fi), desc.getId());
				} catch (PartInitException e) {
					e.printStackTrace();
				}				    	
			}		    			
		}

		if(!task.getProduced().isEmpty()) {
			newArtifact = task.getProduced().get(0);

			for (Artifact a: task.getProduced()) {

				File f = new File(a.getFullPath());
				if (f.exists()) {						
					IFile fi = parent.getRoot().getFileForLocation(new Path(f.getPath()));
					IEditorDescriptor desc = PlatformUI.getWorkbench().
							getEditorRegistry().getDefaultEditor(f.getName());
					try {
						page.openEditor(new FileEditorInput(fi), desc.getId());
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			}
			Share.message.setText("Working on Task " + task.getDescription() + ". Please Complete Task once it is done.");
		}	
	}
	
	private void createContext() {
		
		if(task.getProduced().isEmpty()) {
			ArtifactType newArtifactType = task.getTaskType().getProduced().get(0);
			Component c = Share.status.findDefaultComponentForAT(newArtifactType.getType());
			newArtifact = new Artifact(c.getFullPath(),newArtifactType.getType()+Share.status.getNewArtifactNumber(),newArtifactType);
			Share.status.getArtifacts().add(newArtifact);
			task.getProduced().add(newArtifact);
			c.getArtifacts().add(newArtifact);
			Share.status.setNewArtifactNumber(Share.status.getNewArtifactNumber()+1);
			if(task.getTaskType().getDependencies().get(0).getMultiplicity() > 1) {
				int mult = task.getTaskType().getDependencies().get(0).getMultiplicity() - 1;
				for(int i = 0; i < mult; i++) {
					Artifact newMultArtifact = new Artifact(c.getFullPath(),newArtifactType.getType()+Share.status.getNewArtifactNumber(),newArtifactType);
					Share.status.getArtifacts().add(newMultArtifact);
					task.getProduced().add(newMultArtifact);
					c.getArtifacts().add(newMultArtifact);
					Share.status.setNewArtifactNumber(Share.status.getNewArtifactNumber()+1);
				}
			}	
			Share.message.setText("Context created. Please open context");
			Share.project.saveStatus();	
		}
		else {
			newArtifact = task.getProduced().get(0);
		}				
	}
	
	public Artifact getNewArtifact() {
		return newArtifact;
	}

	public void setNewArtifact(Artifact newArtifact) {
		this.newArtifact = newArtifact;
	}

	public MainTaskView getParentView() {
		return parent;
	}

	public void setParentView(MainTaskView mainTaskView) {
		this.parent = mainTaskView;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}