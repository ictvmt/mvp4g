package com.mvp4g.util.config.loader;

import org.apache.commons.configuration.XMLConfiguration;

import com.mvp4g.util.config.element.PresenterElement;

public class PresentersLoaderTest extends AbstractMvp4gElementLoaderTest<PresenterElement, PresentersLoader> {

	@Override
	protected String getTagName() {
		return "presenter";
	}

	@Override
	protected boolean isSingleNode() {
		return false;
	}

	@Override
	protected PresentersLoader newLoader( XMLConfiguration xml ) {
		return new PresentersLoader( xml );
	}

}
