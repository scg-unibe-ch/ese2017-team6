package ch.ese.team6.Service;

import ch.ese.team6.Exception.BadSizeException;
import ch.ese.team6.Exception.DupplicateEntryException;

public interface SampleDataService {
	
	void loadData() throws BadSizeException, DupplicateEntryException;
}
