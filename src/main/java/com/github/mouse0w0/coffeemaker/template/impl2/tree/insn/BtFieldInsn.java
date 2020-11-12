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
import com.github.mouse0w0.coffeemaker.template.impl2.tree.BtNode;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;

/**
 * A node that represents a field instruction. A field instruction is an instruction that loads or
 * stores the value of a field of an object.
 *
 * @author Eric Bruneton
 * @author Mouse0w0 (modify)
 */
public class BtFieldInsn extends BtInsnBase {

    /**
     * The internal name of the field's owner class (see {@link
     * org.objectweb.asm.Type#getInternalName}).
     */
    public static final String OWNER = "owner";

    /**
     * The field's name.
     */
    public static final String NAME = "name";

    /**
     * The field's descriptor (see {@link org.objectweb.asm.Type}).
     */
    public static final String DESCRIPTOR = "descriptor";

    /**
     * Constructs a new {@link BtFieldInsn}.
     *
     * @param opcode     the opcode of the type instruction to be constructed. This opcode must be
     *                   GETSTATIC, PUTSTATIC, GETFIELD or PUTFIELD.
     * @param owner      the internal name of the field's owner class (see {@link
     *                   org.objectweb.asm.Type#getInternalName}).
     * @param name       the field's name.
     * @param descriptor the field's descriptor (see {@link org.objectweb.asm.Type}).
     */
    public BtFieldInsn(
            final int opcode, final String owner, final String name, final String descriptor) {
        super(opcode);
        putValue(OWNER, owner);
        putValue(NAME, name);
        putValue(DESCRIPTOR, descriptor);
    }

    private BtFieldInsn(
            final int opcode, final BtNode owner, final BtNode name, final BtNode descriptor) {
        super(opcode);
        put(OWNER, owner);
        put(NAME, name);
        put(DESCRIPTOR, descriptor);
    }

    /**
     * Sets the opcode of this instruction.
     *
     * @param opcode the new instruction opcode. This opcode must be GETSTATIC, PUTSTATIC, GETFIELD or
     *               PUTFIELD.
     */
    public void setOpcode(final int opcode) {
        this.opcode = opcode;
    }

    @Override
    public int getType() {
        return FIELD_INSN;
    }

    @Override
    public void accept(final MethodVisitor methodVisitor, final Evaluator evaluator) {
        methodVisitor.visitFieldInsn(opcode,
                computeString(OWNER, evaluator),
                computeString(NAME, evaluator),
                computeString(DESCRIPTOR, evaluator));
        acceptAnnotations(methodVisitor);
    }

    @Override
    public BtInsnBase clone(final Map<BtLabel, BtLabel> clonedLabels) {
        return new BtFieldInsn(opcode, get(OWNER), get(NAME), get(DESCRIPTOR)).cloneAnnotations(this);
    }
}
