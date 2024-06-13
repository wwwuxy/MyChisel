package npc

import chisel3._
import chisel3.util._

class inputalu extends Module{
    val io = IO(new Bundle {
        val rs1 = Input(UInt(32.W))
        val rs2 = Input(UInt(32.W))
        val imm = Input(UInt(32.W))
        val rs1_en = Input(Bool())
        val rs2_en = Input(Bool())
        val imm_en = Input(Bool())
        val op1 = Output(UInt(32.W))
        val op2 = Output(UInt(32.W))
    })


    io.op1 := Mux(io.rs1_en, io.rs1, 0.U)
    io.op2 := Mux(io.rs2_en, io.rs2, io.imm)
//imm_en 备用，S、B型指令可能使用
}

// object inputalu extends App{
//     emitVerilog(new inputalu(), Array("--target-dir", "generated"))
// }
