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
package org.mybatis.generator;

import org.junit.Test;
import org.mybatis.generator.api.ShellRunner;


public class RunTest {

    @Test
    public void run() {
        // 调试初始化参数
        RunTest test = new RunTest();
        // 取得根目录路径
        String rootPath = test.getClass().getResource("/").getFile().toString();
        // 当前目录路径
        // String currentPath1=test.getClass().getResource(".").getFile().toString();
        // String currentPath2=test.getClass().getResource("").getFile().toString();
        // 当前目录的上级目录路径
        // String parentPath=test.getClass().getResource("../").getFile().toString();
        String[] arg = new String[]{"-configfile", rootPath + "mybatis-generator.xml",
                "-overwrite", "-verbose"};
        // String[] arg = new String[]{"-configfile", rootPath +
        // "test/generatorConfigForMySql.xml", "-overwrite"};

        ShellRunner.main(arg);
    }

}
