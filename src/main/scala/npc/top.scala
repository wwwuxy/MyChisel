package npc

import chisel3._
import chisel3.util._
import chisel3.stage._

// import org.fusesource.jansi.internal.Kernel32.COORD

//BlackBox
// class mem extends BlackBox with HasBlackBoxResource{
//     val io = IO(new Bundle{
//         val clk = Input(Clock())
//         val pc = Input(UInt(32.W))
//         val addr = Input(UInt(32.W))
//         val data = Input(UInt(32.W))
//         val wen = Input(Bool())
//         val inst = Output(UInt(32.W))
//     })
//     addResource("/mem.v")
// }


// top module
class top extends Module{
    val io =IO(new Bundle {
        val inst = Input(UInt(32.W))
        val pc = Output(UInt(32.W))
        val addr = Output(UInt(32.W))
        val data = Output(UInt(32.W))
        val mem_wr = Output(Bool())
        val imm = Output(UInt(32.W))
    })
    // io.nemutrap := false.B

//实例化各个组件
    val Pc = Module(new pc())
    val Alu = Module(new alu())
    val RegisterFile = Module(new registerfile())
    val Controller = Module(new controller())
    val InputAlu = Module(new inputalu())
    // val Mem = Module(new mem())
    val InputReg = Module(new inputreg())
// PC
    // Mem.io.inst := Pc.io.next_pc
    Pc.io.jump_en := Controller.io.jump_en
    Pc.io.jump_pc := Alu.io.rsl
    io.pc := Pc.io.next_pc
    InputAlu.io.pc := Pc.io.next_pc

//Mem 
    // Controller.io.inst := Mem.io.im_out
    // InputReg.io.dm_out := Mem.io.dm_out
    // Mem.io.dm_addr := Alu.io.rsl
    // Mem.io.dm_in := RegisterFile.io.rd2
    // Mem.io.mem_wr := Controller.io.mem_wr

//Controller
    Controller.io.inst := io.inst
    InputAlu.io.imm := Controller.io.imm
    InputAlu.io.alu_a_sel := Controller.io.alu_a_sel
    InputAlu.io.alu_b_sel := Controller.io.alu_b_sel
    InputReg.io.rf_wr_sel := Controller.io.rf_wr_sel
    RegisterFile.io.wr_en := Controller.io.rf_wr_en
    Alu.io.alu_sel := Controller.io.alu_sel

//InputReg
    RegisterFile.io.wd := InputReg.io.wd
    InputReg.io.alu_out := Alu.io.rsl
    InputReg.io.dm_out := io.inst
    InputReg.io.storepc := Pc.io.dnpc

//RegisterFile
    RegisterFile.io.inst := io.inst
    InputAlu.io.rs1 := RegisterFile.io.rd1
    InputAlu.io.rs2 := RegisterFile.io.rd2

//InputAlu
    Alu.io.op1 := InputAlu.io.op1
    Alu.io.op2 := InputAlu.io.op2

    io.addr := Alu.io.rsl
    io.data := RegisterFile.io.rd2
    io.mem_wr := Controller.io.mem_wr

//for debug
    io.imm := Controller.io.imm
}
 

object top extends App{
    emitVerilog(new top(), Array("--target-dir", "generated", "--target:verilog"))
}

