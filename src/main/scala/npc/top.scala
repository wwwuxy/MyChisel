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


    val rslReg = RegInit(Alu.io.rsl)

    val pc = Pc.io.next_pc
    RegisterFile.io.pc := pc
    // RegisterFile.io.pc := Pc.io.next_pc

    val inst = RegisterFile.io.inst
    Controller.io.inst := inst

    val rs1 = RegisterFile.io.rs1
    InputAlu.io.rs1 := rs1

    val rs2 = RegisterFile.io.rs2
    InputAlu.io.rs2 := rs2

    val rd_en = Controller.io.rd_en
    RegisterFile.io.rd_en := rd_en

    val imm = Controller.io.imm
    InputAlu.io.imm := imm

    val rs1_en = Controller.io.rs1_en
    InputAlu.io.rs1_en := rs1_en

    val rs2_en = Controller.io.rs2_en
    InputAlu.io.rs2_en := rs2_en

    val imm_en = Controller.io.imm_en
    InputAlu.io.imm_en := imm_en

    val alu_sel = Controller.io.alu_sel
    Alu.io.alu_sel := alu_sel

    val pc_jump = Controller.io.pc_jump
    Pc.io.jump_en := pc_jump

    val op1 = InputAlu.io.op1
    Alu.io.op1 := op1

    val op2 = InputAlu.io.op2
    Alu.io.op2 := op2

    val rsl_reg = rslReg
    RegisterFile.io.rsl := rsl_reg

    val rsl_pc = rslReg
    Pc.io.dnpc := rsl_pc

}


object top extends App{
    emitVerilog(new top(), Array("--target-dir", "generated"))
}

