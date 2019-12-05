package plugin.shells;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import plugin.classes.TableCreator;
import plugin.views.MainActivityView;

public class ArtifactsInActivityShell extends Shell {

	public ArtifactsInActivityShell(Display display, MainActivityView parent) {
		super(display, SWT.SHELL_TRIM);
		createContents(parent);
	}

	protected void createContents(MainActivityView parent) {
		setText("Artifacts in Activity");
		setSize(300, 300);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 15;
		composite.setLayout(gl_composite);				
		TableViewer tableA = new TableCreator().createTable(composite, "activityA", 6);			
		Button buttonClose = new Button (composite, SWT.CENTER);
		buttonClose.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));		
		
		buttonClose.setText ("OK");
		buttonClose.addSelectionListener(widgetSelectedAdapter(g -> {	
			this.dispose();
		}));
		
		this.pack();
		this.setMinimumSize(500, 300);
		this.redraw();
		this.update();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}