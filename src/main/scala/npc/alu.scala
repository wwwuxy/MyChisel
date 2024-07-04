package npc

import chisel3._
import chisel3.util._

class alu extends Module{
    val io = IO(new Bundle {
        val op1 = Input(UInt(32.W))
        val op2 = Input(UInt(32.W))
        val alu_sel = Input(UInt(13.W))
        val rsl = Output(UInt(32.W))
    })

    io.rsl := 0.U

//解释为有符号数和无符号数
    val op1_signed = io.op1.asSInt
    val op2_signed = io.op2.asSInt
    val op1_unsigned = io.op1
    val op2_unsigned = io.op2

    val add = (op1_signed + op2_signed).asUInt   
    val sub = (op1_signed - op2_signed).asUInt
    val umovl = op1_unsigned << op2_unsigned(4, 0)
    val umovr = op1_unsigned >> op2_unsigned(4, 0)
    val smovr = Cat(Fill(4, op1_unsigned(31)), (op1_unsigned >> op2_unsigned)(26, 0))     //算术右移
    val not = ~op1_unsigned
    val rd2 = op2_unsigned
    val and = op1_unsigned & op2_unsigned
    val or = op1_unsigned | op2_unsigned
    val xor = op1_unsigned ^ op2_unsigned
    val cmp = Mux((op1_signed < op2_signed), 1.S, 0.S).asUInt
    val eql = Mux((op1_signed === op2_signed), 1.S, 0.S).asUInt
    val jalr = ((op1_signed + op2_signed).asUInt) & ~1.U

    val res = VecInit(Seq(add, sub, umovl, umovr, smovr, not, rd2, and, or, xor, cmp, eql, jalr))

    io.rsl := Mux1H(io.alu_sel, res)
}

// object alu extends App{
//     emitVerilog(new alu(), Array("--target-dir", "generated"))
// }

