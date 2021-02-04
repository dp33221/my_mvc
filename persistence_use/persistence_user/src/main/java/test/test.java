package test;


import org.junit.Test;
import service.MySimpleSpringServiceCgLib;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/12 5:05 下午
 */
public class test implements Itest {
    private MySimpleSpringServiceCgLib mySimpleSpringService;

    @Test
    public void main() throws Exception {
//        new ClassScanner().initObject();
//        final Itest test = new DefaultBeanFactory().getBean("test");
//
//        test.testInfo();
        final Itest o = (Itest) myTest();
        System.out.println(1/0);
//        System.out.println(Info.class.getName());
        o.testInfo();
//        final List<Class> classes = loadClassByLoader(ClassLoader.getSystemClassLoader());
//        classes.forEach(clazz-> System.out.println(clazz.getName()));
    }

    private List<Class> loadClassByLoader(ClassLoader load) throws Exception{
        Enumeration<URL> urls = load.getResources("");
        //放所有类型
        List<Class> classes = new ArrayList<Class>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            //文件类型（其实是文件夹）
            if (url.getProtocol().equals("file")) {
                loadClassByPath(null, url.getPath(), classes, load);
            }
        }
        return classes;
    }
    //通过文件路径加载所有类 root 主要用来替换path中前缀（除包路径以外的路径）
    private void loadClassByPath(String root, String path, List<Class> list, ClassLoader load) {
        File f = new File(path);
        if(root==null)
        {
            root = f.getPath();
        }
        //判断是否是class文件
        if (f.isFile() && f.getName().matches("^.*\\.class$")) {
            try {
                String classPath = f.getPath();
                //截取出className 将路径分割符替换为.（windows是\ linux、mac是/）
                String className = classPath.substring(root.length()+1,classPath.length()-6).replace('/','.').replace('\\','.');
                list.add(load.loadClass(className));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            File[] fs = f.listFiles();
            if (fs == null)
            {
                return;
            }
            for (File file : fs) {
                loadClassByPath(root,file.getPath(), list, load);
            }
        }
    }




    public static Object myTest() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        final Class<?> test = Class.forName("test.test");
        final Method[] methods = test.getMethods();
        for (Method method : methods) {
            final Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                System.out.println(annotation.annotationType().getName()+"-----");
            }
            final Info annotation = method.getAnnotation(Info.class);
            if(annotation!=null){
                final Object o = Proxy.newProxyInstance(test.getClassLoader(),test.class.getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        System.out.println(123);
                        return method.invoke(test.newInstance(),objects);
                    }
                });
                return o;
            }

        }

        return test.newInstance();
    }

    @Info
    @Override
//    @MyTransactional
    public void testInfo() throws Exception {
        mySimpleSpringService.test();
        System.out.println(123332);
    }

    public static String execCurl(String[] cmds) {
        ProcessBuilder process = new ProcessBuilder(cmds);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            return builder.toString();

        } catch (IOException e) {
            System.out.print("error");
            e.printStackTrace();
        }
        return null;

    }



}
