package com.httpplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

public  class HttpPlugin implements Plugin<Project>{

    @Override
    public void apply(Project project){
        def isApp = project.plugins.hasPlugin(AppPlugin)
        def android = project.extensions.getByType(AppExtension)
        println 'xxxxxxxxxxx into httpplugin'
        if(isApp) {
            HttpTransForm httpTransForm = new HttpTransForm()
            android.registerTransform(httpTransForm)
        }
    }

}