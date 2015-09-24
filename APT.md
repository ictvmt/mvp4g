## Introduction ##
All the Mvp4g configuration errors are detected during GWT compilation or while launching GWT dev mode. Thanks to Mvp4g APT, you can detect most of the errors in the IDE.

The following errors can be detected thanks to the APT:
  * Handler doesn't define the right handling method for an event.
  * Handler is not annotated with @Presenter or @EventHandler
  * Incompatible event bus is injected to an handler.
  * Incompatible view is injected to a presenter.
  * History Converter doesn't define the right converting method for an event.
  * Incompatible event bus is injected to an history converter.
  * History Converter is not annotated with @History.
  * Parent or child module doesn't define the right event

## Configuring Eclipse to use Mvp4g APT ##

  * Download the Mvp4g APT jar ([Download here](http://code.google.com/p/mvp4g/downloads/list))
  * Open the properties for your project.
  * Ticking all the boxes on the Annotation Processing page.
> ![http://mvp4g.googlecode.com/svn-history/r840/wiki/images/apt_eclipse1.png](http://mvp4g.googlecode.com/svn-history/r840/wiki/images/apt_eclipse1.png)
  * Add the Mvp4g jar to the factory path.
> ![http://mvp4g.googlecode.com/svn-history/r840/wiki/images/apt_eclipse2.png](http://mvp4g.googlecode.com/svn-history/r840/wiki/images/apt_eclipse2.png)