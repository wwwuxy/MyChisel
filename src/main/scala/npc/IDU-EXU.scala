package npc

import chisel3._
import chisel3.util._

class IDU_EXU extends Bundle{
    val rs1 = Output(UInt(32.W))    //要给pc进行jal jalr
    val rs2 = Output(UInt(32.W))
    val imm = Output(UInt(32.W))
    val alu_sel = Output(UInt(13.W))
    val alu_a_sel = Output(Bool())
    val alu_b_sel = Output(Bool()) 
    val pc = Output(UInt(32.W))
    val jump_en = Output(Bool())
    val jump_jalr = Output(Bool())
    val is_ecall = Output(Bool())
    val is_mret = Output(Bool())
    val is_csr = Output(Bool())
    val mtvec = Output(UInt(32.W))
    val epc = Output(UInt(32.W))
    val mem_rd_en = Output(Bool())
    val mem_wr_en = Output(Bool())
    val rf_wr_en = Output(Bool())
    val len = Output(UInt(32.W))
    val load_unsign = Output(Bool())
//for isu
    val is_load = Output(Bool())
    val isS_type = Output(Bool())
    val is_j = Output(Bool())
//for wbu
    val is_cmp = Output(Bool())
} 
