package com.mvp4g.client.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.mvp4g.client.Mvp4gException;

public class EventBusTest {

	private static final String TEST = "Test";
	private static final String NULL = "Null";
	private static final String CLASS = "Class";

	private EventBus bus = null;

	private static enum EventType {

		TEST_TYPE(TEST), TEST_NULL(NULL), TEST_CLASS(CLASS);

		private String type;

		private EventType( String type ) {
			this.type = type;
		}

		@Override
		public String toString() {
			return type;
		}
	}

	@Before
	public void setUp() {
		bus = new EventBus();
	}

	@Test
	public void testAddAndDispatchWithFormOk() {
		String formValue = "formValue";
		addAssertCommand( formValue, false );
		bus.dispatch( TEST, formValue, false );
		bus.dispatch( EventType.TEST_TYPE, formValue, false );
	}

	@Test
	public void testAddAndDispatchWithFormOkAndHistory() {
		String formValue = "formValue";
		addAssertCommand( formValue, true );
		bus.dispatch( TEST, formValue, true );
		bus.dispatch( EventType.TEST_TYPE, formValue, true );
		bus.dispatch( TEST, formValue );
		bus.dispatch( EventType.TEST_TYPE, formValue );

	}

	@Test
	public void testAddAndDispatchWithNoFormOk() {
		addAssertCommand( null, false );
		bus.dispatch( TEST, null, false );
		bus.dispatch( TEST, false );
		bus.dispatch( EventType.TEST_TYPE, null, false );
		bus.dispatch( EventType.TEST_TYPE, false );
	}

	@Test
	public void testAddAndDispatchWithNoFormOkAndHistory() {
		addAssertCommand( null, true );
		bus.dispatch( TEST, null, true );
		bus.dispatch( TEST, true );
		bus.dispatch( EventType.TEST_TYPE, null, true );
		bus.dispatch( EventType.TEST_TYPE, true );
		bus.dispatch( TEST, null );
		bus.dispatch( TEST );
		bus.dispatch( EventType.TEST_TYPE, null );
		bus.dispatch( EventType.TEST_TYPE );
	}

	@Test
	public void testDispatchEventNotHandle() {

		try {
			bus.dispatch( TEST, null, false );
			fail();
		} catch ( Mvp4gException exp ) {
			assertUnknownEvent( TEST, exp.getMessage() );
		}

		try {
			bus.dispatch( TEST, false );
			fail();
		} catch ( Mvp4gException exp ) {
			assertUnknownEvent( TEST, exp.getMessage() );
		}

		try {
			bus.dispatch( EventType.TEST_TYPE, null, false );
			fail();
		} catch ( Mvp4gException exp ) {
			assertUnknownEvent( TEST, exp.getMessage() );
		}

		try {
			bus.dispatch( EventType.TEST_TYPE, false );
			fail();
		} catch ( Mvp4gException exp ) {
			assertUnknownEvent( TEST, exp.getMessage() );
		}

		try {
			bus.dispatch( TEST, null );
			fail();
		} catch ( Mvp4gException exp ) {
			assertUnknownEvent( TEST, exp.getMessage() );
		}

		try {
			bus.dispatch( TEST );
			fail();
		} catch ( Mvp4gException exp ) {
			assertUnknownEvent( TEST, exp.getMessage() );
		}

		try {
			bus.dispatch( EventType.TEST_TYPE, null );
			fail();
		} catch ( Mvp4gException exp ) {
			assertUnknownEvent( TEST, exp.getMessage() );
		}

		try {
			bus.dispatch( EventType.TEST_TYPE );
			fail();
		} catch ( Mvp4gException exp ) {
			assertUnknownEvent( TEST, exp.getMessage() );
		}

	}

