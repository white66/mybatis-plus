package com.rtst.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

/**
 * @Author White Liu
 * @Description 详情
 * @Date 2020/9/18 16:23
 * @Version 1.0
 */
public class MyCodeGeneration {
    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();
        //1.全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath+"/src/main/java");
        gc.setAuthor("White Liu");//设置作者
        gc.setOpen(false);//是否打开文件
        gc.setFileOverride(false);//是否覆盖
        gc.setServiceName("%sService");//去service的I前缀
        gc.setIdType(IdType.AUTO);//设置主键策略自增
        gc.setDateType(DateType.ONLY_DATE);//设置时间类型
        gc.setSwagger2(true);//支持swagger2
        mpg.setGlobalConfig(gc);

        //2.配置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/demo?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8&allowMultiQueries=true");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("abc123");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        //3.包配置
        PackageConfig pc =new PackageConfig();
        pc.setParent("com.rtst.mybatisplus");
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("repository");
        pc.setXml("mapper");
        mpg.setPackageInfo(pc);

        //4.配置策略
        StrategyConfig sc = new StrategyConfig();
        sc.setInclude("user");//设置要映射的表名
        sc.setNaming(NamingStrategy.underline_to_camel);
        sc.setColumnNaming(NamingStrategy.underline_to_camel);
        sc.setEntityLombokModel(true);//自动lombok
        sc.setLogicDeleteFieldName("deleted");
        //自动填充
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        TableFill updateTime = new TableFill("update_time",FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(createTime);
        tableFillList.add(updateTime);
        sc.setTableFillList(tableFillList);
        //乐观锁
        sc.setVersionFieldName("version");
        sc.setRestControllerStyle(true);
        sc.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(sc);

        //执行自动生成器
        mpg.execute();
    }
}
