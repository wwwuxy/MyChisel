package npc

import chisel3._
import chisel3.util._

class WBU extends Module{
    val io = IO(new Bundle{
        val in = Flipped(Decoupled(new ISU_WBU))
        val out = Decoupled(new WBU_PC)
        val dm_out = Output(UInt(32.W))
        val alu_out = Output(UInt(32.W))
        val wbu_valid = Output(Bool())
    })

//for regfiles
    io.dm_out := io.in.bits.dm_out
    io.alu_out := io.in.bits.alu_out

    io.wbu_valid := (io.in.bits.is_load && io.in.bits.finish_load) || (io.in.bits.isS_type && io.in.bits.can_wirte) || (!io.in.bits.is_load && !io.in.bits.isS_type && io.in.valid)

//for pc
    io.out.bits.imm := io.in.bits.imm
    io.out.bits.jump_en := io.in.bits.jump_en
    io.out.bits.jump_jalr := io.in.bits.jump_jalr
    io.out.bits.is_ecall := io.in.bits.is_ecall
    io.out.bits.is_mret := io.in.bits.is_mret
    io.out.bits.mtvec := io.in.bits.mtvec
    io.out.bits.epc := io.in.bits.epc
    io.out.bits.rd1 := io.in.bits.rd1

    io.in.ready := io.out.ready
    io.out.valid := io.in.valid
}   
