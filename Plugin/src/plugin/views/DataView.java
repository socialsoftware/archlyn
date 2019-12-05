package plugin.views;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableItem;

import plugin.classes.Artifact;
import plugin.classes.Goal;
import plugin.classes.Share;
import plugin.classes.TableCreator;
import plugin.classes.View;
import plugin.shells.ApplyDataTemplateShell;
import plugin.shells.EditArtifactShell;
import plugin.shells.EditComponentShell;
import plugin.shells.EditGoalShell;
import plugin.shells.NewDependencyShell;

public class DataView extends View {
	
	private SelectionListener listenerMIdataTemplate = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Shell shellApplyDataTemplate = new ApplyDataTemplateShell(views.getDisplay(), true);
			shellApplyDataTemplate.setLocation(getDisplay().getCursorLocation().x-300,getDisplay().getCursorLocation().y);
			shellApplyDataTemplate.open();
			shellApplyDataTemplate.layout();				
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {			
		}		
	};

	public DataView() {	
	}

	public void create(Composite compositeMain) {
		
	    TabFolder views = new TabFolder(compositeMain, SWT.BORDER);
		views.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		
		for(TabItem ti: views.getItems()) {
			if(ti.getText().equals("Data")) {
				ti.dispose();
				Share.dataView.stopRefresh();
				break;
			}
		}
		
		TabItem viewDV = new TabItem(views, SWT.NULL);

		//structure

		scrolledComposite = new ScrolledComposite(views, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite compositeDV = new Composite(scrolledComposite, SWT.NONE);
		compositeDV.setLayout(new GridLayout(1, false));
		scrolledComposite.setContent(compositeDV);
		scrolledComposite.setSize(compositeDV.computeSize(SWT.DEFAULT, SWT.DEFAULT));;

		MenuItem miApplyDataTemplateC;
		MenuItem miApplyDataTemplateA;
		MenuItem miApplyDataTemplateD;	

		final TabFolder tabs = new TabFolder(compositeDV, SWT.BORDER);		    
		tabs.setSize(400, 200);	

		//Component Tab	    
		Composite compositeC = new Composite(tabs, SWT.NONE);
		compositeC.setLayout(new GridLayout(1, true));	
		TableViewer tableC = new TableCreator().createTable(compositeC, "c", 4);		 
		Menu menuC = new Menu(tableC.getTable());
		MenuItem miEditC = new MenuItem(menuC, SWT.NONE);		
		new MenuItem(menuC, SWT.SEPARATOR);		
		MenuItem miRescanC = new MenuItem(menuC, SWT.NONE);
		new MenuItem(menuC, SWT.SEPARATOR);	
		TabItem tabC = new TabItem(tabs, SWT.NULL);

		//Artifact Tab
		Composite compositeA = new Composite(tabs, SWT.NONE);
		compositeA.setLayout(new GridLayout(1, true));
		TableViewer tableA = new TableCreator().createTable(compositeA, "a", 6);	
		Menu menuA = new Menu(tableA.getTable());
		MenuItem miEditA = new MenuItem(menuA, SWT.NONE);	
		MenuItem miRemoveA = new MenuItem(menuA, SWT.NONE);	
		new MenuItem(menuA, SWT.SEPARATOR);		
		MenuItem miCompleteA = new MenuItem(menuA, SWT.NONE);	
		new MenuItem(menuA, SWT.SEPARATOR);				
		MenuItem miRescanA = new MenuItem(menuA, SWT.NONE);
		new MenuItem(menuA, SWT.SEPARATOR);	
		TabItem tabA = new TabItem(tabs, SWT.NULL);

		//Dependencies Tab	    
		Composite compositeD = new Composite(tabs, SWT.NONE);
		compositeD.setLayout(new GridLayout(1, true));
		TableViewer tableD = new TableCreator().createTable(compositeD, "d", 3);	
		Menu menuD = new Menu(tableD.getTable());
		MenuItem miNewD = new MenuItem(menuD, SWT.NONE);	
		MenuItem miEditD = new MenuItem(menuD, SWT.NONE);	
		MenuItem miRemoveD = new MenuItem(menuD, SWT.NONE);	
		new MenuItem(menuD, SWT.SEPARATOR);	
		TabItem tabD = new TabItem(tabs, SWT.NULL);

		/*
		//Tasks Tab	   
		Composite compositeT = new Composite(tabs, SWT.NONE);
		compositeT.setLayout(new GridLayout(1, true));
		TableViewer tableT = new TableCreator().createTable(compositeT, "t", 7);		
		Menu menuT = new Menu(tableT.getTable());
		MenuItem miSuggestT = new MenuItem(menuT, SWT.NONE);	
		MenuItem miEditT = new MenuItem(menuT, SWT.NONE);	
		MenuItem miRemoveT = new MenuItem(menuT, SWT.NONE);	
		new MenuItem(menuT, SWT.SEPARATOR);	
		MenuItem miShowCompletedT = new MenuItem(menuT, SWT.CHECK);	
		new MenuItem(menuT, SWT.SEPARATOR);	
		TabItem tabT = new TabItem(tabs, SWT.NULL);
		*/
		
		//Goals Tab	    
		Composite compositeG = new Composite(tabs, SWT.NONE);
		compositeG.setLayout(new GridLayout(1, true));
		TableViewer tableG = new TableCreator().createTable(compositeG, "g", 2);
		Composite compositeNewG = new Composite(compositeG, SWT.NONE);
		compositeNewG.setLayout(new GridLayout(1, true));		
		Menu menuG = new Menu(tableG.getTable());
		MenuItem miNewG = new MenuItem(menuG, SWT.NONE);	
		MenuItem miEditG = new MenuItem(menuG, SWT.NONE);	
		MenuItem miRemoveG = new MenuItem(menuG, SWT.NONE);				
		new MenuItem(menuG, SWT.SEPARATOR);	
		MenuItem miSwitchStatusG = new MenuItem(menuG, SWT.NONE);				
		new MenuItem(menuG, SWT.SEPARATOR);	
		TabItem tabG = new TabItem(tabs, SWT.NULL);		

		//view content		

		tabs.setVisible(true);
		widgets.add(tabs);	  

		//Component Tab	    

		viewers.add(tableC);    

		tableC.getTable().addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				menuC.setVisible(true);
			}
		});

		miEditC.setText("Edit Package");
		miEditC.addSelectionListener(widgetSelectedAdapter(g -> {
			for(TableItem t: tableC.getTable().getItems()) {
				if (t.getChecked()) {
					Shell shellEditC = new EditComponentShell(compositeMain.getDisplay(), tableC);
					shellEditC.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
					shellEditC.open();
					shellEditC.layout();
					break;
				}
			}
		}));				

		miRescanC.setText ("Rescan Packages");
		miRescanC.addSelectionListener(widgetSelectedAdapter(f -> {
			Share.status.rescanArtifacts();	
			Share.project.saveStatus();						
		}));

		tabC.setText("Packages");
		tabC.setControl(compositeC);			

		//Artifact Tab   

		viewers.add(tableA); 

		tableA.getTable().addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				menuA.setVisible(true);
			}
		});

		miRescanA.setText ("Rescan Artifacts");
		miRescanA.addSelectionListener(widgetSelectedAdapter(f -> {
			Share.status.rescanArtifacts();	
			Share.project.saveStatus();						
		}));

		miRemoveA.setText ("Remove Artifact");
		miRemoveA.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem a: tableA.getTable().getItems()) {
				if (a.getChecked() && !a.getText(0).equals("Start")) {
					Artifact checked = Share.status.findArtifact(a.getText(5));
					Share.status.getArtifacts().remove(checked);						
					a.setChecked(false);
				}
			}				
			tableA.refresh();
		}));

		miEditA.setText("Edit Artifact");		
		miEditA.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem t: tableA.getTable().getItems()) {
				if (t.getChecked()) {
					Shell shellEditA = new EditArtifactShell(compositeMain.getDisplay(), tableA);
					shellEditA.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
					shellEditA.open();
					shellEditA.layout();
					break;
				}
			}
		}));	

		miCompleteA.setText("Switch Artifact Status");
		miCompleteA.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem a: tableA.getTable().getItems()) {
				if (a.getChecked() && !a.getText(0).equals("Start")) {
					Artifact checked = Share.status.findArtifact(a.getText(5));
					if (checked.getComplete()) {
						checked.setComplete(false);
					} else {
						checked.setComplete(true);
					}
					a.setChecked(false);
				}
			}					
			Share.project.saveStatus();
		}));

		tabA.setText("Artifacts");
		tabA.setControl(compositeA);			

		//Dependencies Tab		

		viewers.add(tableD); 	 

		tableD.getTable().addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				menuD.setVisible(true);
			}
		});

		miNewD.setText ("New Dependency");
		miNewD.addSelectionListener(widgetSelectedAdapter(f -> {
			Shell shellNewD = new NewDependencyShell(compositeMain.getDisplay(), tableD, false);
			shellNewD.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
			shellNewD.open();
			shellNewD.layout();				
		}));
		
		miEditD.setText ("Edit Dependency");
		miEditD.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem t: tableD.getTable().getItems()) {
				if (t.getChecked()) {
					Shell shellEditD = new NewDependencyShell(compositeMain.getDisplay(), tableD, true);
					shellEditD.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
					shellEditD.open();
					shellEditD.layout();	
				}
			}
		}));
		
		miRemoveD.setText ("Remove Dependency");
		miRemoveD.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem d: tableD.getTable().getItems()) {
				if (d.getChecked()) {
					Share.status.getDependencies().remove(Share.status.findDependency(d.getText(0),d.getText(1),d.getText(2)));
					d.setChecked(false);
				}
			}
			Share.project.saveStatus();	
		}));
	
		tabD.setText("Dependencies");
		tabD.setControl(compositeD);

		/*
		//Tasks Tab		

		viewers.add(tableT);	
		
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
	

		miSuggestT.setText ("Suggest New Tasks");
		miSuggestT.addSelectionListener(widgetSelectedAdapter(f -> {
			new TaskSuggester().suggest();					
		}));
		
		miEditT.setText ("Edit Task");
		miEditT.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem t: tableT.getTable().getItems()) {
				if (t.getChecked()) {
					Shell shellEditT = new EditTaskShell(compositeMain.getDisplay(), tableT);
					shellEditT.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
					shellEditT.open();
					shellEditT.layout();	
				}
			}
		}));
		
		miRemoveT.setText ("Remove All Tasks");
		miRemoveT.addSelectionListener(widgetSelectedAdapter(f -> {
			Share.status.getTasks().clear();
			tableT.setInput(Share.status.getTasks());
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
			tableT.setInput(Share.status.getTasksOfStatus(Share.status.getTasks(),Task.getActiveFilter()));
		}));

		tabT.setText("Tasks");
		tabT.setControl(compositeT);	
		*/	
		
		//Goals Tab

		viewers.add(tableG); 
		
		tableG.getTable().addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				menuG.setVisible(true);
			}
		});

		miNewG.setText ("New Goal");
		miNewG.addSelectionListener(widgetSelectedAdapter(f -> {
			Shell shellNewG = new EditGoalShell(compositeMain.getDisplay(), tableG, false);
			shellNewG.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
			shellNewG.open();
			shellNewG.layout();				
		}));
		
		miEditG.setText ("Edit Goal");
		miEditG.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem t: tableG.getTable().getItems()) {
				if (t.getChecked()) {
					Shell shellEditG = new EditGoalShell(compositeMain.getDisplay(), tableG, true);
					shellEditG.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
					shellEditG.open();
					shellEditG.layout();	
				}
			}
		}));
		
		miRemoveG.setText ("Remove Goal");
		miRemoveG.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem g: tableG.getTable().getItems()) {
				if (g.getChecked()) {
					Goal goal = Share.status.findGoal(g.getText(0));
					Share.status.getGoals().remove(goal);
					g.setChecked(false);
					if(Share.status.getGithub() != null && Share.status.getGithub().getClient() != null) {
						Share.status.getGithub().deleteMilestone(goal);
					}
				}
			}
			Share.project.saveStatus();	
		}));		
		
		miSwitchStatusG.setText ("Switch Status");
		miSwitchStatusG.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem g: tableG.getTable().getItems()) {
				if (g.getChecked()) {
					Goal checked = Share.status.findGoal(g.getText(0));
					if (checked.getStatus().equals("open")) {
						checked.setStatus("closed");
					} else {
						checked.setStatus("open");
					}
					g.setChecked(false);
				}
			}					
			Share.project.saveStatus();
		}));
		
		tabG.setText("Goals");
		tabG.setControl(compositeG);	

		miApplyDataTemplateC = new MenuItem(menuC, SWT.NONE);	
		miApplyDataTemplateA = new MenuItem(menuA, SWT.NONE);
		miApplyDataTemplateD = new MenuItem(menuD, SWT.NONE);
		//miApplyDataTemplateT = new MenuItem(menuT, SWT.NONE);
		//miApplyDataTemplateG = new MenuItem(menuG, SWT.NONE);	
		miApplyDataTemplateC.setText ("Apply Data Template");
		miApplyDataTemplateA.setText ("Apply Data Template");
		miApplyDataTemplateD.setText ("Apply Data Template");
		//miApplyDataTemplateT.setText ("Apply Data Template");
		//miApplyDataTemplateG.setText ("Apply Data Template");	
		miApplyDataTemplateC.addSelectionListener(listenerMIdataTemplate);
		miApplyDataTemplateA.addSelectionListener(listenerMIdataTemplate);
		miApplyDataTemplateD.addSelectionListener(listenerMIdataTemplate);
		//miApplyDataTemplateT.addSelectionListener(listenerMIdataTemplate);
		//miApplyDataTemplateG.addSelectionListener(listenerMIdataTemplate);
		
		viewDV.setText("Data");
		viewDV.setControl(scrolledComposite);	
		//viewDV.setImage(new Image(views.getDisplay(), Share.project.getLocationBase()+"/icons/dv.png"));	
		
		startRefresh();
	}
}