	@Test
	public void testNullPointerInsideTheCommand() {
		addNullCommand();

		try {
			bus.dispatch( NULL, null, false );
			fail();
		} catch ( NullPointerException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( NULL, false );
			fail();
		} catch ( NullPointerException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( EventType.TEST_NULL, null, false );
			fail();
		} catch ( NullPointerException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( EventType.TEST_NULL, false );
			fail();
		} catch ( NullPointerException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( NULL, null );
			fail();
		} catch ( NullPointerException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( NULL );
			fail();
		} catch ( NullPointerException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( EventType.TEST_NULL, null );
			fail();
		} catch ( NullPointerException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( EventType.TEST_NULL );
			fail();
		} catch ( NullPointerException exp ) {
			//nothing to verify
		}

	}

	@Test
	public void testDispatchFormClassIncorrect() {
		Integer form = new Integer( 3 );
		addCommand( TEST );

		try {
			bus.dispatch( TEST, form, false );
			fail();
		} catch ( Mvp4gException exp ) {
			assertIncorrectFormClass( TEST, exp.getMessage() );
		}
		try {
			bus.dispatch( EventType.TEST_TYPE, form, false );
			fail();
		} catch ( Mvp4gException exp ) {
			assertIncorrectFormClass( TEST, exp.getMessage() );
		}

		try {
			bus.dispatch( TEST, form );
			fail();
		} catch ( Mvp4gException exp ) {
			assertIncorrectFormClass( TEST, exp.getMessage() );
		}
		try {
			bus.dispatch( EventType.TEST_TYPE, form );
			fail();
		} catch ( Mvp4gException exp ) {
			assertIncorrectFormClass( TEST, exp.getMessage() );
		}
	}

	@Test
	public void testClassCastErrorInsideHandlers() {
		addClassCastCommand();

		try {
			bus.dispatch( CLASS, null, false );
			fail();
		} catch ( ClassCastException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( CLASS, false );
			fail();
		} catch ( ClassCastException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( EventType.TEST_CLASS, null, false );
			fail();
		} catch ( ClassCastException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( EventType.TEST_CLASS, false );
			fail();
		} catch ( ClassCastException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( CLASS, null );
			fail();
		} catch ( ClassCastException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( CLASS );
			fail();
		} catch ( ClassCastException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( EventType.TEST_CLASS, null );
			fail();
		} catch ( ClassCastException exp ) {
			//nothing to verify
		}

		try {
			bus.dispatch( EventType.TEST_CLASS );
			fail();
		} catch ( ClassCastException exp ) {
			//nothing to verify
		}
	}

	private void addAssertCommand( final String formSent, final boolean expectedStoreInHistory ) {
		bus.addEvent( TEST, new Command<String>() {

			public void execute( String form, boolean storeInHistory ) {
				assertSame( form, formSent );
				assertSame( storeInHistory, expectedStoreInHistory );
			}

		} );
	}

	private void addCommand( final String formSent ) {
		bus.addEvent( TEST, new Command<String>() {

			public void execute( String form, boolean storeInHistory ) {
				// Nothing to do
			}

		} );
	}

	private void addNullCommand() {
		bus.addEvent( NULL, new Command<String>() {

			@SuppressWarnings( "null" )
			public void execute( String form, boolean storeInHistory ) {
				String nullString = null;
				nullString.length();
			}

		} );
	}

	private void addClassCastCommand() {
		bus.addEvent( CLASS, new Command<String>() {

			public void execute( String form, boolean storeInHistory ) {
				classCastError();
			}

		} );
	}

	protected void classCastError() {
		Object o = new Object();
		@SuppressWarnings( "unused" )
		Integer i = (Integer)o;
	}

	private void assertUnknownEvent( String eventType, String exceptionMessage ) {
		String errorMessage = "Event " + eventType + " doesn't exist. Have you forgotten to add it to your Mvp4g configuration file?";
		assertEquals( errorMessage, exceptionMessage );

	}

	private void assertIncorrectFormClass( String eventType, String exceptionMessage ) {
		String errorMessage = "Class of the object sent with event " + eventType
				+ " is incorrect. It should be the same as the one configured in the Mvp4g configuration file.";
		assertEquals( errorMessage, exceptionMessage );

	}

}
