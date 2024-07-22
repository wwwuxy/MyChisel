package npc

import chisel3._
import chisel3.util.HasBlackBoxResource
import chisel3.util.Decoupled

//BlackBox for Inst_Memory
class Inst_Memory extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
//imem
    val pc = Input(UInt(32.W))
    val inst = Output(UInt(32.W))
  })

  addResource("/Inst_Memory.v")
}


class IFU extends  Module{
    val io = IO(new Bundle {
        val pc = Input(UInt(32.W)) 
        val out = Decoupled(new IFU_IDU)
    })

    val imem = Module(new Inst_Memory)

//imm
    imem.io.pc := io.pc
//out
    io.out.bits.inst := imem.io.inst
    io.out.bits.pc := io.pc

    io.out.valid := true.B
}
