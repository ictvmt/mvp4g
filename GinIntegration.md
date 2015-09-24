

# GIN Integration #

Mvp4g manages the injection of the view to a presenter thanks to the @Presenter annotation. Now the framework integrates GIN in order to give the possibility to use the power of GIN. Any Mvp4g element(presenters, views, history converters & event handlers) is now generated thanks to a GIN injector, which means that you can use GIN features to inject any object to your presenters, views & history converters:

```
@Presenter(view=OneView.class)
public class OnePresenter extends LazyPresenter<IOneView> {

	@Inject
	private IOneServiceAsync oneService;

}
```

The injection of the view to the presenter still has to be managed by Mvp4g (thanks to the @Presenter annotation). Trying to inject the view to the presenter thanks GIN
would be useless as it would create 2 views for your presenters (one created by Mvp4g, the other by the GIN injection). This choice was made to keep the use of GIN optional
and also to let Mvp4g control that a view is injected to the presenter.

You can now use the GIN feature to inject your services to your presenters but you can still the @InjectService annotation.

## Configuring your own GIN modules ##

If you start using GIN, the first you may have to do is to define your GIN modules (see [see GIN website for more information](http://code.google.com/p/google-gin/)). Once you have defined your modules, you will need to tell Mvp4g to use them thanks to the ginModules attribute of the @Events that annotates your event bus interface.
You can specify one or several GIN modules to use.

```
@Events(..., ginModules={MyGinModule1.class, MyGinModule2.class})
public interface MyEventBus extends EventBus {

}
```

## Using GIN to manage nested views ##

Before GIN injection, if you wanted to display a view inside another view, you had to:
- define an event in order the nested presenter pass its view to the main presenter.
- have the main presenter handles this event and call a method on the main view to add the nested view.
- define a method in the view to add the nested view.

This method is really useful in case the nested view can be dynamically changed. It may seem like a lot of boilerplate code in case your view will always stay the same (for example, you may have an application with a header, a menu and a footer that will always be the same but you want to have a presenter for each of these sections). Thanks to GIN, you can ease this process by injecting the view of the nested presenter directly inside the view of the main presenter.

In order to do this, you need to:
- inject the nested view inside the main view:
```
public class MainView ... {

	@Inject
	NestedView nestedView;
	...
	
}
```
-set the nested view as a singleton in order the same instance to be injected in the nested presenter & the main presenter:
```
@Singleton
public class NestedView ... {
	...
}
```
-make sure your nested presenter is binded with the nested view before its view is displayed, otherwise no action from the view will be managed. The easiest way to do this, it's use a bind feature. For example, if your nested view is displayed at start, it can handle the start event.
```
@EventBus(startView=MainView.class)
public interface OneEventBus extends EventBus {
	
	@Event(handler=MainPresenter.class, bind=NestedPresenter.class}
	void start();
	...
	
}

@Presenter(view=NestedView.class)
public class NestedPresenter extends BasePresenter<NestedPresenter.INestedView, OneEventBus>{

	public interface INestedView {...}
	
	public void bind(){
		//bind your view
	}
	
	...
	
}
```

## Creating Cross-Module Singleton ##

Since each module has its own injector, if you define a class as a singleton with GIN, you will still have one instance for each module.

If you ever need a cross-module singleton, this would be the best approach:
```
public class MyGinModule extends AbstractGinModule { 

    private static CrossModuleSingleton SINGLETON; 

    @Override 
    protected void configure() { 
        //add here any extra configuration you need 
    } 


    @Provides 
    public CrossModuleSingleton getCrossModuleObject(){ 
        if(SINGLETON == null){ 
	    SINGLETON = new CrossModuleSingleton(); 
        } 
	return SINGLETON; 
    } 
	
} 

//for each event bus 
@EventBus(ginModules = MyGinModule.class) 
public interface MyEventBus ... { ... } 
```

## Setting GIN module for multiple devices ##
Modern applications usually have one version for each type of device (desktop, mobile, tablet). Thanks to the MVP pattern, you can easily create a version for
each device by just switching the injected view implementation. To switch the view, you just have to change the GIN module used to configure your injection.
You can easily do this with Mvp4g thanks to the attribute 'ginModuleProperties' of @Events.

The idea is that you can configure a deferred property inside your GWT xml configuration file that Mvp4g will use when generating your event bus. This property
should define a GIN module class name. Before doing so, you may want to set mobile user agent, you can read [this page](http://code.google.com/p/google-web-toolkit/wiki/ConditionalProperties) to learn how to do it (don't forget to read the example 2 to avoid permutation explosion).

The first step is then to define a deferred property inside our GWT xml file:
```
<module rename-to='mvp4gmodules'>  
	...
	<define-property name="ginModule" values="com$mvp4g$client$DefaultMvp4gGinModule,com$mvp4g$example$client$MobileGinModule,com$mvp4g$example$client$TabletGinModule" />

	<set-property name="ginModule" value="com$mvp4g$client$DefaultMvp4gGinModule" >
		<when-property-is name="mobile.user.agent" value="not_mobile" />
	</set-property>
	<set-property name="ginModule" value="com$mvp4g$example$client$MobileGinModule">  
		<any>
			<when-property-is name="mobile.user.agent" value="android" />
			<when-property-is name="mobile.user.agent" value="iphone" />
		</any>	
	</set-property>
	<set-property name="ginModule" value="com$mvp4g$example$client$TabletGinModule">  
		<when-property-is name="mobile.user.agent" value="ipad" />
	</set-property>
	...
</module>		
```

![http://www.qondio.com/graphics/attention.png](http://www.qondio.com/graphics/attention.png) '.' is not a valid value for a deferred property so you will have to replace it by '$'. Thus `com.mvp4g.client.DefaultMvp4gGinModule` becomes `com$mvp4g$client$DefaultMvp4gGinModule`

Now that you have defined this property, you can just tell Mvp4g to use it:
```
@EventBus(..., ginModuleProperties="ginModule")
public interface OneEvent extends EventBus 
```