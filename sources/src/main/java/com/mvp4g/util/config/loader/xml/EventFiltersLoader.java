/*
 * Copyright 2009 Pierre-Laurent Coirier
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.mvp4g.util.config.loader.xml;

import org.apache.commons.configuration.XMLConfiguration;

import com.mvp4g.util.config.element.EventFilterElement;

public class EventFiltersLoader extends Mvp4gElementLoader<EventFilterElement> {

    static final String[] REQUIRED_ATTRIBUTES = { "name", "class" };
    static final String[] PARENT_ATTRIBUTES = { "package" };

	@SuppressWarnings( "unchecked" )
	public EventFiltersLoader( XMLConfiguration xmlConfig ) {
		super( xmlConfig.configurationsAt( "filters.filter" ) );
	}

	@Override
	String getElementLabel() {
		return "EventFilter";
	}

	@Override
	String[] getRequiredAttributeNames() {
		return REQUIRED_ATTRIBUTES;
	}

	@Override
	protected EventFilterElement newElement() {
		return new EventFilterElement();
	}
	
	@Override
	protected String[] getParentAttributeNames() {
		return PARENT_ATTRIBUTES;
	}

}
