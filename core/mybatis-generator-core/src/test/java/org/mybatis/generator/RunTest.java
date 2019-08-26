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
//        String[] arg = new String[]{"-configfile", rootPath + "mybatis-generator_01.xml",
//                "-overwrite", "-verbose"};
        String[] arg = new String[]{"-configfile", rootPath + "mybatis-generator_auths.xml",
                "-overwrite", "-verbose"};
        // String[] arg = new String[]{"-configfile", rootPath +
        // "test/generatorConfigForMySql.xml", "-overwrite"};

        ShellRunner.main(arg);
    }

    @Test
    public void run_dafw() {
        RunTest test = new RunTest();
        String rootPath = test.getClass().getResource("/").getFile().toString();
        String[] arg = new String[]{"-configfile", rootPath + "mybatis-generator_dafw.xml",
                "-overwrite", "-verbose"};
        ShellRunner.main(arg);
    }

    @Test
    public void run_depository() {
        RunTest test = new RunTest();
        String rootPath = test.getClass().getResource("/").getFile().toString();
        String[] arg = new String[]{"-configfile", rootPath + "mybatis-generator_depository.xml",
                "-overwrite", "-verbose"};
        ShellRunner.main(arg);
    }

    @Test
    public void run_personinfo() {
        RunTest test = new RunTest();
        String rootPath = test.getClass().getResource("/").getFile().toString();
        String[] arg = new String[]{"-configfile", rootPath + "mybatis-generator_personinfo.xml",
                "-overwrite", "-verbose"};
        ShellRunner.main(arg);
    }

}
