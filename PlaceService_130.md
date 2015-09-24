

# Place Service #

## Description ##

Mvp4g instantiates a Place Service to easily manage history based on History converter.

History converters have two goals:
  * convert event parameters to a string (to add it to the token) and/or store them (in cookie for example) when an event is stored in browser history ([??? see for more information]).
  * convert, when history changes, a token to an event, retrieve information to build its parameters (thanks to the token, database, cookie...) and then fires the converted event to event bus. This conversion is done by convertFromToken method.

This is how Mvp4g stores an event to the browser history:

![http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/place_service.png](http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/place_service.png)

This is how Mvp4g retrieves an event from browser history:

![http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/place_service_reverse.png](http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/place_service_reverse.png)

The token stored in the browser history will be built the following way: event type + "?" + value returned by the handling method of the event. This is the default implementation but you can easily override it ([??? see for more information]).

Any event can be stored in history. All you have to do is to associate an history converter to an event.

If you need to store event information when the event is stored in the browser history, you can do this thanks to the event to token conversion method.<br />
If you need to retrieve event information when browser history changes, you can do this in the convertFromToken method of the history converter.

## Create an History Converter ##

To create an history converter, you have to:
  * create a class that implements HistoryConverter
  * have a constructor with no parameter or compatible with GIN  (ie annotated with @Inject, see [see GIN website for more information](http://code.google.com/p/google-gin/)).
  * annotate your class with @History
  * have your history converter implement the event to token conversion method ([??? see for more information]).

```
@History
public class CompanyHistoryConverter implements HistoryConverter<CompanyBean, CompanyEventBus> {...}
```

You can also give a name to your converter thanks to the attribute "name" of @History annotation (it may be used, but not recommended, to associate a history converter to an event, [see next paragraph](http://code.google.com/p/mvp4g/wiki/PlaceService#Associate_an_history_converter_to_an_event) ).

The @History annotation has also an attribute type. The event to token method that you will have to define for the event will depend on the type ([??? see for more information]).

## Associate an History Converter to an event ##
To add an history converter to an event, you need to specify the history converter attribute of the @Event annotation that annotates the method of your event.

```
@Event(..., historyConverter = CompanyHistoryConverter.class)
public void goToCompany(long id);
```

By defining the history converter class, Mvp4g will be able to find the instance of history converter with this class and associate it with the event.<br />
Mvp4g generates instances of history converter as singleton so for one class, it generates only one instance, which means that if for several events, the same history converter class is associated, then the events will share the same instance of the converter.

Instead of the class, you can also specify the name of the history converter thanks of the attribute historyConverterName of @Event annotation. In this case, you need to make sure that an history converter with this name exits.

When an history converter is associated to an event, it needs to implement the conversion method of this event. The method to define will depend on the history converter type:
  * NONE: parameters won't be converted, only the event's name will be stored in browser's history.
  * DEFAULT: the history converter needs to define the handling method of the event but this method must return a String. The returned String will be added to the token and stored in browser's history.
For example for the previous event, you need to define the following method in your history converter:
```
public String goToCompany(long id);
```
  * SIMPLE: the history converter needs to define one convertToToken method for each event that has a different parameters signature. This convertToToken method must return String and must have the same parameters as the event to convert plus a first String parameter. Mvp4g will use this first String parameter to pass the event's name. The returned String will added to the token and stored in browser's history.
For example, if you have the following event bus:
```
public interface OneEventBus ... { 
    
    @Event(..., historyConverter=OneHistoryConverter.class) 
    void event1(int i); 

    @Event(..., historyConverter=OneHistoryConverter.class) 
    void event2(int i); 

    @Event(..., historyConverter=OneHistoryConverter.class) 
    void event3(int i, String s); 

} 
```
you would need to define this history converter with 2 convertToToken methods:
```
public class OneHistoryConverter... { 

    public String convertToToken(String eventType, int i){ 
        //called by event1 and event2 
        ... 
    }
 
    public String convertToToken(String eventType, int i, String s){ 
        //called by event 3 
        ... 
    } 

} 
```

You can define the history converter type thanks to the "type" attribute of the @History annotation:
```
@History(type = HistoryConverterType.SIMPLE)
public class OneHistoryConverter implements HistoryConverter { ... }
```

By default, the type attribute is equals to DEFAULT.

## Inject services to history converters ##

You may need to call the server while converting your event from a token, that's why Mvp4g allows you to easily inject services inside history converters.

You can inject services the same way as for presenters ([see this section for more information](http://code.google.com/p/mvp4g/wiki/PresenterViewService#Injecting_services)).

## Init and NotFound events ##

When dealing with history, two particular cases can happen:
  * token stored in history is null or empty (ie equals to "").
  * token is incorrect (for example, user tried to modify the url and event type stored in the token is not correct).

For both of these cases, Mvp4g lets you define events that can be fired if they happen. You can annotate the method defining an event in your event bus with:
  * @InitHistory, to manage the case when the token is empty
  * @NotFoundHistory, to manage the case when the token is incorrect.

```
@InitHistory
@Event(handlers={RootTemplatePresenter.class, TopBarPresenter.class})
public void init();
	
@NotFoundHistory
@Event(handlers=RootTemplatePresenter.class)
public void notFound();
```

@InitHistory must be set if you have events with history converters.
@NotFoundHistory is always optional. If you have events with history converters and you haven't set the @NotFoundHistory, then the event annotated with @InitHistory will be fired in case the token is incorrect.

No object can be fired with event(s) annotated with @InitHistory or @NotFoundHistory.

## Clear History Token ##

For some event, you may want to delete history token stored in the URL. In order to do so, you just have to associate your event to a particular HistoryConverter provided by the framework, ClearHistory.

```
@Event(handlers = MainPresenter.class, historyConverter=ClearHistory.class)
public void clearHistory();
```

## History on start ##
When you start your application, you may want to fire the current history state in order to convert any token that could be stored in the URL.<br />
In order to do so, you have to set the attribute historyOnStart of the @Events annotation of your event bus to true. By default this parameter is false.

```
@Events(...historyOnStart = true)
public interface MainEventBus extends EventBusWithLookup {...}
```

## Temporary disable history ##

In some cases, you may need to temporary stop storing events in browser history. In order to do so, Mvp4g provides three methods using the event bus:
  * setApplicationHistoryStored: this method enables/disables conversion (and storage to browser's history) of any events of any Mvp4g event bus.
  * setHistoryStored: this method enables/disables conversion (and storage to browser's history) of any events fired on this event bus only.
  * setHistoryStoredForNextOne: this method enables/disables conversion (and storage to browser's history) of the next event fired on this event bus, associated with an history converter (other events fired while handling this event won't be affected by the call of this method).

In this example, none of the events fired on any event bus will be stored in browser history:
```
	eventBus.setApplicationHistoryStored(false);
	eventBus.selectCompanyMenu();
```

In this example, none of the events fired on this event will be stored in browser history:
```
	eventBus.setHistoryStored(false);
	eventBus.selectCompanyMenu();
```

In this example, only the selectCompanyMenu event won't be stored in browser history:
```
	eventBus.setHistoryStoredForNextOne(false);
	eventBus.selectCompanyMenu();
```

In this example, selectCompany event and all the events fired by handlers of selectCompany event won't be stored in browser history:
```
	eventBus.setApplicationHistoryStored(false);
	eventBus.selectCompanyMenu();
	eventBus.setApplicationHistoryStored(true);
```

## Crawlable Event ##
When you store you event in the browser history, you may want the event url to be crawlable by search engine. To have crawlable url, you need to add a "!" before the token ([http://code.google.com/web/ajaxcrawling/ see Making AJAX Applications Crawlable for more information):
www.mysite.com/#!displayCompany?id=1234.

You can ask the history converter to add this "!" by having the isCrawable method returns true:
```
public class OneCrawlableHistoryConverter implements HistoryConverter<MainEventBus> {

	public boolean isCrawlable() {
		return true;
	}

}
```

Mvp4g takes care of making the url crawlable but you will still have to configure your server to handle pages requested by the crawler ([see part 2](http://code.google.com/web/ajaxcrawling/docs/getting-started.html)).

Mvp4g will react the same way to crawlable and not crawable urls. It means that the 2 following urls will produce the same result:
www.mysite.com/#!displayCompany?id=1234.
www.mysite.com/#displayCompany?id=1234.

## Setting a Custom Place Service ##
You can easily set your own place service to override the behavior of the Mvp4g default place service.

To set your own service, you need to:
  1. Create an abstract class that overrides PlaceService. This class shouldn't implement sendInitEvent and sendNotFoundEvent methods, these methods will be implemented by Mvp4g.
  1. Set this custom place service by annotating the Root Mvp4g module (ie Mvp4g module with no parent) with @PlaceService.

```
@PlaceService( CustomPlaceService.class )
@Events(...)
public interface MainEventBus extends EventBusWithLookup {...}
```

![http://www.qondio.com/graphics/attention.png](http://www.qondio.com/graphics/attention.png) parameters stored in the token shouldn't contain the character used to separate the event name from its parameters.
![http://www.qondio.com/graphics/attention.png](http://www.qondio.com/graphics/attention.png) if you use "/" to separate the event name from its parameters, you need to have "/" always added to the token name even if there is no parameters. By default, "/" is also used to separate child module history name (see below) and when parsing the url, Mvp4g needs to be sure that the last "/" is used to seperate the event name from its parameters.

# History Management and Multi-Modules feature #

Any module (child or parent module) can store its events into history by associating an history converter to an event. When an event of a module (except for the Root module) needs to be stored in history, the event will be converted the same way as described before except that the name of the module and all its parents (**but the Root Module**) will also be stored in the token.

For example, the following token: #module/submodule/event1 means:
  * event1: name of the event to forward
  * submodule: name of the module to which you can forward the event
  * module: name of the parent module. Since this is the top name, it means that the parent of the parent module is the Root Module.

![http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/place_service_children.png](http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/place_service_children.png)

When history token changes, the framework will automatically download if needed the module associated with the event and forwards the event to the module.

For example, if the value of token is: #module/submodule/event1 then:
  * module will be loaded.
  * When done loading module, submodule will be loaded
  * when done loading submodule, event1 is forwarded to submodule.

![http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/place_service_reverse_children.png](http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/place_service_reverse_children.png)


If your module needs to store events in history, then you need to define its history name. In order to so, you need to annotate your module interface with @HistoryName.

```
@HistoryName("company")
public interface CompanyModule extends Mvp4gModule {...}
```

This name will be stored in the token (and thus displayed in the url).

Only the Root module knows the place service, which means that you can configure a History init event, a not found event and the flag historyOnStart only inside the Root module. You can't define these parameters in other modules.

# Hyperlink Token #

You can easily generate an event's token for hyperlink widgets by following these steps:
  * have an history converter associated to the event.
  * change the return type of the event to String.

```
@Event(..., historyConverter = OneHistoryConverter.class)
String oneEvent(OneBean bean1, AnotherBean bean2);
```

To generate a token, you will just have to execute this code inside your presenter:
```
String token = getTokenGenerator().oneEvent(bean1,bean2);
```

## Hyperlink Token and cross-modules events ##

For a child module's event, you can retrieve the generated token of the parent's event. In this case you will have to:
  * have the child's event return type to be String
  * have no history converter associated to the child's event.
  * have the parent's event configured to generate token (see rules below).

If an event is forwarded to a child module, you can not retrieve the token of the child's event because forwarding an event to a child module is an asynchronous process (because of GWT code splitting feature) whereas token generation is synchronous. Also an event could be forwarded to several child modules and you could have each child module generating different tokens.