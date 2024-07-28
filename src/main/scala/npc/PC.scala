package npc

import chisel3._
import chisel3.util._

class PC extends Module{
    val io = IO(new Bundle {
        val next_pc = Output(UInt(32.W))
        val in = Flipped(Decoupled(new WBU_PC))
        // val wbu_valid = Input(Bool())
        // val no_ld = Input(Bool())
    }) 

    val pc = RegInit("h8000_0000".U(32.W))
    io.next_pc := pc
    val jump_pc = pc + io.in.bits.imm
    val jalr_pc = io.in.bits.rd1 + io.in.bits.imm

    io.in.ready := false.B
    when(io.in.valid){
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
    
    val sIdle :: sUpdate :: Nil = Enum(2)
    val state = RegInit(sIdle)

    switch(state) {
        is(sIdle) {
            when(io.in.valid) {
                io.in.ready := true.B
                state := sUpdate
            }
        }
        is(sUpdate) {
            when(io.in.ready && io.in.valid) {
                when(io.in.bits.jump_en) {
                    pc := jump_pc
                }.elsewhen(io.in.bits.jump_jalr) {
                    pc := jalr_pc
                }.elsewhen(io.in.bits.is_ecall) {
                    pc := io.in.bits.mtvec
                }.elsewhen(io.in.bits.is_mret) {
                    pc := io.in.bits.epc
                }.otherwise {
                    pc := pc + 4.U
                }
                io.in.ready := false.B
                state := sIdle
            }
        }
    }

    // Default assignments
    io.in.ready := false.B
}
