package com.axt.jvmcallgraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class TargetMethodCollector extends ClassVisitor {

	private final List<Predicate<MethodInfo>> targetMethods;
	private final List<MethodInfo> collectedMethods = new ArrayList<>();
	
	public TargetMethodCollector(List<Predicate<MethodInfo>> targetMethods) {
		super(Opcodes.ASM5);
		this.targetMethods = targetMethods;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodInfo mi = new MethodInfo(name, desc, access);
		for (Predicate<MethodInfo> predicate : targetMethods) {
			if (predicate.test(mi)) {
				collectedMethods.add(mi);
			}
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
	
	public List<MethodInfo> getCollectedMethods() {
		return Collections.unmodifiableList(collectedMethods);
	}
	
	public boolean hasCollectedMethods() {
		return collectedMethods.size() > 0;
	}
}