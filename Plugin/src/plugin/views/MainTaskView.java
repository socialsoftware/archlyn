package plugin.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import plugin.classes.View;
import plugin.composites.TaskComposite;

public class MainTaskView extends View {
	
	public MainTaskView() {	
	}
	
	public void create(Composite compositeMain) {
		
		scrolledComposite = new ScrolledComposite(compositeMain, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        
		TaskComposite taskComposite = new TaskComposite(scrolledComposite, SWT.NONE);
		taskComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		taskComposite.setParentView(this);
		taskComposite.createTaskView();		
	
		scrolledComposite.setContent(taskComposite);
		scrolledComposite.setMinHeight(400);
		scrolledComposite.setMinWidth(400);		
        scrolledComposite.setSize(taskComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));   
        
        startRefresh();
	}
}