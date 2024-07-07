package npc

import chisel3._
import chisel3.util.experimental.loadMemoryFromFile


class mem extends Module{
    val io = IO(new Bundle {
        val inst = Output(UInt(32.W))   
    })
    
}


// object mem extends App{
//     emitVerilog(new mem(), Array("--target-dir", "generated"))
// }