package npctest

import chisel3._
import chisel3.util._

class inputalu1 extends Module{
    val io = IO(new Bundle {
        val rs1 = Input(UInt(32.W))
        val rs2 = Input(UInt(32.W))
        val imm = Input(UInt(32.W))
        val alu_a_sel = Input(Bool())
        val alu_b_sel = Input(Bool())
        val op1 = Output(UInt(32.W))
        val op2 = Output(UInt(32.W))
    })


    io.op1 := Mux(io.alu_a_sel, io.rs1, 0.U)
    io.op2 := Mux(io.alu_b_sel, io.rs2, io.imm)
}

// object inputalu extends App{
//     emitVerilog(new inputalu(), Array("--target-dir", "generated"))
// }
