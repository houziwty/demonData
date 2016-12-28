package com.tools;

/**
 * Created by Haoweilai on 2016/12/27.
 */
public class ClassLoaderUtil {
    // ==========================================================================
    // 取得context class loader的方法。
    // ==========================================================================

    /**
     * 取得当前线程的<code>ClassLoader</code>。需要JDK1.2或更高版本的JDK的支持。
     * <p>
     * return 当前线程的<code>ClassLoader</code>
     */
    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
// ==========================================================================
    // 装入类的方法。
    // ==========================================================================

    /**
     * 从当前线程的<code>ClassLoader</code>装入类。对于JDK1.2以下，则相当于
     * <code>Class.forName</code>。
     * <p>
     * param className 要装入的类名
     * return 已装入的类
     * throws ClassNotFoundException 如果类没找到
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        return loadClass(className, getContextClassLoader());
    }

    /**
     * 从指定的调用者的<code>ClassLoader</code>装入类。
     * <p>
     * param className 要装入的类名
     * param referrer  调用者类，如果为<code>null</code>，则该方法相当于
     * <code>Class.forName</code>
     * return 已装入的类
     * throws ClassNotFoundException 如果类没找到
     */
    public static Class<?> loadClass(String className, Class<?> referrer) {
        ClassLoader classLoader = getReferrerClassLoader(referrer);
        // 如果classLoader为null，表示从ClassLoaderUtil所在的classloader中装载，
        // 这个classloader未必是System class loader。
        return loadClass(className, referrer);
    }

    /**
     * 从指定的<code>ClassLoader</code>中装入类。如果未指定<code>ClassLoader</code>， 则从装载
     * <code>ClassLoaderUtil</code>的<code>ClassLoader</code>中装入。
     * <p>
     * param className   要装入的类名
     * param classLoader 从指定的<code>ClassLoader</code>中装入类，如果为<code>null</code>
     * ，表示从<code>ClassLoaderUtil</code>所在的class loader中装载
     * return 已装入的类
     * throws ClassNotFoundException 如果类没找到
     */
    public static Class<?> loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
        if (className == null) {
            return null;
        }
        if (classLoader == null) {
            return Class.forName(className);
        } else {
            return Class.forName(className, true, classLoader);
        }
    }

    /**
     * 取得调用者的class loader。
     * <p>
     * param referrer 调用者类
     * return 调用者的class loader，如果referrer为<code>null</code>，则返回
     * <code>null</code>
     */
    private static ClassLoader getReferrerClassLoader(Class<?> referrer) {
        ClassLoader classloader = null;
        if (referrer != null) {
            classloader = referrer.getClassLoader();
            // classLoader为null，说明referrer类是由bootstrap classloader装载的，
            // 例如：java.lang.String
            if (classloader == null) {
                classloader = ClassLoader.getSystemClassLoader();
            }
        }

        return classloader;
    }

    // ==========================================================================
    // 装入并实例化类的方法。
    // ==========================================================================

    /**
     * 从当前线程的<code>ClassLoader</code>装入类并实例化之。
     * <p>
     * param className 要实例化的类名
     * return 指定类名的实例
     * throws ClassNotFoundException      如果类没找到
     * throws ClassInstantiationException 如果实例化失败
     */
    public static Object newInstance(String className) throws ClassNotFoundException {
        return newInstance(loadClass(className));

    }

    /**
     * 从指定的调用者的<code>ClassLoader</code>装入类并实例化之。
     * <p>
     * param className 要实例化的类名
     * param referrer  调用者类，如果为<code>null</code>，则从<code>ClassLoaderUtil</code>
     * 所在的class loader装载
     * return 指定类名的实例
     * throws ClassNotFoundException      如果类没找到
     * throws ClassInstantiationException 如果实例化失败
     */
    public static Object newInstance(String className, Class<?> referrer) throws ClassNotFoundException {
        return newInstance(loadClass(className, referrer));
    }

    /**
     * 从指定的<code>ClassLoader</code>中装入类并实例化之。如果未指定<code>ClassLoader</code>， 则从装载
     * <code>ClassLoaderUtil</code>的<code>ClassLoader</code>中装入。
     * <p>
     * param className   要实例化的类名
     * param classLoader 从指定的<code>ClassLoader</code>中装入类，如果为<code>null</code>
     * ，表示从<code>ClassLoaderUtil</code>所在的class loader中装载
     * return 指定类名的实例
     * throws ClassNotFoundException      如果类没找到
     * throws ClassInstantiationException 如果实例化失败
     */
    public static Object newInstance(String className, ClassLoader classLoader) throws ClassNotFoundException {
        return newInstance(loadClass(className, classLoader));
    }

    /**
     * 创建指定类的实例。
     *
     * param clazz 要创建实例的类
     * return 指定类的实例
     * throws ClassInstantiationException 如果实例化失败
     */
    private static Object newInstance(Class<?> className)  {
        if (className == null)
            return null;
        try {
            return className.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
}
