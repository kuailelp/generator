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

// FIXME: Controller 微服务 接口 生成器
public class JavaControllerCloudCreatePlugin extends PluginAdapter {


    private FullyQualifiedJavaType actionType;
    private FullyQualifiedJavaType pojoTypeExp;
    private FullyQualifiedJavaType getMapping;
    private FullyQualifiedJavaType postMapping;
    private FullyQualifiedJavaType fBody;
    private FullyQualifiedJavaType fHeaders;


    private FullyQualifiedJavaType listType;
    private FullyQualifiedJavaType component;
    private FullyQualifiedJavaType feignClient;
    private FullyQualifiedJavaType resultType;
    private FullyQualifiedJavaType resultTypePojo;
    private FullyQualifiedJavaType resultTypeLong;
    private FullyQualifiedJavaType resultTypePage;
    private FullyQualifiedJavaType resultTypeList;
    private FullyQualifiedJavaType bodys;
    private FullyQualifiedJavaType bodysPorp;
    private FullyQualifiedJavaType pageType;
    private String controllerPack;
    private String project;
    private String pojoUrl;

    // 主键
    private IntrospectedColumn introspectedColumn;

    private List<Method> methods;


    public JavaControllerCloudCreatePlugin() {
        super();
        methods = new ArrayList();
    }

    @Override
    public boolean validate(List<String> warnings) {
        controllerPack = properties.getProperty("targetPackage");
        project = properties.getProperty("targetProject");
        pojoUrl = context.getJavaModelGeneratorConfiguration().getTargetPackage();
        component = new FullyQualifiedJavaType("org.springframework.stereotype.Component");
        feignClient = new FullyQualifiedJavaType("org.springframework.cloud.openfeign.FeignClient");
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
        actionType = new FullyQualifiedJavaType(controllerPack + "." + tableName + "Action");
        pojoTypeExp = new FullyQualifiedJavaType(pojoUrl + "." + tableName + "Exp");
        pageType = new FullyQualifiedJavaType("com.wonders.toolkit.model.PageInfo");
        listType = new FullyQualifiedJavaType("java.util.List");
        resultType = new FullyQualifiedJavaType("com.wonders.toolkit.model.Return");
        resultTypePage = new FullyQualifiedJavaType("com.wonders.toolkit.model.Return<PageInfo>");
        resultTypeLong = new FullyQualifiedJavaType("com.wonders.toolkit.model.Return<Long>");
        resultTypePojo = new FullyQualifiedJavaType("com.wonders.toolkit.model.Return<" + pojoTypeExp.getShortName() + ">");
        resultTypeList = new FullyQualifiedJavaType("com.wonders.toolkit.model.Return<List<" + pojoTypeExp.getShortName() + ">>");
        bodys = new FullyQualifiedJavaType("com.wonders.toolkit.model.Bodys");
        bodysPorp = new FullyQualifiedJavaType("com.wonders.toolkit.model.Bodys<" + pojoTypeExp.getShortName() + ".Property,String>");
        postMapping = new FullyQualifiedJavaType("org.springframework.web.bind.annotation.PostMapping");
        fBody = new FullyQualifiedJavaType("feign.Body");
        fHeaders = new FullyQualifiedJavaType("feign.Headers");
        Interface in = new Interface(actionType);
        // 导入必要的类
        addImport(in);
        // 实现类
        addController(in, introspectedTable, tableName, files);

        return files;
    }

    /**
     * 导入需要的类
     */
    private void addImport(Interface topLevelClass) {
        topLevelClass.addImportedType(pojoTypeExp);
        topLevelClass.addImportedType(listType);
        topLevelClass.addImportedType(feignClient);
        topLevelClass.addImportedType(component);
        topLevelClass.addImportedType(pageType);
        topLevelClass.addImportedType(resultType);
        topLevelClass.addImportedType(getMapping);
        topLevelClass.addImportedType(postMapping);
        topLevelClass.addImportedType(bodys);
        topLevelClass.addImportedType(fBody);
        topLevelClass.addImportedType(fHeaders);

    }


