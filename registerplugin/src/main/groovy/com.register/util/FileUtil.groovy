package com.register.util

import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.AnnotationNode
import org.objectweb.asm.tree.ClassNode
import java.util.regex.Matcher
import java.util.regex.Pattern

class FileUtil {
    static boolean isSystemFile(String file) {
        if (file.startsWith("android/") || file.startsWith('androidx/'))
            return true
        def fileName = file.substring(0, file.lastIndexOf('.'))
        //过滤掉R资源文件
        Pattern pattern = Pattern.compile('.*/R(\\$[^/]*)?')
        Matcher matcher1 = pattern.matcher(fileName)
        //过滤掉BuildConfig文件
        pattern = Pattern.compile('.*/BuildConfig$')
        Matcher matcher2 = pattern.matcher(fileName)
        if(matcher1.matches() || matcher2.matches()){
            return true
        }
        return false
    }

    static ClassNode getClassNode(InputStream inputStream){
        ClassReader classReader;
        classReader = new ClassReader(inputStream)
        ClassNode classNode = new ClassNode()
        classReader.accept(classNode, 0)
        inputStream.close()
        return classNode
    }

    static List<String> getAnnotationNames(ClassNode classNode){
        List<AnnotationNode> annotationNodes = classNode.visibleAnnotations
        if(annotationNodes == null) return null
        List<String> annotations = new ArrayList<>()
        for(AnnotationNode annotationNode: annotationNodes){
            String anno = annotationNode.desc//.replaceAll("/", ".");
            String annoName = anno.substring(1, anno.length()-1);
            annotations.add(annoName)
        }
        return annotations
    }

    static String getSuperClassName(ClassNode classNode){
        return classNode.superName
    }
}
