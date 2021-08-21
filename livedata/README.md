## 定义：
LiveData是一种具有生命周期感知能力的可观察数据持有类。 可以保证屏幕上的显示内容和数据一直保持同步。
## 特点：
- LiveData 了解UI界面的状态，如果Activity不在屏幕上显示，LiveData不会触发不必要的界面更新。 如果Activity已经被销毁，会自动清空与Observer的连接，意外调用不会发生。
- LiveData 是一个LifecycleOwner,可以直接感知Activity或Fragment的生命周期

## 基本使用：
### 1. 定义LiveData
livedata一般都存放在ViewModel中， 保证app配置变更时，数据不会丢失

```
/**
 * 横竖屏切换，并不会改变数据
 */
class NewsViewModel: ViewModel() {
    var count = 0
    // livedata既是观察者(观察Activity生命周期), 又是被观察者(livedata数据变化时，通知其他观察者)
    private val nameLiveData = MutableLiveData<String>()

    fun getNameLiveData() = nameLiveData
}
```
### 2. LiveData订阅观察者
定义观察者以观察livedata中数据的变化

```
// observe中的lambda便是观察者对象
viewModel.getNameLiveData().observe(this, { content ->
    tvName.text = content
})
```
### 3. LiveData发送消息通知观察者更新数据

```
// 通过setValue或postValue发送消息
viewModel.getNameLiveData().value = "Hello World ${++viewModel.count}"
```
上面代码会回调Observer的onChange方法

## LiveDataBus设计
设计思路: 通过一个集合统一管理所有的LiveData
设计目的：不同页面之间信息数据传递
缺陷: 原来的执行顺序为 创建LiveData -> 绑定Observer -> setValue -> 回调onChanged()
而LiveDataBus在使用时，会出现数据粘滞问题，即在订阅之前就发送消息，订阅之后，会收到订阅前发送的消息。 即执行顺序为 创建LiveData ->setValue -> 回调onChanged() -> 绑定Observer

缺陷处理方案：
通过反射，让第一次setValue不起作用即可