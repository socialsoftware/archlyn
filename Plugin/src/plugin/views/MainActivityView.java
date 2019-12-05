package plugin.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import plugin.classes.View;
import plugin.composites.ActivityComposite;

public class MainActivityView extends View {
	
	public MainActivityView() {	
	}
	
	public void create(Composite compositeMain) {
		
		scrolledComposite = new ScrolledComposite(compositeMain, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        
		ActivityComposite activityComposite = new ActivityComposite(scrolledComposite, SWT.NONE);
		activityComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		activityComposite.setParentView(this);
		activityComposite.createTasksView();		
	
		scrolledComposite.setContent(activityComposite);
		scrolledComposite.setMinHeight(400);
		scrolledComposite.setMinWidth(400);		
        
        startRefresh();
	}
}