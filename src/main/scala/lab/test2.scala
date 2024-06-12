import chisel3._
import chisel3.util._

class encode83 extends Module{
    val io =  IO(new Bundle {
        val in = Input(UInt(8.W))
        val out = Output(UInt(3.W))
    })

    io.out := 0.U
    
    switch(io.in){
        is(0.U) {io.out := 0.U}
        is(1.U) {io.out := 1.U}
        is(2.U) {io.out := 2.U}
        is(4.U) {io.out := 3.U}
        is(8.U) {io.out := 4.U}
        is(16.U) {io.out := 5.U}
        is(32.U) {io.out := 6.U}
        is(64.U) {io.out := 7.U}
    }
}

object   encode83 extends App{
    emitVerilog(new encode83(), Array("--target-dir", "generated"))
}