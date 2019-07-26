package com.httpplugin

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

import javax.lang.model.element.Modifier
import java.util.jar.JarEntry
import java.util.jar.JarFile

public class HttpTransForm extends Transform {

    HttpTransForm(){}

    @Override
    String getName() {
        return "httpplugin"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider, boolean isIncremental)
            throws IOException, TransformException, InterruptedException {
//        super.transform(context, inputs, referencedInputs, outputProvider, isIncremental)
        println 'xxxxxxxx into tansform'

        def clearCache = !isIncremental
        // clean build cache
        if (clearCache) {
            outputProvider.deleteAll()
        }

        // 遍历输入文件
        inputs.each { TransformInput input ->
            // 遍历jar
            input.jarInputs.each { JarInput jarInput ->
                File src = jarInput.file
//                println 'xxxxxxxxxx jar ' + src.absolutePath
                File dest = getDestFile(jarInput, outputProvider)
                scanJarFile(src)
                FileUtils.copyFile(src, dest)
            }

            //遍历文件夹
            input.directoryInputs.each {
                DirectoryInput directoryInput ->
                    File dest = outputProvider.getContentLocation(directoryInput.name,
                            directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                    directoryInput.file.eachFileRecurse {
                        File file ->
//                        println 'xxxxxxxx '+ file.absolutePath
                    }
                    FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }
//        Register.register("")
    }

    static File getDestFile(JarInput jarInput, TransformOutputProvider outputProvider) {
        def destName = jarInput.name
        // 重名名输出文件,因为可能同名,会覆盖
        def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath)
        if (destName.endsWith(".jar")) {
            destName = destName.substring(0, destName.length() - 4)
        }
        // 获得输出文件
        File dest = outputProvider.getContentLocation(destName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
        return dest
    }

    void scanJarFile(File jarFile){
        def file = new JarFile(jarFile)
        Enumeration enumeration = file.entries()
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()
            if(entryName.endsWith("RequsetAPI.class")){
                println 'xxxxxxxxxx jar file ' + entryName
//                Register.register(entryName)
            }
        }
        if (null != file) {
            file.close()
        }
    }
}