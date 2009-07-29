package com.mvp4g.client.presenter;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.mvp4g.client.event.EventBus;

public class PresenterTest {

	@Test
	public void testDefaultConstructor() {
		PresenterForTest<Object> p = new PresenterForTest<Object>();
		assertNull( p.getView() );
		assertNull( p.getEventBus() );
	}

	@Test
	public void testConstructorWithArgument() {
		String view = "View";
		final EventBus bus = new EventBus();
		PresenterForTest<String> p = new PresenterForTest<String>( view, bus );
		assertSame( p.getView(), view );
		assertSame( p.getEventBus(), bus );
	}

	public void testSetter() {
		String view = "View";
		final EventBus bus = new EventBus();
		PresenterForTest<String> p = new PresenterForTest<String>();
		p.setEventBus( bus );
		p.setView( view );
		assertSame( p.getView(), view );
		assertSame( p.getEventBus(), bus );
	}

	public void testBindCalledBySetView() {
		Presenter<String> p = new Presenter<String>() {
			@Override
			public void bind() {
				view = "bind";
			}
		};

		p.setView( "view" );
		assertSame( p.getView(), "bind" );
	}

	private class PresenterForTest<V> extends Presenter<V> {

		public PresenterForTest() {
			super();
		}

		public PresenterForTest( V view, EventBus eventBus ) {
			super( view, eventBus );
		}

		public EventBus getEventBus() {
			return eventBus;
		}

	}

}
