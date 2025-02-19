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
    val smovr = (op1_signed >> op2_signed(4, 0)).asUInt   //算术右移
    
    val not = ~op1_unsigned
    val rd2 = op2_unsigned
    val and = op1_unsigned & op2_unsigned
    val or = op1_unsigned | op2_unsigned
    val xor = op1_unsigned ^ op2_unsigned
    
    val cmpu = Mux((op1_unsigned < op2_unsigned), 1.U, 0.U).asUInt
    val cmp = Mux((op1_signed < op2_signed), 1.U, 0.U).asUInt
    val eql = Mux((op1_unsigned === op2_unsigned), 1.U, 0.U).asUInt

    val res = VecInit(Seq(add, sub, umovl, umovr, smovr, not, rd2, and, or, xor, cmpu, cmp, eql))

    io.rsl := Mux1H(io.alu_sel, res)
}

