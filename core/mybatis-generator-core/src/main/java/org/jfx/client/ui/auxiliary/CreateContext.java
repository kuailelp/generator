package org.jfx.client.ui.auxiliary;

import org.jfx.client.model.PropertyModel;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 描述：动态生成 mybatis-generator.xml <br>
 * 创建人：廖鹏 | 创建日期：2019/2/22 17:07 <br>
 */
public class CreateContext {

    private Document document;
    private XmlElement rootElement;
    private XmlElement classPathEntry;
    private XmlElement context;

    /**
     * 描述：初始化配置文件内容 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/24 11:29 <br>
     */
    public void init() {
        createDocument();
        createClassPathEntry();
        createContext();
        setProperty();
        System.out.println(document.getFormattedContent());
    }

    /**
     * 描述：创建基础结构 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/22 17:17 <br>
     */
    private void createDocument() {
        document = new Document(
                XmlConstants.MYBATIS_GENERATOR_CONFIG_PUBLIC_ID,
                XmlConstants.MYBATIS_GENERATOR_CONFIG_SYSTEM_ID);
        rootElement = new XmlElement("generatorConfiguration"); //$NON-NLS-1$
        document.setRootElement(rootElement);
    }

    /**
     * 描述：创建类依赖 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/22 17:46 <br>
     */
    private void createClassPathEntry() {
        classPathEntry = new XmlElement("classPathEntry"); //$NON-NLS-1$
        classPathEntry.addAttribute(new Attribute("location", "D:\\m2\\repository\\mysql\\mysql-connector-java\\5.1.46\\mysql-connector-java-5.1.46.jar"));
        rootElement.addElement(classPathEntry);
    }

    /**
     * 描述：创建context <br>
     * 创建人：廖鹏 | 创建日期：2019/2/22 17:46 <br>
     */
    private void createContext() {
        context = new XmlElement("Context");
        context.addAttribute(new Attribute("id", "context"));
        context.addAttribute(new Attribute("targetRuntime", "MyBatis3"));
    }

    /**
     * 描述：循环添加配置属性 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/24 11:28 <br>
     */
    private void setProperty() {

    }

    private void setPlugin() {

    }


    /**
     * 描述：添加配置属性 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/24 11:27 <br>
     */
    private XmlElement addProperty(String key, String value) {
        XmlElement property = new XmlElement("property");
        property.addAttribute(new Attribute(key, value));
        return property;
    }

    /**
     * 描述：添加查询信息 <br>
     * 创建人：廖鹏 | 创建日期：2019/2/25 14:08 <br>
     */
    private XmlElement addPlugin(String type, List<PropertyModel> propertys) {
        XmlElement plugin = new XmlElement("plugin");
        plugin.addAttribute(new Attribute("type", type));
        propertys.forEach(property -> {
            plugin.addElement(addProperty(property.getKey(), property.getValue()));
        });
        return plugin;
    }


}
