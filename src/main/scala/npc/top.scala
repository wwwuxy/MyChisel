package npc

import chisel3._
import chisel3.util.HasBlackBoxResource
import chisel3.stage._
import circt.stage.ChiselStage

// import org.fusesource.jansi.internal.Kernel32.COORD

//BlackBox
class Memory extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val pc = Input(UInt(32.W))
    val alu_out = Input(UInt(32.W))
    val data = Input(UInt(32.W))   
    val wr_en = Input(Bool())
    val rd_en = Input(Bool())
    val len = Input(UInt(32.W))
    val load_unsign = Input(Bool())
    val inst = Output(UInt(32.W))
    val dm_out = Output(UInt(32.W))
  })

  addResource("/Memory.v")
}

// top module
class top extends Module{
    val io =IO(new Bundle {
        // val mem_rd_en = Input(Bool())
        val inst = Output(UInt(32.W))
        val pc = Output(UInt(32.W))
        val addr = Output(UInt(32.W))
        val data = Output(UInt(32.W))
        val nextpc = Output(UInt(32.W))     //for ftrace的call
        val alu_out =  Output(UInt(32.W))
        val alu_op1 = Output(UInt(32.W))
        val alu_op2 = Output(UInt(32.W))
        val imm = Output(UInt(32.W))
    })
    // io.nemutrap := false.B

//实例化各个组件
    val Pc = Module(new pc())
    val Alu = Module(new alu())
    val RegisterFile = Module(new registerfile())
    val Controller = Module(new controller())
    val InputAlu = Module(new inputalu())
    val Mem = Module(new Memory())
    val InputReg = Module(new inputreg())
// PC
    Pc.io.jump_jalr := Controller.io.jump_jalr
    Pc.io.jump_en := Controller.io.jump_en
    Pc.io.jump_pc := Alu.io.rsl
    Pc.io.imm := Controller.io.imm
    InputAlu.io.pc := Pc.io.next_pc
    Pc.io.mtvec := RegisterFile.io.rd2
    Pc.io.is_ecall := Controller.io.is_ecall

//Controller
    Controller.io.inst := Mem.io.inst
    InputAlu.io.imm := Controller.io.imm
    InputAlu.io.alu_a_sel := Controller.io.alu_a_sel
    InputAlu.io.alu_b_sel := Controller.io.alu_b_sel
    InputReg.io.rf_wr_sel := Controller.io.rf_wr_sel
    RegisterFile.io.wr_en := Controller.io.rf_wr_en
    Alu.io.alu_sel := Controller.io.alu_sel
    Controller.io.alu_out := Alu.io.rsl
    Controller.io.rs1 := RegisterFile.io.rd1
    Controller.io.rs2 := RegisterFile.io.rd2


//InputReg
    RegisterFile.io.wd := InputReg.io.wd
    // InputReg.io.alu_out := Alu.io.rsl
    InputReg.io.dm_out := Mem.io.dm_out
    InputReg.io.storepc := Pc.io.dnpc

//RegisterFile
    RegisterFile.io.inst := Mem.io.inst
    InputAlu.io.rs1 := RegisterFile.io.rd1
    InputAlu.io.rs2 := RegisterFile.io.rd2
    RegisterFile.io.is_csr := Controller.io.is_csr
    RegisterFile.io.is_ecall := Controller.io.is_ecall
    RegisterFile.io.pc := Pc.io.next_pc

//InputAlu
    Alu.io.op1 := InputAlu.io.op1
    Alu.io.op2 := InputAlu.io.op2

//Mem
    Mem.io.alu_out := Alu.io.rsl
    Mem.io.data := RegisterFile.io.rd2
    Mem.io.wr_en := Controller.io.mem_wr_en
    Mem.io.rd_en := Controller.io.mem_rd_en
    Mem.io.pc := Pc.io.next_pc
    Mem.io.len := Controller.io.len
    Mem.io.load_unsign := Controller.io.load_unsign

//for dubug
    io.addr := Alu.io.rsl
    io.data := RegisterFile.io.rd2
    io.nextpc := Alu.io.rsl
    io.pc := Pc.io.next_pc
    io.inst := Mem.io.inst
    io.alu_out := Alu.io.rsl
    io.alu_op1 := InputAlu.io.op1
    io.alu_op2 := InputAlu.io.op2
    io.imm := Controller.io.imm
}
 

object top extends App{
    var filltlflag = Array[String]()
    filltlflag = filltlflag ++ Array(
        "--target-dir", "generated",
        "--target:verilog",
        // "--split-verilog",
        // "--lowering-options=" + Seq(
        //     "disallowLocalVariables",
        //     "disallowPackedArrays"
        // ).mkString(","),
        // "--disable-all-randomization"
        )

    ChiselStage.emitSystemVerilogFile(
        new top,
        filltlflag
    )
}


