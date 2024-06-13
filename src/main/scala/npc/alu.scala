package npc

import chisel3._
import chisel3.util._

class alu extends Module{
    val io = IO(new Bundle {
        val op1 = Input(UInt(32.W))
        val op2 = Input(UInt(32.W))
        val alu_sel = Input(UInt(8.W))
        val rsl = Output(UInt(32.W))
    })

    io.rsl := 0.U

    val add = io.op1 + io.op2
    val sub = io.op1 - io.op2
    val not = !io.op1
    val and = io.op1 & io.op2
    val or = io.op1 | io.op2
    val xor = io.op1 ^ io.op2
    val cmp = Mux(io.op1 < io.op2, 1.U, 0.U)
    val eql = Mux(io.op1 === io.op2, 1.U, 0.U)

    val res = VecInit(Seq(add, sub, not, and, or, xor, cmp, eql))

    io.rsl := Mux1H(io.alu_sel, res)
}

// object alu extends App{
//     emitVerilog(new alu(), Array("--target-dir", "generated"))
// }

