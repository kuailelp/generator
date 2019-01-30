package org.mybatis.generator.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改生成对象位置
 */
public class JavaCreateBaseMapperPlugin extends PluginAdapter {


    private FullyQualifiedJavaType modelType;
    private FullyQualifiedJavaType modelBaseType;
    private FullyQualifiedJavaType mapperType;
    private FullyQualifiedJavaType mapperBaseType;
    private String pojoUrl;
    private String packageModel;
    private String projectMode;
    private String packageXML;
    private String projectXML;
    private String packageMapper;
    private String projectMapper;


    /**
     * This method is called after all the setXXX methods are called, but before
     * any other method is called. This allows the plugin to determine whether
     * it can run or not. For example, if the plugin requires certain properties
     * to be set, and the properties are not set, then the plugin is invalid and
     * will not run.
     *
     * @param warnings add strings to this list to specify warnings. For example, if
     *                 the plugin is invalid, you should specify why. Warnings are
     *                 reported to users after the completion of the run.
     * @return true if the plugin is in a valid state. Invalid plugins will not
     * be called
     */
    @Override
    public boolean validate(List<String> warnings) {
        pojoUrl = context.getJavaModelGeneratorConfiguration().getTargetPackage();
        packageModel = context.getJavaModelGeneratorConfiguration().getTargetPackage();
        packageModel = packageModel.replace(".base", "");
        projectMode = context.getJavaModelGeneratorConfiguration().getTargetProject();
        packageXML = context.getSqlMapGeneratorConfiguration().getTargetPackage();
        packageXML = packageXML.replace(".base", "");
        projectXML = context.getSqlMapGeneratorConfiguration().getTargetProject();
        packageMapper = context.getJavaClientGeneratorConfiguration().getTargetPackage();
        packageMapper = packageMapper.replace(".base", "");
        projectMapper = context.getJavaClientGeneratorConfiguration().getTargetProject();

        return true;
    }

    @Override
    public void setContext(Context context) {
        // 修改映射XML文件到base包下
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration =
                context.getSqlMapGeneratorConfiguration();
        String sqlTargetPackage = sqlMapGeneratorConfiguration.getTargetPackage();
        sqlTargetPackage += ".base";
        sqlMapGeneratorConfiguration.setTargetPackage(sqlTargetPackage);
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
        // 修改映射Mapper文件到base包下
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration =
                context.getJavaClientGeneratorConfiguration();
        String clientTargetPackage = javaClientGeneratorConfiguration.getTargetPackage();
        clientTargetPackage += ".base";
        javaClientGeneratorConfiguration.setTargetPackage(clientTargetPackage);
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
        // 修改映射Entity文件到base包下
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration =
                context.getJavaModelGeneratorConfiguration();
        String modelTargetPackage = javaModelGeneratorConfiguration.getTargetPackage();
        modelTargetPackage += ".base";
        javaModelGeneratorConfiguration.setTargetPackage(modelTargetPackage);
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);
        super.setContext(context);
    }


    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        String table = introspectedTable.getBaseRecordType();
        String tableName = table.replaceAll(this.pojoUrl + ".", "");
        modelType = new FullyQualifiedJavaType(packageModel + "." + tableName + "Exp");
        modelBaseType = new FullyQualifiedJavaType(table);
        mapperType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType()
                .replace(".base", "").replace("Mapper", "ExpMapper"));
        mapperBaseType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        List<GeneratedJavaFile> files = new ArrayList();
        files.add(createExpModel(introspectedTable, tableName));
        files.add(createExpMapper(introspectedTable, tableName));
        return files;
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        List<GeneratedXmlFile> files = new ArrayList();
        files.add(createExpXml(introspectedTable));
        return files;
    }


    /**
     * 描述：生成扩展类属性 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 21:55 <br>
     */
    private GeneratedJavaFile createExpModel(IntrospectedTable introspectedTable, String tableName) {
        TopLevelClass topLevelClass = new TopLevelClass(modelType);
        topLevelClass.setSuperClass(modelBaseType);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addImportedType(modelBaseType);
        GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass, projectMode, context.getJavaFormatter());
        return file;
    }

    /**
     * 描述：生成扩展接口属性 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 21:55 <br>
     */
    private GeneratedJavaFile createExpMapper(IntrospectedTable introspectedTable, String tableName) {
        Interface interface1 = new Interface(mapperType);
        interface1.addSuperInterface(mapperBaseType);
        interface1.setVisibility(JavaVisibility.PUBLIC);
        interface1.addImportedType(mapperBaseType);
        GeneratedJavaFile file = new GeneratedJavaFile(interface1, projectMapper, context.getJavaFormatter());
        return file;
    }

    /**
     * 描述：生成扩展接口属性 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 21:55 <br>
     */
    private GeneratedXmlFile createExpXml(IntrospectedTable introspectedTable) {
        Document document = new Document();
        XmlElement rootElement = new XmlElement("mapper");
        rootElement.addAttribute(new Attribute("namespace", mapperType.getFullyQualifiedNameWithoutTypeParameters()));
        //创建集成数据集
        XmlElement resultMap = new XmlElement("resultMap");
        resultMap.addAttribute(new Attribute("extends", mapperBaseType.getFullyQualifiedNameWithoutTypeParameters() + ".BaseResultMap"));
        resultMap.addAttribute(new Attribute("id", "BaseResultExpMap"));
        rootElement.addElement(resultMap);
        document.setRootElement(rootElement);
        GeneratedXmlFile file = new GeneratedXmlFile(document,
                mapperType.getShortName() + ".xml", packageXML, projectMode,
                true, context.getXmlFormatter());
        return file;
    }

}
