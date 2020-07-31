package com.github.mouse0w0.coffeemaker.impl.processor;

import com.github.mouse0w0.asm.extree.AnnotationNodeEx;
import com.github.mouse0w0.asm.extree.ClassNodeEx;
import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.Processor;
import com.github.mouse0w0.coffeemaker.impl.handler.AnnotationHandler;
import com.github.mouse0w0.coffeemaker.syntax.ModifyAnnotation;
import com.github.mouse0w0.coffeemaker.syntax.ModifyAnnotations;
import org.objectweb.asm.Type;

import java.util.List;

public class ModifyAnnotationProcessor implements Processor {

    public static AnnotationHandler handler() {
        return new Handler();
    }

    private final String type;
    private final String name;
    private final String statement;

    private ModifyAnnotationProcessor(AnnotationNodeEx annotation) {
        this.type = annotation.<Type>getValue("type").getDescriptor();
        this.name = annotation.getValue("name");
        this.statement = annotation.getValue("statement");
    }

    @Override
    public void process(ClassNodeEx classNode, Evaluator evaluator) {
        AnnotationNodeEx annotation = classNode.getAnnotation(type);
        annotation.putValue(name, evaluator.eval(statement));
    }

    private static class Handler implements AnnotationHandler {

        private static final String MODIFY_ANNOTATION_DESC = Type.getDescriptor(ModifyAnnotation.class);
        private static final String MODIFY_ANNOTATIONS_DESC = Type.getDescriptor(ModifyAnnotations.class);

        @Override
        public String[] getSupportedAnnotationTypes() {
            return new String[]{MODIFY_ANNOTATION_DESC, MODIFY_ANNOTATIONS_DESC};
        }

        @Override
        public void handle(Object owner, AnnotationNodeEx annotation, List<Processor> processors) {
            if (annotation.desc.equals(MODIFY_ANNOTATION_DESC)) {
                processors.add(new ModifyAnnotationProcessor(annotation));
            } else if (annotation.desc.equals(MODIFY_ANNOTATIONS_DESC)) {
                List<AnnotationNodeEx> values = annotation.getValue("value");
                for (AnnotationNodeEx value : values) {
                    processors.add(new ModifyAnnotationProcessor(value));
                }
            }
        }
    }
}
