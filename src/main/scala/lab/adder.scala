package lab

import chisel3._

class adder extends Module{
    val io = IO(new Bundle {
        val a = Input(UInt(32.W))
        val b = Input(UInt(32.W))
        val y = Output(UInt(32.W))
    })
     
    io.y := io.a + io.b
}



