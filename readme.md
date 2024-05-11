​     

# ProtoStuff Generate



![](/data/img/plugin_img1.png)




1. Generate standard JavaBean based on proto file
2. Generate a JavaBean with protostuff @Tag annotation based on the proto file
3. Generate a JavaBean with protostuff @Tag annotation based on the proto file
4. Generate a JavaBean (LomBook) with protostuff @Tag annotation based on the proto file



​	Usage: Right click on the proto file, GenJavaBean





1. 根据proto文件生成标准的javaBean
2. 根据proto文件生成带有protostuff @Tag注解的javaBean
3. 根据proto文件生成标准的javaBean(LomBook)
4. 根据proto文件生成带有protostuff @Tag注解的javaBean(LomBook)



​	用法：右键proto文件，GenJavaBean



#### 常见问题

【错误描述】：Run Configuration Error: No plugin module specified for configuration. 
【解决方式】：IDEA里修改项目中和.idea文件同级目录下的.iml文件中的<module>标签中的type属性，将【JAVA_MODULE】改成【PLUGIN_MODULE】。