package ch.ese.team6.Model;

import ch.ese.team6.Exception.InconsistentOrderStateException;

public interface IDelivarable {
	public int getWeight();
	public int getSize();
	public int getOpenSize();
	public int getOpenWeight();
	public Address getAddress();
	public void setRoute(Route r);
	public void schedule() throws InconsistentOrderStateException;
	public void acceptDelivery() throws InconsistentOrderStateException;
	public void rejectDelivery() throws InconsistentOrderStateException;
}
