package com.mvp4g.util.config.element;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mvp4g.util.exception.element.DuplicatePropertyNameException;

public class SimpleMvp4gElementTest extends AbstractMvp4gElementTest<SimpleMvp4gElement> {

	protected static final String[] properties = { "name" };

	@Test
	public void testSetClassName() throws DuplicatePropertyNameException{

		String className = "Test";

		element.setClassName( className );
		assertEquals( element.getProperty( "class" ), className );
	}

	@Test
	public void testGetClassName() throws DuplicatePropertyNameException{

		String packageName = "com.test";
		String className = "Test";

		element.setProperty( "package", packageName );
		element.setProperty( "class", className );
		assertEquals( element.getClassName(), new ClassResolver().getClassNameFrom( packageName, className ) );
	}

	@Test
	public void testToString() throws DuplicatePropertyNameException{
		String name = "test";
		String className = "com.test.Test";

		element.setName( name );
		element.setClassName( className );

		assertEquals( "[" + name + " : " + className + "]", element.toString() );
	}

	@Override
	protected String[] getProperties() {
		return properties;
	}

	@Override
	protected String getTag() {
		return "simple";
	}

	@Override
	protected String getUniqueIdentifierName() {
		return "name";
	}

	@Override
	protected SimpleMvp4gElement newElement() {
		return new SimpleMvp4gElement();
	}

	protected static String[] addProperties( String[] otherProperties ) {

		int pSize = properties.length;
		int size = pSize + otherProperties.length;
		String[] newProperties = new String[size];

		int i;
		for ( i = 0; i < pSize; i++ ) {
			newProperties[i] = properties[i];
		}

		for ( i = pSize; i < size; i++ ) {
			newProperties[i] = otherProperties[i - pSize];
		}

		return newProperties;

	}

}
