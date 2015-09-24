

# Optimization #

Mvp4g provides different ways to optimize your application.

## Minimum of instances creation ##

Mvp4g creates one instance for each presenter. Each presenter has also its own instance of a view. For services and history converters, the framework uses a singleton pattern to have only one instance for each of those elements, no matter how many times they're used.

## Useless elements are ignored ##

This optimization is done automaticaly by Mvp4g and doesn't need any extra configuration from your part. The framework will automaticaly ignore any element (presenter/view/service/history converter) that are useless. Thus those useless elements won't be instantiated, making your application faster to start and use less memory.

An element is considered useless when:
  * presenter: if it handles no event and its view is not the start view or if it's not a multiple presenter.
  * view: if it is not injected into a useful presenter.
  * history converter: if it is not associated to an event.
  * service: if it is not injected to a useful presenter or history converter.

## Lazy Loading ##

Mvp4g let you implement a mechanism similar to GWT LazyPanel. Like a GWT LazyPanel that is built only when it's visible, you can build your presenter (and its view) only when the presenter has to handle its first event. Thus, when you start your application, the instantiation of your views and presenters will be faster as they will be built only when handling their first event.

Before forwarding any event to a presenter, Mvp4g framework makes sure that the bind method has been called for the presenter. If it's not the case, then the framework calls it. Thus you can build your presenter (and its views) when the bind method is called since this method will be called only once before the presenter has to handle its first event.

![http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/lazy_loading.png](http://mvp4g.googlecode.com/svn/tags/mvp4g-1.2.0/documentation/uml/lazy_loading.png)

1: these methods are called only the first time the presenter has to handle an event. The isActivated method has 2 purposes: check if the presenter is activated, call the bind method if needed.

To create a Lazy Presenter, your presenter has to extend LazyPresenter and overrides the following methods:
  * createPresenter: in this method, you should create your presenter (ie instantiate all the attributes of your presenter). By default this method does nothing.
  * bindView: in this method, you should bind your presenter to its view. By default this method does nothing.

```
public class MyLazyCompanyPresenter extends LazyPresenter<MyLazyViewInterface, MyEventBus> {

	public interface MyLazyViewInterface extends LazyView {
		getLeftButton();
		getRightButton();
	}

	private OnePresenterAttribute oneAttribute;
	private AnotherPresenterAttribute anotherAttribute;


	public void bindView() {
		view.getLeftButton().addClickHandler(new ClickHandler() {...});
		view.getRightButton().addClickHandler(new ClickHandler() {...});
	}


	public void createPresenter() {
		oneAttribute = new OnePresenterAttribute();
		anotherAttribute = new AnotherPresenterAttribute();
	}
	...

}
```

LazyPresenter's view have to implement LazyView. This interface provides a `createView` method to build the view only when the presenter has to handle its first event.

```
public class MyLazyView extends Composite implements MyLazyViewInterface{

        private Button leftButton;
        private Button rightButton;

	public void createView() {
             leftButton = new Button("edit");
             rightButton = new Button("right");
             HorizontalPanel hp = new HorizontalPanel();
             hp.add(leftButton);
             hp.add(rightButton);
             initWidget(hp);		
	}
	...

}
```