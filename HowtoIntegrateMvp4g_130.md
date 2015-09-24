## Introduction ##
To integrate Mvp4g into your project, the following steps need to be done
  1. Add needed library to your project or add a Maven dependency
  1. Modify your GWT configuration file (`*`.gwt.xml)
  1. Set your entry point
  1. Create your event bus, presenters, views...
  1. Set up Mvp4g APT (optional)

## Library ##

Add the following libraries to your project:
  * GIN libraries ([Download here](http://code.google.com/p/google-gin/downloads/list))
  * Mvp4g ([Download here](http://code.google.com/p/mvp4g/downloads/list))

You can also download the Mvp4g examples and copy the libraries inside the lib directory

## Maven Dependency ##

Add the maven dependency:
```
<dependency>
	<groupId>com.googlecode.mvp4g</groupId>
	<artifactId>mvp4g</artifactId>
	<version>1.3.1</version>
</dependency>
```

_Mvp4g will soon be available on Maven central, it means you can use Mvp4g Maven repository: ???_

## GWT configuration file ##

Insert Mvp4g module into your project:
```
<inherits name='com.mvp4g.Mvp4gModule'/>
```


## Set an entry point ##

### Using RootPanel ###

If you want to display your project inside a RootPanel, you can:
  * either choose the Mvp4g Entry Point as an entry-point by modifying your GWT configuration file:
```
<entry-point class='com.mvp4g.client.Mvp4gEntryPoint'/>
```
  * or have the following lines (and only the following lines) in your entry-point
```
Mvp4gModule module = (Mvp4gModule)GWT.create( Mvp4gModule.class );
module.createAndStartModule();
RootPanel.get().add( (Widget)module.getStartView() );
```

### Using RootLayoutPanel ###

If you want to display your project inside a RootLayoutPanel (needed incase you use Layout), you need the following lines (and only the following lines) in your entry-point
:
```
Mvp4gModule module = (Mvp4gModule)GWT.create( Mvp4gModule.class );
module.createAndStartModule();
RootLayoutPanel.get().add( (Widget)module.getStartView() );
```


## Event bus,  presenters, views... ##
Now that you have configured your project to use Mvp4g, you can look at the rest of the documentation to create the different elements you need.

## Mvp4g APT ##
By default, all the errors due to wrong Mvp4g configuration are detected when you start the GWT compilation or the dev mode. Thanks to the APT, you can detect most of these errors directly in the IDE. You can check [Mvp4g APT](APT_130.md) for more information.