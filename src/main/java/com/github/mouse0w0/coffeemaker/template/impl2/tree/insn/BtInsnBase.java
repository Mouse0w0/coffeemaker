// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
//    contributors may be used to endorse or promote products derived from
//    this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
// THE POSSIBILITY OF SUCH DAMAGE.
package com.github.mouse0w0.coffeemaker.template.impl2.tree.insn;

import com.github.mouse0w0.coffeemaker.Evaluator;
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtObject;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.TypeAnnotationNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A node that represents a bytecode instruction. <i>An instruction can appear at most once in at
 * most one {@link BtInsnList} at a time</i>.
 *
 * @author Eric Bruneton
 * @author Mouse0w0 (modify)
 */
public abstract class BtInsnBase extends BtObject {

  public static final String OPCODE = "opcode";

  public static final String TYPE_ANNOTATIONS = "typeAnnotations";

  /**
   * The type of {@link BtInsn} instructions.
   */
  public static final int INSN = 0;

  /**
   * The type of {@link BtIntInsn} instructions.
   */
  public static final int INT_INSN = 1;

  /**
   * The type of {@link BtVarInsn} instructions.
   */
  public static final int VAR_INSN = 2;

  /**
   * The type of {@link BtTypeInsn} instructions.
   */
  public static final int TYPE_INSN = 3;

  /**
   * The type of {@link BtFieldInsn} instructions.
   */
  public static final int FIELD_INSN = 4;

  /**
   * The type of {@link BtMethodInsn} instructions.
   */
  public static final int METHOD_INSN = 5;

  /**
   * The type of {@link BtInvokeDynamicInsn} instructions.
   */
  public static final int INVOKE_DYNAMIC_INSN = 6;

  /**
   * The type of {@link BtJumpInsn} instructions.
   */
  public static final int JUMP_INSN = 7;

  /**
   * The type of {@link BtLabel} "instructions".
   */
  public static final int LABEL = 8;

  /**
   * The type of {@link BtLdcInsn} instructions.
   */
  public static final int LDC_INSN = 9;

  /**
   * The type of {@link BtIincInsn} instructions.
   */
  public static final int IINC_INSN = 10;

  /**
   * The type of {@link BtTableSwitchInsn} instructions.
   */
  public static final int TABLESWITCH_INSN = 11;

  /**
   * The type of {@link BtLookupSwitchInsn} instructions.
   */
  public static final int LOOKUPSWITCH_INSN = 12;

  /**
   * The type of {@link BtMultiANewArrayInsn} instructions.
   */
  public static final int MULTIANEWARRAY_INSN = 13;

  /**
   * The type of {@link BtFrame} "instructions".
   */
  public static final int FRAME = 14;

  /**
   * The type of {@link BtLineNumber} "instructions".
   */
  public static final int LINE = 15;

  /**
   * The opcode of this instruction.
   */
  protected int opcode;

  /**
   * The runtime visible type annotations of this instruction. This field is only used for real
   * instructions (i.e. not for labels, frames, or line number nodes). This list is a list of {@link
   * TypeAnnotationNode} objects. May be {@literal null}.
   */
  public List<TypeAnnotationNode> visibleTypeAnnotations;

  /**
   * The runtime invisible type annotations of this instruction. This field is only used for real
   * instructions (i.e. not for labels, frames, or line number nodes). This list is a list of {@link
   * TypeAnnotationNode} objects. May be {@literal null}.
   */
  public List<TypeAnnotationNode> invisibleTypeAnnotations;

  /**
   * The previous instruction in the list to which this instruction belongs.
   */
  BtInsnBase previousInsn;

  /**
   * The next instruction in the list to which this instruction belongs.
   */
  BtInsnBase nextInsn;

  /**
   * The index of this instruction in the list to which it belongs. The value of this field is
   * correct only when {@link BtInsnList#cache} is not null. A value of -1 indicates that this
   * instruction does not belong to any {@link BtInsnList}.
   */
  int index;

  /**
   * Constructs a new {@link BtInsnBase}.
   *
   * @param opcode the opcode of the instruction to be constructed.
   */
  protected BtInsnBase(final int opcode) {
    this.opcode = opcode;
    this.index = -1;
  }

  /**
   * Returns the opcode of this instruction.
   *
   * @return the opcode of this instruction.
   */
  public int getOpcode() {
    return opcode;
  }

  /**
   * Returns the type of this instruction.
   *
   * @return the type of this instruction, i.e. one the constants defined in this class.
   */
  public abstract int getType();

