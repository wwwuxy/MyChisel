package npc

import chisel3._
import chisel3.util._

// import org.fusesource.jansi.internal.Kernel32.COORD

class top extends Module{

//实例化各个组件
    val Pc = Module(new pc())
    val Alu = Module(new alu())
    val RegisterFile = Module(new registerfile())
    val Controller = Module(new controller())
    val InputAlu = Module(new inputalu())

    val reg_wr = Wire(UInt(32.W))
    reg_wr := Mux(Controller.io.rd_en, Alu.io.rsl, 0.U)

//连线



}


object top extends App{
    emitVerilog(new top(), Array("--target-dir", "generated"))
}

