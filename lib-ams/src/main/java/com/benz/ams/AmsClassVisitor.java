package com.benz.ams;

/**
 * Created by benz on 2020/6/5.
 */

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AmsClassVisitor extends ClassVisitor implements Opcodes {

    private static final String[] methodNames = {"onCreate", "onStart", "onResume", "onPause", "onStop", "onDestroy"};

    private String mClassName;
    private ArrayList<String> names;

    public AmsClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
        List<String> list = Arrays.asList(methodNames);
        names = new ArrayList<>(list);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (this.mClassName.contains("Activity") && !this.mClassName.contains("android/")) {
            if (match(name)) {
                System.out.println("AmsClassVisitor : change method ----> " + name);
                return new AmsMethodVisitor(mv, name);
            }
        }
        return mv;
    }

    private boolean match(String name) {
        return names.contains(name);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}