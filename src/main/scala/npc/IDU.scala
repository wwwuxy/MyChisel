package npc

import chisel3._
import chisel3.util.HasBlackBoxResource
import chisel3.util.Decoupled

class Date_Memory extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val alu_out = Input(UInt(32.W))
    val data = Input(UInt(32.W))
    val wr_en = Input(Bool())
    val rd_en = Input(Bool())
    val len = Input(UInt(32.W))
    val load_unsign = Input(Bool())
    val dm_out = Output(UInt(32.W))
  })
  addResource("/Date_Memory.v")
}

class IDU extends Module{
    val io = IO(new Bundle{
        val in = Flipped(Decoupled(new IFU_IDU))
        val out = Decoupled(new IDU_EXU)
// for write back
        val alu_rsl = Input(UInt(32.W)) 
// for pc        
        val jump_jalr = Output(Bool())
        val jump_en = Output(Bool())
        val imm = Output(UInt(32.W))
        val is_ecall = Output(Bool())
        val is_mret = Output(Bool())
        val mtvec = Output(UInt(32.W))  
        val epc = Output(UInt(32.W)) 
        val rd1 = Output(UInt(32.W))
    })

    val Dmem = Module(new Date_Memory())
    val Contorller = Module(new CONTORLLER())
    val RegisterFile = Module(new REGISTERFILE())

//Dmem
    Dmem.io.alu_out := io.alu_rsl
    Dmem.io.data := RegisterFile.io.rd2
    Dmem.io.wr_en := Contorller.io.mem_wr_en
    Dmem.io.rd_en := Contorller.io.mem_rd_en
    Dmem.io.len := Contorller.io.len
    Dmem.io.load_unsign := Contorller.io.load_unsign

//Controller
    Contorller.io.inst := io.in.bits.inst
    Contorller.io.rs1 := RegisterFile.io.rd1
    Contorller.io.rs2 := RegisterFile.io.rd2
    io.out.bits.alu_sel := Contorller.io.alu_sel
    io.out.bits.alu_a_sel := Contorller.io.alu_a_sel
    io.out.bits.alu_b_sel := Contorller.io.alu_b_sel
    io.out.bits.pc := io.in.bits.pc
//RegisterFile
    RegisterFile.io.inst := io.in.bits.inst
    RegisterFile.io.wr_en := Contorller.io.rf_wr_en
    RegisterFile.io.dm_out := Dmem.io.dm_out
    RegisterFile.io.alu_out := io.alu_rsl
    RegisterFile.io.rf_wr_sel := Contorller.io.rf_wr_sel
    RegisterFile.io.is_csr := Contorller.io.is_csr
    RegisterFile.io.is_ecall := Contorller.io.is_ecall
    RegisterFile.io.pc := io.in.bits.pc
    RegisterFile.io.is_mret := Contorller.io.is_mret
    io.out.bits.rs1 := RegisterFile.io.rd1
    io.out.bits.rs2 := RegisterFile.io.rd2
    io.out.bits.imm := Contorller.io.imm

//for top
    io.jump_jalr := Contorller.io.jump_jalr
    io.jump_en := Contorller.io.jump_en
    io.imm := Contorller.io.imm
    io.is_ecall := Contorller.io.is_ecall
    io.is_mret := Contorller.io.is_mret
    io.mtvec := RegisterFile.io.mtvec
    io.epc := RegisterFile.io.epc
    io.rd1 := RegisterFile.io.rd1

    io.in.ready := true.B
    io.out.valid := true.B
}


