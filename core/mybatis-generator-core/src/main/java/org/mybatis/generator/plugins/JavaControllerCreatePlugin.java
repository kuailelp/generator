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

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class JavaControllerCreatePlugin extends PluginAdapter {


    private FullyQualifiedJavaType slf4jLogger;
    private FullyQualifiedJavaType slf4jLoggerFactory;
    private FullyQualifiedJavaType serviceType;
    private FullyQualifiedJavaType controllerType;
    private FullyQualifiedJavaType pojoType;
    private FullyQualifiedJavaType superClassType;


    private FullyQualifiedJavaType listType;
    private FullyQualifiedJavaType autowired;
    private FullyQualifiedJavaType controller;
    private FullyQualifiedJavaType requestMapping;
    private FullyQualifiedJavaType requestParamType;
    private FullyQualifiedJavaType mapType;
    private FullyQualifiedJavaType resultType;
    private FullyQualifiedJavaType pageType;
    private String controllerPack;
    private String servicePack;
    private String project;
    private String pojoUrl;

    // 主键
    private IntrospectedColumn introspectedColumn;

    private List<Method> methods;


    public JavaControllerCreatePlugin() {
        super();
        // 默认是slf4j
        slf4jLogger = new FullyQualifiedJavaType("org.slf4j.Logger");
        slf4jLoggerFactory = new FullyQualifiedJavaType("org.slf4j.LoggerFactory");
        methods = new ArrayList();
    }

    @Override
    public boolean validate(List<String> warnings) {
        controllerPack = properties.getProperty("targetPackage");
        project = properties.getProperty("targetProject");
        servicePack = properties.getProperty("servicePackage");
        pojoUrl = context.getJavaModelGeneratorConfiguration().getTargetPackage();

        autowired = new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired");
        controller = new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RestController");
        requestMapping = new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestMapping");
        requestParamType = new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestParam");
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
        serviceType = new FullyQualifiedJavaType(servicePack + "." + tableName + "Service");
        controllerType = new FullyQualifiedJavaType(controllerPack + "." + tableName + "Controller");
        pojoType = new FullyQualifiedJavaType(pojoUrl + "." + tableName + "Exp");
        pageType = new FullyQualifiedJavaType("com.wonders.common.base.Page");
        listType = new FullyQualifiedJavaType("java.util.List");
        mapType = new FullyQualifiedJavaType("java.util.Map");
        resultType = new FullyQualifiedJavaType("com.wonders.common.base.Return");
        superClassType = new FullyQualifiedJavaType("com.wonders.common.action.PagesController");
        TopLevelClass topLevelClass = new TopLevelClass(controllerType);
        topLevelClass.addAnnotation("@KSPage");
        topLevelClass.addImportedType("com.wonders.aops.controller.KSPage");
        // 导入必要的类
        addImport(topLevelClass);
        // 实现类
        addController(topLevelClass, introspectedTable, tableName, files);
        // 添加日志信息
        addLogger(topLevelClass);
        return files;
    }


    /**
     * 添加实现类
     *
     * @param introspectedTable
     * @param tableName
     * @param files
     */
    protected void addController(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        // 设置父类
        topLevelClass.setSuperClass(superClassType);
        // 添加注解
        topLevelClass.addAnnotation("@RestController");
        topLevelClass.addImportedType(controller);
        topLevelClass.addAnnotation("@RequestMapping(\"" + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName().toLowerCase() + "\")");
        // 添加引用Services
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
        topLevelClass.addJavaDocLine(" * 描述：" + introspectedTable.getFullyQualifiedTable().getRemark() + " 业务数据传输层 <br>");
        topLevelClass.addJavaDocLine(" * 创建人：Mybatis Genertor <br>");
        topLevelClass.addJavaDocLine(" * 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        topLevelClass.addJavaDocLine(" * 修改人：");
        topLevelClass.addJavaDocLine(" * 修改日期：");
        topLevelClass.addJavaDocLine(" * 修改内容：");
        topLevelClass.addJavaDocLine(" * 1.;");
        topLevelClass.addJavaDocLine(" **/");
        Properties properties = context.getJavaClientGeneratorConfiguration().getProperties();
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
        field.setName(toLowerCase(serviceType.getShortName()));
        topLevelClass.addImportedType(serviceType);
        field.setType(serviceType); // 类型
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
    private void addImport(TopLevelClass topLevelClass) {
        topLevelClass.addImportedType(pojoType);
        topLevelClass.addImportedType(listType);
        topLevelClass.addImportedType(slf4jLogger);
        topLevelClass.addImportedType(slf4jLoggerFactory);
        topLevelClass.addImportedType(controller);
        topLevelClass.addImportedType(requestMapping);
        topLevelClass.addImportedType(requestParamType);
        topLevelClass.addImportedType(autowired);
        topLevelClass.addImportedType(pageType);
        topLevelClass.addImportedType(resultType);
        topLevelClass.addImportedType(superClassType);
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

    private void setIntrospectedColumn(IntrospectedTable introspectedTable) {
        if (introspectedTable.getPrimaryKeyColumns().size() > 0) {
            introspectedColumn = introspectedTable.getPrimaryKeyColumns().get(0);
        }
    }


    /**
     * 描述：添加查询条目数方法 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 8:40 <br>
     */
    private Method countByExample(IntrospectedTable introspectedTable, String tableName) {
        setIntrospectedColumn(introspectedTable);
        Method method = new Method();
        method.setName("countByExample");
        method.addAnnotation("@RequestMapping(value = \"count_by_example\")");
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("try {");
        method.addBodyLine("Long count = " + toLowerCase(serviceType.getShortName()) + ".countByExample(getParams());");
        method.addBodyLine("return Return.build().setCode(Return.SUSSESS_CODE).setData(count);");
        method.addBodyLine("} catch (Exception e) {");
        method.addBodyLine("e.printStackTrace();");
        method.addBodyLine("return Return.build().setCode(Return.ERROR_CODE).setMsg(e.getMessage());");
        method.addBodyLine("}");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：查询" + introspectedTable.getFullyQualifiedTable().getRemark() + "条目数 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 查询结果条目数");
        method.addJavaDocLine(" */");
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
        method.addAnnotation("@RequestMapping(value = \"delete_by_example\")");
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("try {");
        method.addBodyLine("int result = " + toLowerCase(serviceType.getShortName()) + ".deleteByExample(getParams());");
        method.addBodyLine("return Return.build().setCode(Return.SUSSESS_CODE).setData(result).setMsg(\"数据删除成功！\");");
        method.addBodyLine("} catch (Exception e) {");
        method.addBodyLine("e.printStackTrace();");
        method.addBodyLine("return Return.build().setCode(Return.ERROR_CODE).setMsg(\"数据删除失败：\"+e.getMessage());");
        method.addBodyLine("}");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据条件删除" + introspectedTable.getFullyQualifiedTable().getRemark() + " <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
        method.addJavaDocLine(" */");
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
        method.addAnnotation("@RequestMapping(value = \"delete_by_primarykey\")");
        method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(),
                introspectedColumn.getJavaProperty(), "@RequestParam(value = \"" + introspectedColumn.getJavaProperty() + "\")"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("try {");
        method.addBodyLine("int result = " + toLowerCase(serviceType.getShortName()) + ".deleteByPrimaryKey(" + introspectedColumn.getJavaProperty() + ");");
        method.addBodyLine("return Return.build().setCode(Return.SUSSESS_CODE).setData(result).setMsg(\"数据删除成功！\");");
        method.addBodyLine("} catch (Exception e) {");
        method.addBodyLine("e.printStackTrace();");
        method.addBodyLine("return Return.build().setCode(Return.ERROR_CODE).setMsg(\"数据删除失败：\"+e.getMessage());");
        method.addBodyLine("}");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据主键删除" + introspectedTable.getFullyQualifiedTable().getRemark() + " <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
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
        method.setName("insert");
        method.addAnnotation("@RequestMapping(value = \"insert\")");
        method.addParameter(new Parameter(pojoType, "record"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("try {");
        method.addBodyLine("int result = " + toLowerCase(serviceType.getShortName()) + ".insert(record);");
        method.addBodyLine("return Return.build().setCode(Return.SUSSESS_CODE).setData(result).setMsg(\"新增数据成功！\");");
        method.addBodyLine("} catch (Exception e) {");
        method.addBodyLine("e.printStackTrace();");
        method.addBodyLine("return Return.build().setCode(Return.ERROR_CODE).setMsg(\"新增数据失败：\"+e.getMessage());");
        method.addBodyLine("}");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：全局新增" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @param    record  " + introspectedTable.getFullyQualifiedTable().getRemark() + "对象信息");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
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
        method.setName("insertSelective");
        method.addAnnotation("@RequestMapping(value = \"insert_selective\")");
        method.addParameter(new Parameter(pojoType, "record"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("try {");
        method.addBodyLine("int result = " + toLowerCase(serviceType.getShortName()) + ".insertSelective(record);");
        method.addBodyLine("return Return.build().setCode(Return.SUSSESS_CODE).setData(result).setMsg(\"新增数据成功！\");");
        method.addBodyLine("} catch (Exception e) {");
        method.addBodyLine("e.printStackTrace();");
        method.addBodyLine("return Return.build().setCode(Return.ERROR_CODE).setMsg(\"新增数据失败：\"+e.getMessage());");
        method.addBodyLine("}");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据对象属性状态新增" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @param    record  " + introspectedTable.getFullyQualifiedTable().getRemark() + "对象信息");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
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
        method.setName("selectByExample");
        method.addAnnotation("@RequestMapping(value = \"select_by_example\")");
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("try {");
        method.addBodyLine("List<" + tableName + "Exp> results = " + toLowerCase(serviceType.getShortName()) + ".selectByExample(getParams());");
        method.addBodyLine("return Return.build().setCode(Return.SUSSESS_CODE).setData(results).setMsg(\"查询数据成功！\");");
        method.addBodyLine("} catch (Exception e) {");
        method.addBodyLine("e.printStackTrace();");
        method.addBodyLine("return Return.build().setCode(Return.ERROR_CODE).setMsg(\"查询数据失败：\"+e.getMessage());");
        method.addBodyLine("}");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据条件获取" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
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
        method.setName("selectByPrimaryKey");
        method.addAnnotation("@RequestMapping(value = \"select_by_primarykey\")");
        method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(),
                introspectedColumn.getJavaProperty(), "@RequestParam(value = \"" + introspectedColumn.getJavaProperty() + "\")"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("try {");
        method.addBodyLine(tableName + "Exp result = " + toLowerCase(serviceType.getShortName()) +
                ".selectByPrimaryKey(" + introspectedColumn.getJavaProperty() + ");");
        method.addBodyLine("return Return.build().setCode(Return.SUSSESS_CODE).setData(result).setMsg(\"查询数据成功！\");");
        method.addBodyLine("} catch (Exception e) {");
        method.addBodyLine("e.printStackTrace();");
        method.addBodyLine("return Return.build().setCode(Return.ERROR_CODE).setMsg(\"查询数据失败：\"+e.getMessage());");
        method.addBodyLine("}");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据主键获取" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
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
        method.setName("updateByExample");
        method.addAnnotation("@RequestMapping(value = \"update_by_example\")");
        method.addParameter(new Parameter(new FullyQualifiedJavaType(tableName + "Exp"), "record"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("try {");
        method.addBodyLine("int result = " + toLowerCase(serviceType.getShortName()) + ".updateByExample(record,getParams());");
        method.addBodyLine("return Return.build().setCode(Return.SUSSESS_CODE).setData(result).setMsg(\"修改数据成功！\");");
        method.addBodyLine("} catch (Exception e) {");
        method.addBodyLine("e.printStackTrace();");
        method.addBodyLine("return Return.build().setCode(Return.ERROR_CODE).setMsg(\"修改数据失败：\"+e.getMessage());");
        method.addBodyLine("}");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据条件修改" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
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
        method.setName("updateByExampleSelective");
        method.addAnnotation("@RequestMapping(value = \"update_by_ex_sel\")");
        method.addParameter(new Parameter(new FullyQualifiedJavaType(tableName + "Exp"), "record"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("try {");
        method.addBodyLine("int result = " + toLowerCase(serviceType.getShortName()) + ".updateByExample(record,getParams());");
        method.addBodyLine("return Return.build().setCode(Return.SUSSESS_CODE).setData(result).setMsg(\"修改数据成功！\");");
        method.addBodyLine("} catch (Exception e) {");
        method.addBodyLine("e.printStackTrace();");
        method.addBodyLine("return Return.build().setCode(Return.ERROR_CODE).setMsg(\"修改数据失败：\"+e.getMessage());");
        method.addBodyLine("}");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据条件修改部分" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
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
        method.setName("updateByPrimaryKeySelective");
        method.addAnnotation("@RequestMapping(value = \"update_by_primary_key_sel\")");
        method.addParameter(new Parameter(new FullyQualifiedJavaType(tableName + "Exp"), "record"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("try {");
        method.addBodyLine("int result = " + toLowerCase(serviceType.getShortName()) + ".updateByPrimaryKeySelective(record);");
        method.addBodyLine("return Return.build().setCode(Return.SUSSESS_CODE).setData(result).setMsg(\"修改数据成功！\");");
        method.addBodyLine("} catch (Exception e) {");
        method.addBodyLine("e.printStackTrace();");
        method.addBodyLine("return Return.build().setCode(Return.ERROR_CODE).setMsg(\"修改数据失败：\"+e.getMessage());");
        method.addBodyLine("}");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据单一对象主键修改" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
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
        method.setName("updateByPrimaryKey");
        method.addAnnotation("@RequestMapping(value = \"update_by_primary_key\")");
        method.addParameter(new Parameter(new FullyQualifiedJavaType(tableName + "Exp"), "record"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("try {");
        method.addBodyLine("int result = " + toLowerCase(serviceType.getShortName()) + ".updateByPrimaryKey(record);");
        method.addBodyLine("return Return.build().setCode(Return.SUSSESS_CODE).setData(result).setMsg(\"修改数据成功！\");");
        method.addBodyLine("} catch (Exception e) {");
        method.addBodyLine("e.printStackTrace();");
        method.addBodyLine("return Return.build().setCode(Return.ERROR_CODE).setMsg(\"修改数据失败：\"+e.getMessage());");
        method.addBodyLine("}");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据单一对象主键修改全部" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
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
        method.setName("selectByPage");
        method.addAnnotation("@RequestMapping(value = \"select_by_page\")");
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("try {");
        method.addBodyLine("Page<" + tableName + "Exp> result = " + toLowerCase(serviceType.getShortName()) + ".selectByPage(getPage(),getParams());");
        method.addBodyLine("return Return.build().setCode(Return.SUSSESS_CODE).setData(result).setMsg(\"查询数据成功！\");");
        method.addBodyLine("} catch (Exception e) {");
        method.addBodyLine("e.printStackTrace();");
        method.addBodyLine("return Return.build().setCode(Return.ERROR_CODE).setMsg(\"查询数据失败：\"+e.getMessage());");
        method.addBodyLine("}");
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：分页查询" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
        method.addJavaDocLine(" */");
        return method;
    }

}
