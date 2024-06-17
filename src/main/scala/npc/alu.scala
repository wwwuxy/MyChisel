package npc

import chisel3._
import chisel3.util._

class alu extends Module{
    val io = IO(new Bundle {
        val op1 = Input(UInt(32.W))
        val op2 = Input(UInt(32.W))
        val alu_sel = Input(UInt(12.W))
        val rsl = Output(UInt(32.W))
    })

    io.rsl := 0.U

    val add = io.op1 + io.op2   
    val sub = io.op1 - io.op2
    val umovl = io.op1 << io.op2(4, 0)
    val umovr = io.op1 >> io.op2(4, 0)
    val smovr = Cat(Fill(4, io.op1(31)), (io.op1 >> io.op2)(26, 0))     //算术右移
    val not = !io.op1
    val rd2 = io.op2
    val and = io.op1 & io.op2
    val or = io.op1 | io.op2
    val xor = io.op1 ^ io.op2
    val cmp = Mux(io.op1 < io.op2, 1.U, 0.U)
    val eql = Mux(io.op1 === io.op2, 1.U, 0.U)

    val res = VecInit(Seq(add, sub, umovl, umovr, smovr, not, rd2, and, or, xor, cmp, eql))

    io.rsl := Mux1H(io.alu_sel, res)
}

// object alu extends App{
//     emitVerilog(new alu(), Array("--target-dir", "generated"))
// }

