package edu.mwsu.cs.se.mad.common;

/**
 * 
 * The SelectionCriteria model instance adapter class. This class creates a
 * single instance of the SelectionCriteria class instance and make it available
 * for other classes
 * 
 */
public class SelectionCriateriaAdapter /*
										 * extends Observable if MVC is
										 * implemented
										 */{

	/**
	 * the single instance of the selection criteria
	 */
	private static SelectionCriteria selectionData = new SelectionCriteria(
			null, null, null);
	private boolean stateChanged = false;

	protected void setChanged() {
		setStateChanged(true);
	}

	public boolean hasChanged() {
		return isStateChanged();
	}

	public SelectionCriteria getSelectionData() {
		return selectionData;
	}

	public void setSelectionData(SelectionCriteria selectionData) {
		SelectionCriateriaAdapter.selectionData = selectionData;
		setChanged();
	}

	/**
	 * @param stateChanged
	 *            the stateChanged to set
	 */
	public void setStateChanged(boolean stateChanged) {
		this.stateChanged = stateChanged;
	}

	/**
	 * @return the stateChanged
	 */
	public boolean isStateChanged() {
		return stateChanged;
	}

}
