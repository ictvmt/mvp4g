

# Code Splitting #

In Mvp4g, you have 2 ways to create fragments with code splitting. You can:
  * divide your application into Mvp4g modules. Each module will then generate a fragment. This is the technique you should use most of the times. The main advantage of this solution is that it forces you to divide your application into smaller modules, easier to develop and maintain.
  * create a splitter for the element you want to split (ie either a presenter/event handler or a group of them). Each splitter will then generate a fragment. This technique should be used only in particular case where boilerplate code to create a module is important compared to the element code (for example, the split element exchange a lot of different events with the rest of the application or you only want to split a few presenters).

Whatever technique you decide to use, you shouldn't generate too many fragments or fragments that are too small otherwise you will increase the number of roundtrip to your server for no reason and will actually make your application slower. A good fragment should be between 5k and 500k. A bad practice would be to have a splitter for each single handler.

## Multi-Modules ##

Thanks to Mvp4g, developers can easily split their application into modules. Each module will have its own event bus and own presenters/views/services.

A module will be able to communicate with its parent, its childmodule(s) and its direct sibling{s} by forwarding them events. When an event is forwarding to other module(s):
  * the other module(s) must be able to handle this event. If object(s) are fired with the event, the object(s) will also be forwarded.
  * the other module(s) will handle this event the same as if it was thrown by one of its own presenters.

An event can be handled by zero to many presenters and/or forwarded to zero to many other modules.

Thanks to Mvp4g and GWT Code splitting feature, you can either load the code of your module at start or only when it needs to handle its first event.

### Creating modules ###

  1. You need to create an interface that extends Mvp4gModule:
```
public interface CompanyModule extends Mvp4gModule {}
```
  1. You need to define an event bus for the module and links this event bus to your module. To do this, you need to specify the attribute module of @Events annotation of your event bus interface. The value of this attribute must be equal to the class of your module.
```
@Events(..., module=CompanyModule.class)
public interface CompanyEventBus extends EventBus {...}
```

### Root Module ###

The root module is the top module of your application, the only module without parent. This module is the only module created manually by developers when calling:
```
 Mvp4gModule module = GWT.create(Mvp4gModule.class); 
```
This code is usually in your entry point. By default the top module class is Mvp4gModule, but you can use any Mvp4gModule you created.

### Event forwarding from child to parent module ###

To forward an event to a parent module, you need to set the attribute forwardToParent to "true" thanks to @Event annotation.

```
@Event(forwardToParent=true)
public void changeBody(Widget body);
```

![http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/forward_parent.png](http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/forward_parent.png)

1: these methods are called when the child module is loaded and allow to retrieve the parent event bus.

## Event forwarding to other module(s) ##

You can easily forward an event to other module(s) by setting the attribute forwardToModules of the @Event annotation of your event. This attribute contains an array of other module(s) classname to which the event need to be forwarded.
```
@Event(forwardToModules = CompanyModule.class)
public void goToCompany();
```

