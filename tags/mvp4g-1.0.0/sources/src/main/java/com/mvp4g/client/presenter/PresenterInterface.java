package com.mvp4g.client.presenter;

import com.mvp4g.client.event.EventBus;

/**
 * Interface that defines a presenter.<br/>
 * <br/>
 * This interface provides getter and setter for a view and an event bus.
 * 
 * @author plcoirier
 * 
 */
public interface PresenterInterface<V> {

	/**
	 * Set an event bus to the presenter
	 * 
	 * @param eventBus
	 *            event bus to set
	 */
	public void setEventBus( EventBus eventBus );
	
	/**
	 * Get the view associated with the presenter
	 * 
	 * @return eventBus manipulated by the presenter.
	 */
	public EventBus getEventBus();

	/**
	 * Sets the view associated with the presenter.
	 * 
	 * @param view
	 *            view to set
	 */
	public void setView( V view );

	/**
	 * Gets the view associated with the presenter.
	 * 
	 * @return view manipulated by the presenter.
	 */
	public V getView();

	/**
	 * Performs required bindings between this Presenter and its View.</p>
	 * 
	 * This is automatically invoked when the View is wired into the Presenter.
	 * 
	 */
	public void bind();

}