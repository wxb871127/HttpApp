package com.register.util

import org.apache.commons.io.IOUtils
import org.objectweb.asm.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class FileInvoke {
    //文件调用，以文件形式实现方法调用
    //new param对象，调用file文件中的className类的method方法
    static void invoke(File file, String className, String initMethod, String method, List<String> params){
        if(!file) return
        byte [] bytes
        if(file.name.endsWith('.jar')){
//            println 'jar file ' + file.name
            def jarFile = new JarFile(file)
            def optJar = new File(file.getParent(), file.name + ".opt")
            if (optJar.exists())
                optJar.delete()
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))
            Enumeration enumeration = jarFile.entries()
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                jarOutputStream.putNextEntry(zipEntry)
                InputStream inputStream = jarFile.getInputStream(jarEntry)
                entryName = entryName.substring(0, entryName.lastIndexOf('.class'))
                if (entryName.equals(className)) {
                    println 'find ' + className + ' in ' + file.name
                    bytes = invokedSteam(inputStream, className, initMethod, method ,params)
                    jarOutputStream.write(bytes)
                }else {
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                inputStream.close()
                jarOutputStream.closeEntry()
            }
            jarOutputStream.close()
            jarFile.close()
            if (file.exists()) {
                file.delete()
            }
            optJar.renameTo(file)
        }else {
            bytes = invokedSteam(new FileInputStream(file), className, initMethod, method,params)
            def optClass = new File(file.getParent(), file.name + ".opt")
            FileOutputStream outputStream = new FileOutputStream(optClass)
            outputStream.write(bytes)
            outputStream.close()
            if (file.exists()) {
                file.delete()
            }
            optClass.renameTo(file)
        }
    }

    //获取方法调用之后产生的新的文件流
    static byte[] invokedSteam(InputStream inputStream, String className, String initMethod ,String method, List<String> params){
//        println 'xxxxxxxxxx className = ' + className + ' initMethod = ' + initMethod +  ' method = ' + method
        ClassReader classReader = new ClassReader(inputStream);
        ClassWriter classWriter = new ClassWriter(classReader, 0);
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM5, classWriter) {
            @Override
            public void visit(int i, int i1, String s, String s1, String s2, String[] strings) {
                super.visit(i, i1, s, s1, s2, strings);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
                if(name.equals(initMethod)){
//                    println 'find initMethod ' + initMethod
                    methodVisitor = new MethodVisitor(Opcodes.ASM5, methodVisitor) {
                        @Override
                        void visitInsn(int opcode) {
                            if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                                params.each {
                                    param ->
                                        param = param.replaceAll('/','.')
//                                        println 'visit param ' + param
                                        methodVisitor.visitLdcInsn(param)//类名
                                        //调用指定方法进行注册
                                        methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, className,
                                                method, "(Ljava/lang/String;)V", false)
                                }
                            }
                            super.visitInsn(opcode)
                        }

                        @Override
                        public void visitMaxs(int i, int i1) {
                            super.visitMaxs(i + 4, i1)
                        }
                    };
                }
                return methodVisitor
            }
        };
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        inputStream.close()
        return classWriter.toByteArray()
    }
}
