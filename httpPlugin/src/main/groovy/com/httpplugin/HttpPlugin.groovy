package com.httpplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

public  class HttpPlugin implements Plugin<Project>{

    @Override
    public void apply(Project project){
        println 'xxxxxxxxxxx into httpplugin'
        def isApp = project.plugins.hasPlugin(AppPlugin)
        if (isApp) {
            println 'project(' + project.name + ') apply http-plugin plugin'

//            Register.register("")
//            def android = project.extensions.getByType(AppExtension)
//            def transformImpl = new HttpTransForm()
//            android.registerTransform(transformImpl)
////            project.afterEvaluate {
////                init(project, transformImpl)//此处要先于transformImpl.transform方法执行
////            }
        }

//         dump();
//        Register.register("")
    }





}