package plugin.composites;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.awt.event.MouseEvent;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import plugin.classes.Artifact;
import plugin.classes.Share;
import plugin.classes.TableCreator;
import plugin.classes.Task;
import plugin.classes.TaskSuggester;
import plugin.shells.ArtifactsInActivityShell;
import plugin.shells.EditTaskShell;
import plugin.views.MainActivityView;

public class ActivityComposite extends Composite {
	
	private Artifact newArtifact;
	private MainActivityView parent;

	public ActivityComposite(Composite scrolledComposite, int style) {
		super(scrolledComposite, SWT.None);
		setLayout(new GridLayout(1, false));	
		setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
	}

	public void createTasksView() {

		TableViewer tableT = new TableCreator().createTable(this, "MactivityT", 3);		
		Menu menuT = new Menu(tableT.getTable());
		MenuItem miActivateT = new MenuItem(menuT, SWT.NONE);
		new MenuItem(menuT, SWT.SEPARATOR);
		MenuItem miSuggestT = new MenuItem(menuT, SWT.NONE);	
		MenuItem miEditT = new MenuItem(menuT, SWT.NONE);	
		MenuItem miRemoveT = new MenuItem(menuT, SWT.NONE);	
		new MenuItem(menuT, SWT.SEPARATOR);	
		MenuItem miShowCompletedT = new MenuItem(menuT, SWT.CHECK);	
		new MenuItem(menuT, SWT.SEPARATOR);	
		MenuItem miOpenAiAShell = new MenuItem(menuT, SWT.NONE);				

		tableT.getTable().setLinesVisible(false);
		parent.getViewers().add(tableT);		
		
		tableT.getTable().addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				menuT.setVisible(true);
			}
		});	
		
	    IDoubleClickListener listenerDoubleClick = new IDoubleClickListener() {
				@Override
				public void doubleClick(DoubleClickEvent event) {
					activateTask(tableT);						
				}	    		
	    	};
	    tableT.addDoubleClickListener(listenerDoubleClick);

		miActivateT.setText ("Activate Task");
		miActivateT.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem t: tableT.getTable().getItems()) {
				if (t.getChecked()) {
					this.activateTask(tableT);	
				}
			}
		}));

		miSuggestT.setText ("Suggest New Tasks");
		miSuggestT.addSelectionListener(widgetSelectedAdapter(f -> {
			new TaskSuggester().suggest();					
		}));

		miEditT.setText ("Edit Task");
		miEditT.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem t: tableT.getTable().getItems()) {
				if (t.getChecked()) {
					Shell shellEditT = new EditTaskShell(this.getDisplay(), tableT);
					shellEditT.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
					shellEditT.open();
					shellEditT.layout();	
					break;
				}
			}						
		}));

		miRemoveT.setText ("Remove All Tasks");
		miRemoveT.addSelectionListener(widgetSelectedAdapter(f -> {
			Share.status.getActiveActivity().getTasks().clear();
			tableT.setInput(Share.status.getActiveActivity().getTasks());
			Share.project.saveStatus();
		}));

		miShowCompletedT.setText ("Show Completed");
		miShowCompletedT.setSelection(true);
		miShowCompletedT.addSelectionListener(widgetSelectedAdapter(f -> {
			if(miShowCompletedT.getSelection()) {
				miShowCompletedT.setSelection(false);
				Task.setActiveFilter("All but Complete");			
			}
			else {
				miShowCompletedT.setSelection(true);
				Task.setActiveFilter("All");	
			}
			tableT.setInput(Share.status.getTasksOfStatus(Share.status.getActiveActivity().getTasks(),Task.getActiveFilter()));
		}));
		
		miOpenAiAShell.setText ("Artifacts in Activity");
		miOpenAiAShell.addSelectionListener(widgetSelectedAdapter(f -> {
			Shell shellAiA = new ArtifactsInActivityShell(this.getDisplay(), parent);
			shellAiA.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
			shellAiA.open();
			shellAiA.layout();				
		}));
	}
	
	private void activateTask(TableViewer tableT) {
		for(TableItem t: tableT.getTable().getItems()) {
			if (t.getChecked()) {
				Task checked = Share.status.findTask(t.getText(0));
				checked.setStatus("Active");
				if(Share.status.getActiveTask() != null) {
					Share.status.getActiveTask().setStatus("In Progress");
				}
				Share.status.setActiveTask(checked);
				if(Share.status.getGithub() != null && Share.status.getGithub().getClient() != null) {
					Share.status.getGithub().updateIssue(checked);
				}	
				Share.pluginView.getPluginComposite().openMainTaskView();
				break;
			}	
		}
	}
	
	public Artifact getNewArtifact() {
		return newArtifact;
	}

	public void setNewArtifact(Artifact newArtifact) {
		this.newArtifact = newArtifact;
	}

	public MainActivityView getParentView() {
		return parent;
	}

	public void setParentView(MainActivityView parent) {
		this.parent = parent;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
