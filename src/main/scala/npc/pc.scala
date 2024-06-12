package npc

import chisel3._
import chisel3.util._

class pc extends Module{
    val io = IO(new Bundle {
        val jump_en = Input(Bool())
        val dnpc = Input(UInt(32.W))
        val next_pc = Output(UInt(32.W))
    })
    when(io.jump_en){
        io.next_pc := io.dnpc
    }.otherwise{
        io.next_pc := io.dnpc + 4.U
    }
}

object pc extends App{
    emitVerilog(new pc(), Array("--target-dir", "generated"))
}