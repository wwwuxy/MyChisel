package npctest

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
class top1 extends Module{
    val io =IO(new Bundle {
        val inst = Input(UInt(32.W))
        val pc = Output(UInt(32.W))
        val addr = Output(UInt(32.W))
        val data = Output(UInt(32.W))
    })
    // io.nemutrap := false.B

//实例化各个组件
    val Pc = Module(new pc1())
    val Alu = Module(new alu1())
    val RegisterFile = Module(new registerfile1())
    val Controller = Module(new controller1())
    val InputAlu = Module(new inputalu1())
    // val Mem = Module(new mem())
// PC
    // Mem.io.inst := Pc.io.next_pc
    Pc.io.jump_en := Controller.io.jump_en
    Pc.io.jump_pc := Alu.io.rsl
    io.pc := Pc.io.next_pc


//Controller
    Controller.io.inst := io.inst
    InputAlu.io.imm := Controller.io.imm
    InputAlu.io.alu_a_sel := Controller.io.alu_a_sel
    InputAlu.io.alu_b_sel := Controller.io.alu_b_sel
    RegisterFile.io.wr_en := Controller.io.rf_wr_en
    Alu.io.alu_sel := Controller.io.alu_sel

//RegisterFile
    RegisterFile.io.inst := io.inst
    InputAlu.io.rs1 := RegisterFile.io.rd1
    InputAlu.io.rs2 := RegisterFile.io.rd2
    RegisterFile.io.wd := Alu.io.rsl

//InputAlu
    Alu.io.op1 := InputAlu.io.op1
    Alu.io.op2 := InputAlu.io.op2

    io.data := RegisterFile.io.rd2
    io.addr := Alu.io.rsl
// //ebreak
//     Isbreak.io.flag := Controller.io.nemutrap
//     io.nemutrap := Isbreak.io.nemu_trap
}
 

object top1 extends App{
    emitVerilog(new top1(), Array("--target-dir", "generated", "--target:verilog"))
}

