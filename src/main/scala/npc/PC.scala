package npc

import chisel3._
import chisel3.util._

class PC extends Module{
    val io = IO(new Bundle {
        val next_pc = Output(UInt(32.W))
        val in = Flipped(Decoupled(new WBU_PC))
        val wbu_valid = Input(Bool())
        val diff_test = Output(Bool())
        // val no_ld = Input(Bool())
    }) 

    val pc = RegInit("h8000_0000".U(32.W))
    io.next_pc := pc
    val jump_pc = pc + io.in.bits.imm
    val jalr_pc = io.in.bits.rd1 + io.in.bits.imm

    io.diff_test := io.wbu_valid
    when(io.wbu_valid){
        when(io.in.bits.jump_en){
            pc := jump_pc
        }.elsewhen(io.in.bits.jump_jalr){
            pc := jalr_pc
        }.elsewhen(io.in.bits.is_ecall){
            pc := io.in.bits.mtvec
        }.elsewhen(io.in.bits.is_mret){
            pc := io.in.bits.epc
        }.otherwise{
            pc := pc + 4.U
        }
    }
    
    io.in.ready := true.B
}
