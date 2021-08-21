## 1. Lifecycle优点
- Lifecycle可以有效避免内存泄漏和解决android 生命周期的常见难题
- Lifecycle是一个表示android生命周期及状态的对象
- LifecycleOwner 用于连接有生命周期的对象。 如Activity，Fragment
- LifecycleObserver用于观察LifecycleOwner
##  2. 基本使用
Lifecycle 框架使用观察者模式实现观察者监听被观察者的生命周期

### (1). 定义被观察者
通过实现LifecycleOwner接口

```
public class ComponentActivity extends androidx.core.app.ComponentActivity implements
        ContextAware,
        LifecycleOwner,
        ViewModelStoreOwner,
        HasDefaultViewModelProviderFactory,
        SavedStateRegistryOwner,
        OnBackPressedDispatcherOwner,
        ActivityResultRegistryOwner,
        ActivityResultCaller {
            ...
        }
```
ComponentActivity已经实现LifecycleOwner接口，因此我们的Activity无需再实现。

### (2). 定义观察者
通过实现LifecycleObserver接口

```
open class BasePresenter: LifecycleObserver {
    val TAG: String = MODULE_TAG + javaClass.canonicalName
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
        Log.d(TAG, "onCreate: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Log.d(TAG, "onStart: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.d(TAG, "onResume: ")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.d(TAG, "onPause: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Log.d(TAG, "onStop: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
    }
}
```
通过注解在观察者类中定义需要监听的生命周期

### (3). 实现订阅关系
在Activity中使用getLifecycle().addObserver(presenter)实现订阅

##  2. 实现原理

借鉴了Glide生命周期实现原理，在ComponentActivity中创建了ReportFragment来监听Activity生命周期的变化。

这里的核心是生命周期事件的变化是如何进行的？

Lifecycle采用了枚举类型枚举了生命周期事件

```
public enum Event {
    /**
     * Constant for onCreate event of the {@link LifecycleOwner}.
     */
    ON_CREATE,
    /**
     * Constant for onStart event of the {@link LifecycleOwner}.
     */
    ON_START,
    /**
     * Constant for onResume event of the {@link LifecycleOwner}.
     */
    ON_RESUME,
    /**
     * Constant for onPause event of the {@link LifecycleOwner}.
     */
    ON_PAUSE,
    /**
     * Constant for onStop event of the {@link LifecycleOwner}.
     */
    ON_STOP,
    /**
     * Constant for onDestroy event of the {@link LifecycleOwner}.
     */
    ON_DESTROY,
    /**
     * An {@link Event Event} constant that can be used to match all events.
     */
    ON_ANY;

    ...
}
```
使用LifecycleRegistry类中的mState变量记录状态(同样是枚举类型State)的变化，通过moveToState同步生命周期状态变化。变化过程及状态如下：

```
@NonNull
public State getTargetState() {
    switch (this) {
        case ON_CREATE:
        case ON_STOP:
            return State.CREATED;
        case ON_START:
        case ON_PAUSE:
            return State.STARTED;
        case ON_RESUME:
            return State.RESUMED;
        case ON_DESTROY:
            return State.DESTROYED;
        case ON_ANY:
            break;
    }
    throw new IllegalArgumentException(this + " has no target state");
}
```
状态变化图如下：

![状态变化图](https://note.youdao.com/yws/public/resource/1a898619258d1acef279486b9de3b002/xmlnote/WEBRESOURCE2c39fbb5ef85367b1f3294e3a40c5abf/22459)

通过反射实现LifecycleObserver接口的子类， 力阳OnLifecycleEvent注解记录需要回调相应事件的方法，从而进行调用实现
Lifecycle底层原理整个时序图：

![Lifecycle时序图](https://note.youdao.com/yws/public/resource/1a898619258d1acef279486b9de3b002/xmlnote/WEBRESOURCEe3b2ef7b6120739d40a99e47f9ddd963/22460)