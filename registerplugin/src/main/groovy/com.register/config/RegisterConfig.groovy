package com.register.config
/**
 *   支持配置方式注册
 *   1、 注册scanAnnotationClass的注解类
 *   2、 注册superClass的子类
 */
class RegisterConfig{
    public List<Map<String, Object>> registerInfo = []  //外部设置的注册信息 来自gradle或代码设置
    List<RegisterInfo> registerInfoList = new ArrayList<>() //存放每项注册信息
    public def enabledCache = true  //是否需要缓存

    RegisterConfig(){

    }

    void parseParams(){
        registerInfo.each {
            map ->
                RegisterInfo registerInfo = new RegisterInfo()
                registerInfo.scanAnnotationClass = map.get('scanAnnotationClass')
                registerInfo.registerIntoClass = map.get('registerIntoClass')
                registerInfo.initMethod = map.get('initMethod')
                registerInfo.registerMethod = map.get('registerMethod')
                registerInfo.superClass = map.get('superClass')
                registerInfo.init()
                registerInfoList.add(registerInfo)
        }
    }

    void matchRegisterFile(String fileName, File file){
        for(RegisterInfo registerInfo: registerInfoList){
            fileName = fileName.substring(0, fileName.lastIndexOf('.class'))
            if(registerInfo.registerIntoClass.equals(fileName)) {
//                println 'xxxxxxxxxx matchRegisterFile file ' + file
                registerInfo.registerIntoFile = file
                return
            }
        }
    }

    RegisterInfo isMatchAnnotation(String annotationName){
        for(RegisterInfo registerInfo: registerInfoList){
            if(registerInfo.scanAnnotationClass.equals(annotationName))
                return registerInfo
        }
        return null
    }

    void matchAnnotation(List<String> annotationNames, String fileName){
        if(annotationNames == null) return
        for(String annotationName : annotationNames){
            RegisterInfo registerInfo = isMatchAnnotation(annotationName)
            if(registerInfo != null && !registerInfo.needRegisterClass.contains(fileName)) {
//                println 'xxxxxxxxx find registerInfo ' + fileName
                fileName = fileName.substring(0, fileName.lastIndexOf('.class'))
                registerInfo.needRegisterClass.add(fileName)
            }
        }
    }

    void matchSuperClass(String superClassName, String fileName){
        for(RegisterInfo registerInfo: registerInfoList){
            if(registerInfo.superClass.equals(superClassName)) {
                if (!registerInfo.needRegisterClass.contains(fileName)) {
                    fileName = fileName.substring(0, fileName.lastIndexOf('.class'))
                    registerInfo.needRegisterClass.add(fileName)
                }
            }
        }
    }

    @Override
    String toString() {
        StringBuilder stringBuilder = new StringBuilder()
        stringBuilder.append('enabledCache='+enabledCache).append('\n')

        for(int i=0; i<registerInfoList.size(); i++){
            stringBuilder.append(registerInfoList.get(i).toString())
        }
        return stringBuilder.toString()
    }
}
