package npc

import chisel3._
import chisel3.util._

class pc extends Module{
    val io = IO(new Bundle {
        val jump_en = Input(Bool())
        val jump_pc = Input(UInt(32.W))
        val dnpc = Output(UInt(32.W))
        val next_pc = Output(UInt(32.W))
    })
//pc复位值为0x80000000
    val pc = RegInit("h8000_0000".U(32.W))
    io.next_pc := pc
    io.dnpc := pc   //初始化，只有在jump_en为1时，dnpc的值才有意义，用来保存跳转指令的下一条指令地址

    when(io.jump_en){
        io.dnpc := pc + 4.U
        pc := io.jump_pc
    }.otherwise{
        pc := pc + 4.U  //因为在testbench中，内存是按四字节为单位的，所以这里+1
    }
}

// object pc extends App{
//     emitVerilog(new pc(), Array("--target-dir", "generated"))
// }