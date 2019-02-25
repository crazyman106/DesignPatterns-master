知识点:
1. ThreadLocal
2. HandlerThread:为线程提供了Looper,可以在该线程中创建对应的Handler(new Handler(getmLooper())),与其他线程(包含主线程)进行通信
2. ConcurrentHashMap:线程安全，效率提升
3. CopyOnWriteArrayList:线程安全
4. Collections.synchronizedList():同步线程安全



单词:
* concurrent:并发


# ThreadLocal
## 概述
> ThreadLocal的意思是thread local variable(线程局部变量),它的功能非常简单,就是为
> 每一个使用该变量的线程提供一个变量值的副本,是java中一种较为特殊的线程绑定机制,即:每一个线程都可以独立的改变自己的副本,而不会和其他线程的副本发生冲突
> 从线程的角度看,每个线程都保持一个对其线程局部变量副本隐式引用,只要线程是活动的并且ThreadLocal示例是可以访问的;
> 在线程消失后,该线程局部实例的所有副本都会被垃圾回收(除非存在对这些副本的其他引用)

通过ThreadLocal存取的数据,总是与单前的线程相关联,也就是说,JVM为每个运行的线程,绑定了私有的本地实例存取空间,从而为多线程环境出现的并发访问问题提供了一种隔离机制

ThreadLocal如何实现为每一个线程维护变量的副本呢?
实际上在ThreadLocal中有一个Map,用于存储每一个线程的变量副本.

概括起来说,相对于多线程资源共享安全问题,同步机制采取了"以时间换空间"的方式,而ThreadLocal采用了"以空间换时间"的方式.同步机制仅提供一份变量,让不同的线程排队访问,而`ThreadLocal`为每一个线程都提供了一份变量,因此可以同时访问而不相互影响

## API
* `ThreadLocal()`:创建一个线程本地变量
* `T get()`:返回此线程局部变量的单前线程副本中的值,如果这是线程第一次调用该方法,则创建并初始化此副本.
* `T initialValue()`:返回次线程局部变量的单前线程的初始值;最多在每次访问线程获取每个线程局部变量时调用此方法一次,即线程第一次调用`get()`方法访问变量的时候.如果线程先与get()方法调用`set(T)`方法,则不会在线程中在调用`initialValue()`方法.若该实现只返回null:如果我们希望将线程局部变量初始化为null意外的某个值,则必须为ThreadLocal创建子类,并重写该方法.通常使用匿名内部类.initialValue()的典型实现将调用一个适当的构造函数,并返回新构造的对象.
* `void remove()`:移除此线程中局部变量的值.这可能有助于减少线程局部变量的存储需求.如果再次访问此线程局部变量,那么在默认情况下它将拥有其initialValue().
* `void set(T value)`:将此线程局部变量的当前线程副本中的值设置为制定值.
**注意:** 在程序中一般都重新initialValue()方法,给一个特定的初始值.

## 实例

创建一个Bean，通过不同的线程对象设置Bean属性，保证各个线程Bean对象的独立性.
```java
public class Student{
    
    private int age;
    
    public void setAge(int age){
        this.age = age;
    }
    public int getAge(){
        return age;
    }
}

public class ThreadLocalDemo implements Runnable{
    
    // 创建线程局部变量studentLocal，在后面用来保存Student对象
    private final static ThreadLocal studentLocal = new ThreadLocal();
    
    public static void main(String ages[]){
        ThreadLocalDemo td = new ThreadLocalDemo();
        Thread t1 = new Thread(td,"a");//Thread(Runnable target, String name) name:线程名称
        Thread t2 = new Thread(td,"b");
        t1.start();
        t2.start();
    }
    
    @Override
    public void run(){
        accessStudent();
    }
    
    /**
     * 示例业务方法,用来测试
     */
    public void accessStudent(){
        // 获取当前线程的名字
        String currentThreadName = Thread.currentThread().getName();
        System.out.println(currentThreadName+"is running!");
        // 产生一个随机数并打印
        java.util.Random random = new java.util.Random();
        int age = random.nextInt(100);
        System.out.println("thread "+currentThreadName+" set age to: "+age);
        // 获取一个Student对象,并将随机数年龄插入到对象属性中
        Student student = getStudent();
        student.setAge(age);
        System.out.println("thread " + currentThreadName+" first read age is:" + student.getAge());
        try{
            Thread.sleep(500);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("thread " + currentThreadName + " second read age is:" + student.getAge());
    }
    
    protected Student getStudent(){
        // 获取本地线程变量并强制转化为Student类型
        Student student = (Student) studentLocal.get();
        // 线程首次执行此方法的时候，studentLocal.get()肯定为null
        if (student == null){
            // 创建一个Student 对象,并保存到本地线程变量中
            student = new Student();
            studentLocal.set(student);
        }
    }
}
```
// 通过上述示例我们可以看到a,b两个线程在不同时刻打印的值时是相同的,所以我们可以使用ThreadLocal实现线程并发中的数据安全

## 总结
`ThreadLocal`使用场景主要解决了多线程中数据因并发产生不一致性问题.ThreadLocal为每个线程中并发访问的数据提供了一个副本,
通过访问副本来运行业务,这样的结果就是耗费了内存,但是减少了线程同步所产生的性能消耗,同时也减少了线程并发控制的复杂度

`ThreadLocal`不能使用原子类型,只能使用Object类型.ThreadLocal的使用比synchronized要简单得多.

`ThreadLocal`和`Synchonized`都用于解决多线程并发访问。但是`ThreadLocal`与`synchronized`有本质的区别。`synchronized`是利用锁的机制，
使变量或代码块在某一时该只能被一个线程访问。而`ThreadLocal`为每一个线程都提供了变量的副本，使得每个线程在某一时间访问到的并不是同一个对象，
这样就隔离了多个线程对数据的数据共享。而`Synchronized`却正好相反，它用于在多个线程间通信时能够获得数据共享。

Synchronized用于线程间的数据共享，而ThreadLocal则用于线程间的数据隔离。

当然ThreadLocal并不能替代synchronized,它们处理不同的问题域。Synchronized用于实现同步机制，比ThreadLocal更加复杂。

## ThreadLocal使用的一般步骤
1. 在多线程的类中(如ThreadDemo类)中,创建一个ThreadLocal对象xxx,用来保存线程间需要隔离处理的对象
2. 在ThreadDemo类中，创建一个获取要隔离访问的数据的方法getXxx()，在方法中判断，若ThreadLocal对象为null时候，应该new()一个隔离访问类型的对象，并强制转换为要应用的类型。
3. 在ThreadDemo类的run()方法中，通过getXxx()方法获取要操作的数据，这样可以保证每个线程对应一个数据对象，在任何时刻都操作的是这个对象。

... 当然了,在具体的使用中也可以和上述步骤不一样,这个看我们自己发挥了,只要实现了我们想要的功能就OK了

