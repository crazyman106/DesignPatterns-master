(阅读)http://gityuan.com/android/?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io

# idl(进程间通信)
**Linux现有的所有进程间IPC方式:**
1. 管道：在创建时分配一个page大小的内存，缓存区大小比较有限；
2. 消息队列：信息复制两次，额外的CPU消耗；不合适频繁或信息量大的通信；
3. 共享内存：无须复制，共享缓冲区直接付附加到进程虚拟地址空间，速度快；但进程间的同步问题操作系统无法实现，必须各进程利用同步工具解决；
4. 套接字：作为更通用的接口，传输效率低，主要用于不通机器或跨网络的通信；
5. 信号量：常作为一种锁机制，防止某进程正在访问共享资源时，其他进程也访问该资源。因此，主要作为进程间以及同一进程内不同线程之间的同步手段。
6. 信号: 不适用于信息交换，更适用于进程中断控制，比如非法内存访问，杀死某个进程等；



## aidl(android 进程间通信)

1. **从性能的角度 数据拷贝次数：** Binder数据拷贝只需要一次，而管道、消息队列、Socket都需要2次，但共享内存方式一次内存拷贝都不需要；从性能角度看，Binder性能仅次于共享内存。
2. **从稳定性的角度** Binder是基于C/S架构的，简单解释下C/S架构，是指客户端(Client)和服务端(Server)组成的架构，Client端有什么需求，直接发送给Server端去完成，架构清晰明朗，Server端与Client端相对独立，稳定性较好；而共享内存实现方式复杂，没有客户与服务端之别， 需要充分考虑到访问临界资源的并发同步问题，否则可能会出现死锁等问题；从这稳定性角度看，Binder架构优越于共享内存。
3. **从安全的角度**传统Linux IPC的接收方无法获得对方进程可靠的UID/PID，从而无法鉴别对方身份；而Android作为一个开放的开源体系，拥有非常多的开发平台，App来源甚广，因此手机的安全显得额外重要；对于普通用户，绝不希望从App商店下载偷窥隐射数据、后台造成手机耗电等等问题，传统Linux IPC无任何保护措施，完全由上层协议来确保。 Android为每个安装好的应用程序分配了自己的UID，故进程的UID是鉴别进程身份的重要标志，前面提到C/S架构，Android系统中对外只暴露Client端，Client端将任务发送给Server端，Server端会根据权限控制策略，判断UID/PID是否满足访问权限，目前权限控制很多时候是通过弹出权限询问对话框，让用户选择是否运行。Android 6.0，也称为Android M.传统IPC只能由用户在数据包里填入UID/PID；另外，可靠的身份标记只有由IPC机制本身在内核中添加。其次传统IPC访问接入点是开放的，无法建立私有通道。从安全角度，Binder的安全性更高。
4. **从语言层面的角度**Linux是基于C语言(面向过程的语言)，而Android是基于Java语言(面向对象的语句)，而对于Binder恰恰也符合面向对象的思想，将进程间通信转化为通过对某个Binder对象的引用调用该对象的方法，而其独特之处在于Binder对象是一个可以跨进程引用的对象，它的实体位于一个进程中，而它的引用却遍布于系统的各个进程之中。可以从一个进程传给其它进程，让大家都能访问同一Server，就像将一个对象或引用赋值给另一个引用一样。Binder模糊了进程边界，淡化了进程间通信过程，整个系统仿佛运行于同一个面向对象的程序之中。从语言层面，Binder更适合基于面向对象语言的Android系统，对于Linux系统可能会有点“水土不服”。**Android OS中的Zygote进程的IPC采用的是Socket（套接字）机制** **Android中的Kill Process采用的signal（信号）机制等等** **Binder更多则用在system_server进程与上层App层的IPC交互**


**Binder**在`Zygote`孵化出`system_server`进程后，在`system_server`进程中出初始化支持整个`Android framework`的各种各样的`Service`，而这些Service从大的方向来划分，分为**Java层Framework**和**Native Framework层(C++)** 的Service，几乎都是基于Binder IPC机制。

1. **java framework**:作为Server端继承(或间接继承)于Binder类,Client端继承(或间接继承)于BinderProxy类.例如:ActivityManagerService(用于控制Activity、Service、进程等) 这个服务作为Server端,间接继承Binder类,而相应的ActivityManager作为Client端，间接继承于BinderProxy类.当然还有PackageManagerService、WindowManagerService等等很多系统服务都是采用C/S架构；
2. **Native Framework**:这是C++层，作为Server端继承(或间接继承)于BBinder类，Client端继承(或间接继承)于BpBinder。例如MediaPlayService(用于多媒体相关)作为Server端，继承于BBinder类，而相应的MediaPlay作为Client端，间接继承于BpBinder类。


`android`中可以使用process属性指定一个进程,还可以通过jni在native层fork出一个进程来.
pid:是进程id,uid是用户id,由于android系统是单用户系统,所以可以通过uid来进行数据共享;
使用process指定多进程的方式有两种:
1. ：remote 进程名以 “:”的含义是指要在进程名前面附加上当前的包名，这个进程属于当前应用的私有进程，其他应用不可以和他跑在同一个进程。
2. xxxxxx 这种属于全局进程，其他应用可以通过ShareUID方式可以和它跑在同一个进程，我们都知道系统会为每个应用分配一个唯一的UID，具有相同UID的应用才能共享数据。两个应用通过ShareUID跑在同一个进程，是需要相同的ShareUID并且签名相同才可以。不管它们是不是跑在同一个进程中，具有相同ShareUID的它们可以访问对方的私有数据，如：data目录、组件信息等。当然如果是在同一个进程中，除了data目录、组件信息还能共享内存数据。


**多进程运行机制:**
Android为每一个应用分配了一个独立的虚拟机，或者说为每一个进程都分配了一个独立的虚拟机，不同的虚拟机在内存分配上有不同的地址空间，这就导致在不同的虚拟机访问同一个类的对象会产生多分副本。
所有运行在不同进程中的四大组件，只要它们之间需要通过内存来共享数据，都会共享失败，这也是多进程所带来的主要影响，一般来说，使用多进程会造成如下几方面的问题。

* 静态成员和单例模式完全失效（都是不同的虚拟机）
* 线程同步机制完全失效（都不是同一块内存了）
* SharePreferences 的可靠性下降（底层通过XML去执行操作的，并发很可能出问题，甚至并发读、写都有可能出问题）
* Application会多次创建（当一个组件跑在一个新的进程中，由于系统要在创建新的进程同时分配独立的虚拟机，所以这个过程其实就是启动一个应用的过程，因此系统又把这个应用重新启动了一遍，既然都重新启动了，那么自然会创建新的Application）

https://blog.csdn.net/a565102223/article/details/70186070



               
  
             
          






https://blog.csdn.net/qq_30379689/article/details/79451596
https://www.jianshu.com/p/429a1ff3560c?spm=a2c4e.11153940.blogcont686986.16.4ab5983fjhv9jU