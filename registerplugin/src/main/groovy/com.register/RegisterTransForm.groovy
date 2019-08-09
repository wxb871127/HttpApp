package com.register

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.register.config.RegisterConfig
import com.register.util.FileInvoke
import com.register.util.FileUtil
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.objectweb.asm.tree.ClassNode
import java.util.jar.JarEntry
import java.util.jar.JarFile

class RegisterTransForm extends Transform{
    RegisterConfig registerConfig

    RegisterTransForm(){}

    @Override
    String getName() {
        return "registerplugin"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /**
     * 是否支持增量编译
     * @return
     */
    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs
                   , Collection<TransformInput> referencedInputs
                   , TransformOutputProvider outputProvider
                   , boolean isIncremental) throws IOException, TransformException, InterruptedException {
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
                File dest = getDestFile(jarInput, outputProvider)
                def file = new JarFile(src)
                Enumeration enumeration = file.entries()
                while (enumeration.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                    String fileName = jarEntry.getName()
                    if(jarEntry.isDirectory()) continue
                    if (!fileName.endsWith('.class')) continue
                    if (!FileUtil.isSystemFile(fileName)) {
                        InputStream inputStream = file.getInputStream(jarEntry)
                        ClassNode classNode = FileUtil.getClassNode(inputStream)
                        List<String> list = FileUtil.getAnnotationNames(classNode)
                        registerConfig.matchRegisterFile(fileName, dest)
                        registerConfig.matchAnnotation(list, fileName)
                        registerConfig.matchSuperClass(classNode.superName, fileName)
                    }
                }
                FileUtils.copyFile(src, dest)
            }

            boolean leftSlash = File.separator == '/'
            //遍历文件夹
            input.directoryInputs.each {
                DirectoryInput directory ->
                    File dest = outputProvider.getContentLocation(directory.name,
                            directory.contentTypes, directory.scopes, Format.DIRECTORY)
                    String root = directory.file.absolutePath
                    if (!root.endsWith(File.separator))
                        root += File.separator
                    directory.file.eachFileRecurse { File file ->
                        def fileName = file.absolutePath.replace(root, '')
                        if (!leftSlash) {
                            fileName = fileName.replaceAll("\\\\", "/")
                        }
                        if (fileName.endsWith('.class')) {
                            if (!FileUtil.isSystemFile(fileName)) {
                                registerConfig.matchRegisterFile(fileName, file)
                                InputStream inputStream = new FileInputStream(file)
                                ClassNode classNode = FileUtil.getClassNode(inputStream)
                                List<String> list = FileUtil.getAnnotationNames(classNode)
                                registerConfig.matchAnnotation(list, fileName)
                                registerConfig.matchSuperClass(classNode.superName, fileName)
                            }
                        }
                    }
                    FileUtils.copyDirectory(directory.file, dest)
            }
        }

        println  registerConfig.toString()

        registerConfig.registerInfoList.each {
            registerInfo->
                FileInvoke.invoke(registerInfo.registerIntoFile,
                        registerInfo.registerIntoClass,
                        registerInfo.initMethod,
                        registerInfo.registerMethod, registerInfo.needRegisterClass)

        }
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
}
