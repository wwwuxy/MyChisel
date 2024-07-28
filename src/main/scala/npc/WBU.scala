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
    val isu_finish = Wire(Bool())
    
    when(io.in.bits.is_load){
        isu_finish := io.in.bits.load_finish
    }.elsewhen(io.in.bits.isS_type){
        isu_finish := io.in.bits.store_finish
    }.otherwise{
        isu_finish := true.B
    }   

//for pc
    io.out.bits.imm := io.in.bits.imm
    io.out.bits.jump_en := io.in.bits.jump_en
    io.out.bits.jump_jalr := io.in.bits.jump_jalr
    io.out.bits.is_ecall := io.in.bits.is_ecall
    io.out.bits.is_mret := io.in.bits.is_mret
    io.out.bits.mtvec := io.in.bits.mtvec
    io.out.bits.epc := io.in.bits.epc
    io.out.bits.rd1 := io.in.bits.rd1

  // State Machine
    val sIdle :: sWait :: sValid :: Nil = Enum(3)
    val state = RegInit(sIdle)    
    switch(state) {
      is(sIdle) {
        when(io.in.valid) {
          state := sWait
        }
      }
      is(sWait){
        when(isu_finish){
            state := sValid
        }
      }
      is(sValid) {
          state := sIdle
        }
      }

    io.in.ready := (state === sIdle)
    io.out.valid := (state === sValid)
    io.wbu_valid := (state === sValid)
}