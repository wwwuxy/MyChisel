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

    imem.io.pc := io.pc

    val IR = RegInit(0.U(32.W))
    val inst_reg = RegInit(0.U(32.W))
    val vaild_reg = RegInit(false.B)

    inst_reg := imem.io.inst
    IR := io.pc
    
    when(io.pc =/= IR){
        IR := io.pc
    }

//out
    io.out.bits.pc := IR
    io.out.bits.inst := inst_reg
    io.out.valid := true.B
}
