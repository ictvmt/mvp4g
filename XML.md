

The first version of Mvp4g was based on an XML configuration file. To be as compatible as possible with the previous version, Mvp4g still offers the possibility of using an XML file instead of an EventBus interface.

All the Mvp4g features can be configured thanks to an XML configuration file. However, use of an EventBus interface is recommended instead of an XML configuration file since the XML configuration doesn't allow for a type-safe eventbus as it is based on an EventBusWithLookup [see EventBusWithLookup](http://code.google.com/p/mvp4g/wiki/EventBus#with_lookup).

With XML configuration, Mvp4g removes useless elements [see Mvp4g Optimisation](http://code.google.com/p/mvp4g/wiki/Mvp4gOptimization#Minimum_of_instances_creation), however if you declare two instances with the same class and they are both used, then two instances will be created.

This section describes how to use the XML configuration file. However it doesn't give a  description of the features.

# Mvp4g Configuration file #
Create a file in your project source directory with the name mvp4g-conf.xml. Your file must start and end with the tag `<mvp4g>`.
```
YourGWTModule
     |--- src
           |---mvp4g-conf.xml
```

# Views #
Add your views to the configuration file.

```
<views package="com.mvp4g.example.client.view" >
	<view name="viewOne" class="View1" />
	<view name="viewTwo" class="com.mvp4g.example.client.view.special.View2" />
	...
</views>
```

  * package (optional): you can specify the package where your views are located so as to not be required to write it for each of them ([see package attribute for more information](http://code.google.com/p/mvp4g/wiki/XML?ts=1268511969&updated=XML#Package_attribute)).
  * name: name of the view. It must be unique amongst all elements.
  * class: Class of the view (package attribute may be used to build the full name ([see package attribute for more information](http://code.google.com/p/mvp4g/wiki/XML?ts=1268511969&updated=XML#Package_attribute)).

# Services #
Add your services to the configuration file. Only services declared in this configuration can be injected into presenters and history converters.
```
<services package="com.mvp4g.example.client" >
	<service name="service1" class="Service1" />
	<service name="service2" class="com.mvp4g.example.client.service.special.Service2" path="/myapp/myservice"/>
	...
</services>
```

  * package (optional): you can specify the package where your services are located in order to not to have write it for each of them ([see package attribute for more information](http://code.google.com/p/mvp4g/wiki/XML?ts=1268511969&updated=XML#Package_attribute)). **name: name of the service. It must be unique amongst all elements.
  * class: Interface of the service (package attribute may be used to build the full name ([see package attribute for more information](http://code.google.com/p/mvp4g/wiki/XML?ts=1268511969&updated=XML#Package_attribute)).** path (optional): allows for the definition the service entry point. Similar to:
```
service2.setServiceEntryPoint("/myapp/myservice"); 
```
  * generatedClass (optional): allows for the definition of the class that needs to be generated for the service. Needed if you want to use a third party library to define a service other than GWT-RPC (like EdgeBox). For example:
```
<service name="service3" class="com.mvp4g.example.client.service.special.Service3" generatedClass="com.mvp4g.example.client.service.special.Service3" >
```
will generate this code:
```
Service3 service3 = GWT.create(service3);
```
If not specified, the value is equal to class attribute + "Async".


If a service is injected into a presenter or a history converter, the presenter or the history converter must define the appropriate setter. This method must be called: "set" + name of the service and have one parameter with a class equal to the class of the service + "Async". For example, to be able to inject `service1` into a presenter, the presenter needs to define the following method:
```
public void setService1(com.mvp4g.example.client.Service1Async service1)
```

# History #
Create your history converters by extending XmlHistoryConverter. An XmlHistoryConverter is like a regular history converter(see [Place Service](http://code.google.com/p/mvp4g/wiki/PlaceService) but since you don't define an event bus interface, it automaticaly uses an EventBusWithLookup.

Add history to the configuration file.

```
<history package="com.mvp4g.example.client.history" initEvent="init" notFoundEvent="notFound">
	<converter name="converter1" class="Converter1" services="service1, service2" />
	<converter name="converter2" class=".display.Converter1" services="service3" />
	<converter name="converter3" class="com.mvp4g.example.client.special.Converter" />
</history>
```

  * package (optional): you can specify the package where your history converters are located so as to not be required to write it for each of them ([see package attribute for more information](http://code.google.com/p/mvp4g/wiki/XML?ts=1268511969&updated=XML#Package_attribute)).
  * initEvent: type of the event thrown by the PlaceService in case the history is fired with an empty token (null or equal to ""). This event will be fired with no parameter.
  * notFoundEvent (optional): type of the event fired by the PlaceService if the token stored in browser history is not matched with a history converter. If not specified, then the same event as initEvent will be thrown.
  * name: name of the history converter. It must be unique amongst all elements.
  * class: Class of the history converter (package attribute may be used to build the full name ([see package attribute for more information](http://code.google.com/p/mvp4g/wiki/XML?ts=1268511969&updated=XML#Package_attribute)). **service (optional) : name(s) of the service(s) to inject in the history converter. The name(s) of the service(s) must be equal to one of the service names declared in the configuration file. Each service name must be separated by a comma.**

# Presenters #
Create your presenters by extending the XmlPresenter (or LazyXmlPresenter) class. An XmlPresenter is like a regular presenter (see [presenter](http://code.google.com/p/mvp4g/wiki/PresenterViewService#Presenter) but since you don't define an event bus interface, it automaticaly uses an EventBusWithLookup. Thus to fire an event, you will use the following code:
```
eventBus.dispatch("event", new MyBean());
```

Add your presenters to the configuration file. You have to inject one view into each presenter and can inject one or more services into it.
```
<presenters package="com.mvp4g.example.client.presenter" >
	<presenter name="presenter1" class="Presenter1" view="viewOne" />
	<presenter name="presenter2" class="com.mvp4g.example.client.presenter.special.Presenter2" view="viewTwo" services="service1, service2" />
	...
</presenters>
```

  * package (optional): you can specify the package where your presenters are located in order not have to write it for each of them ([see package attribute for more information](http://code.google.com/p/mvp4g/wiki/XML?ts=1268511969&updated=XML#Package_attribute)). **name: name of the presenter. It must be unique amongst all elements.
  * class: Class of the presenter (package attribute may be used to build the full name ([see package attribute for more information](http://code.google.com/p/mvp4g/wiki/XML?ts=1268511969&updated=XML#Package_attribute)).
  * view: name of the view to inject in the presenter. The name must be equal to one of the view names declared in the configuration file.
  * service (optional) : name(s) of the service(s) to inject in the presenter. The name(s) of the service(s) must be equal to one of the service names declared in the configuration file. Each service name must be separated by a comma.**

# Events #
Add events that can be fired by presenters in the configuration file.
```
<events>
	<event type="event1" calledMethod="onEvent1" eventObjectClass="com.mvp4g.example.client.bean.Bean1" handlers="presenter1,presenter2" history="beanConverter" />
	...
</events>
```

  * type: type of the event. It must be unique amongst events. This is the type given to the event bus to dispatch an event.
  * calledMethod (optional): name of the method that presenters, which handle this event, need to define. This method will have only one parameter, the class of which is the one defined in the `eventObjectClass` attribute. If not defined, the value of this attribute will be deduced from the type attribute: on + type of the event (for example, considering an event with type "display", the default value will be "onDisplay") .
  * eventObjectClass (optional): class of the object that is sent with the event. If not defined, the value of this attribute is deduced from the method definition of the first handler. This attribute is also optional if no event is sent with the event.
  * handlers: name(s) of the presenter(s) that handle this event. The name(s) of the presenters(s) must be equal to one of the presenter names declared in the configuration file. Each presenter name must be separated by a comma. Each presenter must implement the handler method called when this type of event is fired. For example, handlers of `event1` event must implement the following method:
```
public void onEvent1(com.mvp4g.example.client.bean.Bean1 bean)
```
  * history (optional): name of history converter that must be used to place event into browser history. The name of the history converter must be equal to one of the history converter names declared in the configuration file. If no history converter is associated with the event, it means that the event can not be placed into the browser history ([see Place Service feature](http://code.google.com/p/mvp4g/wiki/PlaceService)).
  * modulesToLoad (optional): name of the child modules to load when this event is fired. The name of the child modules must be equal to one of the child modules declared in the configuration file (http://code.google.com/p/mvp4g/wiki/MultiModules see Multi-Modules feature).
  * forwardToParent(optional): if this attribute is equal to "true", then the event is forwarded to the parent module (http://code.google.com/p/mvp4g/wiki/MultiModules see Multi-Modules feature).

# Start #
You can decide to fire an event and to add a view to the RootPanel/RootLayoutPanel when you start your application.
If this is the XML configuration file of a child module, then the start view is the view that will be automaticaly display if the AutoDisplay feature is activated ([http://code.google.com/p/mvp4g/wiki/MultiModules see Multi-Modules features)
```
<start view="rootView" eventType="start" history="true"/>
```
  * view: view to add to the RootPanel when the application starts.
  * eventType (optional): event type sent to the event bus when the application starts. No object will be sent with the event.
  * history (optional): if this attribute is equal to "true", then the current history state is fired.

# Child Module #
Add your child modules to the configuration file. Each module needs to have its own interface and its own configuration file.([see Multi-Modules features](http://code.google.com/p/mvp4g/wiki/MultiModules)).
```
<childModules package="com.mvp4g.example.client" beforeEvent="beforeLoad" afterEvent="afterLoad" errorEvent="errorOnLoad">
	<childModule name="companyModule" class=".company.CompanyModule" eventToDisplayView="changeBody"/>
	<childModule name="productModule" class=".product.ProductModule" async="false" autoDisplay="false"/>	
         ...
</childModules>
```

  * package (optional): you can specify the package where your child modules are located so as to not be required to write it for each of them ([see package attribute for more information](http://code.google.com/p/mvp4g/wiki/XML?ts=1268511969&updated=XML#Package_attribute)).
  * beforeEvent (optional): event to fire when starting to load the child module (only if child module is loaded asynchronously)
  * afterEvent (optional): event to fire when the child module is done loading (only if child module is loaded asynchronously)
  * errorEvent (optional): event to fire when an error occurs while loading the child module.
  * name: name of the child module. It must be unique amongst all elements.
  * class: interface of child module (package attribute may be used to build the full name ([see package attribute for more information](http://code.google.com/p/mvp4g/wiki/XML?ts=1268511969&updated=XML#Package_attribute)).
  * async (optional): If this attribute is set to "true", the module will be loaded only when it has to handle its first event thanks to the GWT 2.0 GWT splitting feature. By default, it sets to "true".
  * autoDisplay(optional) : If this attribute is set to "true", the child start view will be displayed automaticaly thanks to eventToDisplayView event. By default, it sets to "true".
  * eventToDisplayView (required if autoDisplay activated): event used to display child start view.

# Child Module Interface #

As with EventBus interface, you need to create a Mvp4g module interface for each of your child modules ([http://code.google.com/p/mvp4g/wiki/MultiModules#Creating_modules see Multi-module feature). Moreover, you need to specify the xml configuration file of your child module thanks to @XmlFilePath.

```
@XmlFilePath("xmlConfig/company-mvp4g.xml")
public interface CompanyModule extends Mvp4gModule {
	
	public void setParentModule(Mvp4gModule eventBus);

}
```

# Package attribute #
The package attribute makes it easier to type element class name. When this attribute is used, the following rules are applied to build the class name:

•the class attribute doesn't contain any "." or starts with a ".", then the class name is equals to package attribute + class attribute.
•the class attribute starts contains "." but doesn't start by ".", then the class name is equals to class attribute.

For example:



&lt;views package="com.mvp4g.example.client.view" &gt;


> 

&lt;view name="rootView" class="com.mvp4g.example.client.RootView" /&gt;


> 

&lt;view name="userCreateView" class="UserCreateView" /&gt;


> 

&lt;view name="userDisplayView" class=".display.UserDisplayView"/&gt;




&lt;/views&gt;



will generate the following class names: com.mvp4g.example.client.RootView, com.mvp4g.example.client.view.UserCreateView and com.mvp4g.example.client.view.display.UserDisplayView.

# Full example #
```
<?xml version="1.0" encoding="ISO-8859-1" ?>
<mvp4g xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://mvp4g.googlecode.com/svn/tags/mvp4g-1.0.0/xsd/mvp4g-conf.xsd">

	<debug enabled="true" />

	<childModules package="com.mvp4g.example.client" beforeEvent="beforeLoad" afterEvent="afterLoad" errorEvent="errorOnLoad">
		<childModule name="companyModule" class=".company.CompanyModule" eventToDisplayView="changeBody"/>
		<childModule name="productModule" class=".product.ProductModule" async="false" autoDisplay="false"/>	
	</childModules>
	<history package="com.mvp4g.example.client.main.historyConverter" initEvent="start">
		<converter name="noParamHC" class="NoParamHistoryConverter"/>
	</history>
	<views package="com.mvp4g.example.client.main" >
		<view name="rootView" class="MainView" />		
	</views>	
	<presenters package="com.mvp4g.example.client.main" >
		<presenter name="rootPresenter" class="MainPresenter" view="rootView" />		
	</presenters>
	<events>
		<event type="goToCompany" handlers="" modulesToLoad="companyModule" history="noParamHC"/>
		<event type="goToProduct" handlers="" modulesToLoad="productModule" history="noParamHC" />
		<event type="changeBody" handlers="rootPresenter" />
		<event type="errorOnLoad" handlers="rootPresenter" />
		<event type="beforeLoad" handlers="rootPresenter"/>
		<event type="afterLoad" handlers="rootPresenter"/>
		<event type="displayMessage" handlers="rootPresenter"/>
		<event type="start" handlers="" />		
		<event type="selectCompanyMenu" handlers="rootPresenter"/>
		<event type="selectProductMenu" handlers="rootPresenter"/>		
	</events>
	
	<start view="rootView" history="true"/>
	
</mvp4g>					
```