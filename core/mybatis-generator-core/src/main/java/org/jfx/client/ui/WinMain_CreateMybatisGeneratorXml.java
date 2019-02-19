package org.jfx.client.ui;

import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.XmlConstants;

/**
 * 文件名：WinMain_CreateMybatisGeneratorXml.java
 * 描述：动态生成mybaist生成配置文件
 * 创建人：廖鹏
 * 创建日期：2019/2/19 8:47
 * 修改人：
 * 修改日期：
 * 修改内容：
 * 1.;
 */
public class WinMain_CreateMybatisGeneratorXml {

    // 最总生成文档
    private Document document;

    public WinMain_CreateMybatisGeneratorXml() {
        document = new Document(
                XmlConstants.MYBATIS3_MAPPER_CONFIG_PUBLIC_ID,
                XmlConstants.MYBATIS3_MAPPER_CONFIG_SYSTEM_ID);
    }





}
