package npctest

import chisel3._
import chisel3.util._

class pc1 extends Module{
    val io = IO(new Bundle {
        val jump_en = Input(Bool())
        val jump_pc = Input(UInt(32.W))
        val next_pc = Output(UInt(32.W))
    })
//pc复位值为0x80000000
    val pc = RegInit("h8000_0000".U(32.W))
    io.next_pc := pc

    when(io.jump_en){
        pc := io.jump_pc
    }.otherwise{
        pc := pc + 4.U
    }

}

// object pc extends App{
//     emitVerilog(new pc(), Array("--target-dir", "generated"))
// }