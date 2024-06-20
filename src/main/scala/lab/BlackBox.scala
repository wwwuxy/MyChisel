package lab

import chisel3._
import chisel3.util._
import chisel3.experimental._

 
class Dut extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val a = Input(UInt(32.W))
    val clk = Input(Clock())
    val reset = Input(Bool())
    val b = Output(UInt(4.W))  
  })
  
  addResource("/dut.v")//不能写成“./dut.v”
}


class UseDut extends Module {
  val io = IO(new Bundle {
    val toDut_a = Input(UInt(32.W))
    val toDut_b = Output(UInt(4.W))  
  })
 
  val u0 = Module(new Dut)
 
  u0.io.a := io.toDut_a
  u0.io.clk := clock
  u0.io.reset := reset
  io.toDut_b := u0.io.b
}

object UseDut extends App{
    emitVerilog(new UseDut(), Array("--target-dir", "generated", "--target:verilog"))
}