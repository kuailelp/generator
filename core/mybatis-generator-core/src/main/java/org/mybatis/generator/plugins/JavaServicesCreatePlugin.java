/**
 * Copyright 2006-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mybatis.generator.plugins;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JavaServicesCreatePlugin extends PluginAdapter {


    private FullyQualifiedJavaType slf4jLogger;
    private FullyQualifiedJavaType slf4jLoggerFactory;
    private FullyQualifiedJavaType serviceType;
    private FullyQualifiedJavaType daoType;
    private FullyQualifiedJavaType interfaceType;
    private FullyQualifiedJavaType pojoType;
//    private FullyQualifiedJavaType pojoTypeExp;
    private FullyQualifiedJavaType pojoCriteriaType;

    private FullyQualifiedJavaType listType;
    private FullyQualifiedJavaType autowired;
    private FullyQualifiedJavaType service;
    private FullyQualifiedJavaType transactional;
    private FullyQualifiedJavaType mapType;
    private FullyQualifiedJavaType wonderType;
    private FullyQualifiedJavaType idworkType;
    private FullyQualifiedJavaType pageType;
    private FullyQualifiedJavaType pageHelpType;
    private String servicePack;
    private String serviceImplPack;
    private String project;
    private String pojoUrl;
    private String mapper;

    // 主键
    private IntrospectedColumn introspectedColumn;

    private List<Method> methods;


    public JavaServicesCreatePlugin() {
        super();
        // 默认是slf4j
        slf4jLogger = new FullyQualifiedJavaType("org.slf4j.Logger");
        slf4jLoggerFactory = new FullyQualifiedJavaType("org.slf4j.LoggerFactory");
        methods = new ArrayList();
    }

    @Override
    public boolean validate(List<String> warnings) {
        servicePack = properties.getProperty("targetPackage");
        serviceImplPack = properties.getProperty("implementationPackage");
        project = properties.getProperty("targetProject");
        pojoUrl = context.getJavaModelGeneratorConfiguration().getTargetPackage();
        mapper = context.getJavaClientGeneratorConfiguration().getTargetPackage();
        autowired = new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired");
        service = new FullyQualifiedJavaType("org.springframework.stereotype.Component");
        transactional = new FullyQualifiedJavaType("org.springframework.transaction.annotation.Transactional");
        return true;
    }


    /**
     * 描述：生成java文件 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/29 9:51 <br>
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> files = new ArrayList();
        String table = introspectedTable.getBaseRecordType();
        String tableName = table.replaceAll(this.pojoUrl + ".", "");
        tableName = tableName.replace("base.", "");
        mapper = mapper.replace(".base", "");
        daoType = new FullyQualifiedJavaType(mapper + "." + tableName + "ExpMapper");
        if (servicePack != null && servicePack.length() > 0) {
            serviceType = new FullyQualifiedJavaType(serviceImplPack + "." + tableName + "ServiceImpl");
        } else {
            serviceType = new FullyQualifiedJavaType(serviceImplPack + "." + tableName + "Service");
        }
        pojoType = new FullyQualifiedJavaType(pojoUrl + ".base." + tableName);
//        pojoTypeExp = new FullyQualifiedJavaType(pojoUrl + "." + tableName + "Exp");
        pageType = new FullyQualifiedJavaType("com.wonders.common.base.Page");
        pojoCriteriaType = new FullyQualifiedJavaType(pojoUrl + "."
                + table.replaceAll(this.pojoUrl + ".", "") + "Criteria");
        listType = new FullyQualifiedJavaType("java.util.List");
        mapType = new FullyQualifiedJavaType("java.util.Map");
        wonderType = new FullyQualifiedJavaType("com.wonders.common.util.WonderParams");
        idworkType = new FullyQualifiedJavaType("com.wonders.common.util.IdWorker");
        pageHelpType = new FullyQualifiedJavaType("com.wonders.common.util.PageHelper");
        TopLevelClass topLevelClass = new TopLevelClass(serviceType);
        if (servicePack != null && servicePack.length() > 0) {
            interfaceType = new FullyQualifiedJavaType(servicePack + "." + tableName + "Service");
            Interface interface1 = new Interface(interfaceType);
            // 导入必要的类
            addImport(interface1, topLevelClass);
            // 接口
            addService(interface1, introspectedTable, tableName, files);
        } else {
            addImport(null, topLevelClass);
        }
        // 实现类
        addServiceImpl(topLevelClass, introspectedTable, tableName, files);
        // 添加日志信息
        addLogger(topLevelClass);
        return files;
    }

    /**
     * 描述：增加接口方法 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/29 9:50 <br>
     */
    protected void addService(Interface interface1, IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {
        interface1.setVisibility(JavaVisibility.PUBLIC);
        // 添加查询总量方法
        Method countByExample = countByExample(introspectedTable, tableName);
        countByExample.removeAllBodyLines();
        interface1.addMethod(countByExample);
        // 根据条件删除
        Method deleteByExample = deleteByExample(introspectedTable, tableName);
        deleteByExample.removeAllBodyLines();
        interface1.addMethod(deleteByExample);
        // 根据主键删除
        Method deleteByPrimaryKey = deleteByPrimaryKey(introspectedTable, tableName);
        deleteByPrimaryKey.removeAllBodyLines();
        interface1.addMethod(deleteByPrimaryKey);
        // 全局新增数据
        Method insert = insert(introspectedTable, tableName);
        insert.removeAllBodyLines();
        interface1.addMethod(insert);
        // 条件新增数据
        Method insertSelective = insertSelective(introspectedTable, tableName);
        insertSelective.removeAllBodyLines();
        interface1.addMethod(insertSelective);
        // 条件查询
        Method selectByExample = selectByExample(introspectedTable, tableName);
        selectByExample.removeAllBodyLines();
        interface1.addMethod(selectByExample);
        // 根据主键查询
        Method selectByPrimaryKey = selectByPrimaryKey(introspectedTable, tableName);
        selectByPrimaryKey.removeAllBodyLines();
        interface1.addMethod(selectByPrimaryKey);

        // 根据条件修改全部对象信息
        Method updateByExample = updateByExample(introspectedTable, tableName);
        updateByExample.removeAllBodyLines();
        interface1.addMethod(updateByExample);

        // 根据条件修改对象信息
        Method updateByExampleSelective = updateByExampleSelective(introspectedTable, tableName);
        updateByExampleSelective.removeAllBodyLines();
        interface1.addMethod(updateByExampleSelective);


        // 根据单一对象主键修改信息
        Method updateByPrimaryKeySelective = updateByPrimaryKeySelective(introspectedTable, tableName);
        updateByPrimaryKeySelective.removeAllBodyLines();
        interface1.addMethod(updateByPrimaryKeySelective);

        // 根据单一对象主键修改全部信息
        Method updateByPrimaryKey = updateByPrimaryKey(introspectedTable, tableName);
        updateByPrimaryKey.removeAllBodyLines();
        interface1.addMethod(updateByPrimaryKey);

        // 根据单一对象主键修改全部信息
        Method selectByPage = selectByPage(introspectedTable, tableName);
        selectByPage.removeAllBodyLines();
        interface1.addMethod(selectByPage);

        GeneratedJavaFile file = new GeneratedJavaFile(interface1, project, context.getJavaFormatter());
        interface1.addJavaDocLine("/**");
        interface1.addJavaDocLine(" * 文件名：" + file.getFileName() + " <br>");
        interface1.addJavaDocLine(" * 描述：" + introspectedTable.getFullyQualifiedTable().getRemark() + " 业务接口 <br>");
        interface1.addJavaDocLine(" * 创建人：Mybatis Genertor <br>");
        interface1.addJavaDocLine(" * 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        interface1.addJavaDocLine(" * 修改人：");
        interface1.addJavaDocLine(" * 修改日期：");
        interface1.addJavaDocLine(" * 修改内容：");
        interface1.addJavaDocLine(" * 1.;");
        interface1.addJavaDocLine(" **/");
        file = new GeneratedJavaFile(interface1, project, context.getJavaFormatter());

        files.add(file);
    }


    /**
     * 添加实现类
     *
     * @param introspectedTable
     * @param tableName
     * @param files
     */
    protected void addServiceImpl(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        // 设置实现的接口
        if (servicePack != null && servicePack.length() > 0) {
            topLevelClass.addSuperInterface(interfaceType);
        }
        // 添加注解
        topLevelClass.addAnnotation("@Component");
        topLevelClass.addAnnotation("@Transactional(rollbackFor = Exception.class)");
        topLevelClass.addImportedType(service);
        topLevelClass.addImportedType(transactional);
        // 添加引用dao
        addField(topLevelClass, tableName);
        // 添加条目数查询
        topLevelClass.addMethod(countByExample(introspectedTable, tableName));
        // 根据条件删除
        topLevelClass.addMethod(deleteByExample(introspectedTable, tableName));
        // 根据主键删除
        topLevelClass.addMethod(deleteByPrimaryKey(introspectedTable, tableName));
        // 全局新增数据
        topLevelClass.addMethod(insert(introspectedTable, tableName));
        // 条件新增数据
        topLevelClass.addMethod(insertSelective(introspectedTable, tableName));
        // 条件查询
        topLevelClass.addMethod(selectByExample(introspectedTable, tableName));
        // 根据主键查询
        topLevelClass.addMethod(selectByPrimaryKey(introspectedTable, tableName));
        // 根据添加修改全部对象
        topLevelClass.addMethod(updateByExample(introspectedTable, tableName));
        // 根据添加修改对象
        topLevelClass.addMethod(updateByExampleSelective(introspectedTable, tableName));
        // 根据单一对象主键修改信息
        topLevelClass.addMethod(updateByPrimaryKeySelective(introspectedTable, tableName));
        // 根据单一对象主键修改全部信息
        topLevelClass.addMethod(updateByPrimaryKey(introspectedTable, tableName));
        // 根据单一对象主键修改全部信息
        topLevelClass.addMethod(selectByPage(introspectedTable, tableName));
        // 生成文件
        GeneratedJavaFile file = new GeneratedJavaFile(topLevelClass, project, context.getJavaFormatter());

        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * 文件名：" + file.getFileName() + " <br>");
        topLevelClass.addJavaDocLine(" * 描述：" + introspectedTable.getFullyQualifiedTable().getRemark() + " 业务实现 <br>");
        topLevelClass.addJavaDocLine(" * 创建人：Mybatis Genertor <br>");
        topLevelClass.addJavaDocLine(" * 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        topLevelClass.addJavaDocLine(" * 修改人：");
        topLevelClass.addJavaDocLine(" * 修改日期：");
        topLevelClass.addJavaDocLine(" * 修改内容：");
        topLevelClass.addJavaDocLine(" * 1.;");
        topLevelClass.addJavaDocLine(" **/");
        file = new GeneratedJavaFile(topLevelClass, project, context.getJavaFormatter());
        files.add(file);
    }


    /**
     * 添加字段
     *
     * @param topLevelClass
     */
    protected void addField(TopLevelClass topLevelClass, String tableName) {
        // 添加 dao
        Field field = new Field();
        // 设置变量名
        field.setName(toLowerCase(daoType.getShortName()));
        topLevelClass.addImportedType(daoType);
        field.setType(daoType); // 类型
        field.setVisibility(JavaVisibility.PRIVATE);
        // 增加注解
        field.addAnnotation("@Autowired");
        topLevelClass.addField(field);
    }


    /**
     * BaseUsers to baseUsers
     *
     * @param tableName
     * @return
     */
    protected String toLowerCase(String tableName) {
        StringBuilder sb = new StringBuilder(tableName);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }

    /**
     * BaseUsers to baseUsers
     *
     * @param tableName
     * @return
     */
    protected String toUpperCase(String tableName) {
        StringBuilder sb = new StringBuilder(tableName);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    /**
     * 导入需要的类
     */
    private void addImport(Interface interfaces, TopLevelClass topLevelClass) {
        // 接口包导入
        if (servicePack != null && servicePack.length() > 0 && interfaces != null) {
            interfaces.addImportedType(pojoType);
//            interfaces.addImportedType(pojoTypeExp);
            interfaces.addImportedType(listType);
            interfaces.addImportedType(pageType);
            interfaces.addImportedType(mapType);
        }
        // 实现包导入
        topLevelClass.addImportedType(daoType);
        topLevelClass.addImportedType(interfaceType);
        topLevelClass.addImportedType(pojoType);
//        topLevelClass.addImportedType(pojoTypeExp);
        topLevelClass.addImportedType(pojoCriteriaType);
        topLevelClass.addImportedType(listType);
        topLevelClass.addImportedType(slf4jLogger);
        topLevelClass.addImportedType(slf4jLoggerFactory);
        topLevelClass.addImportedType(service);
        topLevelClass.addImportedType(transactional);
        topLevelClass.addImportedType(autowired);
        topLevelClass.addImportedType(pageType);
        topLevelClass.addImportedType(mapType);
        topLevelClass.addImportedType(wonderType);
        topLevelClass.addImportedType(idworkType);
        topLevelClass.addImportedType(pageHelpType);
    }

    private void setIntrospectedColumn(IntrospectedTable introspectedTable) {
        if (introspectedTable.getPrimaryKeyColumns().size() > 0) {
            introspectedColumn = introspectedTable.getPrimaryKeyColumns().get(0);
        }
    }

    /**
     * 导入logger
     */
    private void addLogger(TopLevelClass topLevelClass) {
        Field field = new Field();
        field.setFinal(true);
        field.setInitializationString("LoggerFactory.getLogger(" + topLevelClass.getType().getShortName() + ".class)"); // 设置值
        field.setName("logger"); // 设置变量名
        field.setStatic(true);
        field.setType(new FullyQualifiedJavaType("Logger")); // 类型
        field.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(field);
    }


    /**
     * 描述：添加查询条目数方法 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 8:40 <br>
     */
    private Method countByExample(IntrospectedTable introspectedTable, String tableName) {
        Method method = new Method();
        method.setName("countByExample");
        method.setReturnType(PrimitiveTypeWrapper.getLongInstance());
        method.addParameter(new Parameter(FullyQualifiedJavaType.getNewMapInstance(), "params"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：查询" + introspectedTable.getFullyQualifiedTable().getRemark() + "条目数 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @param    params  查询参数");
        method.addJavaDocLine(" */");
        method.addBodyLine("logger.debug(\"查询" + introspectedTable.getFullyQualifiedTable().getRemark() + "条目数\");");
        method.addBodyLine(pojoCriteriaType.getShortName() + " criteria = new " + pojoCriteriaType.getShortName() + "();");
        method.addBodyLine(wonderType.getShortName() + ".parseExample(params,criteria.createCriteria());");
        method.addBodyLine("long count = " + toLowerCase(daoType.getShortName()) + ".countByExample(criteria);");
        method.addBodyLine("return count;");
        return method;
    }

    /**
     * 描述：生成条件删除方法 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 10:36 <br>
     */
    private Method deleteByExample(IntrospectedTable introspectedTable, String tableName) {
        setIntrospectedColumn(introspectedTable);
        Method method = new Method();
        method.setName("deleteByExample");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getNewMapInstance(), "params"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据条件删除" + introspectedTable.getFullyQualifiedTable().getRemark() + "<br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @param    params   删除参数");
        method.addJavaDocLine(" */");
        method.addBodyLine("logger.debug(\"根据条件删除" + introspectedTable.getFullyQualifiedTable().getRemark() + "\");");
        method.addBodyLine(pojoCriteriaType.getShortName() + " criteria = new " + pojoCriteriaType.getShortName() + "();");
        method.addBodyLine(wonderType.getShortName() + ".parseExample(params,criteria.createCriteria());");
        method.addBodyLine("int result = " + toLowerCase(daoType.getShortName()) + ".deleteByExample(criteria);");
        method.addBodyLine("return result;");
        return method;
    }

    /**
     * 描述：根据主键删除数据 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 10:52 <br>
     */
    private Method deleteByPrimaryKey(IntrospectedTable introspectedTable, String tableName) {
        setIntrospectedColumn(introspectedTable);
        Method method = new Method();
        method.setName("deleteByPrimaryKey");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(),
                introspectedColumn.getJavaProperty()));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("logger.debug(\"根据主键删除" + introspectedTable.getFullyQualifiedTable().getRemark() + "\");");
        method.addBodyLine("int result = " + toLowerCase(daoType.getShortName()) + ".deleteByPrimaryKey(" + introspectedColumn.getJavaProperty() + ");");
        method.addBodyLine("return result;");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据主键删除" + introspectedTable.getFullyQualifiedTable().getRemark() + "<br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @param " + introspectedColumn.getJavaProperty() + " " + introspectedColumn.getRemarks());
        method.addJavaDocLine(" */");

        return method;
    }

    /**
     * 描述：全局新增数据 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 11:03 <br>
     */
    private Method insert(IntrospectedTable introspectedTable, String tableName) {
        setIntrospectedColumn(introspectedTable);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("insert");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addParameter(new Parameter(pojoType, "record"));
        FullyQualifiedJavaType key = introspectedColumn.getFullyQualifiedJavaType();
        String idkey = introspectedColumn.getJavaProperty();
        method.addBodyLine("logger.debug(\"全局新增" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息\");");
        if (key.equals(FullyQualifiedJavaType.getStringInstance())) {
            method.addBodyLine("record.set" + toUpperCase(idkey) + "(" + idworkType.getShortName() + ".getIdStr()" + ");");
        } else {
            method.addBodyLine("record.set" + toUpperCase(idkey) + "(" + idworkType.getShortName() + ".getId()" + ");");
        }
        method.addBodyLine("int result = " + toLowerCase(daoType.getShortName()) + "." + introspectedTable.getInsertStatementId() + "(record);");
        method.addBodyLine("return result;");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：全局新增" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息<br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @param  record  " + introspectedTable.getFullyQualifiedTable().getRemark() + "对象信息");
        method.addJavaDocLine(" */");
        return method;
    }


    /**
     * 描述：条件新增数据 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 11:03 <br>
     */
    private Method insertSelective(IntrospectedTable introspectedTable, String tableName) {
        setIntrospectedColumn(introspectedTable);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("insertSelective");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addParameter(new Parameter(pojoType, "record"));
        FullyQualifiedJavaType key = introspectedColumn.getFullyQualifiedJavaType();
        String idkey = introspectedColumn.getJavaProperty();
        method.addBodyLine("logger.debug(\"条件新增" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息\");");
        if (key.equals(FullyQualifiedJavaType.getStringInstance())) {
            method.addBodyLine("record.set" + toUpperCase(idkey) + "(" + idworkType.getShortName() + ".getIdStr()" + ");");
        } else {
            method.addBodyLine("record.set" + toUpperCase(idkey) + "(" + idworkType.getShortName() + ".getId()" + ");");
        }
        method.addBodyLine("int result = " + toLowerCase(daoType.getShortName()) + "." + introspectedTable.getInsertSelectiveStatementId() + "(record);");
        method.addBodyLine("return result;");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：条件新增" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息<br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @param  record  " + introspectedTable.getFullyQualifiedTable().getRemark() + "对象信息");
        method.addJavaDocLine(" */");
        return method;
    }

    /**
     * 描述：条件查询 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 11:38 <br>
     */
    private Method selectByExample(IntrospectedTable introspectedTable, String tableName) {
        setIntrospectedColumn(introspectedTable);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("selectByExample");
        method.setReturnType(new FullyQualifiedJavaType("List"));
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getNewMapInstance(), "params"));
        method.addBodyLine("logger.debug(\"条件查询" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息\");");
        method.addBodyLine(pojoCriteriaType.getShortName() + " criteria = new " + pojoCriteriaType.getShortName() + "();");
        method.addBodyLine(wonderType.getShortName() + ".parseExample(params,criteria.createCriteria());");
        method.addBodyLine("return " + toLowerCase(daoType.getShortName()) + "." +
                (introspectedTable.getRules().generateResultMapWithBLOBs() ?
                        introspectedTable.getSelectByExampleWithBLOBsStatementId()
                        : introspectedTable.getSelectByExampleStatementId()) + "(criteria);");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：条件查询" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息<br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @param  params " + introspectedTable.getFullyQualifiedTable().getRemark() + "查询对象信息");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return " + introspectedTable.getFullyQualifiedTable().getRemark() + "信息集合");
        method.addJavaDocLine(" */");
        return method;
    }

    /**
     * 描述：根据主键查询对象 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 11:38 <br>
     */
    private Method selectByPrimaryKey(IntrospectedTable introspectedTable, String tableName) {
        setIntrospectedColumn(introspectedTable);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("selectByPrimaryKey");
        method.setReturnType(new FullyQualifiedJavaType(pojoType.getShortName()));
        method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(), introspectedColumn.getJavaProperty()));
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addBodyLine("logger.debug(\"根据主键查询" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息\");");
        method.addBodyLine("return " + toLowerCase(daoType.getShortName()) + "." + introspectedTable.getSelectByPrimaryKeyStatementId() + "(" + introspectedColumn.getJavaProperty() + ");");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据主键查询" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息<br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @param " + introspectedColumn.getJavaProperty() + "  " + introspectedTable.getFullyQualifiedTable().getRemark());
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return " + introspectedTable.getFullyQualifiedTable().getRemark() + "对象信息");
        method.addJavaDocLine(" */");
        return method;
    }


    /**
     * 描述： 根据条件更改全部对象信息<br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 15:09 <br>
     */
    private Method updateByExample(IntrospectedTable introspectedTable, String tableName) {
        setIntrospectedColumn(introspectedTable);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("updateByExample");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addParameter(new Parameter(pojoType, "record"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getNewMapInstance(), "params"));
        method.addBodyLine("logger.debug(\"根据条件修改全部" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息\");");
        method.addBodyLine(pojoCriteriaType.getShortName() + " criteria = new " + pojoCriteriaType.getShortName() + "();");
        method.addBodyLine(wonderType.getShortName() + ".parseExample(params,criteria.createCriteria());");
        method.addBodyLine(FullyQualifiedJavaType.getIntInstance() + " result = " +
                toLowerCase(daoType.getShortName()) + "." + (
                introspectedTable.getUpdateByExampleSelectiveStatementId()
        ) + "(" +
                "record, " +
                "criteria" +
                ");");
        method.addBodyLine("return result;");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据条件修改全部" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息<br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @param record 修改信息");
        method.addJavaDocLine(" * @param params 查询过滤数据");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 修改结果");
        method.addJavaDocLine(" */");
        return method;
    }


    /**
     * 描述：根据条件更改部分对象信息 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 14:44 <br>
     */
    private Method updateByExampleSelective(IntrospectedTable introspectedTable, String tableName) {
        setIntrospectedColumn(introspectedTable);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("updateByExampleSelective");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addParameter(new Parameter(pojoType, "record"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getNewMapInstance(), "params"));
        method.addBodyLine("logger.debug(\"根据条件修改部分" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息\");");
        method.addBodyLine(pojoCriteriaType.getShortName() + " criteria = new " + pojoCriteriaType.getShortName() + "();");
        method.addBodyLine(wonderType.getShortName() + ".parseExample(params,criteria.createCriteria());");
        method.addBodyLine(FullyQualifiedJavaType.getIntInstance() + " result = " +
                toLowerCase(daoType.getShortName()) + "." + (introspectedTable.getUpdateByExampleSelectiveStatementId()) + "(" +
                "record, " +
                "criteria" +
                ");");
        method.addBodyLine("return result;");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据条件修改部分" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息<br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @param record 修改信息");
        method.addJavaDocLine(" * @param params 查询过滤数据");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 修改结果");
        method.addJavaDocLine(" */");
        return method;
    }


    /**
     * 描述：根据单一对象主键修改信息 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 15:21 <br>
     */
    private Method updateByPrimaryKeySelective(IntrospectedTable introspectedTable, String tableName) {
        setIntrospectedColumn(introspectedTable);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("updateByPrimaryKeySelective");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addParameter(new Parameter(pojoType, "record"));
        method.addBodyLine("logger.debug(\"根据单一对象主键修改" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息\");");
        method.addBodyLine(FullyQualifiedJavaType.getIntInstance() + " result = " +
                toLowerCase(daoType.getShortName()) + "." + (
                introspectedTable.getUpdateByPrimaryKeySelectiveStatementId()

        ) + "(record);");
        method.addBodyLine("return result;");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据单一对象主键修改" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息<br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @param record 修改信息");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 修改结果");
        method.addJavaDocLine(" */");
        return method;
    }

    /**
     * 描述：根据单一对象主键修改全部信息 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 15:21 <br>
     */
    private Method updateByPrimaryKey(IntrospectedTable introspectedTable, String tableName) {
        setIntrospectedColumn(introspectedTable);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("updateByPrimaryKey");
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addParameter(new Parameter(pojoType, "record"));
        method.addBodyLine("logger.debug(\"根据单一对象主键修改全部" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息\");");
        method.addBodyLine(FullyQualifiedJavaType.getIntInstance() + " result = " +
                toLowerCase(daoType.getShortName()) + "." + (
                introspectedTable.getRules().generateResultMapWithBLOBs() ?
                        introspectedTable.getUpdateByPrimaryKeyWithBLOBsStatementId() :
                        introspectedTable.getUpdateByPrimaryKeyStatementId()

        ) + "(record);");
        method.addBodyLine("return result;");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据单一对象主键修改全部" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息<br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @param record 修改信息");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 修改结果");
        method.addJavaDocLine(" */");
        return method;
    }

    /**
     * 描述：分页查询 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 15:37 <br>
     */
    private Method selectByPage(IntrospectedTable introspectedTable, String tableName) {
        setIntrospectedColumn(introspectedTable);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("selectByPage");
        method.setReturnType(new FullyQualifiedJavaType("Page<" + tableName + ">"));
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addParameter(new Parameter(pageType, "page"));
        method.addParameter(new Parameter(FullyQualifiedJavaType.getNewMapInstance(), "params"));
        method.addBodyLine("logger.debug(\"分页查询" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息\");");
        method.addBodyLine(pojoCriteriaType.getShortName() + " criteria = new " + pojoCriteriaType.getShortName() + "();");
        method.addBodyLine(wonderType.getShortName() + ".parseExample(params,criteria.createCriteria());");
        method.addBodyLine(pageHelpType.getShortName() + ".setPagination(page);");
        method.addBodyLine("Page<" + tableName + "> result = new Page(" +
                toLowerCase(daoType.getShortName()) + "." + (
                introspectedTable.getRules().generateResultMapWithBLOBs() ?
                        introspectedTable.getSelectByExampleWithBLOBsStatementId() :
                        introspectedTable.getSelectByExampleStatementId()

        ) + "(criteria));");
        method.addBodyLine("return result;");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：分页查询" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息<br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" *");
        method.addJavaDocLine(" * @param page 分页信息");
        method.addJavaDocLine(" * @param params 查询参数");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 分页结果");
        method.addJavaDocLine(" */");
        return method;
    }

}
