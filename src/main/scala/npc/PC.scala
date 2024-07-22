package npc

import chisel3._
import chisel3.util._

class PC extends Module{
    val io = IO(new Bundle {
        val jump_jalr = Input(Bool())
        val jump_en = Input(Bool())
        val imm = Input(UInt(32.W))
        val is_ecall = Input(Bool())
        val is_mret = Input(Bool())
        val mtvec = Input(UInt(32.W))
        val epc = Input(UInt(32.W))
        val rd1 = Input(UInt(32.W))     //for jalr
        val next_pc = Output(UInt(32.W))
    }) 

    val pc = RegInit("h8000_0000".U(32.W))
    io.next_pc := pc
    val jump_pc = pc + io.imm
    val jalr_pc = io.rd1 + io.imm

    when(io.jump_jalr){
        pc := Cat(jalr_pc(31,1), 0.U(1.W))
    }.elsewhen(io.jump_en){
        pc := jump_pc
    }.elsewhen(io.is_ecall){
        pc := io.mtvec 
    }.elsewhen(io.is_mret){
        pc := io.epc
    }.otherwise{
        pc := pc + 4.U
    }
}
