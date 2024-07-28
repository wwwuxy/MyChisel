package npc

import chisel3._
import chisel3.util._

class EXU_ISU extends  Bundle{
//for isu
    val alu_out = Output(UInt(32.W))
    val data = Output(UInt(32.W))
    val mem_wr_en = Output(Bool())
    val mem_rd_en = Output(Bool())
    val rf_wr_en = Output(Bool())
    val len = Output(UInt(32.W))
    val load_unsign = Output(Bool())
// for pc
    val jump_jalr = Bool()
    val jump_en = Bool()
    val imm = UInt(32.W)
    val is_ecall = Bool()
    val is_mret = Bool()
    val is_csr = Output(Bool())
    val mtvec = UInt(32.W)
    val epc = UInt(32.W)
    val rd1 = UInt(32.W)     //for jalr
    val pc = UInt(32.W)
//for isu
    val is_load = Output(Bool())
    val isS_type = Output(Bool())
    val is_j = Output(Bool())
//for wbu
    val is_cmp = Output(Bool())
}
