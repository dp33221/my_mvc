package scanner;

import annotation.MyAutowired;
import beanfactory.DefaultBeanFactory;
import invoke.MyConfigurationHandle;
import invoke.MyServiceInvokeHandle;
import invoke.MyTransactionalInvokeHandle;
import invoke.ProxyHandle;
import resources.Resource;
import utils.HelpUtils;

import javax.sql.DataSource;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static beanfactory.DefaultBeanFactory.*;


public class ClassScanner {

  {
    new MyServiceInvokeHandle();
    new MyTransactionalInvokeHandle();
    new MyConfigurationHandle();
  }
  public ClassScanner(){

  }

  public void initObject() throws Exception {
//        final Itest o = (Itest) myTest();
//
//        o.testInfo();
    //获取所有类路径
    final List<Class> classes = loadClassByLoader(ClassLoader.getSystemClassLoader());
    //循环遍历处理
    for (Class clazz : classes) {
      //如果是接口或者注解则不处理
      if(clazz.isAnnotation()||clazz.isInterface()){
        continue;
      }
      //获取类注解
      final Annotation[] annotations = clazz.getAnnotations();
      //遍历
      for (Annotation annotation : annotations) {
        final String name = annotation.annotationType().getName();
        //获取注解对应的处理类
        final ProxyHandle proxyHandle = proxyFactories.get(name);
        if (proxyHandle==null){
          continue;
        }
        //判断是否继承接口
        if(clazz.getInterfaces().length==0){
          proxyHandle.CgLibDynamic(clazz.newInstance());
        }
        else {
          proxyHandle.JdkDynamic(clazz.newInstance());
        }
      }

      final DataSource dateSource = new DefaultBeanFactory().getBean("dateSource");
      if(dateSource!=null&&Resource.conn.get()==null){
        final Connection connection = dateSource.getConnection();
        Resource.conn.set(connection);
      }

    }

    //注入属性
    for (Class clazz : classes) {

      if("".equals(clazz.getSimpleName())){
        continue;
      }
      String className = HelpUtils.toLowerCaseFirstOne(clazz.getSimpleName());
      final Object clazzObject = singletonObjects.get(className);
      if(clazzObject==null){
        continue;
      }
      final Field[] fields = clazz.getDeclaredFields();

      for (Field field : fields) {
        final MyAutowired annotation = field.getAnnotation(MyAutowired.class);
        if(annotation==null){
          continue;
        }
        final String value = annotation.value();
        field.setAccessible(true);
        if("".equalsIgnoreCase(value)){
          final String typeName = field.getType().getSimpleName();
          final String s = HelpUtils.toLowerCaseFirstOne(typeName);
          final Object o = singletonObjects.get(s);
          field.set(clazzObject,o);
        }else {
          final Object o = singletonObjects.get(value);
          field.set(clazzObject,o);
        }
      }
    }

    //生成代理
    for(Class clazz:classes){
      if(clazz.isAnnotation()||clazz.isInterface()){
        continue;
      }
      //解析方法注解生成代理对象
      final Method[] methods = clazz.getMethods();
      for (Method method : methods) {
        final Annotation[] methodAnnotations = method.getAnnotations();
        for (Annotation methodAnnotation : methodAnnotations) {
          final String name = methodAnnotation.annotationType().getName();
          //获取注解对应的处理类
          final ProxyHandle proxyHandle = proxyFactories.get(name);
          if (proxyHandle==null){
            continue;
          }
          Object proxy = singletonObjects.get(HelpUtils.toLowerCaseFirstOne(clazz.getSimpleName()));
          //判断是否继承接口
          if(clazz.getInterfaces().length==0){
            proxy = proxyHandle.CgLibDynamic(proxy);
          }
          else {
            proxy = proxyHandle.JdkDynamic(proxy);
          }
          singletonObjects.put(HelpUtils.toLowerCaseFirstOne(clazz.getSimpleName()),proxy);

        }
      }
    }

  }



  private List<Class> loadClassByLoader(ClassLoader load) throws Exception{
    Enumeration<URL> urls = load.getResources("");
    //放所有类型
//    "file:/Users/ding/Documents/git/persistence_use/persistence_user/target/classes/"
//    "file:/Users/ding/Documents/git/persistence_use/persistence/target/classes/"
//    "file:/Users/ding/Documents/git/persistence_use/myspring/target/classes/"
//    "file:/Users/ding/Documents/git/persistence_use/myspringmvc/target/classes/";
    List<Class> classes = new ArrayList<Class>();
    URL url=new URL("file:/Users/ding/Documents/git/persistence_use/persistence_user/target/classes/");
    loadClassByPath(null, url.getPath(), classes, load);
    URL url1=new URL("file:/Users/ding/Documents/git/persistence_use/persistence/target/classes/");
    loadClassByPath(null, url1.getPath(), classes, load);
    URL url2=new URL("file:/Users/ding/Documents/git/persistence_use/myspring/target/classes/");
    loadClassByPath(null, url2.getPath(), classes, load);
    URL url3=new URL("file:/Users/ding/Documents/git/persistence_use/myspringmvc/target/classes/");
    loadClassByPath(null, url3.getPath(), classes, load);

//    while (urls.hasMoreElements()) {
//      URL url = urls.nextElement();
//      //文件类型（其实是文件夹）
//      if (url.getProtocol().equals("file")) {
//        loadClassByPath(null, url.getPath(), classes, load);
//      }
//    }
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
        list.add(Class.forName(className));
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


}