    /**
     * 添加实现类
     *
     * @param introspectedTable
     * @param tableName
     * @param files
     */
    protected void addController(Interface topLevelClass, IntrospectedTable introspectedTable, String tableName, List<GeneratedJavaFile> files) {
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        // 添加注解
        topLevelClass.addAnnotation("@Component");
        topLevelClass.addAnnotation("@FeignClient( name = #微服编号 , path = \"#请求'REST APIs'地址/" + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName().toLowerCase() + "\")");
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
        topLevelClass.addJavaDocLine(" * 描述：" + introspectedTable.getFullyQualifiedTable().getRemark() + " 微服调用接口 <br>");
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


    private void setIntrospectedColumn(IntrospectedTable introspectedTable) {
        if (introspectedTable.getPrimaryKeyColumns().size() > 0) {
            introspectedColumn = introspectedTable.getPrimaryKeyColumns().get(0);
        }
    }

    /**
     * 描述：设置类属性接口说明 <br>
     * 创建人：廖鹏 | 创建日期：2020/3/12 20:22 <br>
     */
    private void setAnnotationMethodHeaders(Method method) {
        method.addAnnotation("@Headers({\n" +
                "            \"Accept: application/json\",\n" +
                "            \"Content-Type: application/json\"\n" +
                "    })");
        method.addAnnotation("@Body(\"{body}\")");
    }


    /**
     * 描述：添加查询条目数方法 <br>
     * 创建人：廖鹏 | 创建日期：2019/1/30 8:40 <br>
     */
    private Method countByExample(IntrospectedTable introspectedTable, String tableName) {
        setIntrospectedColumn(introspectedTable);
        Method method = new Method();
        method.setName("countByExample");
        method.addAnnotation("@PostMapping(\"count_by_example\")");
        method.setReturnType(resultTypeLong);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(bodysPorp, "bodys"));
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：查询" + introspectedTable.getFullyQualifiedTable().getRemark() + "条目数 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 查询结果条目数");
        method.addJavaDocLine(" */");
        setAnnotationMethodHeaders(method);
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
        method.addAnnotation("@PostMapping(\"delete_by_example\")");
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(bodysPorp, "bodys"));
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据条件删除" + introspectedTable.getFullyQualifiedTable().getRemark() + " <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
        method.addJavaDocLine(" */");
        setAnnotationMethodHeaders(method);
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
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addAnnotation("@PostMapping(value = \"delete_by_primarykey\")");
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(bodysPorp, "bodys"));
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据主键删除" + introspectedTable.getFullyQualifiedTable().getRemark() + " <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
        method.addJavaDocLine(" */");
        setAnnotationMethodHeaders(method);
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
        method.addAnnotation("@PostMapping(\"insert\")");
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(bodysPorp, "bodys"));
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：全局新增" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @param    bodys  " + introspectedTable.getFullyQualifiedTable().getRemark() + "对象信息");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
        method.addJavaDocLine(" */");
        setAnnotationMethodHeaders(method);
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
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addAnnotation("@PostMapping(value = \"insert_selective\")");
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(bodysPorp, "bodys"));
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据对象属性状态新增" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @param    bodys  " + introspectedTable.getFullyQualifiedTable().getRemark() + "对象信息");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
        method.addJavaDocLine(" */");
        setAnnotationMethodHeaders(method);
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
        method.addAnnotation("@PostMapping(value = \"select_by_example\")");
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.setReturnType(resultTypeList);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(bodysPorp, "bodys"));
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据条件获取" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
        method.addJavaDocLine(" */");
        setAnnotationMethodHeaders(method);
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
        method.addAnnotation("@PostMapping(\"select_by_primarykey\")");
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.setReturnType(resultTypePojo);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(bodysPorp, "bodys"));
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据主键获取" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
        method.addJavaDocLine(" */");
        setAnnotationMethodHeaders(method);
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
        method.addAnnotation("@PostMapping(\"update_by_example\")");
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(bodysPorp, "bodys"));
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据条件修改" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
        method.addJavaDocLine(" */");
        setAnnotationMethodHeaders(method);
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
        method.addAnnotation("@PostMapping(\"update_by_ex_sel\")");
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(bodysPorp, "bodys"));
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据条件修改部分" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
        method.addJavaDocLine(" */");
        setAnnotationMethodHeaders(method);
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
        method.addAnnotation("@PostMapping(value = \"update_by_primary_key_sel\")");
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(bodysPorp, "bodys"));
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据单一对象主键修改" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
        method.addJavaDocLine(" */");
        setAnnotationMethodHeaders(method);
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
        method.addAnnotation("@PostMapping(\"update_by_primary_key\")");
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.setReturnType(resultType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(bodysPorp, "bodys"));
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：根据单一对象主键修改全部" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
        method.addJavaDocLine(" */");
        setAnnotationMethodHeaders(method);
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
        method.addException(new FullyQualifiedJavaType("java.lang.Exception"));
        method.addAnnotation("@PostMapping(\"select_by_page\")");
        method.setReturnType(resultTypePage);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(bodysPorp, "bodys"));
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 描述：分页查询" + introspectedTable.getFullyQualifiedTable().getRemark() + "信息 <br>");
        method.addJavaDocLine(" * 创建人：Mybatis Genertor | 创建日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " <br>");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 结果");
        method.addJavaDocLine(" */");
        setAnnotationMethodHeaders(method);
        return method;
    }

}
