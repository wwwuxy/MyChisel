package npc

import chisel3._
import chisel3.util._

class EXU extends Module{
    val io = IO(new Bundle {
        val in = Flipped(Decoupled(new IDU_EXU))
        val alu_rsl = Output(UInt(32.W))
    })

    val Alu = Module(new ALU)

    Alu.io.pc := io.in.bits.pc
    Alu.io.rs1 := io.in.bits.rs1
    Alu.io.rs2 := io.in.bits.rs2
    Alu.io.imm := io.in.bits.imm
    Alu.io.alu_a_sel := io.in.bits.alu_a_sel
    Alu.io.alu_b_sel := io.in.bits.alu_b_sel
    Alu.io.alu_sel := io.in.bits.alu_sel

    io.alu_rsl := Alu.io.rsl

    io.in.ready := true.B
}
