package plugin.shells;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import plugin.classes.CustomMetadata;
import plugin.classes.ProjectCreator;
import plugin.classes.Share;

public class ApplyDataTemplateShell extends Shell {

	public ApplyDataTemplateShell(Display display, boolean data) {
		super(display, SWT.SHELL_TRIM);
		createContents(data);
	}

	protected void createContents(boolean data) {
		if(data) {
			setText("Apply Data Template");
			
			setSize(300, 300);
			setLayout(new GridLayout(1, false));

			Composite composite = new Composite(this, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
			GridLayout gl_composite = new GridLayout(1, false);
			gl_composite.verticalSpacing = 15;
			composite.setLayout(gl_composite);

			Label labelSelectDataTemplate = new Label (composite, SWT.NONE);
			labelSelectDataTemplate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			Combo comboSelectDataTemplate = new Combo (composite, SWT.READ_ONLY);
			GridData gd_comboSelectDataTemplate = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
			gd_comboSelectDataTemplate.minimumWidth = 110;
			comboSelectDataTemplate.setLayoutData(gd_comboSelectDataTemplate);		
			Button buttonApplyDataTemplate = new Button (composite, SWT.CENTER);
			buttonApplyDataTemplate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

			labelSelectDataTemplate.setText("Data Template");
			comboSelectDataTemplate.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent event) {
					comboSelectDataTemplate.setItems(new ProjectCreator().getProjectTypesSA());
				}
				public void focusLost(FocusEvent event){
				}
			});	

			buttonApplyDataTemplate.setText("OK");				   
			buttonApplyDataTemplate.addSelectionListener(widgetSelectedAdapter(e -> {
				if(!comboSelectDataTemplate.getText().equals("")) {
					if(data) {
						//new ProjectCreator().applyDataTemplate(comboSelectDataTemplate.getText(),true);
					}
					else {
						new ProjectCreator().applyMetadataTemplate(comboSelectDataTemplate.getText(),true);
						Share.project.saveStatus();			
						Share.metadataView.getViewers().get(0).setInput(Share.status.getArtifactTypes());
						Share.metadataView.getViewers().get(1).setInput(Share.status.getDependencyTypes());
						Share.metadataView.getViewers().get(2).setInput(Share.status.getTaskTypes());
						Share.metadataView.getViewers().get(3).setInput(Share.status.getRules()); 
						Share.status.getTaskContext().clear();
					}			
					Share.project.saveStatus();	
				}
				this.dispose();
			}));
			
			this.pack();
			this.setMinimumSize(500, 300);
			this.redraw();
			this.update();
		}
		else {
			setText("Save Metadata Template");
			
			setSize(300, 300);
			setLayout(new GridLayout(1, false));

			Composite composite = new Composite(this, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
			GridLayout gl_composite = new GridLayout(1, false);
			gl_composite.verticalSpacing = 15;
			composite.setLayout(gl_composite);

			Label labelChooseATname = new Label (composite, SWT.NONE);
			labelChooseATname.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			Text textChooseATname = new Text (composite, SWT.NONE);
			GridData gd_textChooseATname = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
			gd_textChooseATname.minimumWidth = 140;		
			textChooseATname.setLayoutData(gd_textChooseATname);	
			Button buttonApplyDataTemplate = new Button (composite, SWT.CENTER);
			buttonApplyDataTemplate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

			labelChooseATname.setText("Metadata Template Name:");
			textChooseATname.setText("");

			buttonApplyDataTemplate.setText("OK");				   
			buttonApplyDataTemplate.addSelectionListener(widgetSelectedAdapter(e -> {
				if(!textChooseATname.getText().equals("")) {
					Share.status.getCustomMetadata().add(new CustomMetadata(textChooseATname.getText()));
					Share.project.saveStatus();	
				}
				this.dispose();
			}));
			
			this.pack();
			this.setMinimumSize(500, 300);
			this.redraw();
			this.update();
		}
		
		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}