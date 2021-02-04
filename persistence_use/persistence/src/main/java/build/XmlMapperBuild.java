package build;

import config.Configuration;
import config.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * @author dingpei
 * @Description: mapper.xml解析类
 * @date 2020/11/10 8:02 下午
 */
public class XmlMapperBuild {
    private Configuration configuration;
    public XmlMapperBuild(Configuration configuration){
        this.configuration=configuration;
    }

    /**
     * @Description: 读取mapper文件 封装成mappedStatement对象
     * @param inputStream 字节输入流
     * @return void
     * @Author: dingpei
     * @Date: 2020/11/10 8:14 下午
     */
    public void build(InputStream inputStream) throws DocumentException, ClassNotFoundException {
        //解析字节流
        final Document document = new SAXReader().read(inputStream);
        //获取根标签
        final Element rootElement = document.getRootElement();
        //获取mapper文件namespace
        final String namespace = rootElement.attributeValue("namespace");
        //查询所有的select标签
        final List<Element> selectList = rootElement.selectNodes("//select");

        iteratorElement(selectList,namespace,"select");

        //查询所有update标签
        final List<Element> updateList = rootElement.selectNodes("//update");
        iteratorElement(updateList,namespace,"update");

        //查询所有insert标签
        final List<Element> insertList = rootElement.selectNodes("//insert");
        iteratorElement(insertList,namespace,"insert");

        //查询所有delete标签
        final List<Element> deleteList = rootElement.selectNodes("//delete");
        iteratorElement(deleteList,namespace,"delete");




    }

    /**
     * @Description: 遍历标签存入configuraation的mappedStatement集合
     * @param elementList element集合
     * @param namespace 全限名
     * @return void
     * @Author: dingpei
     * @Date: 2020/11/17 9:21 下午
     */
    private void iteratorElement(List<Element> elementList,String namespace,String executorType) throws ClassNotFoundException {
        for (Element element : elementList) {
            final String id = element.attributeValue("id");
            final String resultType = element.attributeValue("resultType");
            final String paramType = element.attributeValue("paramType");
            Class<?> paramClassType=getClass(paramType);
            Class<?> resultClassType= getClass(resultType);
            final String sql = element.getTextTrim();
            String statementId=namespace+id;
            configuration.getMappedStatementList().put(statementId, MappedStatement.builder().id(id).paramType(paramClassType).resultType(resultClassType).executorType(executorType).sql(sql).build());
        }
    }

    private Class<?> getClass(String s) throws ClassNotFoundException {
        if(null==s||s.equals("")){
            return null;
        }
        return Class.forName(s);
    }
}
