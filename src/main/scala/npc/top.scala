package npc

import chisel3._
import chisel3.util._

// import org.fusesource.jansi.internal.Kernel32.COORD

class top extends Module{
    val io =IO(new Bundle {
        val ret = Output(UInt(32.W))
    })
//实例化各个组件
    val Pc = Module(new pc())
    val Alu = Module(new alu())
    val RegisterFile = Module(new registerfile())
    val Controller = Module(new controller())
    val InputAlu = Module(new inputalu())
    val Mem = Module(new mem())
    val InputReg = Module(new inputreg())

// PC
    Mem.io.im_addr := Pc.io.next_pc
    Pc.io.jump_en := Controller.io.jump_en
    Pc.io.jump_pc := Alu.io.rsl

//Mem 
    Controller.io.inst := Mem.io.im_out
    InputReg.io.dm_out := Mem.io.dm_out
    Mem.io.dm_addr := Alu.io.rsl
    Mem.io.dm_in := RegisterFile.io.rd2
    Mem.io.mem_wr := Controller.io.mem_wr

//Controller
    InputAlu.io.imm := Controller.io.imm
    InputAlu.io.alu_a_sel := Controller.io.alu_a_sel
    InputAlu.io.alu_b_sel := Controller.io.alu_b_sel
    InputReg.io.rf_wr_sel := Controller.io.mem_wr
    RegisterFile.io.wr_en := Controller.io.rf_wr_en
    Alu.io.alu_sel := Controller.io.alu_sel

//InputReg
    RegisterFile.io.wd := InputReg.io.wd
    InputReg.io.alu_out := Alu.io.rsl

//RegisterFile
    RegisterFile.io.inst := Mem.io.im_out
    InputAlu.io.rs1 := RegisterFile.io.rd1
    InputAlu.io.rs2 := RegisterFile.io.rd2

//InputAlu
    Alu.io.op1 := InputAlu.io.rs1
    Alu.io.op2 := InputAlu.io.rs2

    io.ret := Alu.io.rsl
}


object top extends App{
    emitVerilog(new top(), Array("--target-dir", "generated"))
}

