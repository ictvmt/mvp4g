

Mvp4g mainly helps you define 3 types of elements:
  * Presenter (or Event Handlers)
  * View
  * Services

This page will help you to understand how to build these different types of elements.

# Presenter #

## Creating a presenter ##

To create a presenter, you need to:
  1. extend BasePresenter<V,E>:
    * V: type of the view that will be injected in the presenter
    * E: type of your event bus interface.

By extending BasePresenter, you will have access to the following protected attributes:
  * V view: instance of the view injected to the presenter
  * E eventBus: instance of the event bus injected to the presenter

You should also override the bind method to bind your view to your presenter.

Instead of extending BasePresenter, you can also implement PresenterInterface<V,E>.

> 2. have a constructor with no parameter or compatible with GIN (ie annotated with @Inject, see [see GIN website for more information](http://code.google.com/p/google-gin/)).

> 3. annotate it with @Presenter and set the attribute 'view'. This attribute must define the class of the view that implements the presenter's view interface.

Mvp4g will automaticaly create an instance for each class annoted with @Presenter. Mvp4g will also create one instance of the view for each presenter. If two presenters are injected with views with the same class, two instances of this view will be created.

You can also decide to create a Lazy Presenter ([see Mvp4g Lazy Loading for more information](http://code.google.com/p/mvp4g/wiki/Mvp4gOptimization#Lazy_Loading)).

## Injecting services ##

With GIN, you can easily inject your service into your presenter thanks to @Inject:
```
@Presenter(view=OneView.class)
public class OnePresenter extends BasePresenter<IOneView, OneEventBus>{
	
	@Inject
	private ServiceAsync service;
	
}
```

GIN will automatically create your service by calling GWT.create(Service.class).

## Multiple presenter ##
By default, a presenter will be created as a singleton, which means that you will have one instance of the presenter when the application starts. A multiple feature is available in order to allow developers to have several instances of the same presenter. When the multiple feature is activated for a presenter, all the instances of this presenter will have to be created manually by the developer (which means that no instance is created when the application starts).

To activate the multiple feature, you need to set the multiple attribute of the @Presenter to "true":
```
@Presenter(view=OneView.class, multiple=true)
public class OnePresenter extends BasePresenter<IOneView, OneEventBus>{...}
```

When you have activated the multiple feature for a presenter, you can create a new instance by calling the addHandler method of the event bus with the class of the presenter to create:
```
OnePresenter presenter = eventBus.addHandler( OnePresenter.class );
```

You can also delete an instance of the presenter set as multiple by calling the removeHandler method of the event bus with the instance to remove:
```
eventBus.removeHandler( handler );
```

![http://www.qondio.com/graphics/attention.png](http://www.qondio.com/graphics/attention.png) Only presenters set as multiple can be added/removed. If you try to add/remove a presenter not set as multiple, an error will occur at runtime.

## On Before Event ##
Presenters provides a method that will be called right before each event that it needs to handle: onBeforeEvent. You can override this method if you need to execute any action before each event.

## Cycle Presenter ##
A cycle presenter is a presenter that will detect when its view is attached to/detached from the DOM. The onLoad() method will be called when the view is attached to the DOM and the onUnload() will be called when the view is detached from the DOM. You can override these methods if you need to execute any action when the view is attached/detached.

A cycle presenter needs to be associated to a special type of a view: CycleView. A cycle view is a view that should fire GWT events:
  * a LoadEvent when it wants to tell the presenter it is loaded
  * a UnloadEvent when it wants to tell the presenter it is unloaded

The cycle presenter will handle these events and call the onLoad/onUnload.

Mvp4g provides a base implementation of a cycle view, BaseCycleView, that extends Composite. You can also easily create your own implementation by firing the load/unload event when you need the presenter to know that the view is loaded/unloaded. For example, if the view is a popup, you can fire the LoadEvent when the popup is shown and the UnloadEvent when the popup is hidden.


# Event Handlers #

An event handler is a presenter with no view. It can be used the same way as a presenter and has the same properties (except for the view).

To create an event handler, you need to:
  1. extend BaseEventHandler

&lt;E&gt;

:
    * E: type of your event bus interface.

By extending BaseEventHandler, you will have access to the following protected attributes:
  * E eventBus: instance of the event bus injected to the presenter

Instead of extending BaseEventHandler, you can also implement EventHandlerInterface

&lt;E&gt;

.

> 2. have a constructor with no parameter or compatible with GIN (ie annotated with @Inject, see [see GIN website for more information](http://code.google.com/p/google-gin/)).

> 3. annotate it with @EventHandler.


# View #

To create a view, all you need is to create a class with a constructor with no parameter or compatible with GIN (ie annotated with @Inject, see [see GIN website for more information](http://code.google.com/p/google-gin/)).

You can also decide to create a Lazy View ([see Mvp4g Lazy Loading for more information](http://code.google.com/p/mvp4g/wiki/Mvp4gOptimization#Lazy_Loading)).

## Reverse View ##

The GWT team describred another strategy in their second article about MVP to implement this pattern: [Reverse MVP](http://code.google.com/webtoolkit/articles/mvp-architecture-2.html).

With this approach, you will need to inject the presenter to the view, that's why Mvp4g introduces a new feature, Reverse View.

This feature allows you to tell the framework to automatically inject the presenter to the view. All you have to do is having your view implement the ReverseViewInterface.

```
public OneView extends Composite implements ReverseViewInterface<IOnePresenter>{
	
	private IOnePresenter presenter;

	void setPresenter(IOnePresenter presenter){
		this.presenter = presenter;
	}
	
	IOnePresenter getPresenter(){
		return presenter;
	}
	
	...

}
```

The presenter injected to your view will be the presenter where your view is injected.