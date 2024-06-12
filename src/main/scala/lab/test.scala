import chisel3._
import chisel3.util._

class mux42 extends Module{
    val io = IO(new Bundle {
        val x0 = Input(UInt(2.W))
        val x1 = Input(UInt(2.W))
        val x2 = Input(UInt(2.W))
        val x3 = Input(UInt(2.W))
        val y = Input(UInt(2.W))
        val f = Output(UInt(2.W))
    })

    io.f := 0.U

    switch(io.y){
        is(0.U) {io.f := io.x0}
        is(1.U) {io.f := io.x1}
        is(2.U) {io.f := io.x2}
        is(3.U) {io.f := io.x3}
    }
}

object mux42 extends  App{
    emitVerilog(new mux42(),Array("--target-dir", "generated"))
}