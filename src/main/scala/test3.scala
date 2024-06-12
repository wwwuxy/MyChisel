import chisel3._
import  chisel3.util._

class alu extends Module{
    val io = IO(new Bundle {
        val a = Input(UInt(4.W))
        val b = Input(UInt(4.W))
        val sel = Input(UInt(3.W))
        val y = Output(UInt(4.W))
    })

    io.y := 0.U

    switch(io.sel){
        is("b000".U) {io.y := io.a + io.b}
        is("b001".U) {io.y := io.a - io.b}
        is("b010".U) {io.y := !io.a}
        is("b011".U) {io.y := io.a & io.b}
        is("b100".U) {io.y := io.a | io.b}
        is("b101".U) {io.y := io.a ^ io.b}
        is("b110".U) {io.y := Mux(io.a < io.b, 1.U, 0.U)}
        is("b111".U) {io.y := Mux(io.a === io.b, 1.U, 0.U)}
    }
}

object alu extends App{
    emitVerilog(new alu(), Array("--target-dir","generated"))
}