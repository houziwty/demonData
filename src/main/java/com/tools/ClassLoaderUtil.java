package com.tools;

/**
 * Created by Haoweilai on 2016/12/27.
 *
 */
public class ClassLoaderUtil {
    // ==========================================================================
    // 取得context class loader的方法。
    // ==========================================================================

    /**
     * 取得当前线程的<code>ClassLoader</code>。需要JDK1.2或更高版本的JDK的支持。
     *
     * return 当前线程的<code>ClassLoader</code>
     */
    public static ClassLoader getContextClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }
// ==========================================================================
    // 装入类的方法。
    // ==========================================================================

    /**
     * 从当前线程的<code>ClassLoader</code>装入类。对于JDK1.2以下，则相当于
     * <code>Class.forName</code>。
     *
     * param className 要装入的类名
     * return 已装入的类
     * throws ClassNotFoundException 如果类没找到
     */
    public static Class<?>loadClass(String className)throws  ClassNotFoundException{
        return loadClass(className,getContextClassLoader());
    }

    /**
     * 从指定的调用者的<code>ClassLoader</code>装入类。
     *
     * param className 要装入的类名
     * param referrer  调用者类，如果为<code>null</code>，则该方法相当于
     *                  <code>Class.forName</code>
     * return 已装入的类
     * throws ClassNotFoundException 如果类没找到
     */
    public static Class<?>loadClass(String name,Class<?>referrer){
        ClassLoader classLoader=getReferrerClassLoader(referrer);
return null;
    }
    
    private static ClassLoader getReferrerClassLoader(Class<?> referrer) {
        return null;
    }
    
    /**
     * 从指定的<code>ClassLoader</code>中装入类。如果未指定<code>ClassLoader</code>， 则从装载
     * <code>ClassLoaderUtil</code>的<code>ClassLoader</code>中装入。
     *
     * param className   要装入的类名
     * param classLoader 从指定的<code>ClassLoader</code>中装入类，如果为<code>null</code>
     *                    ，表示从<code>ClassLoaderUtil</code>所在的class loader中装载
     * return 已装入的类
     * throws ClassNotFoundException 如果类没找到
     */
    public static Class<?> loadClass(String className, ClassLoader classLoader)throws ClassNotFoundException {

        return null;
    }
    
}
