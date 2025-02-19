package npc

import chisel3._
import chisel3.util._

class pc extends Module{
    val io = IO(new Bundle {
        val jump_jalr = Input(Bool())
        val jump_pc = Input(UInt(32.W))
        val jump_en = Input(Bool())
        val imm = Input(UInt(32.W))
        val is_ecall = Input(Bool())
        val mtvec = Input(UInt(32.W))
        val dnpc = Output(UInt(32.W))
        val next_pc = Output(UInt(32.W))
    })
//pc复位值为0x80000000
    val pc = RegInit("h8000_0000".U(32.W))
    io.next_pc := pc
    io.dnpc := pc   //初始化，只有在jump时，dnpc的值才有意义，用来保存跳转指令的下一条指令地址

    when(io.jump_jalr && io.jump_en === false.B){
        io.dnpc := pc + 4.U
        pc := Cat(io.jump_pc(31,1), 0.U(1.W))
    }.elsewhen(io.jump_en && io.jump_jalr === false.B){
        io.dnpc := pc + 4.U
        pc := io.jump_pc
    }.elsewhen(io.is_ecall){
        pc := io.mtvec 
    }.otherwise{
        pc := pc + 4.U
    }
}

// object pc extends App{
//     emitVerilog(new pc(), Array("--target-dir", "generated"))
// }