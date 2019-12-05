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

import plugin.classes.Share;
import plugin.classes.TableCreator;
import plugin.classes.View;
import plugin.shells.ApplyDataTemplateShell;
import plugin.shells.EditArtifactTypeShell;
import plugin.shells.EditDependencyTypeShell;
import plugin.shells.EditRuleShell;
import plugin.shells.EditTaskTypeShell;

public class MetadataView extends View {
	
	private SelectionListener listenerMImetadataTemplate = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Shell shellApplyMetadataTemplate = new ApplyDataTemplateShell(getDisplay(), false);
			shellApplyMetadataTemplate.setLocation(getDisplay().getCursorLocation().x-300,getDisplay().getCursorLocation().y);
			shellApplyMetadataTemplate.open();
			shellApplyMetadataTemplate.layout();	
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {			
		}		
	};
	
	public MetadataView(){
	}

	public void create(Composite compositeMain) {
		
		TabFolder views = (TabFolder) compositeMain.getChildren()[0];
		if(views == null) {
		 	views = new TabFolder(compositeMain, SWT.BORDER);
			views.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		}
		
		for(TabItem ti: views.getItems()) {
			if(ti.getText().equals("Metadata")) {
				ti.dispose();
				Share.metadataView.stopRefresh();
				break;
			}
		}
		
		TabItem viewMDV = new TabItem(views, SWT.NULL);
		
			//structure		
		scrolledComposite = new ScrolledComposite(views, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        
		Composite compositeMDV = new Composite(scrolledComposite, SWT.NONE);
		compositeMDV.setLayout(new GridLayout(1, false));
		scrolledComposite.setContent(compositeMDV);
        scrolledComposite.setSize(compositeMDV.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        
        MenuItem miApplyMetadataTemplateAT;
		MenuItem miApplyMetadataTemplateDT;
		MenuItem miApplyMetadataTemplateTT;
		MenuItem miApplyMetadataTemplateR;	
				
		final TabFolder tabs = new TabFolder(compositeMDV, SWT.BORDER);		    
	    tabs.setSize(400, 200);	
	    
	    //Artifact Type Tab  
	    Composite compositeAT = new Composite(tabs, SWT.NONE);
		compositeAT.setLayout(new GridLayout(1, true));				
		TableViewer tableAT = new TableCreator().createTable(compositeAT, "at", 3);
		Menu menuAT = new Menu(tableAT.getTable());
		MenuItem miNewAT = new MenuItem(menuAT, SWT.NONE);	
		MenuItem miEditAT = new MenuItem(menuAT, SWT.NONE);	
		MenuItem miRemoveAT = new MenuItem(menuAT, SWT.NONE);		
		new MenuItem(menuAT, SWT.SEPARATOR);				
		TabItem tabAT = new TabItem(tabs, SWT.NULL);
		
		//Dependency Types Tab	
		Composite compositeDT = new Composite(tabs, SWT.NONE);
		compositeDT.setLayout( new GridLayout(1, true));
		TableViewer tableDT = new TableCreator().createTable(compositeDT, "dt", 4);
		Menu menuDT = new Menu(tableDT.getTable());
		MenuItem miNewDT = new MenuItem(menuDT, SWT.NONE);	
		MenuItem miEditDT = new MenuItem(menuDT, SWT.NONE);	
		MenuItem miRemoveDT = new MenuItem(menuDT, SWT.NONE);		
		new MenuItem(menuDT, SWT.SEPARATOR);		
		TabItem tabDT = new TabItem(tabs, SWT.NULL);
		
		//Task Type Tab		
		Composite compositeTT = new Composite(tabs, SWT.NONE);
		compositeTT.setLayout(new GridLayout(1, true));
		TableViewer tableTT = new TableCreator().createTable(compositeTT, "tt", 4);
		Menu menuTT = new Menu(tableTT.getTable());
		MenuItem miNewTT = new MenuItem(menuTT, SWT.NONE);	
		MenuItem miEditTT = new MenuItem(menuTT, SWT.NONE);	
		MenuItem miRemoveTT = new MenuItem(menuTT, SWT.NONE);		
		new MenuItem(menuTT, SWT.SEPARATOR);		
		TabItem tabTT = new TabItem(tabs, SWT.NULL);
		
		//Rules Tab  
	    Composite compositeR = new Composite(tabs, SWT.NONE);
		compositeR.setLayout(new GridLayout(1, true));				
		TableViewer tableR = new TableCreator().createTable(compositeR, "r", 2);
		Menu menuR = new Menu(tableR.getTable());
		MenuItem miNewR = new MenuItem(menuR, SWT.NONE);	
		MenuItem miRemoveR = new MenuItem(menuR, SWT.NONE);		
		new MenuItem(menuR, SWT.SEPARATOR);		
		TabItem tabR = new TabItem(tabs, SWT.NULL);
		
			//view content
	
	    tabs.setVisible(true);
	    widgets.add(tabs);			
				    
		//Artifact Type Tab    
	    
        viewers.add(tableAT);	
        
        tableAT.getTable().addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				menuAT.setVisible(true);
			}
		});
        
        miNewAT.setText("New Artifact Type");		
		miNewAT.addSelectionListener(widgetSelectedAdapter(f -> {
			Shell shellEditAT = new EditArtifactTypeShell(compositeMain.getDisplay(), tableAT, false);
			shellEditAT.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
			shellEditAT.open();
			shellEditAT.layout();
		}));	

		miEditAT.setText("Edit Artifact Type");		
		miEditAT.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem t: tableAT.getTable().getItems()) {
				if (t.getChecked() && !t.getText(0).equals("Start")) {
					Shell shellEditAT = new EditArtifactTypeShell(compositeMain.getDisplay(), tableAT, true);
					shellEditAT.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
					shellEditAT.open();
					shellEditAT.layout();
					break;
				}
			}
		}));	

		miRemoveAT.setText ("Remove Artifact Type");
		miRemoveAT.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem at: tableAT.getTable().getItems()) {
				if (at.getChecked() && !at.getText(0).equals("Start")) {
					Share.status.getArtifactTypes().remove(Share.status.findArtifactType(at.getText()));
					at.setChecked(false);
				}
			}
			Share.project.saveStatus();
		}));
		
	    tabAT.setText("Artifact Types");
	    tabAT.setControl(compositeAT);			
	    
	    //Dependency Types Tab	
	    
	    viewers.add(tableDT);				
	    
	    tableDT.getTable().addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				menuDT.setVisible(true);
			}
		});
        
        miNewDT.setText("New Dependency Type");		
		miNewDT.addSelectionListener(widgetSelectedAdapter(f -> {
			Shell shellEditDT = new EditDependencyTypeShell(compositeMain.getDisplay(), tableDT, false);
			shellEditDT.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
			shellEditDT.open();
			shellEditDT.layout();
		}));	

		miEditDT.setText("Edit Dependency Type");		
		miEditDT.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem t: tableDT.getTable().getItems()) {
				if (t.getChecked()) {
					Shell shellEditDT = new EditDependencyTypeShell(compositeMain.getDisplay(), tableDT, true);
					shellEditDT.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
					shellEditDT.open();
					shellEditDT.layout();
					break;
				}
			}
		}));	

		miRemoveDT.setText ("Remove Dependency Type");
		miRemoveDT.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem dt: tableDT.getTable().getItems()) {
				if (dt.getChecked()) {
					Share.status.getDependencyTypes().remove(Share.status.findDependencyType(dt.getText(2)));
					dt.setChecked(false);
				}
			}
			Share.project.saveStatus();		
		}));
		
	    tabDT.setText("Dependency Types");
	    tabDT.setControl(compositeDT);
	    
	    //Task Type Tab	    
	
        viewers.add(tableTT);
        
        tableTT.getTable().addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				menuTT.setVisible(true);
			}
		});
        
        miNewTT.setText("New Task Type");		
		miNewTT.addSelectionListener(widgetSelectedAdapter(f -> {
			Shell shellEditTT = new EditTaskTypeShell(compositeMain.getDisplay(), tableTT, false);
			shellEditTT.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
			shellEditTT.open();
			shellEditTT.layout();
		}));	

		miEditTT.setText("Edit Task Type");		
		miEditTT.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem t: tableTT.getTable().getItems()) {
				if (t.getChecked()) {
					Shell shellEditTT = new EditTaskTypeShell(compositeMain.getDisplay(), tableTT, true);
					shellEditTT.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
					shellEditTT.open();
					shellEditTT.layout();
					break;
				}
			}
		}));	

		miRemoveTT.setText ("Remove Task Type");
		miRemoveTT.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem tt: tableTT.getTable().getItems()) {
				if (tt.getChecked()) {
					Share.status.getTaskTypes().remove(Share.status.findTaskType(tt.getText(3)));
					tt.setChecked(false);
				}
			}
			Share.project.saveStatus();			
		}));
		
	    tabTT.setText("Task Types");
	    tabTT.setControl(compositeTT);	
	    
	    //Rules Tab    
	    
        viewers.add(tableR);
        
        tableR.getTable().addListener(SWT.MenuDetect, new Listener() {
			@Override
			public void handleEvent(Event event) {
				menuR.setVisible(true);
			}
		});
        
        miNewR.setText("New Rule");		
		miNewR.addSelectionListener(widgetSelectedAdapter(f -> {
			Shell shellEditR = new EditRuleShell(compositeMain.getDisplay(), tableR);
			shellEditR.setLocation(this.getDisplay().getCursorLocation().x-300,this.getDisplay().getCursorLocation().y);
			shellEditR.open();
			shellEditR.layout();
		}));	

		miRemoveR.setText ("Remove Rule");
		miRemoveR.addSelectionListener(widgetSelectedAdapter(f -> {
			for(TableItem r: tableR.getTable().getItems()) {
				if (r.getChecked()) {
					Share.status.getRules().remove(Share.status.findRule(r.getText(0),r.getText(1)));
					r.setChecked(false);
				}
			}
			Share.project.saveStatus();		
		}));			
		
	    tabR.setText("Rules");
	    tabR.setControl(compositeR);	
	    
	    miApplyMetadataTemplateAT = new MenuItem(menuAT, SWT.NONE);	
		miApplyMetadataTemplateDT = new MenuItem(menuDT, SWT.NONE);
		miApplyMetadataTemplateTT = new MenuItem(menuTT, SWT.NONE);
		miApplyMetadataTemplateR = new MenuItem(menuR, SWT.NONE);
		miApplyMetadataTemplateAT.setText ("Save Metadata Template");
		miApplyMetadataTemplateDT.setText ("Save Metadata Template");
		miApplyMetadataTemplateTT.setText ("Save Metadata Template");
		miApplyMetadataTemplateR.setText ("Save Metadata Template");
		miApplyMetadataTemplateAT.addSelectionListener(listenerMImetadataTemplate);
		miApplyMetadataTemplateDT.addSelectionListener(listenerMImetadataTemplate);
		miApplyMetadataTemplateTT.addSelectionListener(listenerMImetadataTemplate);
		miApplyMetadataTemplateR.addSelectionListener(listenerMImetadataTemplate);
	   
		viewMDV.setText("Metadata");
		viewMDV.setControl(scrolledComposite);	
		//viewMDV.setImage(new Image(views.getDisplay(), Share.project.getLocationBase()+ "/icons/dv.png"));		
		startRefresh();
	}
}