You can forward an event only to:
  * the parent module
  * the child module(s) ([ see setting child modules)
  * the sibling module(s) (ie the child modules of the parent module).

When forwarding an event to a sibling module, this sibling module will be loaded the same way as if its parent forwarded it an event.

### Setting child modules ###

You can define child modules of a module thanks to ChildModule<b>s</b> annotation. This annotation contains a list of ChildModule annotation. Each annotation of this list define a child module.

A ChildModule annotation has three attributes:
  * moduleClass: class of the child module. It must be one of the class you created at step 1.
  * async (default:true): if set to true, then the code containing the child module is loaded when handling its first event otherwise it is loaded when application starts. In the first case, Mvp4g uses GWT Code Splitting feature to load the child module.
  * autoDisplay (default:true): if set to true, the start view of the child module will be displayed when the child module is done loaded. When this option is set to true, the parent module's event, needed to display the child module's start view must be defined (see [AutoDisplay](http://code.google.com/p/mvp4g/wiki/MultiModules#Child_module)).

A child module will be started (ie its presenters/views/services will be instantiated and its start event will be fired) only the first time an event is forwarded to it.

### `Child module AutoDisplay` ###
For the Root Module, Mvp4g automaticaly add the Root Module start presenter's view to the RootPanel/RootLayoutPanel when the application starts.

The same thing can be done for child modules in order them not to take care of it when they're done loaded. This is done thanks to the AutoDisplay feature.

Whereas the Root Module is added to the RootPanel/RootLayoutPanel, the child module will be added to one of its parent's views. In order to do so, Mvp4g will use an event and pass to this event the child module's start presenter's view. The presenter that handles this event will be in charge of adding the child module's start presenter's view to its own view.

This event will be fired each time an event is forwarded to a child module.

By default, AutoDisplay option is set to true.

When this option is activated, you need to indicate which event to use to display the child module's start presenter's view. To set this event, you have to annotate the event's method with @DisplayChildModuleView.

```
@DisplayChildModuleView(CompanyModule.class)
@Event(handlers = MainPresenter.class)
public void changeBody(Widget newBody);
```

The only attribute of this annotation is a list of child module class name. You can specify one to several child module classes thanks to this attribute. At most one event should be set for each child module.

The parent module's event used to display child module's start presenter's view must have one parameter which type is compatible with child module's start presenter's view interface.

### Forward Event ###

Whereas the start event is fired only the first time an event is forwarded to a child module, you can define an event that will be fired all the time an event is forwarded to the child module: a forward event.

To define a forward event, you need to annotate an event with @Forward. No object can be fired with the event annotated with @Forward. You can have only one forward event by module. Root module can't have a forward module.

```
@Events(..., module=CompanyModule.class)
public interface CompanyEventBus extends EventBus {

     @Forward
     @Event(...)
     void forward();

}
```

### Sequence Diagram ###

![http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/forward_children.png](http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/forward_children.png)

  1. these methods are called only the first time an event is forwarded to the child module.
  1. child module is created thanks to GWT.create process.
  1. when you decide not to load your child module asynchronously (ie load your child module at first), these methods are still called.
  1. called only if the child module auto-display feature is activated
  1. called only if you have configured a forward event
  1. you can set any event for the start, forward or display child view events

### Understanding events fired when an event is forwarded to a child/sibling module ###

When a child/sibling module is done loaded the following events will be fired:
  1. child module start event, on child event bus, only the first time an event is forwarded to the child module.
  1. child module forward event, on child event bus, every time an event is forwarded to a child module.
  1. event to display child module start presenter's view, on parent event bus, only if AutoDisplay option is activated, every time an event is forwarded to a child module.
  1. event forwarded to child module, on child event bus, every time an event is forwarded to a child module.

## Splitter ##

In Mvp4g, you can easily create a code fragment for a presenter/event handler or a group of them. The code of the split element will be loaded when the split presenter/event handler has to handle its first event (or if you split a group of them, when one of the split presenters/event handlers has to handle its first event). The rest of the application considers a split presenter/event hander and a non-split presenter/event handler the same way (ie the rest of the application is not aware that a presenter/event handler is spit). The only restrictions are a start presenter can't be split.

### Split a single presenter/event handler ###

To split a single presenter/event handler, all you have to do is set the 'async' attribute of the @Presenter (or @EventHandler) to SingleSplitter.class.

```
@Presenter(…, async = SingleSplitter.class)
public class OneSplitPresenter … { … }

@Events(…)
public interface OneEventBus … {

    @Event(handlers = OneSplitPresenter.class)
    void oneEvent(String oneParam);

}
```

In the example above, OneSplitPresenter will be split and its code fragment will be loaded when oneEvent is fired.

### Split a group of presenters/event handlers ###

To split a group of presenters/event handlers, you first have to create an interface extending Mvp4gSplitter.

```
public interface MySplitter extends Mvp4gSplitter {
    // Do not add any method here.
}
```

You then need to set the 'async' attribute of the @Presenter (or @EventHandler) of the presenters/event handlers you wish to split to this new splitter.

```
@Presenter(…, async = MySplitter.class)
public class SplitPresenter1 … { … }

@Presenter(…, async = MySplitter.class)
public class SplitPresenter2 … { … }

@Presenter(…, async = MySplitter.class)
public class SplitPresenter3 … { … }

@Events(…)
public interface OneEventBus … {

    @Event(handlers = SplitPresenter1.class)
    void event1(String oneParam);

    @Event(handlers = {SplitPresenter2.class, SplitPresenter3.class})
    void event2(String oneParam);
}
```

In the example above, SplitPresenter1, SplitPresenter2 and SplitPresenter3 will be split and one code fragment will be created for the three of them. This fragment will be loaded when event1 or event2 is fired.

### Splitter and Multiple Presenter/Event Handler ###

You can split any multiple presenter/event handler like a regular presenter/event handler. The only limitation is a split multiple presenter/event handler can only be generated thanks to an event, using addHandler won't work.

For example, if you have the following split multiple presenter:
```
@Presenter(..., multiple = true, async=SingleSplitter.class)
public class OneSplitMultiplePresenter extends BasePresenter<OneEventBus> {...}
```

You can generate it thanks to an event:
```
@Event(..., generate = OneSplitMultiplePresenter.class)
void addNewSthg();
```

but using addHandler will fire an exception:
```
eventHandler.addHandler(OneMultiplePresenter.class);
```

## Before, After, OnError ##

When the code of a child module or a splitter is loaded thanks to GWT Code Splitting feature, you have the possibility to define:
  * a before event: this event will be called before starting to load the code. You can for example decide to display a wait popup.
  * an after event: this event will be called after the code is done loading. You can for example decide to hide a wait popup.
  * an error event: this event will be called in case an error occurs while loading the code.

You can define the three events thanks to three annotations: @BeforeLoadChildModule, @AfterLoadChildModule, @LoadChildModuleError.<br />
No object can be fired with the event(s) used before and/or after.<br />
An object may be fired for the event used in case of error but the type of this object must be compatible with java.lang.Throwable. In this case, the error returned by the RunAsync object is passed to the event.

```
@LoadChildModuleError
@Event(handlers = MainPresenter.class)
public void errorOnLoad(Throwable reason);

@BeforeLoadChildModule
@Event(handlers = MainPresenter.class)
public void beforeLoad();

@AfterLoadChildModule
@Event(handlers = MainPresenter.class)
public void afterLoad();
```

## Loader ##

A loader let you control a module/splitter loading. You can:
  * execute asynchronous action(s) before the module/splitter loads,
  * prevent the module/splitter from being loaded,
  * execute actions after the module/splitter is done loaded or in case of error.

### Creating a loader ###

You need to create a class that implements Mvp4gLoader. This interface defines 3 methods that you need to override:
  * preLoad: it is called before the module/splitter is loaded. When you're done executing your actions, you have to call the load command, passed as a parameter, to start the module/splitter loading. You can also use this method to prevent a module/splitter from being loaded by not executing the load command.
  * onSuccess and onFailure are called when the module is loaded successfuly/ failed to load.

Here is an example that prevents a module/splitter of being loaded is the user is not logged in:
```
public class MyLoader extends implements Mvp4gLoader<MyEventBus> {

	...

	@Override
	public void preLoad( MainEventBus eventBus, String eventName, Object[] params, Command load ) {
		// Loads the module only if the user is logged in.
		if ( isUserLoggedIn() ) {
			load.execute();
		}
	}

	@Override
	public void onSuccess( MainEventBus eventBus, final String eventName, Object[] params ) {
		// Nothing to do.
	}

	@Override
	public void onFailure( MainEventBus eventBus, String eventName, Object[] params, Throwable err ) {
		// Nothing to do.
	}

}
```

### Setting a loader ###

To set a loader for a module/splitter, you need to annotate it with @Loader and set the annotation value attribute to your loader class.
```
@Loader( MyLoader.class )
public interface MyChildModule extends Mvp4gModule {}
```

```
@Loader( MyLoader.class )
public interface MyStatusSplitter extends Mvp4gSplitter {}
```