  /**
   * Returns the previous instruction in the list to which this instruction belongs, if any.
   *
   * @return the previous instruction in the list to which this instruction belongs, if any. May be
   * {@literal null}.
   */
  public BtInsnBase getPrevious() {
    return previousInsn;
  }

  /**
   * Returns the next instruction in the list to which this instruction belongs, if any.
   *
   * @return the next instruction in the list to which this instruction belongs, if any. May be
   * {@literal null}.
   */
  public BtInsnBase getNext() {
    return nextInsn;
  }

  /**
   * Makes the given method visitor visit this instruction.
   *
   * @param methodVisitor a method visitor.
   */
  public abstract void accept(MethodVisitor methodVisitor, Evaluator evaluator);

  /**
   * Makes the given visitor visit the annotations of this instruction.
   *
   * @param methodVisitor a method visitor.
   */
  protected final void acceptAnnotations(final MethodVisitor methodVisitor) {
    if (visibleTypeAnnotations != null) {
      for (int i = 0, n = visibleTypeAnnotations.size(); i < n; ++i) {
        TypeAnnotationNode typeAnnotation = visibleTypeAnnotations.get(i);
        typeAnnotation.accept(
                methodVisitor.visitInsnAnnotation(
                        typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, true));
      }
    }
    if (invisibleTypeAnnotations != null) {
      for (int i = 0, n = invisibleTypeAnnotations.size(); i < n; ++i) {
        TypeAnnotationNode typeAnnotation = invisibleTypeAnnotations.get(i);
        typeAnnotation.accept(
                methodVisitor.visitInsnAnnotation(
                        typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, false));
      }
    }
  }

  /**
   * Returns a copy of this instruction.
   *
   * @param clonedLabels a map from LabelNodes to cloned LabelNodes.
   * @return a copy of this instruction. The returned instruction does not belong to any {@link
   * BtInsnList}.
   */
  public abstract BtInsnBase clone(Map<BtLabel, BtLabel> clonedLabels);

  /**
   * Returns the clone of the given label.
   *
   * @param label        a label.
   * @param clonedLabels a map from LabelNodes to cloned LabelNodes.
   * @return the clone of the given label.
   */
  static BtLabel clone(final BtLabel label, final Map<BtLabel, BtLabel> clonedLabels) {
    return clonedLabels.get(label);
  }

  /**
   * Returns the clones of the given labels.
   *
   * @param labels       a list of labels.
   * @param clonedLabels a map from LabelNodes to cloned LabelNodes.
   * @return the clones of the given labels.
   */
  static BtLabel[] clone(
          final List<BtLabel> labels, final Map<BtLabel, BtLabel> clonedLabels) {
    BtLabel[] clones = new BtLabel[labels.size()];
    for (int i = 0, n = clones.length; i < n; ++i) {
      clones[i] = clonedLabels.get(labels.get(i));
    }
    return clones;
  }

  /**
   * Clones the annotations of the given instruction into this instruction.
   *
   * @param insnNode the source instruction.
   * @return this instruction.
   */
  protected final BtInsnBase cloneAnnotations(final BtInsnBase insnNode) {
    if (insnNode.visibleTypeAnnotations != null) {
      this.visibleTypeAnnotations = new ArrayList<>();
      for (int i = 0, n = insnNode.visibleTypeAnnotations.size(); i < n; ++i) {
        TypeAnnotationNode sourceAnnotation = insnNode.visibleTypeAnnotations.get(i);
        TypeAnnotationNode cloneAnnotation =
                new TypeAnnotationNode(
                        sourceAnnotation.typeRef, sourceAnnotation.typePath, sourceAnnotation.desc);
        sourceAnnotation.accept(cloneAnnotation);
        this.visibleTypeAnnotations.add(cloneAnnotation);
      }
    }
    if (insnNode.invisibleTypeAnnotations != null) {
      this.invisibleTypeAnnotations = new ArrayList<>();
      for (int i = 0, n = insnNode.invisibleTypeAnnotations.size(); i < n; ++i) {
        TypeAnnotationNode sourceAnnotation = insnNode.invisibleTypeAnnotations.get(i);
        TypeAnnotationNode cloneAnnotation =
                new TypeAnnotationNode(
                        sourceAnnotation.typeRef, sourceAnnotation.typePath, sourceAnnotation.desc);
        sourceAnnotation.accept(cloneAnnotation);
        this.invisibleTypeAnnotations.add(cloneAnnotation);
      }
    }
    return this;
  }
}