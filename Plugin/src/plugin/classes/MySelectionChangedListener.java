package plugin.classes;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

public class MySelectionChangedListener implements ISelectionChangedListener {
	
	private TableViewer tableViewer;
	private int identifyingColumn;
	
	public MySelectionChangedListener(TableViewer tableViewer, int identifyingColumn){
		this.tableViewer = tableViewer;
		this.identifyingColumn = identifyingColumn;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		System.out.println("Selection");		
		for(TableItem i: tableViewer.getTable().getItems()){
			if(i.getText(identifyingColumn).equals(event.getSelection().toString().substring(1, event.getSelection().toString().length()-1))){
				System.out.println(i.getText() + " " + i.getChecked());
				i.setChecked(!i.getChecked());
				System.out.println(i.getText() + " " + i.getChecked());				 
			}
		}				
	}
}
