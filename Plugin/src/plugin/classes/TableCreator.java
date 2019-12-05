package plugin.classes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class TableCreator {
	
	private IDoubleClickListener openArtifactInEditor = new IDoubleClickListener() {
		@Override
		public void doubleClick(DoubleClickEvent event) {
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			Artifact a = (Artifact)selection.getFirstElement();
			if(!a.getName().equals("Start")) {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				File f = new File(a.getFullPath());
				IFile fi = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(f.getPath()));
				IEditorDescriptor desc = PlatformUI.getWorkbench().
						getEditorRegistry().getDefaultEditor(f.getName());
				try {
					page.openEditor(new FileEditorInput(fi), desc.getId());
				} catch (PartInitException e) {
					e.printStackTrace();
				}	
			}
		}
	};

	public TableCreator(){		
	}
	
	public TableViewer createTable(Composite composite, String type, int columns) {
		TableViewer tableViewer = new TableViewer(composite, SWT.CHECK | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        switch(type) {
	        case "at":	createArtifactTypesColumns(composite,tableViewer); break;
	        case "dt":	createDependencyTypesColumns(composite,tableViewer); break;
	        case "tt":	createTaskTypesColumns(composite,tableViewer); break;
	        case "c":	createComponentColumns(composite,tableViewer); break;
	        case "a":	createArtifactColumns(composite,tableViewer); break;
	        case "activityA":	createArtifactColumns(composite,tableViewer); break;
	        case "d":	createDependencyColumns(composite,tableViewer); break;
	        case "t":	createTaskColumns(composite,tableViewer); break;
	        case "MactivityT": createMTaskColumns(composite,tableViewer); break;
	        case "activityT": createTaskColumns(composite,tableViewer); break;
	        case "g":	createGoalColumns(composite,tableViewer); break;
	        case "ua":	createArtifactColumns(composite,tableViewer); break;
	        case "pa":	createArtifactColumns(composite,tableViewer); break;
	        case "r":	createRuleColumns(composite,tableViewer); break;
	        default: break;        
        }
        final Table table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        tableViewer.setContentProvider(new ArrayContentProvider());
        switch(type) {
		    case "at":	tableViewer.setInput(Share.status.getArtifactTypes()); break;
		    case "dt":	tableViewer.setInput(Share.status.getDependencyTypes()); break;
		    case "tt":	tableViewer.setInput(Share.status.getTaskTypes()); break;
		    case "c":	tableViewer.setInput(Share.status.getComponents()); break;
		    case "a":	tableViewer.setInput(Share.status.getArtifacts()); break;
		    case "activityA":	tableViewer.setInput(Share.status.getActiveActivity().getArtifacts()); break;
		    case "d":	tableViewer.setInput(Share.status.getDependencies()); break;
		    //case "t":	tableViewer.setInput(Share.status.getTasks()); break;
		    case "MactivityT":	tableViewer.setInput(Share.status.getActiveActivity().getTasks()); break;
		    case "activityT":	tableViewer.setInput(Share.status.getActiveActivity().getTasks()); break;
		    case "g":	tableViewer.setInput(Share.status.getGoals()); break;
		    case "ua":	tableViewer.setInput(Share.status.getActiveTask().getUsed()); break;
		    case "pa":	tableViewer.setInput(Share.status.getActiveTask().getProduced()); break;
		    case "r":	tableViewer.setInput(Share.status.getRules()); break;
		    default: break;
        }  
       
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = columns;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.minimumHeight = 200;
        final GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        layoutData.minimumHeight = 200;
        
        tableViewer.getControl().setLayoutData(layoutData);	
       
        tableViewer.getTable().addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(event.button == 3) {
			    	TableItem t = tableViewer.getTable().getItem(new Point(event.x,event.y));
			    	t.setChecked(!t.getChecked());			        
				}
			}
		});	
  
        return tableViewer;
	}
	

	private void createArtifactTypesColumns(final Composite parent, final TableViewer viewer) {
        String[] titles = { "Name", "Extension", "Reward"};
        int[] bounds = { 100, 100, 100};

        // first column is for the first name
        TableViewerColumn col = createTableViewerColumn(viewer, titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                ArtifactType at = (ArtifactType) element;
                return at.getType();
            }
        });

        // second column is for the last name
        col = createTableViewerColumn(viewer, titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 ArtifactType at = (ArtifactType) element;
                 return at.getExtension();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 ArtifactType at = (ArtifactType) element;
                 return at.getReward().toString();
            }
        });
        
        viewer.addSelectionChangedListener(new MySelectionChangedListener(viewer, 0));
    }
	
	private void createDependencyTypesColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Origin","Target","Dependency","Multiplicity"};
        int[] bounds = { 100, 100, 100, 100};
     
        TableViewerColumn col = createTableViewerColumn(viewer, titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                DependencyType dt = (DependencyType) element;
                return dt.getOriginAT().getType();
            }
        });
    
        col = createTableViewerColumn(viewer, titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	DependencyType dt = (DependencyType) element;
                return dt.getTargetAT().getType();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	DependencyType dt = (DependencyType) element;
                return dt.getDependency();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[3], bounds[3], 3);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	DependencyType dt = (DependencyType) element;
            	if(dt.getMultiplicity() == 0) {
            		return "*";
            	}
            	else return Integer.toString(dt.getMultiplicity());
            }
        });
        
        viewer.addSelectionChangedListener(new MySelectionChangedListener(viewer, 2));
    }
	
	private void createTaskTypesColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Used Artifact Types","Produced Artifact Types","Dependencies","Description"};
        int[] bounds = { 100, 100, 100, 100};
     
        TableViewerColumn col = createTableViewerColumn(viewer, titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                TaskType tt = (TaskType) element;
                return Arrays.toString(tt.getUsed().toArray());
            }
        });
    
        col = createTableViewerColumn(viewer, titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 TaskType tt = (TaskType) element;
                 return Arrays.toString(tt.getProduced().toArray());
            }
        });
        
        col = createTableViewerColumn(viewer, titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	TaskType tt = (TaskType) element;
            	return Arrays.toString(tt.getDependencies().toArray());
            }
        });
        
        col = createTableViewerColumn(viewer, titles[3], bounds[3], 3);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	TaskType tt = (TaskType) element;
            	return tt.getDescription();
            }
        });
        
        viewer.addSelectionChangedListener(new MySelectionChangedListener(viewer, 3));
    }

    private TableViewerColumn createTableViewerColumn(TableViewer viewer, String title, int bound, final int colNumber) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }
	
	private void createComponentColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Name", "Type", "Default", "Path"};
        int[] bounds = { 100, 100, 100, 100};
     
        TableViewerColumn col = createTableViewerColumn(viewer, titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Component p = (Component) element;
                return p.getName();
            }
        });
    
        col = createTableViewerColumn(viewer, titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 Component p = (Component) element;
                 return p.getType().getType();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 Component p = (Component) element;
                 return p.getDefaultForType();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[3], bounds[3], 3);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 Component p = (Component) element;
                 return p.getPath();
            }
        });
        
        viewer.addSelectionChangedListener(new MySelectionChangedListener(viewer, 3));
    }
	
	private void createArtifactColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Name", "Type", "Status", "BeeingWorkedOn", "Reward", "Path" };
        int[] bounds = { 100, 100, 100, 100, 100, 100};
     
        TableViewerColumn col = createTableViewerColumn(viewer, titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Artifact a = (Artifact) element;
                return a.getName();
            }
        });
    
        col = createTableViewerColumn(viewer, titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	Artifact a = (Artifact) element;
                return a.getType().getType();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	Artifact a = (Artifact) element;
                return a.isComplete();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[3], bounds[3], 3);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	Artifact a = (Artifact) element;
                return a.isBeeingWorkedOn();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[4], bounds[4], 4);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	Artifact a = (Artifact) element;
                return a.getType().getReward().toString();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[5], bounds[5], 5);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	Artifact a = (Artifact) element;
                return a.getPath();
            }
        });
        
        viewer.addSelectionChangedListener(new MySelectionChangedListener(viewer, 5));
        viewer.addDoubleClickListener(openArtifactInEditor);
    }
	
	
	private void createDependencyColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Origin","Target","Dependency"};
        int[] bounds = { 100, 100, 100};
     
        TableViewerColumn col = createTableViewerColumn(viewer, titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Dependency d = (Dependency) element;
                return d.getOrigin().getName();
            }
        });
    
        col = createTableViewerColumn(viewer, titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 Dependency d = (Dependency) element;
                 return d.getTarget().getName();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 Dependency d = (Dependency) element;
                 return d.getDependency().getDependency();
            }
        });
        
        viewer.addSelectionChangedListener(new MySelectionChangedListener(viewer, 2));
    }
	
	private void createTaskColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Used Artifacts","Produced Artifacts","Dependencies","Description","Status","User","UserStatus"};
        int[] bounds = { 100, 100, 100, 100, 100, 100, 100};
     
        TableViewerColumn col = createTableViewerColumn(viewer, titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Task t = (Task) element;
                return Arrays.toString(t.getUsed().toArray());
            }
        });
    
        col = createTableViewerColumn(viewer, titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 Task t = (Task) element;
                 return Arrays.toString(t.getProduced().toArray());
            }
        });
        
        col = createTableViewerColumn(viewer, titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	Task t = (Task) element;
                return Arrays.toString(t.getTaskType().getDependencies().toArray());
            }
        });
        
        col = createTableViewerColumn(viewer, titles[3], bounds[3], 3);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	Task t = (Task) element;
                return t.getDescription();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[4], bounds[4], 4);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	Task t = (Task) element;
                return t.getStatus();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[5], bounds[5], 5);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	Task t = (Task) element;
                return t.getUser();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[6], bounds[6], 6);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	Task t = (Task) element;
                return t.getUserStatus();
            }
        });
        
        viewer.addSelectionChangedListener(new MySelectionChangedListener(viewer, 3));
    }
	
	private void createMTaskColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Description","Status","User"};
        int[] bounds = { 100, 100, 100};
     
        TableViewerColumn col = createTableViewerColumn(viewer, titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Task t = (Task) element;
                return t.getDescription();
            }
        });
        col.getColumn().setWidth(300);
    
        col = createTableViewerColumn(viewer, titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 Task t = (Task) element;
                 return t.getStatus();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	Task t = (Task) element;
                return t.getUser();
            }
        });
        
        viewer.addSelectionChangedListener(new MySelectionChangedListener(viewer, 0));
    }
		
	private void createGoalColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Description","ActivityNames","Due","Status"};
        int[] bounds = { 100, 100, 100, 100};
     
        TableViewerColumn col = createTableViewerColumn(viewer, titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Goal g = (Goal) element;
                return g.getDescription();
            }
        });
    
        col = createTableViewerColumn(viewer, titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 Goal g = (Goal) element;
                 return Arrays.toString(g.getActivityNames().toArray());
            }
        });
        
        col = createTableViewerColumn(viewer, titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 Goal g = (Goal) element;
            	 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                 return format.format(g.getDue());
            }
        });
        
        col = createTableViewerColumn(viewer, titles[3], bounds[3], 3);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 Goal g = (Goal) element;
                 return g.getStatus();
            }
        });
        
        viewer.addSelectionChangedListener(new MySelectionChangedListener(viewer, 0));
    }
	
	private void createRuleColumns(final Composite parent, final TableViewer viewer) {
        String[] titles = { "Artifact Type", "Rule Name", "Rule Description"};
        int[] bounds = { 100, 100, 100};

        TableViewerColumn col = createTableViewerColumn(viewer, titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                Rule r = (Rule) element;
                return r.getArtifactType().getType();
            }
        });

        col = createTableViewerColumn(viewer, titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 Rule r = (Rule) element;
                 return r.getRuleType().getName();
            }
        });
        
        col = createTableViewerColumn(viewer, titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 Rule r = (Rule) element;
                 return r.getRuleType().getDescription();
            }
        });
        
        viewer.addSelectionChangedListener(new MySelectionChangedListener(viewer, 0));
    }
}