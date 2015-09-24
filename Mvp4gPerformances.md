## Introduction ##

The goal of this test is to compare Mvp4g event bus and [GWT HandlerManager](http://google-web-toolkit.googlecode.com/svn/javadoc/latest/com/google/gwt/event/shared/HandlerManager.html) performances in order to verify applications built with Mvp4g framework aren't slower than applications built with GWT HandlerManager.

## Test Description ##

10 events handled by 10 presenters (each presenter handles the 10 events) are sent N times.

Presenters' handle methods do nothing in order to measure only GWT HandlerManager/Mvp4g EventBus process time.

Events instances needed to send events to GWT HandlerManager are created before test starts and the same instances of events are sent for each step of the test.

You can find the test project [here](http://code.google.com/p/mvp4g/source/browse/#svn/tags/mvp4g-1.0.0/examples/Mvp4gPerformances) and try it [here](http://mvp4gperformances.appspot.com).

## Results ##

**IE 8**
|Number of events sent | Mvp4g Event bus<br />(time in ms) | GWT HandlerManager<br />(time in ms) |
|:---------------------|:----------------------------------|:-------------------------------------|
|10 x 1                | 1                                 | 3                                    |
|10 x 10               | 4                                 | 27                                   |
|10 x 100              | 41                                | 276                                  |
|10 x 1000             | 412                               | 2745                                 |
|10 x 10000            | 6561                              | error before the end                 |


**FF 3.5**
|Number of events sent | Mvp4g Event bus<br />(time in ms) | GWT HandlerManager<br />(time in ms) |
|:---------------------|:----------------------------------|:-------------------------------------|
|10 x 1                | 0                                 | 1                                    |
|10 x 10               | 2                                 | 10                                   |
|10 x 100              | 19                                | 96                                   |
|10 x 1000             | 186                               | 948                                  |
|10 x 10000            | 1073                              | 4791                                 |

## Conclusion ##

Mvp4g event bus processes events faster than GWT HandlerManager. However time to process a few events is the same and in most cases applications won't send more than 1 or 2 events at a time.

To sum up, I would just say that application developped with Mvp4g framework aren't slower than an application developped with GWT HandlerManager.