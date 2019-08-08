package com.register.config;

public class RegisterInfo{
    String scanAnnotationClass //扫描注解的类
    String registerIntoClass //自动注册到这个Class
    File registerIntoFile //自动注册到这个文件
    String registerInMethod//在该方法中调用注册方法进行注册
    String registerMethod //注册方法
    String superClass //父类
    List<String> needRegisterClass = new ArrayList<>() //待注册的类集合


    public RegisterInfo(){}

    public void init(){
        if(registerIntoClass != null)
            registerIntoClass = registerIntoClass.replaceAll("\\.","/")
        if(superClass != null)
            superClass = superClass.replaceAll('\\.','/')
        if(scanAnnotationClass != null)
            scanAnnotationClass = scanAnnotationClass.replaceAll('\\.','/')
    }

    @Override
    String toString() {
        String needRegisterClassNames = '';
        needRegisterClass.each {
            name->
                needRegisterClassNames+= name+','
        }
        if(needRegisterClassNames.length() >= 1)
            needRegisterClassNames = needRegisterClassNames.substring(0, needRegisterClassNames.length()-1)

        StringBuilder stringBuilder = new StringBuilder()
        stringBuilder.append('scanAnnotationClass='+scanAnnotationClass)
        .append(';registerIntoFile='+registerIntoFile).append('\n')
        .append(';registerIntoClass='+registerIntoClass)
        .append(';registerInMethod='+registerInMethod)
        .append(';registerMethod='+registerMethod).append('\n')
        .append(';superClass='+superClass)
        .append(';needRegisterClass='+needRegisterClassNames)
        .append('\n');
        return stringBuilder.toString()
    }
}