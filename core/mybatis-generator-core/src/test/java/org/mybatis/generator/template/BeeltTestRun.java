/**
 *    Copyright 2006-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.template;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResource;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;

/**
 * 文件名：BeeltTestRun.java
 * 描述：模板测试文件
 * 创建人：廖鹏
 * 创建日期：2019/1/29 9:21
 * 修改人：
 * 修改日期：
 * 修改内容：
 * 1.;
 */
public class BeeltTestRun {

    @Test
    public void createBeelt() throws IOException {
//        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
//        Configuration cfg = Configuration.defaultConfiguration();
//        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
//        Template t = gt.getTemplate("hello,${name}");
//        t.binding("name", "beetl");
//        String str = t.render();
//        System.out.println(str);
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("wonders/template");
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        Template t = gt.getTemplate("/javadock.txt");
        t.binding("filename","SysQX.java");
        t.binding("fileinfo","权限管理服务");
        t.binding("date", Calendar.getInstance().getTime());
        String str = t.render();
        System.out.println(str);
    }


}
