package npc 

import chisel3._
import chisel3.util._

class ISU_WBU extends Bundle{
    val dm_out = UInt(32.W)
    val alu_out = UInt(32.W)
    val jump_jalr = Bool()
    val jump_en = Bool()
    val imm = UInt(32.W)
    val is_ecall = Bool()
    val is_mret = Bool()
    val is_csr = Output(Bool())
    val mtvec = UInt(32.W)
    val epc = UInt(32.W)
    val rd1 = UInt(32.W)     //for jalr
    val mem_rd_en = Bool()
    val mem_wr_en = Bool()
    val rf_wr_en = Bool()
    val finish_load = Bool()
//for wbu
    val is_cmp = Output(Bool())
    val is_load = Bool()
    val isS_type = Bool()
    val can_wirte = Bool()
}
