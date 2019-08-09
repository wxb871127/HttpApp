package com.register

import com.android.build.gradle.AppExtension
import com.register.config.RegisterConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppPlugin


class RegisterPlugin implements Plugin<Project>{

    public static final String ExtenName = 'registerplugin'

    @Override
    void apply(Project project){
        def isApp = project.plugins.hasPlugin(AppPlugin)

        if(isApp) {
            project.extensions.create(ExtenName, RegisterConfig)
            def android = project.extensions.getByType(AppExtension)
//            def android = project.extensions.getByType(BaseExtension.class)
            RegisterTransForm registerTransForm = new RegisterTransForm()
            android.registerTransform(registerTransForm)

            project.afterEvaluate {
//                RegisterConfig params = project.extensions.findByName(ExtenName) as RegisterConfig
                //将带有HttpRequest注解的类 注册到HttpProxy类的autoRegister方法中
                List<Map<String, Object>> registerInfo = new ArrayList<>()
                Map<String, Object> map = new HashMap<>()
                map.put("scanAnnotationClass","com.httplib.annotation.HttpProxy");
                map.put("registerIntoClass","com.httplib.config.HttpConfig")
                map.put('initMethod','init')
                map.put("registerMethod","autoRegister");
                registerInfo.add(map);

                RegisterConfig params = new RegisterConfig()
                params.registerInfo = registerInfo
                params.parseParams()
                println params.toString()
                registerTransForm.registerConfig = params
            }
        }

    }

}